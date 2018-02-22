package in.ac.nitc.projectallocator;

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
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import in.ac.nitc.projectallocator.Student;


public class StudentProfileFragment extends Fragment{

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
        layout_MainMenu = (FrameLayout) view.findViewById( R.id.mainmenu);
        layout_MainMenu.getForeground().setAlpha( 0);

        final TextView name = view.findViewById(R.id.textview_student_name);
        final TextView email = view.findViewById(R.id.textview_student_email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        final String uid = user.getUid();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference StudentRef = database.child("Student").child(uid);
        Log.d(TAG, "uid:" + uid);

        StudentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                        Student stuUser = dataSnapshot.getValue(Student.class);
                        name.setText(stuUser.getName());
                        email.setText(stuUser.getemail());
                        Log.d(TAG, "email:" + stuUser.getemail());
                        Log.d(TAG, "Name:" + stuUser.getName());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        Button editButton = (Button) view.findViewById(R.id.student_profile_edit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPopup();
            }
        });
        return view;

    }

    private void startPopup()
    {
        final PopupWindow pw;

        try {
            layout_MainMenu.getForeground().setAlpha( 220); // dim

            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) getActivity()
                    .getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popwindow_edit_student_profile,
                    (ViewGroup) getView().findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 300, 470, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            TextView mResultText = layout.findViewById(R.id.server_status_text);
            Button cancelButton =  layout.findViewById(R.id.end_data_send_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layout_MainMenu.getForeground().setAlpha( 0); // restore
                    pw.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}