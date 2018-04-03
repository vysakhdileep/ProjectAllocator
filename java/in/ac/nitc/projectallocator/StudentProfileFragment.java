package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
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

    //design
    LinearLayout fab2,fab3;
    FloatingActionButton fab_main;
    Animation fab_open,fab_close, rotate_acw, rotate_cw;
    boolean isOpen = false;
    //

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
                    name.setText(stuUser.getNameof());
                    email.setText(stuUser.getPersonalemail());
                    phone.setText(stuUser.getPhonenumber());
                    rollnum.setText(stuUser.getRollnumber());

                    Log.d(TAG, "email:" + stuUser.getPersonalemail());
                    Log.d(TAG, "Name:" + stuUser.getNameof());
                    Log.d(TAG, "phone:" + stuUser.getPhonenumber());
                    Log.d(TAG, "roll:" + stuUser.getRollnumber());
                    Log.d(TAG, "groupId:" + stuUser.getGroupid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        FloatingActionButton editButton = view.findViewById(R.id.student_profile_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBUilder = new AlertDialog.Builder(getActivity());
                final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_student_profile,null);
                Log.d(TAG, "onClick:view inflate");
                final EditText newPersonalEmail = (EditText) mView.findViewById(R.id.new_email);
                final EditText newName = (EditText) mView.findViewById(R.id.new_username);
                final EditText newphone = mView.findViewById(R.id.new_phonenumber);

                newName.setText(stuUser.getNameof());
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

                            StudentRef.child("nameof").setValue(newName.getText().toString());
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

        FloatingActionButton signoutButton = view.findViewById(R.id.student_signout);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "signOut...");
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getActivity(), SignIn.class);
                getActivity().startActivity(myIntent);

            }
        });

        fab2 = (LinearLayout) view.findViewById(R.id.fabs1);
        fab3 = (LinearLayout) view.findViewById(R.id.fabs2);
        fab_main = (FloatingActionButton)  view.findViewById(R.id.fab_main);


        fab_main.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isOpen){
//                    fab2.startAnimation(fab_close);
                    fab2.setVisibility(View.INVISIBLE);
//                    fab3.startAnimation(fab_close);
                    fab3.setVisibility(View.INVISIBLE);
//                    fab_main.startAnimation(rotate_cw);
                    fab2.setClickable(false);
                    fab3.setClickable(false);
                    isOpen = false;
                }
                else{
//                    fab1.startAnimation(fab_open);
//                    fab2.startAnimation(fab_open);
                    fab2.setVisibility(View.VISIBLE);
//                    fab3.startAnimation(fab_open);
                    fab3.setVisibility(View.VISIBLE);
//                    fab_main.startAnimation(rotate_acw);
                    fab2.setClickable(true);
                    fab3.setClickable(true);
                    isOpen = true;
                }

            }
        });

        return view;

    }



}