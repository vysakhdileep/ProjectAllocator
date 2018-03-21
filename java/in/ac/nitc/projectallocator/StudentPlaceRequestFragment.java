package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by jsnkrm on 25/02/18.
 */

public class StudentPlaceRequestFragment extends Fragment {

    final String TAG = "StuPlaceRequestFrag";

    FirebaseUser user;
    String uid;
    Student stu;
    DatabaseReference database, StudentRef, StuRef,GroupRef,AreaRef;
    private String num;
    ArrayList<String> GroupAreaId = new ArrayList<>();
    ArrayList<Faculty> requiredFac = new ArrayList<>();


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
        Log.d(TAG,"inside onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_place_request, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();


        database = FirebaseDatabase.getInstance().getReference();
        StudentRef = database.child("Student").child(uid);

        Log.d(TAG, "Student uid:" + uid);


        StudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    stu = dataSnapshot.getValue(Student.class);

                    Log.d(TAG, "Stuemail:" + stu.getPersonalemail());
                    Log.d(TAG, "StuName:" + stu.getNameof());
                    Log.d(TAG, "Stuphone:" + stu.getPhonenumber());
                    Log.d(TAG, "Sturoll:" + stu.getRollnumber());
                    Log.d(TAG, "StugroupId:" + stu.getGroupid());

                    if(stu.getGroupid().isEmpty())
                    {
                        Log.d(TAG,"empty groupid!!");
                        createGroup();
                        getAreaOfInerest();

                    }
                    else
                    {
                        getAreaOfInerest();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;

    }


    void createGroup()
    {

        final Button createGroupButton = getView().findViewById(R.id.create_group_button);

        createGroupButton.setVisibility(View.VISIBLE);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG,"Inside CreateGroup");

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

                        Log.d(TAG,"num:" + getNum());

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

                        final HashSet<String> GroupMembers = new HashSet<>();

                        if(getNum()!= null && getNum().equals("2"))
                        {
                            Log.d(TAG,"inside two!!");
                            GroupMembers.add(member1.getText().toString());
                        }
                        else if(getNum()!= null && getNum().equals("3"))
                        {
                            GroupMembers.add(member1.getText().toString());
                            GroupMembers.add(member2.getText().toString());

                        }
                        else if(getNum()!= null && getNum().equals("4"))
                        {
                            GroupMembers.add(member1.getText().toString());
                            GroupMembers.add(member2.getText().toString());
                            GroupMembers.add(member3.getText().toString());

                        }
                        else if(getNum()!= null && getNum().equals("5"))
                        {
                            GroupMembers.add(member1.getText().toString());
                            GroupMembers.add(member2.getText().toString());
                            GroupMembers.add(member3.getText().toString());
                            GroupMembers.add(member4.getText().toString());
                        }


                            Log.d(TAG,"Got Inside if!! hashsize:" + GroupMembers.size());


                        GroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
                        final String key =  GroupRef.push().getKey();
                        GroupRef.child(key).child("0").setValue(stu.getRollnumber());
                        Log.d(TAG,"new group Key:" + key);

                        StuRef = database.child("Student");
                        StuRef.child(stu.getUid()).child("groupid").setValue(key);
                        final int finalI = 1;
                        StuRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot studentSnapshot: dataSnapshot.getChildren())
                                {
                                    Student stu1 = studentSnapshot.getValue(Student.class);
                                    if(GroupMembers.contains(stu1.getRollnumber()))
                                    {
                                        Log.d(TAG,"rollnum:" + stu1.getRollnumber());
                                        Log.d(TAG,"nameofstu:" + stu1.getNameof());
                                        Log.d(TAG,"stuid:" + stu1.getUid());
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Student").child(stu1.getUid()).child("groupid").setValue(key);
                                        GroupRef.child(key).child(String.valueOf(finalI)).setValue(stu1.getRollnumber());
                                        GroupMembers.remove(stu1.getRollnumber());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });


                alert.setView(mView);
                alert.show();

            }
        });
    }

    void getAreaOfInerest()
    {
        final Button createGroupButton = getView().findViewById(R.id.create_group_button);

        createGroupButton.setVisibility(View.GONE);

        Button moreFac = (Button) getView().findViewById(R.id.button_moreFac);
        final Spinner spinnerFac1 = getView().findViewById(R.id.spinner_fac1);
        final Spinner spinnerFac2 = getView().findViewById(R.id.spinner_fac2);
        final Spinner spinnerFac3 = getView().findViewById(R.id.spinner_fac3);
        final Spinner spinnerFac4 = getView().findViewById(R.id.spinner_fac4);
        final Spinner spinnerFac5 = getView().findViewById(R.id.spinner_fac5);

        spinnerFac1.setVisibility(View.VISIBLE);
        spinnerFac2.setVisibility(View.GONE);
        spinnerFac3.setVisibility(View.GONE);
        spinnerFac4.setVisibility(View.GONE);
        spinnerFac5.setVisibility(View.GONE);

        AreaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");
        final ArrayList<String> areaExpertise = new ArrayList<>();
        final ArrayList<String> areaId = new ArrayList<>();

        AreaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot areaSnapshot: dataSnapshot.getChildren())
                {
                     areaExpertise.add(areaSnapshot.getValue(String.class));
                     areaId.add(areaSnapshot.getKey());
                     Log.d(TAG,"areaId:--------- " + areaSnapshot.getKey());

                }
                showAreaofInterest(areaExpertise,areaId);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void showAreaofInterest(final ArrayList<String> areaExpertise, final ArrayList<String> areaId)
    {

        Log.d(TAG,"inside func" + areaExpertise.size());
        Button selectArea = getView().findViewById(R.id.button_area_of_interest);

        selectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i=0;
                Log.d(TAG,"inside!!!");

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_area_of_interest,null);

                ScrollView sv = mView.findViewById(R.id.area_expertise_scrollview);
                final LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);
                sv.addView(ll);

                while (i<areaExpertise.size())
                {
                    Log.d(TAG,"in while!!!");
                    CheckBox ch = new CheckBox(getActivity());
                    ch.setText((CharSequence) areaExpertise.get(i));
                    ch.setId(i);
                    ch.setTag(areaId.get(i));
                    ll.addView(ch);
                    i++;
                }

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Log.d(TAG,"size: " + areaExpertise.size());
                            int k =0;
                        while(k < areaExpertise.size())
                            {
                                CheckBox ch = (CheckBox) mView.findViewById(k);
                                if(ch.isChecked())
                                {
                                    GroupAreaId.add(String.valueOf(ch.getTag()));
                                    Log.d(TAG,"area selected" + GroupAreaId.get(0));
                                }
                                k++;
                            }
                        getRequiredFac(GroupAreaId);


                    }

                });


                alert.setView(mView);
                alert.show();
            }
        });



    }

    private void getRequiredFac(final ArrayList<String> areaId)
    {
        DatabaseReference facRef;
        facRef = FirebaseDatabase.getInstance().getReference().child("Faculty");
        requiredFac.clear();
        facRef.addValueEventListener(new ValueEventListener() {
            Faculty faculty;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot facSnapshot: dataSnapshot.getChildren())
                {
                   faculty = facSnapshot.getValue(Faculty.class);
                   for(int i=0;i<areaId.size();i++)
                   {
                       if(faculty.getAreas().contains(areaId.get(i)))
                       {
                           requiredFac.add(faculty);
                           break;
                       }
                   }
                }
                setspinners(requiredFac);
                Log.d(TAG,"faculty--------------" + requiredFac.get(0).getNameof());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setspinners(ArrayList<Faculty> requiredFac)
    {

        final ArrayList<String> items = new ArrayList<>();
        int j =0;
        while(j<requiredFac.size())
        {
            items.add(requiredFac.get(j).getNameof());
            j++;
        }
        items.add("Select Faculty");
        final Button moreFac = (Button) getView().findViewById(R.id.button_moreFac);
        moreFac.setEnabled(false);
        final Spinner spinnerFac1 = getView().findViewById(R.id.spinner_fac1);
        final Spinner spinnerFac2 = getView().findViewById(R.id.spinner_fac2);
        final Spinner spinnerFac3 = getView().findViewById(R.id.spinner_fac3);
        final Spinner spinnerFac4 = getView().findViewById(R.id.spinner_fac4);
        final Spinner spinnerFac5 = getView().findViewById(R.id.spinner_fac5);

        moreFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spinnerFac2.getVisibility() == View.GONE)
                    spinnerFac2.setVisibility(View.VISIBLE);
                else if(spinnerFac2.getVisibility() == View.VISIBLE && spinnerFac3.getVisibility() == View.GONE)
                    spinnerFac3.setVisibility(View.VISIBLE);
                else if(spinnerFac3.getVisibility() == View.VISIBLE && spinnerFac4.getVisibility() == View.GONE)
                    spinnerFac4.setVisibility(View.VISIBLE);
                else if(spinnerFac4.getVisibility() == View.VISIBLE && spinnerFac5.getVisibility() == View.GONE)
                    spinnerFac5.setVisibility(View.VISIBLE);
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        spinnerFac1.setAdapter(adapter);
        spinnerFac2.setAdapter(adapter);
        spinnerFac3.setAdapter(adapter);
        spinnerFac4.setAdapter(adapter);
        spinnerFac5.setAdapter(adapter);
        spinnerFac1.setSelection(items.size()-1);
        spinnerFac2.setSelection(items.size()-1);
        spinnerFac3.setSelection(items.size()-1);
        spinnerFac4.setSelection(items.size()-1);
        spinnerFac5.setSelection(items.size()-1);


        spinnerFac1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"pref1---------" + adapterView.getItemAtPosition(i).toString());
                if(i != items.size()-1)
                    moreFac.setEnabled(true);
                else
                    moreFac.setEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFac2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"pref1---------" + adapterView.getItemAtPosition(i).toString());
                if(i != items.size()-1)
                    moreFac.setEnabled(true);
                else
                    moreFac.setEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerFac3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"pref1---------" + adapterView.getItemAtPosition(i).toString());
                if(i != items.size()-1)
                    moreFac.setEnabled(true);
                else
                    moreFac.setEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFac4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"pref1---------" + adapterView.getItemAtPosition(i).toString());
                if(i != items.size()-1)
                    moreFac.setEnabled(true);
                else
                    moreFac.setEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerFac5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"pref1---------" + adapterView.getItemAtPosition(i).toString());
                if(i != items.size()-1)
                    moreFac.setEnabled(true);
                else
                    moreFac.setEnabled(false);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}
