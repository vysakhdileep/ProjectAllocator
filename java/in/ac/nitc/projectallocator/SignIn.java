package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private static final String TAG = "LogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mEmailField = findViewById(R.id.email_id);
        mPasswordField = findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }
        @Override
        public void onClick (View view) {
            signIn();
    }


    private void signIn( ) {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return ;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference FacultyRef = database.child("Faculty");
                        DatabaseReference StudentRef = database.child("Student");
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            final String uid = user.getUid();
                            Log.d(TAG, "UserID:" + uid);
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return ;
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


    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
        }
    }



