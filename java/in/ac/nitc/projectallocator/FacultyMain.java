package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;


public class FacultyMain extends AppCompatActivity {
    private static final String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        Log.d(TAG, "Inside Faculty ");
    }


    public void signOut(View view) {
        Log.d(TAG, "Signing Out ");
        FirebaseAuth.getInstance().signOut();
        Intent myIntent = new Intent(this, SignIn.class);
        this.startActivity(myIntent);
    }




}

