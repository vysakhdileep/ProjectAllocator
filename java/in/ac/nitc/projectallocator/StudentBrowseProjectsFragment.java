package in.ac.nitc.projectallocator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class StudentBrowseProjectsFragment extends Fragment{

    final String TAG = "StuBrowseProjectFrag";

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

        Log.d(TAG,"inside browse");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.project_list, container, false);

        database = FirebaseDatabase.getInstance().getReference();
        ProjectIdeasRef = database.child("ProjectIdeas");
        Log.d(TAG,ProjectIdeasRef.toString());


        ProjectIdeasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG,"llalala");
                if(!dataSnapshot.exists())
                    Log.d(TAG,"NULL!!!");
                for(DataSnapshot projectIdeasSnapshot : dataSnapshot.getChildren())
                {

                    Log.d(TAG, "Receieved snapshot ");
                    projects.add(projectIdeasSnapshot.getValue(ProjectIdeas.class));
                    getProjects(projects);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    public void  getProjects(ArrayList<ProjectIdeas> projectIdeas)
    {
        ProjectAdapter projectAdapter = new ProjectAdapter(getActivity(),R.color.background_color,projectIdeas);

        ListView listView = (ListView) getView().findViewById(R.id.list);

        listView.setAdapter(projectAdapter);

    }

}