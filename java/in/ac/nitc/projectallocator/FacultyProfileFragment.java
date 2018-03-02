package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FacultyProfileFragment extends Fragment {

    FirebaseUser user;
    String uid;
    Faculty facUser;
    DatabaseReference FacultyRef, AreaRef;
    ArrayList<String> FacultyExpertise = new ArrayList<>();  //Contain Expertise name of faculty
    ArrayList<String> ExpertiseKey = new ArrayList<>();      //Contain all Expertise Key
    ArrayList<String> ExpertiseValue = new ArrayList<>();      //Contain all Expertise Value
    Integer size = 0;

    View view;
    private static final String TAG = "FacProfileFrag";

    public FacultyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_faculty_profile, container, false);

        getRequestData();
        Button signoutButton = view.findViewById(R.id.faculty_signout);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "signOut...");
                FirebaseAuth.getInstance().signOut();
                Intent myIntent = new Intent(getActivity(), SignIn.class);
                getActivity().startActivity(myIntent);

            }
        });

        Button editButton = view.findViewById(R.id.faculty_profile_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBUilder = new AlertDialog.Builder(getActivity());
                final View mView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_faculty_profile, null);
                Log.d(TAG, "onClick:view inflate");
                final EditText newPersonalEmail = (EditText) mView.findViewById(R.id.new_email);
                final EditText Limit = (EditText) mView.findViewById(R.id.faculty_limit);
                final EditText newphone = mView.findViewById(R.id.new_phonenumber);

                Limit.setText(String.valueOf(facUser.getLimit()));
                newPersonalEmail.setText(facUser.getPersonalemail());
                newphone.setText(facUser.getPhonenumber());


                mBUilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                    public void onClick(DialogInterface dialog, int which) {
                        //Either of the following two lines should work.
                        if (!Limit.getText().toString().isEmpty() &&
                                !newPersonalEmail.getText().toString().isEmpty() && !newphone.getText().toString().isEmpty()) {

                            Log.d(TAG, "Limit" + Limit.getText().toString());
                            Log.d(TAG, "newMail:" + newPersonalEmail.getText().toString());
                            Log.d(TAG, "newPhone:" + newphone.getText());
                            FacultyRef.child("limit").setValue(Integer.parseInt(Limit.getText().toString()));
                            FacultyRef.child("phonenumber").setValue(newphone.getText().toString());
                            Toast.makeText(getActivity(), "Update Successful!!",
                                    Toast.LENGTH_SHORT).show();


                        } else {
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

        return view;
    }

    public void getRequestData() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        final TextView name = view.findViewById(R.id.textview_faculty_name);
        final TextView email = view.findViewById(R.id.textview_faculty_email);
        final TextView phone_no = view.findViewById(R.id.textview_faculty_phonenumber);
        final TextView limit = view.findViewById(R.id.textview_faculty_limit);
        uid = user.getUid();

        FacultyRef = FirebaseDatabase.getInstance().getReference().child("Faculty").child(uid);
        AreaRef = FirebaseDatabase.getInstance().getReference().child("AreaExpertise");

        Log.d(TAG, "Fetching faculty ");
        FacultyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    facUser = dataSnapshot.getValue(Faculty.class);
                    name.setText(facUser.getNameof());
                    email.setText(facUser.getPersonalemail());
                    phone_no.setText(facUser.getPhonenumber());
                    limit.setText(String.valueOf(facUser.getLimit()));

                    Log.d(TAG, "Name:" + facUser.getNameof());
                    Log.d(TAG, "login:" + facUser.getPersonalemail());
                    Log.d(TAG, "phoneno " + facUser.getPhonenumber());
                    size = 0;
                    if (dataSnapshot.hasChild("areas")) {
                        size = facUser.getAreas().size();
                        Log.d(TAG, "Area Size is " + size);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        Log.d(TAG, "fetching expertise ");
        AreaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Key is " + snapshot.getKey());
                    Log.d(TAG, "Value is " + snapshot.getValue());
                    ExpertiseKey.add(snapshot.getKey());
                    ExpertiseValue.add(snapshot.getValue().toString());
                }

                int i = 0;
                while (i < size) {
                    FacultyExpertise.add(dataSnapshot.child(facUser.getAreas().get(i)).getValue().toString());
                    i++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        //Log.d(TAG,AreaNames.get(0));
    }


}