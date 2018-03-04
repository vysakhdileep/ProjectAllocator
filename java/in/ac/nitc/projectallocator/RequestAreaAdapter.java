package in.ac.nitc.projectallocator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arunjoseph on 03/03/18.
 */

public class RequestAreaAdapter extends ArrayAdapter<String> {
    private static final String TAG = "RequestAreaAdapter";

    public RequestAreaAdapter(@NonNull Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.request_area_item, parent, false);
        }

        String currArea = getItem(position);
        Log.d(TAG, "position: " + position);

        Log.d(TAG, "Area: " + currArea);
        TextView requestArea = (TextView) listItemView.findViewById(R.id.request_area);
        requestArea.setText(currArea);

        return listItemView;
    }
}
