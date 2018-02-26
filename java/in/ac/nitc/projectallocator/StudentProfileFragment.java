package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import in.ac.nitc.projectallocator.Student;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


public class StudentProfileFragment extends Fragment{

    final String TAG = "StudentProfileFragment";


    FirebaseUser user;
    String uid;
    Student stuUser;
    DatabaseReference database, StudentRef;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Inside StudentProfileFragment ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_student_profile, container, false);

        final TextView name = view.findViewById(R.id.textview_student_name);
        final TextView email = view.findViewById(R.id.textview_student_email);
        final TextView loginid = view.findViewById(R.id.textview_student_login_email);
        final TextView phone = view.findViewById(R.id.textview_student_phonenumber);
        final TextView rollnum = view.findViewById(R.id.textview_student_rollnumber);

        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = user.getUid();

        database = FirebaseDatabase.getInstance().getReference();
        StudentRef = database.child("Student").child(uid);
        Log.d(TAG, "uid:" + uid);

        StudentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                        stuUser = dataSnapshot.getValue(Student.class);
                        StudentRef.child("uid").setValue(uid);
                        name.setText(stuUser.getName());
                        email.setText(stuUser.getPersonalemail());
                        loginid.setText(stuUser.getEmail());
                        phone.setText(stuUser.getPhonenumber());
                        rollnum.setText(stuUser.getRollnumber());

                        Log.d(TAG, "email:" + stuUser.getPersonalemail());
                        Log.d(TAG, "Name:" + stuUser.getName());
                        Log.d(TAG, "login:" + stuUser.getEmail());
                        Log.d(TAG, "phone:" + stuUser.getPhonenumber());
                        Log.d(TAG, "roll:" + stuUser.getRollnumber());
                        Log.d(TAG, "groupId:" + stuUser.getGroupId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        Button editButton = view.findViewById(R.id.student_profile_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBUilder = new AlertDialog.Builder(getActivity());
                final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_student_profile,null);
                Log.d(TAG, "onClick:view inflate");
                final EditText newPersonalEmail = (EditText) mView.findViewById(R.id.new_email);
                final EditText newName = (EditText) mView.findViewById(R.id.new_username);
                final EditText newphone = mView.findViewById(R.id.new_phonenumber);

                newName.setText(stuUser.getName());
                newPersonalEmail.setText(stuUser.getPersonalemail());
                newphone.setText(stuUser.getPhonenumber().toString());

                mBUilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                    public void onClick(DialogInterface dialog, int which) {
                        //Either of the following two lines should work.
                        if(!newName.getText().toString().isEmpty() &&
                                !newPersonalEmail.getText().toString().isEmpty() && !newphone.getText().toString().isEmpty())
                        {

                            Log.d(TAG, "newName:" + newName.getText().toString());
                            Log.d(TAG, "newMail:" + newPersonalEmail.getText().toString());
                            Log.d(TAG, "newPhone:" + newphone.getText());

                            StudentRef.child("Name").setValue(newName.getText().toString());
                            StudentRef.child("personalemail").setValue(newPersonalEmail.getText().toString());
                            StudentRef.child("phonenumber").setValue(newphone.getText().toString());
                            Toast.makeText(getActivity(), "Update Successful!!",
                                    Toast.LENGTH_SHORT).show();



                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Please fill all empty values...",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                mBUilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                    public void onClick(DialogInterface dialog, int which) {
                        //Either of the following two lines should work.
                        dialog.cancel();
                        //dialog.dismiss();
                    }
                });

                mBUilder.setView(mView);
                mBUilder.show();
            }
        });

        Button signoutButton = view.findViewById(R.id.student_signout);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "signOut...");
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getActivity(), SignIn.class);
                getActivity().startActivity(myIntent);

            }
        });

        return view;

    }



}