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


public class ViewRequestFragment extends Fragment {

    private static final String TAG = "ViewRequestFrag";
    private DatabaseReference mDatabase, requestQueueRef;
    View view;
    ArrayList<RequestQueue> requestList = new ArrayList<>();
    ArrayList<RequestQueue> requestFacultyList = new ArrayList<>();

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

        requestQueueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    Log.d(TAG, "NULL!!!");
                for (DataSnapshot requestSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Received snapshot ");
                    requestList.add(requestSnapshot.getValue(RequestQueue.class));
                }
                Log.d(TAG, "Start requestList");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int i = 0;
                final String uid = user.getUid();
                while (i < requestList.size()) {
                    int j=requestList.get(i).faculties.size();
                    if(j!=0){
                        if (uid.equals(requestList.get(i).faculties.get(j-1))) {
                            Log.d(TAG, "requestList.get(): " + requestList.get(i).group);
                            requestFacultyList.add(requestList.get(i));
                        }
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


