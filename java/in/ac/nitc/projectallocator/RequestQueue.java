package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by User on 21-02-2018.
 */

public class RequestQueue {
    ArrayList<String> areas;
    ArrayList<String> faculties;
    String group;

    public ArrayList<String> getAreas() {
        return this.areas;
    }

    public ArrayList<String> getFaculties() {
        return this.faculties;
    }

    public String getGroup() {
        return this.group;
    }

    public RequestQueue() {
        //empty constructor
    }

    public RequestQueue(ArrayList<String> areas, ArrayList<String> faculties, String group) {
        this.areas = areas;
        this.faculties = faculties;
        this.group = group;
    }
}
