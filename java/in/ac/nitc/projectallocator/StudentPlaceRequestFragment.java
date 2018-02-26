package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by jsnkrm on 25/02/18.
 */

public class StudentPlaceRequestFragment extends Fragment {

    final String TAG = "StuPlaceRequestFrag";


    FirebaseUser user;
    String uid;
    Student stu;
    DatabaseReference database, StudentRef;
    private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public StudentPlaceRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_place_request, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();


        database = FirebaseDatabase.getInstance().getReference();
        StudentRef = database.child("Student").child(uid);
        Log.d(TAG, "uid:" + uid);

        final Button createGroupButton = view.findViewById(R.id.create_group_button);

        StudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    stu = dataSnapshot.getValue(Student.class);

                    Log.d(TAG, "email:" + stu.getPersonalemail());
                    Log.d(TAG, "Name:" + stu.getName());
                    Log.d(TAG, "login:" + stu.getEmail());
                    Log.d(TAG, "phone:" + stu.getPhonenumber());
                    Log.d(TAG, "roll:" + stu.getRollnumber());
                    Log.d(TAG, "groupId:" + stu.getGroupId());

                    if(stu.getGroupId().equals("null"))
                    {
                        createGroupButton.setVisibility(View.VISIBLE);

                        createGroupButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_group,null);
                                Log.d(TAG, "onClick:view inflate");
                                final EditText member1 = mView.findViewById(R.id.group_member1);
                                final EditText member2 = mView.findViewById(R.id.group_member2);
                                final EditText member3 = mView.findViewById(R.id.group_member3);
                                final EditText member4 = mView.findViewById(R.id.group_member4);
                                member1.setVisibility(View.GONE);
                                member2.setVisibility(View.GONE);
                                member3.setVisibility(View.GONE);
                                member4.setVisibility(View.GONE);
                                //get the spinner from the xml.
                                Spinner dropdown = mView.findViewById(R.id.spinner_noOfMembers);
                                //create a list of items for the spinner.
                                String[] items = new String[]{"2", "3","4","5"};
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                                //set the spinners adapter to the previously created one.
                                dropdown.setAdapter(adapter);

                                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        setNum(adapterView.getItemAtPosition(i).toString());

                                        if(getNum().equals("2"))
                                        {
                                            member1.setVisibility(View.VISIBLE);
                                            member2.setVisibility(View.GONE);
                                            member3.setVisibility(View.GONE);
                                            member4.setVisibility(View.GONE);

                                        }
                                        else if(getNum().equals("3"))
                                        {
                                            member1.setVisibility(View.VISIBLE);
                                            member2.setVisibility(View.VISIBLE);
                                            member3.setVisibility(View.GONE);
                                            member4.setVisibility(View.GONE);

                                        }
                                        else if(getNum().equals("4"))
                                        {
                                            member1.setVisibility(View.VISIBLE);
                                            member2.setVisibility(View.VISIBLE);
                                            member3.setVisibility(View.VISIBLE);
                                            member4.setVisibility(View.GONE);

                                        }
                                        else if(getNum().equals("5"))
                                        {
                                            member1.setVisibility(View.VISIBLE);
                                            member2.setVisibility(View.VISIBLE);
                                            member3.setVisibility(View.VISIBLE);
                                            member4.setVisibility(View.VISIBLE);

                                        }

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });



                                alert.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        if(getNum()!= null && getNum().equals("two"))
                                        {
                                            Log.d(TAG,"Got Inside if!!");
                                            if(!member1.getText().toString().isEmpty())
                                            {

                                                getStuId(member1.getText().toString());
                                            }
                                        }
                                        else
                                        {
                                            Log.d(TAG,"null!!");
                                        }

                                    }
                                });


                                alert.setView(mView);
                                alert.show();

                            }
                        });


                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }

    public void getStuId(final String rollNumber)
    {
        Log.d(TAG,"Inside getStuId");

        database = FirebaseDatabase.getInstance().getReference();

        DatabaseReference requestQueueRef = database.child("Student");
        String StuId;

        ValueEventListener studentListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot studentsnapshot : dataSnapshot.getChildren())
                {
                    Student stu = studentsnapshot.getValue(Student.class);
                    if(stu.getRollnumber().equals(rollNumber))
                        Log.d(TAG,"uid:" + studentsnapshot.getKey());

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "LoadRequest:onCancelled", databaseError.toException());
                // ...
            }

        };

        database.addValueEventListener(studentListener);

    }
}
