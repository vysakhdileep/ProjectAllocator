package in.ac.nitc.projectallocator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by arunjoseph on 02/03/18.
 */

public class RequestAdapter extends ArrayAdapter<RequestQueue> {
    private static final String TAG = "RequestAdapter";

    public RequestAdapter(@NonNull Context context, int resource, ArrayList<RequestQueue> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.request_list_item, parent, false);
        }

        RequestQueue currRequest = getItem(position);

        TextView requestGroup = (TextView) listItemView.findViewById(R.id.request_list_group);
        TextView requestAreas = (TextView) listItemView.findViewById(R.id.request_list_areas);

        requestGroup.setText(currRequest.getGroup());
        requestAreas.setText(currRequest.getAreas().get(0));

        Log.d(TAG, "start acceptButton");
        Button acceptButton = listItemView.findViewById(R.id.accept_request);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                final View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view_request, null);
                Log.d(TAG, "onClick:view inflate");

                mBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                        Toast.makeText(getContext(), "Project Accepted",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.setView(mView);
                mBuilder.show();
            }
        });
        Log.d(TAG, "end rejectButton");

        Log.d(TAG, "start rejectButton");
        Button rejectButton = listItemView.findViewById(R.id.reject_request);
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                final View mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_view_request, null);
                Log.d(TAG, "onClick:view inflate");

                mBuilder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                        Toast.makeText(getContext(), "Project Rejected",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                mBuilder.setView(mView);
                mBuilder.show();
            }
        });
        Log.d(TAG, "end rejectButton");

        return listItemView;
    }
}
