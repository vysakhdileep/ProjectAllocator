package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        isLoggedIn();

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
        return;
    }

    private void FacultyExist() {
        Log.d(TAG, "In faculty exist");
        Log.d(TAG, "Start Faculty Intent");
        Intent myIntent = new Intent(this, FacultyMain.class);
        this.startActivity(myIntent);
        return;
    }

}
