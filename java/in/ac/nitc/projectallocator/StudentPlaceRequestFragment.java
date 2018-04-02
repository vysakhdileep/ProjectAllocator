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
import java.util.HashSet;
import java.util.Objects;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;

/**
 * Created by jsnkrm on 25/02/18.
 */

public class StudentPlaceRequestFragment extends Fragment {

    final String TAG = "StuPlaceRequestFrag";
    boolean hasProject = false;
    boolean hasAcceptedProj = false;
    boolean hasRejectedProj = false;
    View view;
    FirebaseUser user;
    String Facpref1, Facpref2, Facpref3, Facpref4, Facpref5;
    RequestQueue newRequest = new RequestQueue();
    String uid;
    Student stu;
    DatabaseReference database, StudentRef, StuRef, GroupRef, AreaRef;
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
        Log.d(TAG, "inside onCreateView");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_student_place_request, container, false);


        checkForGroup();

        return view;

    }

    private void checkForGroup()
    {
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

                    if (stu.getGroupid().isEmpty()) {
                        Log.d(TAG, "empty groupid!!");
                        createGroup();

                    }
                    else
                    {
                        Log.d(TAG, "groupid====" + stu.getGroupid());
                        hasGroupFunc();
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void hasGroupFunc()
    {
        DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
        reqRef.addValueEventListener(new ValueEventListener() {
            RequestQueue requestQueue;
            boolean check = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot requestSnapshot : dataSnapshot.getChildren())
                {
                    requestQueue = requestSnapshot.getValue(RequestQueue.class);
                    if(Objects.equals(requestQueue.getGroupid(), stu.getGroupid()))
                    {
                        check = true;
                        break;
                    }
                }
                if(check)
                    isHasRejectedProject();
                else
                    checkInAccept();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkInAccept()
    {
        final DatabaseReference acceptRef = FirebaseDatabase.getInstance().getReference().child("AcceptProject");
        acceptRef.addValueEventListener(new ValueEventListener() {
            AcceptProject acceptProject;
            boolean check = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot acceptSnapshot : dataSnapshot.getChildren())
                {
                    acceptProject = acceptSnapshot.getValue(AcceptProject.class);
                    if(Objects.equals(acceptProject.getGroupid(), stu.getGroupid()))
                    {
                        showAcceptProj();
                        check= true;
                    }
                }
                if(!check)
                    getAreaOfInterest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void createGroup() {
        Log.d(TAG, "Inside CreateGroup");

        View linearLayout = getView().findViewById(R.id.place_request_linear_layout);
        linearLayout.setVisibility(View.GONE);

        View ll = getView().findViewById(R.id.existing_request_linear_layout);
        ll.setVisibility(View.GONE);

        final Button createGroupButton = getView().findViewById(R.id.create_group_button);

        createGroupButton.setVisibility(View.VISIBLE);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_create_group, null);
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
                String[] items = new String[]{"2", "3", "4", "5"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                //set the spinners adapter to the previously created one.
                dropdown.setAdapter(adapter);

                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        setNum(adapterView.getItemAtPosition(i).toString());

                        Log.d(TAG, "num:" + getNum());

                        if (getNum().equals("2")) {
                            member1.setVisibility(View.VISIBLE);
                            member2.setVisibility(View.GONE);
                            member3.setVisibility(View.GONE);
                            member4.setVisibility(View.GONE);

                        } else if (getNum().equals("3")) {
                            member1.setVisibility(View.VISIBLE);
                            member2.setVisibility(View.VISIBLE);
                            member3.setVisibility(View.GONE);
                            member4.setVisibility(View.GONE);

                        } else if (getNum().equals("4")) {
                            member1.setVisibility(View.VISIBLE);
                            member2.setVisibility(View.VISIBLE);
                            member3.setVisibility(View.VISIBLE);
                            member4.setVisibility(View.GONE);

                        } else if (getNum().equals("5")) {
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

                        if (getNum() != null && getNum().equals("2")) {
                            Log.d(TAG, "inside two!!");
                            GroupMembers.add(member1.getText().toString());
                        } else if (getNum() != null && getNum().equals("3")) {
                            GroupMembers.add(member1.getText().toString());
                            GroupMembers.add(member2.getText().toString());

                        } else if (getNum() != null && getNum().equals("4")) {
                            GroupMembers.add(member1.getText().toString());
                            GroupMembers.add(member2.getText().toString());
                            GroupMembers.add(member3.getText().toString());

                        } else if (getNum() != null && getNum().equals("5")) {
                            GroupMembers.add(member1.getText().toString());
                            GroupMembers.add(member2.getText().toString());
                            GroupMembers.add(member3.getText().toString());
                            GroupMembers.add(member4.getText().toString());
                        }


                        Log.d(TAG, "Got Inside if!! hashsize:" + GroupMembers.size());


                        GroupRef = FirebaseDatabase.getInstance().getReference().child("Group");
                        final String key = GroupRef.push().getKey();
                        GroupRef.child(key).child("0").setValue(stu.getUid());
                        Log.d(TAG, "new group Key:" + key);

                        StuRef = database.child("Student");
                        StuRef.child(stu.getUid()).child("groupid").setValue(key);
                        final int finalI = 1;
                        StuRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                    Student stu1 = studentSnapshot.getValue(Student.class);
                                    if (GroupMembers.contains(stu1.getRollnumber())) {
                                        Log.d(TAG, "rollnum:" + stu1.getRollnumber());
                                        Log.d(TAG, "nameofstu:" + stu1.getNameof());
                                        Log.d(TAG, "stuid:" + stu1.getUid());
                                        FirebaseDatabase.getInstance().getReference().
                                                child("Student").child(stu1.getUid()).child("groupid").setValue(key);
                                        GroupRef.child(key).child(String.valueOf(finalI)).setValue(stu1.getUid());
                                        GroupMembers.remove(stu1.getRollnumber());
                                    }
                                }
                                hasGroupFunc();
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

    void getAreaOfInterest()
    {
        Log.d(TAG, "INSIDE getAreaOfInterest");

        final Button createGroupButton = getView().findViewById(R.id.create_group_button);
        createGroupButton.setVisibility(View.GONE);

        View ll = getView().findViewById(R.id.existing_request_linear_layout);
        ll.setVisibility(View.GONE);

        View linearLayout = getView().findViewById(R.id.place_request_linear_layout);
        linearLayout.setVisibility(View.VISIBLE);

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
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    areaExpertise.add(areaSnapshot.getValue(String.class));
                    areaId.add(areaSnapshot.getKey());
                    Log.d(TAG, "areaId:--------- " + areaSnapshot.getKey());

                }
                showAreaofInterest(areaExpertise, areaId);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    void showAreaofInterest(final ArrayList<String> areaExpertise, final ArrayList<String> areaId) {

        Log.d(TAG, "inside func" + areaExpertise.size());
        Button selectArea = getView().findViewById(R.id.button_area_of_interest);

        selectArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = 0;
                Log.d(TAG, "inside!!!");

                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_area_of_interest, null);

                ScrollView sv = mView.findViewById(R.id.area_expertise_scrollview);
                final LinearLayout ll = new LinearLayout(getActivity());
                ll.setOrientation(LinearLayout.VERTICAL);
                sv.addView(ll);

                while (i < areaExpertise.size()) {
                    Log.d(TAG, "in while!!!");
                    CheckBox ch = new CheckBox(getActivity());
                    ch.setText((CharSequence) areaExpertise.get(i));
                    ch.setId(i);
                    ch.setTag(areaId.get(i));
                    ll.addView(ch);
                    i++;
                }

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        GroupAreaId.clear();
                        Log.d(TAG, "size: " + areaExpertise.size());
                        int k = 0;
                        while (k < areaExpertise.size()) {
                            CheckBox ch = (CheckBox) mView.findViewById(k);
                            if (ch.isChecked()) {
                                GroupAreaId.add(String.valueOf(ch.getTag()));
                                Log.d(TAG, "area selected" + GroupAreaId.get(0));
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

    private void getRequiredFac(final ArrayList<String> areaId) {
        final ArrayList<String> requiredFacId = new ArrayList<>();
        DatabaseReference facRef;
        Log.d(TAG,"area0!!!!#####______--------> "+areaId.get(0));
        facRef = FirebaseDatabase.getInstance().getReference().child("Faculty");
        requiredFac.clear();
        facRef.addValueEventListener(new ValueEventListener() {
            Faculty faculty;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot facSnapshot : dataSnapshot.getChildren())
                {
                    faculty = facSnapshot.getValue(Faculty.class);
                    requiredFacId.clear();
                    for (int i = 0; i < areaId.size(); i++)
                    {
                        if (!(faculty.getAreas() == null) && faculty.getAreas().contains(areaId.get(i)))
                        {
                            // && faculty.getCount() <= faculty.getLimit()
                            requiredFac.add(faculty);
                            requiredFacId.add(faculty.getUid());
                            break;
                        }
                    }

                }
                if(requiredFacId.isEmpty())
                    Toast.makeText(getContext(),"No Faculty With Selected Domains!!!",Toast.LENGTH_LONG).show();
                else
                {
                    Log.d(TAG,"INSIDE ELSE STATEMENT!!!");
                    setspinners(requiredFac, areaId);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setspinners(final ArrayList<Faculty> requiredFac, final ArrayList<String> areaId) {

        Log.d(TAG,"Inside Setspinner");
        ArrayList<String> prefArray = new ArrayList<>();

        final Button moreFac = (Button) getView().findViewById(R.id.button_moreFac);

        final Spinner spinnerFac1 = getView().findViewById(R.id.spinner_fac1);
        final Spinner spinnerFac2 = getView().findViewById(R.id.spinner_fac2);
        final Spinner spinnerFac3 = getView().findViewById(R.id.spinner_fac3);
        final Spinner spinnerFac4 = getView().findViewById(R.id.spinner_fac4);
        final Spinner spinnerFac5 = getView().findViewById(R.id.spinner_fac5);

        moreFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerFac2.getVisibility() == View.GONE)
                    spinnerFac2.setVisibility(View.VISIBLE);
                else if (spinnerFac2.getVisibility() == View.VISIBLE && spinnerFac3.getVisibility() == View.GONE)
                    spinnerFac3.setVisibility(View.VISIBLE);
                else if (spinnerFac3.getVisibility() == View.VISIBLE && spinnerFac4.getVisibility() == View.GONE)
                    spinnerFac4.setVisibility(View.VISIBLE);
                else if (spinnerFac4.getVisibility() == View.VISIBLE && spinnerFac5.getVisibility() == View.GONE)
                    spinnerFac5.setVisibility(View.VISIBLE);
            }
        });

        final ArrayList<String> items = new ArrayList<>();
        int j = 0;
        while (j < this.requiredFac.size()) {
            items.add(this.requiredFac.get(j).getNameof());
            j++;
        }
        items.add("Select Faculty");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        spinnerFac1.setAdapter(adapter);
        spinnerFac2.setAdapter(adapter);
        spinnerFac3.setAdapter(adapter);
        spinnerFac4.setAdapter(adapter);
        spinnerFac5.setAdapter(adapter);
        spinnerFac1.setSelection(items.size() - 1);
        spinnerFac2.setSelection(items.size() - 1);
        spinnerFac3.setSelection(items.size() - 1);
        spinnerFac4.setSelection(items.size() - 1);
        spinnerFac5.setSelection(items.size() - 1);

        Button placeRequest = getView().findViewById(R.id.button_place_request);
        placeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPref(requiredFac, areaId);
            }
        });

    }

    void getPref(ArrayList<Faculty> requiredFac, ArrayList<String> areaId) {

        EditText topic = getView().findViewById(R.id.project_topic_name);
        EditText desc = getView().findViewById(R.id.project_topic_desc);
        final Spinner spinnerFac1 = getView().findViewById(R.id.spinner_fac1);
        final Spinner spinnerFac2 = getView().findViewById(R.id.spinner_fac2);
        final Spinner spinnerFac3 = getView().findViewById(R.id.spinner_fac3);
        final Spinner spinnerFac4 = getView().findViewById(R.id.spinner_fac4);
        final Spinner spinnerFac5 = getView().findViewById(R.id.spinner_fac5);
        ArrayList<String> Facpref = new ArrayList<>();
        if (!Objects.equals(spinnerFac1.getSelectedItem().toString(), "Select Faculty"))
            Facpref.add((String) spinnerFac1.getSelectedItem());

        if (!Objects.equals(spinnerFac2.getSelectedItem().toString(), "Select Faculty"))
            Facpref.add((String) spinnerFac2.getSelectedItem());

        if (!Objects.equals(spinnerFac3.getSelectedItem().toString(), "Select Faculty"))
            Facpref.add((String) spinnerFac3.getSelectedItem());

        if (!Objects.equals(spinnerFac4.getSelectedItem().toString(), "Select Faculty"))
            Facpref.add((String) spinnerFac4.getSelectedItem());

        if (!Objects.equals(spinnerFac5.getSelectedItem().toString(), "Select Faculty"))
            Facpref.add((String) spinnerFac5.getSelectedItem());

        String projectTopic = topic.getText().toString();
        String projectDesc = desc.getText().toString();
        createRequest(requiredFac, areaId, Facpref, projectTopic, projectDesc);
    }

    private void createRequest(ArrayList<Faculty> requiredFac, ArrayList<String> areaId, ArrayList<String> facpref, String projectTopic, String projectDesc) {
        int i, k;
        for (i = 0; i < facpref.size(); i++) {
            for (k = 0; k < requiredFac.size(); k++) {
                if (Objects.equals(facpref.get(i), requiredFac.get(k).getNameof()))
                    facpref.set(i, requiredFac.get(k).getUid());
            }
        }

        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
        final String key = requestRef.push().getKey();
        for (i = 0; i < facpref.size(); i++) {
            requestRef.child(key).child("faculties").child(String.valueOf(i)).setValue(facpref.get(facpref.size() - i - 1));
        }
        for (i = 0; i < areaId.size(); i++) {
            requestRef.child(key).child("areas").child(String.valueOf(i)).setValue(areaId.get(i));
        }
        requestRef.child(key).child("description").setValue(projectDesc);
        requestRef.child(key).child("topic").setValue(projectTopic);
        requestRef.child(key).child("requestid").setValue(key);
        requestRef.child(key).child("status").setValue("PENDING");
        requestRef.child(key).child("groupid").setValue(stu.getGroupid());
        showExistingProj();
    }

    private void isHasProject() {
        Log.d(TAG, "Inside isHasProject");

        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    RequestQueue requestQueue = requestSnapshot.getValue(RequestQueue.class);
                    if (Objects.equals(requestQueue.getGroupid(), stu.getGroupid())) {
                        hasProject = true;
                        showExistingProj();
                        break;
                    }
                }
                if (!hasProject)
                    getAreaOfInterest();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isHasAcceptProject() {
        Log.d(TAG, "Inside isHasAcceptProject");

        DatabaseReference acceptRef = FirebaseDatabase.getInstance().getReference().child("AcceptProject");
        acceptRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot acceptSnapshot : dataSnapshot.getChildren()) {
                    AcceptProject acceptProject = acceptSnapshot.getValue(AcceptProject.class);
                    if (Objects.equals(acceptProject.getGroupid(), stu.getGroupid())) {
                        hasAcceptedProj = true;
                        showAcceptProj();
                        break;
                    }
                }
                if (!hasProject)
                    isHasRejectedProject();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isHasRejectedProject() {
        Log.d(TAG, "Inside isHasAcceptProject");

        DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
        reqRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren())
                {
                    RequestQueue requestQueue = requestSnapshot.getValue(RequestQueue.class);
                    if (Objects.equals(requestQueue.getGroupid(), stu.getGroupid()))
                    {
                        if(Objects.equals(requestQueue.getStatus(), "REJECTED"))
                        {
                            hasRejectedProj = true;
                            Toast.makeText(getContext(),"Your Previous Request Has Been Completely Rejected." +
                                    "Please Create New Project Request",Toast.LENGTH_LONG).show();
                            removeRejectedRequest(requestQueue.getRequestid());
                            getAreaOfInterest();
                            break;
                        }
                    }
                }
                if (!hasRejectedProj)
                    isHasProject();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void removeRejectedRequest(String requestid)
    {
        DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
        reqRef.child(requestid).getRef().removeValue();
    }

    private void showAcceptProj()
    {
        Log.d(TAG, "Inside showAcceptProj");
        View linearLayout = getView().findViewById(R.id.place_request_linear_layout);
        linearLayout.setVisibility(View.GONE);
        Button createGroupButton = getView().findViewById(R.id.create_group_button);
        createGroupButton.setVisibility(View.GONE);

        View ll = getView().findViewById(R.id.existing_request_linear_layout);
        ll.setVisibility(View.VISIBLE);

        final TextView showTopic = getView().findViewById(R.id.show_topic);
        final TextView showDesc = getView().findViewById(R.id.show_desc);
        final TextView showStatus = getView().findViewById(R.id.show_status);
        final LinearLayout facLayout = getView().findViewById(R.id.show_faculty);
        final ArrayList<String> areaList = new ArrayList<>();
        final ArrayList<String> facList = new ArrayList<>();
        DatabaseReference acceptRef = FirebaseDatabase.getInstance().getReference().child("AcceptProject");
        acceptRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i;
                for (DataSnapshot acceptSnapshot : dataSnapshot.getChildren()) {
                    AcceptProject acceptProject = acceptSnapshot.getValue(AcceptProject.class);
                    if (Objects.equals(acceptProject.getGroupid(), stu.getGroupid()))
                    {
                        showTopic.setText(acceptProject.getTopic());
                        showDesc.setText(acceptProject.getDescription());
                        showStatus.setText("ACCEPTED");
                        showStatus.setTextColor(GREEN);
                        Log.d(TAG, "areasize========" + acceptProject.getAreas().size());
                        areaList.clear();
                        for (i = 0; i < acceptProject.getAreas().size(); i++) {
                            areaList.add(acceptProject.getAreas().get(i));
                        }
                        facList.add(acceptProject.getFaculty());
                        getAreaNames(areaList,facList);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showExistingProj() {

        Log.d(TAG, "Inside showExistingProj");

        View linearLayout = view.findViewById(R.id.place_request_linear_layout);
        linearLayout.setVisibility(View.GONE);

        Button createGroupButton = getView().findViewById(R.id.create_group_button);
        createGroupButton.setVisibility(View.GONE);

        View ll = getView().findViewById(R.id.existing_request_linear_layout);
        ll.setVisibility(View.VISIBLE);

        final TextView showTopic = getView().findViewById(R.id.show_topic);
        final TextView showDesc = getView().findViewById(R.id.show_desc);
        final TextView showStatus = getView().findViewById(R.id.show_status);

        final ArrayList<String> areaList = new ArrayList<>();
        final ArrayList<String> facList = new ArrayList<>();
        DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areaList.clear();
                facList.clear();
                int i;
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    RequestQueue requestQueue = requestSnapshot.getValue(RequestQueue.class);

                    if (Objects.equals(requestQueue.getGroupid(), stu.getGroupid()) && (requestQueue.getFaculties() != null)) {
                        showTopic.setText(requestQueue.getTopic());
                        showDesc.setText(requestQueue.getDescription());
                        showStatus.setText(requestQueue.getStatus());
                        showStatus.setTextColor(RED);
                        Log.d(TAG, "areasize========" + requestQueue.getAreas().size());
                        for (i = 0; i < requestQueue.getAreas().size(); i++) {
                            areaList.add(requestQueue.getAreas().get(i));
                        }
//                        Log.d(TAG, "facsize========" + requestQueue.getFaculties().size());
                        for (i = 0; i < requestQueue.getFaculties().size(); i++) {
                            facList.add(requestQueue.getFaculties().get(i));
                        }
                        getAreaNames(areaList, facList);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getAreaNames(final ArrayList<String> areaList, final ArrayList<String> facList)
    {
        DatabaseReference areaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");
        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String area;
                int i;
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    area = areaSnapshot.getValue(String.class);
                    Log.d(TAG, "areaval----------" + area);
                    for (i = 0; i < areaList.size(); i++) {
                        if (Objects.equals(areaList.get(i), areaSnapshot.getKey()))
                            areaList.set(i, area);
                    }
                }
                Log.d(TAG, "areasize1========" + areaList.size());
                Log.d(TAG, "facsize1========" + facList.size());
                getFacNames(areaList, facList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getFacNames(final ArrayList<String> areaList, final ArrayList<String> facList)
    {
        DatabaseReference facRef = FirebaseDatabase.getInstance().getReference().child("Faculty");
        facRef.addValueEventListener(new ValueEventListener() {
            int i;
            Faculty faculty;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot facSnapshot : dataSnapshot.getChildren()) {
                    faculty = facSnapshot.getValue(Faculty.class);
                    Log.d(TAG,"FACSIZE!!!____>" + facList.size());
                    for (i = 0; i < facList.size(); i++) {
                        if (Objects.equals(faculty.getUid(), facList.get(i)))
                            facList.set(i, faculty.getNameof());
                    }
                }

                Log.d(TAG, "areasize2========" + areaList.size());
                Log.d(TAG, "facsize2========" + facList.size());
                setFields(areaList, facList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setFields(ArrayList<String> areaList, ArrayList<String> facList) {

        Log.d(TAG, "areasize3========" + areaList.size());
        Log.d(TAG, "facsize3========" + facList.size());
        final LinearLayout facLayout = getView().findViewById(R.id.show_faculty);
        final LinearLayout areaLayout = getView().findViewById(R.id.show_areas);
        int i;
        areaLayout.removeAllViews();
        facLayout.removeAllViews();

        for (i = 0; i < areaList.size(); i++) {

            LinearLayout ll1 = new LinearLayout(getActivity());
            ll1.setOrientation(LinearLayout.HORIZONTAL);
            TextView ar = new TextView(getActivity());
            ar.setText("Project Area " + (i + 1) + ":");
            ar.setTextSize(20);
            ar.setPadding(10, 10, 10, 10);

            TextView area = new TextView(getActivity());
            area.setText(areaList.get(i));
            area.setTextSize(20);
            area.setPadding(10, 10, 10, 10);

            ll1.addView(ar);
            ll1.addView(area);

            areaLayout.addView(ll1);
        }

        for (i = 0; i < facList.size(); i++) {
            LinearLayout ll1 = new LinearLayout(getActivity());
            ll1.setOrientation(LinearLayout.HORIZONTAL);

            TextView fac1 = new TextView(getActivity());
            fac1.setText("Preference " + (i + 1) + ":");
            fac1.setTextSize(20);
            fac1.setPadding(10, 10, 10, 10);

            TextView fac = new TextView(getActivity());
            fac.setText(facList.get(facList.size() - i - 1));
            fac.setTextSize(20);
            fac.setPadding(10, 10, 10, 10);

            ll1.addView(fac1);
            ll1.addView(fac);

            facLayout.addView(ll1);
        }
    }


}
