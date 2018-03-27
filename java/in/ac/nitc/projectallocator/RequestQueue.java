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
    String topic;
    String description;

    public RequestQueue() {
    }


    public RequestQueue(ArrayList<String> areas, ArrayList<String> faculties, String status, String group, String topic, String description) {
        this.areas = areas;
        this.faculties = faculties;
        this.status = status;
        this.group = group;
        this.topic = topic;
        this.description = description;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }

    public ArrayList<String> getFaculties() {
        return faculties;
    }

    public void setFaculties(ArrayList<String> faculties) {
        this.faculties = faculties;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
