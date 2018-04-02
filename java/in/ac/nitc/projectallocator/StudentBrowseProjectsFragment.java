package in.ac.nitc.projectallocator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class StudentBrowseProjectsFragment extends Fragment {

    final String TAG = "StuBrowseProjectFrag";
    ProjectIdeas currentProj;


    DatabaseReference database, ProjectIdeasRef;
    ArrayList<ProjectIdeas> projects = new ArrayList<ProjectIdeas>();


    public StudentBrowseProjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "inside browse");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.project_list, container, false);

        database = FirebaseDatabase.getInstance().getReference();
        ProjectIdeasRef = database.child("ProjectIdeas");
        Log.d(TAG, ProjectIdeasRef.toString());


        ProjectIdeasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "OnDataChange");
                if (!dataSnapshot.exists())
                    Log.d(TAG, "NULL!!!");
                for (DataSnapshot projectIdeasSnapshot : dataSnapshot.getChildren()) {

                    Log.d(TAG, "Receieved snapshot ");
                    projects.add(projectIdeasSnapshot.getValue(ProjectIdeas.class));
                }
                getProjects(projects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    public void getProjects(final ArrayList<ProjectIdeas> projectIdeas) {
        final ProjectAdapter projectAdapter = new ProjectAdapter(getActivity(), R.color.background_color, projectIdeas);

        final ListView listView = (ListView) getView().findViewById(R.id.list);

        listView.setAdapter(projectAdapter);

        FirebaseUser user;
        String StuUid;
        user = FirebaseAuth.getInstance().getCurrentUser();
        StuUid = user.getUid();

        DatabaseReference StuRef = FirebaseDatabase.getInstance().getReference().child("Student").child(StuUid);
        StuRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                String stuGrpId = student.getGroupid();
                if (stuGrpId != null) {
                    Log.d(TAG, "Student GID!!!!!!!!!!!->" + stuGrpId);
                    getItemFromAdapter(listView, projectAdapter,stuGrpId);
                }
                else
                    Toast.makeText(getContext(), "Please Create Group First!!!!", Toast.LENGTH_LONG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkExistingProj(final ProjectIdeas currentProj, final String stuGrpId)
    {
        DatabaseReference reqRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
        reqRef.addValueEventListener(new ValueEventListener() {
            RequestQueue requestQueue;
            boolean hasproj = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot requestSnapshot : dataSnapshot.getChildren())
                {
                    requestQueue = requestSnapshot.getValue(RequestQueue.class);
                    if (Objects.equals(requestQueue.getGroupid(), stuGrpId)) {
                        Toast.makeText(getContext(), "Project Request Already Placed!!!", Toast.LENGTH_SHORT).show();
                        hasproj = true;
                    }
                }
                    if(!hasproj)
                        getAreaId(currentProj,stuGrpId);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getItemFromAdapter(final ListView listView, final ProjectAdapter projectAdapter, final String stuGrpId)
    {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                    Log.d(TAG,"INSIDE IF!!!!!!!!!");
                    currentProj = projectAdapter.getItem(i);
                    checkExistingProj(currentProj,stuGrpId);
            }
        });
    }

    private void getAreaId(final ProjectIdeas currentProj, final String stuGrpId)
    {
        DatabaseReference areaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");
        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String area;
                int i;
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    area = areaSnapshot.getValue(String.class);
                    for (i = 0; i < currentProj.getAreas().size(); i++) {
                        if (Objects.equals(area, currentProj.getAreas().get(i))) {
                            currentProj.getAreas().set(i, areaSnapshot.getKey());
                        }
                    }
                }
                placeNewRequest(currentProj,stuGrpId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void placeNewRequest(final ProjectIdeas currentProj, final String stuGrpId)
    {
        Log.d(TAG,"INSIDE placeNewReq");
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        final View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_place_fac_proj,null);

        alert.setPositiveButton("Place Request", new DialogInterface.OnClickListener()
        {
            int j;

            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Log.d(TAG,"Inside ONCLICK ACCEPT");
                DatabaseReference requestRef = FirebaseDatabase.getInstance().getReference().child("RequestQueue");
                final String key =  requestRef.push().getKey();
                for(j=0;j<currentProj.getAreas().size();j++)
                {
                    requestRef.child(key).child("areas").child(String.valueOf(j)).setValue(currentProj.getAreas().get(j));
                    Log.d(TAG,"area " + j +"**************" + currentProj.getAreas().get(j));
                }

                requestRef.child(key).child("description").setValue(currentProj.getDescription());
                requestRef.child(key).child("faculties").child("0").setValue(currentProj.getFacultyid());
                requestRef.child(key).child("groupid").setValue(stuGrpId);
                requestRef.child(key).child("status").setValue("PENDING");
                requestRef.child(key).child("topic").setValue(currentProj.getTopic());
            }
            });
            alert.setView(mView);
            alert.show();
    }


}