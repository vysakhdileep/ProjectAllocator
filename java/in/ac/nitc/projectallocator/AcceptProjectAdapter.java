package in.ac.nitc.projectallocator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by VysakhDileep on 16-03-2018.
 */

public class AcceptProjectAdapter extends ArrayAdapter<AcceptProject> {

    ArrayList<String> area_expertise = new ArrayList<>();
    ArrayList<String> studentlist =new ArrayList<>();
    //ArrayList<String> t =new ArrayList<>();
    View acceptItemView;

    public AcceptProjectAdapter(@NonNull Context context, int resource, ArrayList<AcceptProject> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        acceptItemView = convertView;

        Log.d(TAG," In Adapter  1");
        if (acceptItemView == null) {
            acceptItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.accept_list_item, parent, false);
        }
        final AcceptProject currProject =getItem(position);
        final TextView facultyid = (TextView) acceptItemView.findViewById(R.id.accept_facultyid);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference FacultyRef = FirebaseDatabase.getInstance().getReference().child("Faculty").child(user.getUid());

        FacultyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Faculty faculty = dataSnapshot.getValue(Faculty.class);
                    facultyid.setText(faculty.getNameof());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        TextView description = (TextView) acceptItemView.findViewById(R.id.accept_description);
        description.setText(currProject.getDescription());

        TextView topic = (TextView) acceptItemView.findViewById(R.id.accept_topic);
        topic.setText(currProject.getTopic());

        Log.d(TAG," Size of Areas"+ currProject.getAreas().size());

        DatabaseReference area = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");

        area.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot) {
                Log.d(TAG," In listener firebase");
                if (dataSnapshot.exists()) {
                    area_expertise.clear();
                    for (int i = 0; i < currProject.getAreas().size(); i++) {
                        Log.d(TAG," In listener firebase loop "+dataSnapshot.child(currProject.getAreas().get(i)).getValue(String.class));
                        area_expertise.add(dataSnapshot.child(currProject.getAreas().get(i)).getValue(String.class));

                    }
                    for (int i = 0; i < area_expertise.size(); i++)
                        printArea((LinearLayout) acceptItemView.findViewById(R.id.accept_areas),area_expertise.get(i));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference group = FirebaseDatabase.getInstance().getReference().child("Group").child(currProject.getGroupid());

        group.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    studentlist.clear();
                    studentlist = (ArrayList<String>) dataSnapshot.getValue();
                    Log.d(TAG,"Value added size"+studentlist.size());

                    int i=0;


                    while(i < studentlist.size())
                    {



                        final DatabaseReference Studref = FirebaseDatabase.getInstance().getReference().child("Student").child(studentlist.get(i)).child("nameof");

                        Studref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                     LinearLayout students = (LinearLayout)acceptItemView.findViewById(R.id.accept_students);
                                    TextView item = new TextView(getContext());
                                    String stu = dataSnapshot.getValue(String.class);
                                    item.setText(stu);
                                    Log.d(TAG,"Student name"+stu);
                                    students.addView(item);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        i++;

                    }
                    }

                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return acceptItemView;
    }

    private void printArea(LinearLayout listItemView, String acceptArea) {
        Log.d(TAG, "Area: " + acceptArea);
        TextView textView = new TextView(getContext());
        textView.setText(acceptArea);
        listItemView.addView(textView);
    }
}
