package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Vysakh Dileep on 16-03-2018.
 */

public class AcceptProject {
    String faculty;
    String topic;
    String groupid;
    String description;
    ArrayList<String> areas;

    public AcceptProject() {
    }

    public AcceptProject(String faculty, String topic, String groupid, String description, ArrayList<String> areas) {
        this.faculty = faculty;
        this.topic = topic;
        this.groupid = groupid;
        this.description = description;
        this.areas = areas;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }
}
