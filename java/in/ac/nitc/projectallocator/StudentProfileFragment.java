package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
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
import android.widget.PopupWindow;
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

    FirebaseUser user;
    String uid;
    Student stuUser;
    DatabaseReference database, StudentRef;

    FrameLayout layout_MainMenu;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final String TAG = "GotStuff";


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_student_profile, container, false);

        final TextView name = view.findViewById(R.id.textview_student_name);
        final TextView email = view.findViewById(R.id.textview_student_email);

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
                        name.setText(stuUser.getName());
                        email.setText(stuUser.getPersonalemail());
                        Log.d(TAG, "email:" + stuUser.getPersonalemail());
                        Log.d(TAG, "Name:" + stuUser.getName());

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
                Button confirmButton = (Button) mView.findViewById(R.id.confirm_edit_button);
                Button cancelButton = (Button) mView.findViewById(R.id.cancel_edit_stuProfile);

                newName.setText(stuUser.getName());
                newPersonalEmail.setText(stuUser.getPersonalemail());

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!newName.getText().toString().isEmpty() &&
                                !newPersonalEmail.getText().toString().isEmpty())
                        {
                            StudentRef.child("Name").setValue(newName.getText().toString());
                            StudentRef.child("personalemail").setValue(newPersonalEmail.getText().toString());
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

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getActivity(), SignIn.class);
                getActivity().startActivity(myIntent);

            }
        });

        return view;

    }



}