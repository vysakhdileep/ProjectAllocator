package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by arunjoseph on 02/03/18.
 */

public class RequestAdapter extends ArrayAdapter<RequestQueue> {
    private static final String TAG = "RequestAdapter";
    private DatabaseReference mDatabase, requestDatabase, acceptRequest, groupDatabase, studentDatabase;
    private ArrayList<String> students = new ArrayList<>();

    public RequestAdapter(@NonNull Context context, int resource, ArrayList<RequestQueue> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.request_list_item, parent, false);
        }

        final RequestQueue currRequest = getItem(position);
        Log.d(TAG, "position: " + position);

        final LinearLayout requestGroup = (LinearLayout) listItemView.findViewById(R.id.request_list_group);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        groupDatabase = mDatabase.child("Group").child(currRequest.groupid);
        studentDatabase = mDatabase.child("Student");

        groupDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                students.clear();
                if (!dataSnapshot.exists())
                    Log.d(TAG, "NULL!!!");
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    students.add(requestSnapshot.getValue().toString());
                }


                for (int i = 0; i < students.size(); i++) {
                    final int j = i;
                    studentDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                TextView text = new TextView(getContext());
                                String temp = dataSnapshot.child(students.get(j)).child("nameof").getValue(String.class);
                                text.setText(temp);
                                requestGroup.addView(text);
                                Log.d(TAG, "Student " + temp);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "LoadRequest:onCancelled", databaseError.toException());
                // ...
            }
        });


        TextView requestTopic = (TextView) listItemView.findViewById(R.id.request_topic);
        requestTopic.setText(currRequest.getTopic());

        TextView requestDescription = (TextView) listItemView.findViewById(R.id.request_description);
        requestDescription.setText(currRequest.getDescription());


        Log.d(TAG, "Area size: " + currRequest.getAreas().size());
        for (int i = 0; i < currRequest.getAreas().size(); i++)
            printArea((LinearLayout) listItemView.findViewById(R.id.request_list_areas), currRequest.getAreas().get(i));

        Log.d(TAG, "acceptButton");
        Button acceptButton = listItemView.findViewById(R.id.accept_request);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                final View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view_request, null);
                Log.d(TAG, "onClick:view inflate");

                mBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String accept = "ACCEPTED";
                        mDatabase.child("RequestQueue").child(currRequest.requestid).child("status").setValue(accept);

                        acceptRequest = FirebaseDatabase.getInstance().getReference().child("AcceptProject");
                        String key = acceptRequest.push().getKey();

                        acceptRequest.child(key).child("description").setValue(currRequest.description);
                        acceptRequest.child(key).child("faculty").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        acceptRequest.child(key).child("groupid").setValue(currRequest.groupid);
                        acceptRequest.child(key).child("topic").setValue(currRequest.topic);
                        acceptRequest.child(key).child("description").setValue(currRequest.description);

                        Integer i = 0;
                        while (i < currRequest.areas.size()) {
                            acceptRequest.child(key).child("areas").child(i.toString()).setValue(currRequest.areas.get(i));
                            i++;
                        }

                        mDatabase.child("RequestQueue").child(currRequest.requestid).removeValue();

                        FirebaseMessageService.Listening(accept);
                        Toast.makeText(getContext(), "Project Accepted",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.setView(mView);
                mBuilder.show();
            }
        });

        Log.d(TAG, "rejectButton");
        Button rejectButton = listItemView.findViewById(R.id.reject_request);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                final View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view_request, null);
                Log.d(TAG, "onClick:view inflate");

                mBuilder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final int k = position + 1;
                        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        requestDatabase = mDatabase.child("RequestQueue");

                        requestDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists())
                                    Log.d(TAG, "NULL!!!");
                                long l = dataSnapshot.child(currRequest.getRequestid()).child("faculties").getChildrenCount();
                                Log.d(TAG, "l value is" + l);
                                if (l > 0 && dataSnapshot.child(currRequest.getRequestid()).child("faculties").child(String.valueOf(l - 1)).getValue().equals(user)) {
                                    mDatabase.child("RequestQueue").child(currRequest.getRequestid()).child("faculties").child(String.valueOf(l - 1)).removeValue();
                                    if (l == 1) {
                                        String reject = "REJECTED";
                                        mDatabase.child("RequestQueue").child(currRequest.getRequestid()).child("status").setValue(reject);
                                        FirebaseMessageService.Listening(reject);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Getting Post failed, log a message
                                Log.w(TAG, "LoadRequest:onCancelled", databaseError.toException());
                                // ...
                            }
                        });

                        Toast.makeText(getContext(), "Project Rejected",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.setView(mView);
                mBuilder.show();
            }
        });

        return listItemView;
    }

    private void printArea(LinearLayout listItemView, String requestArea) {
        Log.d(TAG, "Area: " + requestArea);
        TextView textView = new TextView(getContext());
        textView.setText(requestArea);
        listItemView.addView(textView);
    }


}
