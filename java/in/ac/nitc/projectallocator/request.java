package in.ac.nitc.projectallocator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 21-02-2018.
 */

public class request
{
    ArrayList< String> areas;
    ArrayList<String> faculties;
    String group;

    public ArrayList<String> getAreas()
    {
        return this.areas;
    }

    public ArrayList<String> getFaculties()
    {
        return this.faculties;
    }

    public String getGroup()
    {
        return this.group;
    }

    public request()
    {
        //empty constructor
    }

    public request(ArrayList< String> areas, ArrayList<String> faculties, String group)
    {
        this.areas = areas;
        this.faculties = faculties;
        this.group = group;
    }
}
