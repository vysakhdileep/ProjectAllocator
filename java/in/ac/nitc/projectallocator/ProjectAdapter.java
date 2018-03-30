package in.ac.nitc.projectallocator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * Created by jsnkrm on 01/03/18.
 */

public class ProjectAdapter extends ArrayAdapter<ProjectIdeas> {

    final String TAG = "ProjectAdapter";

    public ProjectAdapter(@NonNull Context context, int resource, ArrayList<ProjectIdeas> objects) {
        super(context, resource, objects);

    }


    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        DatabaseReference database, FacRef;
        database = FirebaseDatabase.getInstance().getReference();


        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.project_list_item, parent, false);
        }
        final ProjectIdeas currProject = getItem(position);


        final TextView projFacId = (TextView) listItemView.findViewById(R.id.project_list_projFacId);

        String uid = currProject.getFacultyid();
        Log.d(TAG, "uid" + currProject.getFacultyid());
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
        TextView projDesc = listItemView.findViewById(R.id.project_list_projDesc);


        projName.setText(currProject.getTopic());
        projDesc.setText(currProject.getDescription());
        final ArrayList<String> areas = currProject.getAreas();
        final ArrayList<String> areaId = currProject.getAreas();
        Log.d(TAG,"area0-----------" + areas.get(0));
        Log.d(TAG,"areaID0-----------" + areaId.get(0));

        DatabaseReference areaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");
        final View finalListItemView = listItemView;
        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String area;
                int i;
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    area = areaSnapshot.getValue(String.class);
                    for (i = 0; i < areas.size(); i++) {
                        if (Objects.equals(areaSnapshot.getKey(), areas.get(i))) {
                            areas.set(i, area);
                        }
                    }
                }
                setareas(areas, (LinearLayout) finalListItemView,currProject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return listItemView;
    }

    private void setareas(ArrayList<String> areas, LinearLayout listItemView, ProjectIdeas currProject)
    {
        final LinearLayout projarea = listItemView.findViewById(R.id.project_list_areas);
        int i;
        for (i=0;i<areas.size();i++)
        {
            LinearLayout ll = new LinearLayout(getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            TextView area = new TextView(getContext());
            area.setText("Project Area " + (i+1) + ":");
            area.setPadding(15,15,15,15);
            area.setTextSize(15);

            TextView area1 = new TextView(getContext());
            area1.setText(areas.get(i));
            area1.setPadding(15,15,15,15);
            area1.setTextSize(15);

            ll.addView(area);
            ll.addView(area1);

            projarea.addView(ll);
        }

        checkGrpId(areas,listItemView,currProject);
    }

    private void checkGrpId(final ArrayList<String> areas, LinearLayout listItemView, final ProjectIdeas currProject)
    {
        FirebaseUser user;
        String StuUid;
        user = FirebaseAuth.getInstance().getCurrentUser();
        StuUid = user.getUid();
        Log.d(TAG,"area0!!!!!!!!!!!!!"+areas.get(0));

        DatabaseReference StuRef = FirebaseDatabase.getInstance().getReference().child("Student").child(StuUid);
        final View finalListItemView = listItemView;
        StuRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Student student = dataSnapshot.getValue(Student.class);
                String stuGrpId = student.getGroupid();
                if(stuGrpId != null)
                {
                    Log.d(TAG,"Student GID!!!!!!!!!!!->" + stuGrpId);
                        getAreaId(areas,finalListItemView,stuGrpId,currProject);
                }
                else
                    Toast.makeText(getContext(),"Please Create Group First!!!!",Toast.LENGTH_LONG);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getAreaId(final ArrayList<String> areas, final View finalListItemView, final String stuGrpId, final ProjectIdeas currProject)
    {
        DatabaseReference areaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");
        areaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String area;
                int i;
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    area = areaSnapshot.getValue(String.class);
                    for (i = 0; i < areas.size(); i++) {
                        if (Objects.equals(area, areas.get(i))) {
                            areas.set(i, areaSnapshot.getKey());
                        }
                    }
                }
                placeNewRequest(areas, (LinearLayout) finalListItemView,stuGrpId,currProject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void placeNewRequest(final ArrayList<String> areaId, View finalListItemView, final String stuGrpId, final ProjectIdeas currProject)
    {
        Log.d(TAG,"INSIDE placeNewReq");
        Log.d(TAG,"outside area0?????????->"+ areaId.get(0));
        Button requestButton = finalListItemView.findViewById(R.id.request_facProject);
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                        for(j=0;j<areaId.size();j++)
                        {
                            requestRef.child(key).child("areas").child(String.valueOf(j)).setValue(areaId.get(j));
                            Log.d(TAG,"area " + j +"**************" + areaId.get(j));
                        }

                        requestRef.child(key).child("description").setValue(currProject.getDescription());
                        requestRef.child(key).child("faculties").child("0").setValue(currProject.getFacultyid());
                        requestRef.child(key).child("groupid").setValue(stuGrpId);
                        requestRef.child(key).child("status").setValue("PENDING");
                        requestRef.child(key).child("topic").setValue(currProject.getTopic());
                    }
                });
                alert.setView(mView);
                alert.show();
            }
        });
    }
}
