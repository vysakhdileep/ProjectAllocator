package in.ac.nitc.projectallocator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        return listItemView;
    }
}
