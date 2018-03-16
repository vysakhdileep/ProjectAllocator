package in.ac.nitc.projectallocator;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isLoggedIn();


    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.d(TAG, "Calling on resume");
        Boolean connectivity =isOnline(getBaseContext());
        if(connectivity == true) {
            isLoggedIn();
        }
        else
        {
            Log.d(TAG, "No connection");
            Toast.makeText(MainActivity.this, "No connectivity.....",
                    Toast.LENGTH_LONG).show();

        }
    }


    public void onNetworkConnectionChanged(boolean isConnected) {
        if(!isConnected) {

            Log.d(TAG, "In Listener");
            //show a No Internet Alert or Dialog

        }else{
            Log.d(TAG, "In Listener net exist");
            Toast.makeText(MainActivity.this, "Connected to network", Toast.LENGTH_SHORT).show();
            isLoggedIn();
            // dismiss the dialog or refresh the activity
        }
    }


    private void isLoggedIn() {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d(TAG, "No logged in");
            Intent myIntent = new Intent(this, SignIn.class);
            this.startActivity(myIntent);
            return ;

        } else {
            Log.d(TAG, "login exist");
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            DatabaseReference FacultyRef = database.child("Faculty");
            DatabaseReference StudentRef = database.child("Student");
            final String uid = user.getUid();
            StudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uid).exists()) {
                        Log.d(TAG, "Student exist");
                        StudentExist();
                        return;
                    } else {
                        Log.d(TAG, "Student does not exist");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "In onCancelled Student");
                }
            });
            FacultyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uid).exists()) {
                        Log.d(TAG, "Faculty exist");
                        FacultyExist();
                        return;
                    } else {
                        Log.d(TAG, "Faculty does not exist");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "In onCancelled Faculty");
                }
            });

        }
        return;
    }
    private void StudentExist() {
        Log.d(TAG, "In student exist");
        Log.d(TAG, "Start Student Intent");
        Intent myIntent = new Intent(this, StudentMain.class);
        this.startActivity(myIntent);
        checkNotification();
        return;
    }

    private void FacultyExist() {
        Log.d(TAG, "In faculty exist");
        Log.d(TAG, "Start Faculty Intent");
        Intent myIntent = new Intent(this, FacultyMain.class);
        this.startActivity(myIntent);
        return;
    }

    private void checkNotification(){


    }
    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}
