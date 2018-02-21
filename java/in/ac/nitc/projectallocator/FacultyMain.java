package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class FacultyMain extends AppCompatActivity {
    private static final String TAG = "Faculty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        Log.d(TAG, "Inside Faculty " );
    }

    public void  signOut (View view){
        Log.d(TAG, "Signing Out " );
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(this, SignIn.class);
        this.startActivity(myIntent);
    }

    @IgnoreExtraProperties
    public class requestQueue{
        String[] areas;
        String[] groups;
        String[] faculty;
        public requestQueue() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }
        public requestQueue(String uid, String author, String title, String body) {
            this.areas = areas;
            this.groups = groups;
            this.faculty = faculty;

        }


    }

    public void projectRequest(View view){
        Log.d(TAG, "Requesting Project " );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference RequestRef = database.child("RequestQueue");
        RequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "In onCancelled Faculty");
            }
        });
    }
}
