package in.ac.nitc.projectallocator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.provider.Telephony.Carriers.PASSWORD;


public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private static View view;
    private static CheckBox show_hide_password;
    private static EditText emailid, password;
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private static final String TAG = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mEmailField = findViewById(R.id.email_id);
        mPasswordField = findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        final EditText mPasswordField;
        CheckBox mCbShowPwd;
        CheckBox mRemPwd;

        mPasswordField = (EditText) findViewById(R.id.password);
        // get the show/hide password Checkbox
        mCbShowPwd = (CheckBox) findViewById(R.id.show_hide_password);
        mRemPwd = (CheckBox) findViewById(R.id.rem_pwd);
        // add onCheckedListener on checkbox
        // when user clicks on this checkbox, this is the handler.
        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    mPasswordField.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    mPasswordField.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
       

    }
    @Override
    public void onClick (View view) {
        signIn();

    }

    public void forgotPwd (View view) {
        Log.d(TAG, "viewPassowrd");
        final int[] flag = {0};
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = mEmailField.getText().toString();
        final String emailAddress = mEmailField.toString();
        mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Email sent. 1" + flag[0]);
                    ///////// getProviders().size() will return size 1. if email ID is available.
                    if(task.getResult().getProviders().size() ==1){
                        send_reset_mail();
                        Log.d(TAG, "User exist" );
                    }
                    else{

                        Log.d(TAG, "Email sent. 2" );
                        AlertDialog alertDialog = new AlertDialog.Builder(SignIn.this).create();
                        alertDialog.setTitle("Forgot Password");
                        alertDialog.setMessage("User does not exist");
                        alertDialog.setIcon(R.drawable.user);
                        Log.d(TAG, "User does not exist" );
                        alertDialog.show();

                    }
                }
            }
        });


    }


    public void send_reset_mail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = mEmailField.getText().toString();
        final String emailAddress = mEmailField.toString();
        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent." + emailAddress);
                        }
                    }
                });

        AlertDialog alertDialog = new AlertDialog.Builder(SignIn.this).create();
        alertDialog.setTitle("Forgot Password");
        alertDialog.setMessage("Email is sent");
        alertDialog.setIcon(R.drawable.user);

        alertDialog.show();

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




