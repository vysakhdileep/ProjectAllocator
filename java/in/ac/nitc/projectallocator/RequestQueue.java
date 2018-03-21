package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Vysakh Dileep on 21-02-2018.
 */

public class RequestQueue {
    ArrayList<String> areas;
    ArrayList<String> faculties;
    String status;
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

    public String getStatus() {
        return status;
    }

    public RequestQueue() {
        //empty constructor
    }

    public RequestQueue(ArrayList<String> areas, ArrayList<String> faculties, String status, String group) {
        this.areas = areas;
        this.faculties = faculties;
        this.status = status;
        this.group = group;
    }
}
