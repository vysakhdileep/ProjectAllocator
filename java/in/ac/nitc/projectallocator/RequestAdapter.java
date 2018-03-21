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
import com.google.firebase.auth.FirebaseUser;
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
    private DatabaseReference mDatabase, requestDatabase, acceptDatabase;

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

        RequestQueue currRequest = getItem(position);
        Log.d(TAG, "position: " + position);

        TextView requestGroup = (TextView) listItemView.findViewById(R.id.request_list_group);
        requestGroup.setText(currRequest.getGroup());

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
                        int k = position + 1;
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        String accept = "ACCEPTED";
                        mDatabase.child("RequestQueue").child(String.valueOf(k)).child("status").setValue(accept);

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
                                long l = dataSnapshot.child(String.valueOf(k)).child("faculties").getChildrenCount();
                                if (l > 0 && dataSnapshot.child(String.valueOf(k)).child("faculties").child(String.valueOf(l - 1)).getValue().equals(user)) {
                                    mDatabase.child("RequestQueue").child(String.valueOf(k)).child("faculties").child(String.valueOf(l - 1)).removeValue();
                                    if (l == 1) {
                                        String reject = "REJECTED";
                                        mDatabase.child("RequestQueue").child(String.valueOf(k)).child("status").setValue(reject);
                                        mDatabase.child("RequestQueue").child(String.valueOf(k)).removeValue();
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
