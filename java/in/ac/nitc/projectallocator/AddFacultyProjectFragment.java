package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class AddFacultyProjectFragment extends Fragment {

    private static final String TAG = "AddFacProjectFrag";
    View view;
    String uid;
    FirebaseUser user;
    Integer i = 0;

    ArrayList<Integer> Values =new ArrayList<>();

    ArrayList<String> ExpertiseKey = new ArrayList<>();      //Contain all Expertise Key
    ArrayList<String> ExpertiseValue = new ArrayList<String>();      //Contain all Expertise Value

    DatabaseReference AreaRef, ProjectIdeas;

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
        Log.d(TAG," Area Expertise");
        getAreaExpertise();

        Button CreateProject = view.findViewById(R.id.submit_project);
        CreateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mview) {
                EditText project_name = (EditText) view.findViewById(R.id.name_project);

                String temp = project_name.getText().toString();
                if(temp == null)
                {
                    Toast.makeText(getContext(), "Enter valid Project name",
                            Toast.LENGTH_SHORT).show();
                }
                if(Values.size() ==0)
                {
                    Toast.makeText(getContext(), "Enter area Expertise",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    uid = user.getUid();
                    ProjectIdeas = FirebaseDatabase.getInstance().getReference().child("ProjectIdeas");
                    String key = ProjectIdeas.push().getKey();

                    ProjectIdeas.child(key).child("Name").setValue(temp);
                    ProjectIdeas.child(key).child("facultyid").setValue(uid);

                    Integer t =0;
                    while(t < Values.size())

                    {
                        ProjectIdeas.child(key).child("areas").child(t.toString()).setValue(ExpertiseKey.get(Values.get(t)));
                        t++;

                    }

                    Toast.makeText(getContext(), "Sucessfully created project..",
                            Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(view.getContext(),FacultyMain.class);
                    getActivity().startActivity(myIntent);

                }
            }
        });
        return view;
    }


    public void getAreaExpertise() {
        AreaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");
        Log.d(TAG," Fetching from databse");
        AreaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExpertiseKey.add(snapshot.getKey());
                    ExpertiseValue.add(snapshot.getValue(String.class));
                }

                Log.d(TAG," Checkbox setting.......");
                CheckBox checkBox;
                LinearLayout linearMain;
                linearMain = (LinearLayout) view.findViewById(R.id.linear_checkbox);


                Log.d(TAG," Values "+i+" "+ ExpertiseValue.size());
                while (i < ExpertiseValue.size()) {
                    Log.d(TAG," Inside loop"+i);
                    checkBox = new CheckBox(getContext());
                    checkBox.setId(i);
                    checkBox.setText(ExpertiseValue.get(i));
                    checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
                    linearMain.addView(checkBox);
                    i++;
                }
            }


            View.OnClickListener getOnClickDoSomething(final Button button) {
                Log.d(TAG," in listener   "+button.getId()+ "  "+button.getText().toString());
                return new View.OnClickListener() {
                    public void onClick(View v) {
                        Values.add(button.getId());
                        Log.d(TAG,+button.getId()+"  "+button.getText().toString());


                    }
                };

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });



    }




}


