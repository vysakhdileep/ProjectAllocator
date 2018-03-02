package in.ac.nitc.projectallocator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddFacultyProjectFragment extends Fragment {

    private static final String TAG = "AddFacProjectFrag";
    View view;
    String uid;
    FirebaseUser user;
    int pos;


    ArrayList<String> ExpertiseKey = new ArrayList<>();      //Contain all Expertise Key
    ArrayList<String> ExpertiseValue = new ArrayList<String>();      //Contain all Expertise Value

    DatabaseReference AreaRef,ProjectIdeas;

    public AddFacultyProjectFragment() {
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

       view = inflater.inflate(R.layout.fragment_add_faculty_project, container, false);
        getAreaExpertise();

        Button CreateProject = view.findViewById(R.id.create_project);
        CreateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText name = (EditText) view.findViewById(R.id.name_project);
                //String temp = name.getText().toString();
                user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();
                ProjectIdeas = FirebaseDatabase.getInstance().getReference().child("ProjectIdeas");
                String key =  ProjectIdeas.push().getKey();

                ProjectIdeas.child(key).child("Name").setValue("Project");
                ProjectIdeas.child(key).child("facultyid").setValue(uid);
                ProjectIdeas.child(key).child("areas").child("0").setValue(ExpertiseKey.get(pos));
            }
        });
        return view;
    }





    public void getAreaExpertise(){
        AreaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");

        AreaRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    ExpertiseKey.add(snapshot.getKey());
                    ExpertiseValue.add(snapshot.getValue(String.class));
                    Log.d(TAG,"Key is "+snapshot.getKey());
                    Log.d(TAG, "Value is "+snapshot.getValue());
                }
                Spinner dropdown = (Spinner) view.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, ExpertiseValue);
                dropdown.setAdapter(adapter);
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parentView,
                                               View selectedItemView, int position, long id) {
                        // Object item = parentView.getItemAtPosition(position);

                        pos = position;
                        Log.d(TAG,"position "+pos);

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {// do nothing
                    }

                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }
}
