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
import java.util.HashMap;

public class ViewRequestFragment extends Fragment {

    private static final String TAG = "ViewRequestFrag";
    private DatabaseReference mDatabase, requestQueueRef, areaExpertiseRef;
    View view;
    ArrayList<RequestQueue> requestList = new ArrayList<>();
    ArrayList<RequestQueue> requestFacultyList = new ArrayList<>();
    ArrayList<String> areas;
    HashMap<String, String> areaExpertise = new HashMap<>();

    public ViewRequestFragment() {
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
        Log.d(TAG, "start getRequest()");
        view = inflater.inflate(R.layout.fragment_view_request, container, false);
        getRequestData();
        Log.d(TAG, "end getRequest()");

        return view;
    }

    public void getRequestData() {
        Log.d(TAG, "Requesting ");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        requestQueueRef = mDatabase.child("RequestQueue");
        areaExpertiseRef = mDatabase.child("AreaExpertise");

        areaExpertiseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areaExpertise.clear();
                if (!dataSnapshot.exists())
                    Log.d(TAG, "NULL!!!");
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Received snapshot AreaExpertise");
                    areaExpertise.put(requestSnapshot.getKey(), requestSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "LoadRequest:onCancelled", databaseError.toException());
                // ...
            }
        });

        requestQueueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestList.clear();
                requestFacultyList.clear();
                if (!dataSnapshot.exists())
                    Log.d(TAG, "NULL!!!");
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Received snapshot RequestQueue");
                    requestList.add(requestSnapshot.getValue(RequestQueue.class));
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int i = 0;
                final String uid = user.getUid();
                while (i < requestList.size()) {
                    int j = requestList.get(i).faculties.size();
                    if (j != 0 && uid.equals(requestList.get(i).faculties.get(j - 1))) {
                        areas = new ArrayList<>();
                        for (int k = 0; k < requestList.get(i).getAreas().size(); k++) {
                            areas.add(areaExpertise.get(requestList.get(i).getAreas().get(k)));
                        }
                        requestList.get(i).areas = areas;
                        requestFacultyList.add(requestList.get(i));
                    }
                    i++;
                }
                getRequest(requestFacultyList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "LoadRequest:onCancelled", databaseError.toException());
                // ...
            }
        });
    }

    public void getRequest(ArrayList<RequestQueue> requestQueue) {
        RequestAdapter requestAdapter = new RequestAdapter(getActivity(), R.layout.request_list_item, requestQueue);
        ListView listView = (ListView) view.findViewById(R.id.listView1);
        listView.setAdapter(requestAdapter);
    }
}


