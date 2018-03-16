package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Vysakh Dileep on 16-03-2018.
 */

public class AcceptProject {
    String faculty;
    String groupid;
    ArrayList<String> areas;

    public AcceptProject() {
    }

    public AcceptProject(String faculty, String groupid, ArrayList<String> areas) {
        this.faculty = faculty;
        this.groupid = groupid;
        this.areas = areas;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }
}
