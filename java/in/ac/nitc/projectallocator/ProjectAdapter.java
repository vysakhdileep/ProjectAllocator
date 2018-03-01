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
import java.util.List;

/**
 * Created by jsnkrm on 01/03/18.
 */

public class ProjectAdapter extends ArrayAdapter<ProjectIdeas> {

    public ProjectAdapter(@NonNull Context context, int resource, ArrayList<ProjectIdeas> objects) {
        super(context, resource, objects);

    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.project_list_item, parent, false);
        }

        ProjectIdeas currProject = getItem(position);

        TextView projName = (TextView) listItemView.findViewById(R.id.project_list_projname);
        TextView projFacId = (TextView) listItemView.findViewById(R.id.project_list_projFacId);
        TextView projarea = (TextView) listItemView.findViewById(R.id.project_list_areas);


        projName.setText(currProject.getProjectName());
        projFacId.setText(currProject.getFacultyId());
        projarea.setText(currProject.getAreas().get(0));

        return listItemView;
    }
}
