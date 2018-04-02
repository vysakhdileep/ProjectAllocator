package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Vysakh Dileep on 16-03-2018.
 */

public class AcceptProject
{
    private String faculty;
    private String groupid;
    private ArrayList<String> areas;
    private String description;
    private String topic;

    public AcceptProject() {
    }

    public AcceptProject(String faculty, String groupid, ArrayList<String> areas, String description, String topic) {
        this.faculty = faculty;
        this.groupid = groupid;
        this.areas = areas;
        this.description = description;
        this.topic = topic;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getGroupid() {
        return groupid;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public String getDescription() {
        return description;
    }

    public String getTopic() {
        return topic;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
