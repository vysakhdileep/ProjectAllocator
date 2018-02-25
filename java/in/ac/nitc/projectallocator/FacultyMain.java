package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class FacultyMain extends AppCompatActivity {
    private static final String TAG = "FacultyMain";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        Log.d(TAG, "Inside Faculty ");
    }


    public void signOut(View view)
    {
        Log.d(TAG, "Signing Out ");
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(this, SignIn.class);
        this.startActivity(myIntent);
    }

    public void projectRequestHandler(View view)
    {
        getRequestData();
    }

    public void getRequestData()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference requestQueueRef = mDatabase.child("RequestQueue");
        final ArrayList<request> requestList = new ArrayList<>();


        ValueEventListener requestListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot requestsnapshot : dataSnapshot.getChildren())
                {
                    requestList.add(requestsnapshot.getValue(request.class));
                }

                Log.d(TAG, "requestList.get(0).faculties.get(0) : " + requestList.get(1).faculties.get(2));



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
    private static long back_pressed;
    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //super.onBackPressed();
        }
        else{
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }






}

