package in.ac.nitc.projectallocator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class FacultyProjectFragment extends Fragment {

    public FacultyProjectFragment() {
    }

    DatabaseReference AcceptProjects;
    ArrayList<AcceptProject> ProjectList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_faculty_project, container, false);
        AcceptProjects = FirebaseDatabase.getInstance().getReference().child("AcceptProject");

        AcceptProjects.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    Log.d(TAG, "NULL!!!");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();
                ProjectList.clear();
                for (DataSnapshot projectSnapshot : dataSnapshot.getChildren()) {
                    if (projectSnapshot.child("faculty").getValue().equals(uid)) {
                        ProjectList.add(projectSnapshot.getValue(AcceptProject.class));
                        Log.d(TAG, " faculty project list" + ProjectList.size());

                    }

                }
                Log.d(TAG, " Calling Adapter");
                ListView ListField = (ListView) view.findViewById(R.id.faculty_project);
                ListField.setAdapter(new AcceptProjectAdapter(getContext(), R.layout.fragment_faculty_project, ProjectList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }
}

