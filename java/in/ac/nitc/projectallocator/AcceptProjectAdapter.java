package in.ac.nitc.projectallocator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by VysakhDileep on 16-03-2018.
 */

public class AcceptProjectAdapter extends ArrayAdapter<AcceptProject> {

    ArrayList<String> area_expertise = new ArrayList<>();
    View acceptItemView;

    public AcceptProjectAdapter(@NonNull Context context, int resource, ArrayList<AcceptProject> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        acceptItemView = convertView;

        Log.d(TAG," In Adapter  1");
        if (acceptItemView == null) {
            acceptItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.accept_list_item, parent, false);
        }
        final AcceptProject currProject =getItem(position);
        TextView facultyid = (TextView) acceptItemView.findViewById(R.id.accept_facultyid);
        facultyid.setText(currProject.getFaculty());

        TextView groupid = (TextView) acceptItemView.findViewById(R.id.accept_groupid);
        groupid.setText(currProject.getGroupid());
        Log.d(TAG," Size of Areas"+ currProject.getAreas().size());

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot) {
                Log.d(TAG," In listener firebase");
                if (dataSnapshot.exists()) {
                    for (int i = 0; i < currProject.getAreas().size(); i++) {
                        Log.d(TAG," In listener firebase loop "+dataSnapshot.child(currProject.getAreas().get(i)).getValue(String.class));
                        area_expertise.add(dataSnapshot.child(currProject.getAreas().get(i)).getValue(String.class));

                    }
                    for (int i = 0; i < area_expertise.size(); i++)
                        printArea((LinearLayout) acceptItemView.findViewById(R.id.accept_areas),area_expertise.get(i));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return acceptItemView;
    }

    private void printArea(LinearLayout listItemView, String acceptArea) {
        Log.d(TAG, "Area: " + acceptArea);
        TextView textView = new TextView(getContext());
        textView.setText(acceptArea);
        listItemView.addView(textView);
    }
}
