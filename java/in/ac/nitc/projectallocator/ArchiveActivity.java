package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ArchiveActivity extends AppCompatActivity {

    String year = new String();
    String areaval = new String();
    Spinner list_areas;
    Spinner list_years;
    final ArrayList<String> years = new ArrayList<>();
    private static final String TAG = "Archive";
    ArrayList<ArchiveClass> ArchiveVal = new ArrayList<>();
    ArrayList<String> ArchiveKey = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchfilter();
        setContentView(R.layout.search_archive_layout);


        list_areas = (Spinner) findViewById(R.id.spinner_areas);
        list_years = (Spinner) findViewById(R.id.spinner_year);
        Button submitfilter = (Button) findViewById(R.id.submit_filter);
        submitfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (year.equals("None")) {
                    for (int i = 0; i < years.size(); i++) {
                        viewArchive(years.get(i), areaval);
                    }
                } else {
                    viewArchive(year, areaval);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        searchfilter();
        setContentView(R.layout.search_archive_layout);


        list_areas = (Spinner) findViewById(R.id.spinner_areas);
        list_years = (Spinner) findViewById(R.id.spinner_year);
        Button submitfilter = (Button) findViewById(R.id.submit_filter);
        submitfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (year.equals("None")) {
                    for (int i = 0; i < years.size(); i++) {
                        viewArchive(years.get(i), areaval);
                    }
                } else {
                    viewArchive(year, areaval);
                }
            }
        });
    }


    public void searchfilter() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ArchiveRef = database.child("Archive");


        ArchiveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                years.clear();
                years.add("None");
                if (dataSnapshot.exists()) {

                    for (DataSnapshot archiveSnapshot : dataSnapshot.getChildren()) {
                        years.add(archiveSnapshot.getKey());
                        Log.d(TAG, "years count" + years.size());

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ArchiveActivity.this, android.R.layout.simple_spinner_item, years);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    list_years.setAdapter(adapter);
                    list_years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            year = parent.getItemAtPosition(pos).toString();
                            Log.d(TAG, "Year selected" + year);
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final ArrayList<String> areaexpertise = new ArrayList<>();
        DatabaseReference AreaRef = database.child("AreaExpertise");
        AreaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                areaexpertise.clear();
                areaexpertise.add("None");

                if (dataSnapshot.exists()) {
                    for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                        areaexpertise.add(areaSnapshot.getValue().toString());
                    }
                }
                Log.d(TAG, "Archive area size" + areaexpertise.size());

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ArchiveActivity.this, android.R.layout.simple_spinner_item, areaexpertise);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                list_areas.setAdapter(adapter);
                list_areas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        areaval = parent.getItemAtPosition(pos).toString();
                        Log.d(TAG, "Area slected" + areaval);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void viewArchive(String year, final String areaval) {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ArchiveRef = database.child("Archive").child(year);


        ArchiveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArchiveVal.clear();
                ArchiveKey.clear();
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "Archive exist");

                    if (areaval.equals("None")) {
                        for (DataSnapshot archiveSnapshot : dataSnapshot.getChildren()) {
                            ArchiveVal.add(archiveSnapshot.getValue(ArchiveClass.class));
                            ArchiveKey.add(archiveSnapshot.getKey());
                            Log.d(TAG,"Archieve key is  "+archiveSnapshot.getKey());
                        }
                    } else {
                        for (DataSnapshot archiveSnapshot : dataSnapshot.getChildren()) {
                            ArchiveClass temp = archiveSnapshot.getValue(ArchiveClass.class);
                            Log.d(TAG, "Topic is" + temp.getAreas().toString() + "areavalis" + areaval);
                            for (int i = 0; i < temp.getAreas().size(); i++) {
                                Log.d(TAG, "inside forloop");
                                if (temp.getAreas().get(i).toString().equals(areaval)) {
                                    Log.d(TAG, "inside if");
                                    Log.d(TAG, "Assigning for Topic is" + temp.getAreas().toString());
                                    ArchiveVal.add(temp);
                                    ArchiveKey.add(archiveSnapshot.getKey());
                                }
                            }
                        }
                    }


                    // Log.d(TAG, "Size of student" + ArchiveVal.get(0).getStudent().size());

                    displayArchive(ArchiveVal);

                } else

                {
                    Log.d(TAG, "Archive does not exist");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "In onCancelled Archive" + databaseError.getDetails());
                Log.d(TAG, "In onCancelled Archive Message" + databaseError.getMessage());
            }
        });
        setContentView(R.layout.activity_archive);

    }

    public void displayArchive(final ArrayList<ArchiveClass> ArchiveVal) {

        LinearLayout linearMain;
        linearMain = (LinearLayout) findViewById(R.id.archive_list);
        Log.d(TAG,"All archieve key is"+ArchiveKey.toString());
        for (int i = 0; i < ArchiveVal.size(); i++) {


            TextView nameView = new TextView(this);
            nameView.setText(ArchiveVal.get(i).getTopic());
            final String key =ArchiveKey.get(i);
            Log.d(TAG,"Req archieve key +i"+key+"  "+i);
            TextView decpView = new TextView(this);
            decpView.setText(ArchiveVal.get(i).getDescription());
            Button details =new Button(this);
            details.setText("Details");
            linearMain.addView(nameView);
            linearMain.addView(decpView);
            linearMain.addView(details);

            final int j = i;
            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "In onclick");
                    final AlertDialog.Builder mBUilder = new AlertDialog.Builder(ArchiveActivity.this);
                    final View mView = LayoutInflater.from(getApplication()).inflate(R.layout.dialog_view_archive, null);
                    TextView topic = (TextView) mView.findViewById(R.id.archive_topic);
                    topic.setText(ArchiveVal.get(j).getTopic());
                    TextView description = (TextView) mView.findViewById(R.id.archive_description);
                    description.setText(ArchiveVal.get(j).getTopic());
                    TextView faculty_name = (TextView) mView.findViewById(R.id.archive_faculty_name);
                    faculty_name.setText(ArchiveVal.get(j).getFaculty().getNameof());
                    TextView faculty_email = (TextView) mView.findViewById(R.id.archive_faculty_email);
                    faculty_email.setText(ArchiveVal.get(j).getFaculty().getPersonalemail());
                    TextView faculty_phno = (TextView) mView.findViewById(R.id.archive_faculty_phno);
                    faculty_phno.setText(ArchiveVal.get(j).getFaculty().getPhonenumber());
                    LinearLayout archivelist = (LinearLayout) mView.findViewById(R.id.archive_areas);
                    Log.d(TAG, "In onclick 2");
                    for (int k = 0; k < ArchiveVal.get(j).getAreas().size(); k++) {
                        TextView archive_val = new TextView(getApplicationContext());
                        archive_val.setText(ArchiveVal.get(j).getAreas().get(k));
                        archivelist.addView(archive_val);
                    }

                    LinearLayout archivestudentlist = (LinearLayout) mView.findViewById(R.id.archive_students);
                    Log.d(TAG, "In onclick 2");
                    for (int k = 0; k < ArchiveVal.get(j).getStudent().size(); k++) {
                        TextView archive_student_name_title = new TextView(getApplicationContext());
                        archive_student_name_title.setText("Name  ");
                        archivestudentlist.addView(archive_student_name_title);
                        TextView archive_student_name = new TextView(getApplicationContext());
                        archive_student_name.setText(ArchiveVal.get(j).getStudent().get(k).getNameof());
                        archivestudentlist.addView(archive_student_name);
                        TextView archive_student_email_title = new TextView(getApplicationContext());
                        archive_student_email_title.setText("Email  ");
                        archivestudentlist.addView(archive_student_email_title);
                        TextView archive_student_email = new TextView(getApplicationContext());
                        archive_student_email.setText(ArchiveVal.get(j).getStudent().get(k).getPersonalemail());
                        archivestudentlist.addView(archive_student_email);
                        TextView archive_student_phno_title = new TextView(getApplicationContext());
                        archive_student_phno_title.setText("Phone number  ");

                        archivestudentlist.addView(archive_student_phno_title);
                        TextView archive_student_phno = new TextView(getApplicationContext());
                        archive_student_phno.setText(ArchiveVal.get(j).getStudent().get(k).getPhonenumber());
                        archivestudentlist.addView(archive_student_phno);

                    }
                    Button archive_download = (Button) mView.findViewById(R.id.archive_download);
                    archive_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            StorageReference mStorageRef;
                            mStorageRef = FirebaseStorage.getInstance().getReference();
                            StorageReference archiveRef = mStorageRef.child("archive").child(key).child("project.zip");
                            Log.d(TAG,"Archieve key is"+ArchiveKey.get(j));
                            File localFile = null;
                            File path = getExternalFilesDir("Project");


                            localFile = new File(path, "archieve.zip");




                            final File temp = localFile;
                            archiveRef.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // Successfully downloaded data to local file
                                            // ...

                                            Log.e("firebase ",";local tem file created  created " +temp.toString());
                                            Toast.makeText(getApplicationContext(), "Downloaded Successfully", Toast.LENGTH_LONG).show();
                                            Notification n  = new Notification.Builder(getApplicationContext())
                                                    .setContentTitle("Download")
                                                    .setContentText("Download completed successfully ")
                                                    .setSmallIcon(R.drawable.user)

                                                    .setAutoCancel(true)
                                                   .build();


                                            NotificationManager notificationManager =
                                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                            notificationManager.notify(0, n);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle failed download
                                    // ...
                                }
                            });
                        }
                    });
                    mBUilder.setView(mView);
                    mBUilder.show();
                }
            });
            Log.d(TAG, "printing displayArchive");

        }
    }

    public void uploadArchive() {

        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference archiveRef = mStorageRef.child("images/rivers.jpg");

        archiveRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap back button once more to exit ", Toast.LENGTH_SHORT).show();
            onResume();
        }

        mBackPressed = System.currentTimeMillis();


    }


}


