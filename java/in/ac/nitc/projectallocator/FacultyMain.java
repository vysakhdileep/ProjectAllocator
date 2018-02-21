package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

public class FacultyMain extends AppCompatActivity {
    private static final String TAG = "LogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        Log.d(TAG, "Inside Faculty " );
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(this, SignIn.class);
        this.startActivity(myIntent);

    }
}
