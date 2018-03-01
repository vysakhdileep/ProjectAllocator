package in.ac.nitc.projectallocator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewRequestFragment extends Fragment {

    private static final String TAG = "Test";
    private DatabaseReference mDatabase;
    ArrayList<request> requestList;
    ArrayList<String> requestFacultyprojects;
         public ViewRequestFragment() {
        // Required empty public constructor
        }


       @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);


        }

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState){
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_view_request, container, false);
        }

    public void getRequestData()
    {
        Log.d(TAG, "Requesting ");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference requestQueueRef = mDatabase.child("RequestQueue");
        final ArrayList<request> requestArrayList = new ArrayList<>();


        ValueEventListener requestListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot requestsnapshot : dataSnapshot.getChildren())
                {
                    Log.d(TAG, "Receieved snapshot ");
                    requestList.add(requestsnapshot.getValue(request.class));


                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                int i=0;
                final String uid = user.getUid();
                while(i < requestList.size())
                {
                    int j=0;
                    while(j < requestList.get(i).faculties.size())
                    {
                        if(uid.equals(requestList.get(i).faculties.get(j).toString()))
                        {
                            requestFacultyprojects.add(requestList.get(i).faculties.get(j).toString());
                        }
                        j++;
                    }
                    i++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "LoadRequest:onCancelled", databaseError.toException());
                // ...
            }

        };

        requestQueueRef.addValueEventListener(requestListener);


    }
    }


