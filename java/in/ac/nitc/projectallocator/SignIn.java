package in.ac.nitc.projectallocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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


public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private static View view;
    private static CheckBox show_hide_password;
    private static EditText emailid, password;
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private ProgressBar spinner;
    private static Button login;
    private static TextView forgot_pwd;
    private static final String TAG = "Test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_sign_in);

        mEmailField = findViewById(R.id.email_id);
        mPasswordField = findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(this);
        forgot_pwd = (TextView) findViewById(R.id.forgot_pwd);
        mAuth = FirebaseAuth.getInstance();
        final EditText mPasswordField;
        CheckBox mCbShowPwd;
        CheckBox mRemPwd;

        login = (Button) findViewById(R.id.login);
        spinner = (ProgressBar) findViewById(R.id.loading);
        spinner.setVisibility(View.GONE);
        mPasswordField = (EditText) findViewById(R.id.password);
        // get the show/hide password Checkbox
        mCbShowPwd = (CheckBox) findViewById(R.id.show_hide_password);
        //mRemPwd = (CheckBox) findViewById(R.id.rem_pwd);
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
    public void onClick(View view) {

        login.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        forgot_pwd.setVisibility(View.GONE);
        signIn();

    }

    public void forgotPwd(View view) {
        Log.d(TAG, "forgotPassword");
        login.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
        forgot_pwd.setVisibility(View.GONE);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        String email = mEmailField.getText().toString();
        if (!email.isEmpty())

        {
            Log.d(TAG, "email not empty");
            mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    Log.d(TAG, "checking task");
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent. 1");
                        ///////// getProviders().size() will return size 1. if email ID is available.
                        if (task.getResult().getProviders().size() == 1) {
                            login.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                            forgot_pwd.setVisibility(View.VISIBLE);
                            send_reset_mail();
                            Log.d(TAG, "User exist");
                        } else {

                            Log.d(TAG, "Email sent. 2");
                            AlertDialog alertDialog = new AlertDialog.Builder(SignIn.this).create();
                            alertDialog.setTitle("Forgot Password");
                            alertDialog.setMessage("User does not exist");
                            alertDialog.setIcon(R.drawable.user);
                            Log.d(TAG, "User does not exist");
                            alertDialog.show();
                            login.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                            forgot_pwd.setVisibility(View.VISIBLE);

                        }
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(SignIn.this).create();
                        alertDialog.setTitle("Forgot Password");
                        alertDialog.setMessage("Invalid email");
                        alertDialog.setIcon(R.drawable.user);
                        Log.d(TAG, "Invalid email");
                        alertDialog.show();
                        login.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.GONE);
                        forgot_pwd.setVisibility(View.VISIBLE);

                    }
                }
            });

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(SignIn.this).create();
            alertDialog.setTitle("Forgot Password");
            alertDialog.setMessage("Please enter your email address");
            alertDialog.setIcon(R.drawable.user);
            Log.d(TAG, "User does not exist");
            alertDialog.show();
            login.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            forgot_pwd.setVisibility(View.VISIBLE);
        }
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

    private void signIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            login.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
            forgot_pwd.setVisibility(View.VISIBLE);
            return;
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
                            login.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.GONE);
                            forgot_pwd.setVisibility(View.VISIBLE);
                        }
                    }
                });
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}





