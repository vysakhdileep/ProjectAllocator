package in.ac.nitc.projectallocator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsnkrm on 01/03/18.
 */

public class ProjectAdapter extends ArrayAdapter<ProjectIdeas> {

    public ProjectAdapter(@NonNull Context context, int resource, ArrayList<ProjectIdeas> objects) {
        super(context, resource, objects);

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String TAG = "ProjectAdapter";
        DatabaseReference database,FacRef;
        database = FirebaseDatabase.getInstance().getReference();


        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.project_list_item, parent, false);
        }
        final ProjectIdeas currProject = getItem(position);


        final TextView projFacId = (TextView) listItemView.findViewById(R.id.project_list_projFacId);

        String uid = currProject.getFacultyId();
        Log.d(TAG,"uid" + currProject.getFacultyId());
        FacRef = database.child("Faculty").child(uid);

        FacRef.addValueEventListener(new ValueEventListener() {
            Faculty faculty;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                faculty = dataSnapshot.getValue(Faculty.class);
                    projFacId.setText(faculty.getNameof());
                return;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView projName = listItemView.findViewById(R.id.project_list_projname);
        TextView projarea = listItemView.findViewById(R.id.project_list_areas);


        projName.setText(currProject.getProjectName());
        ArrayList<String> areas = currProject.getAreas();

        return listItemView;
    }
}
