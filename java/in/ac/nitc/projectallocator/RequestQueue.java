package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Vysakh Dileep on 21-02-2018.
 */

public class RequestQueue {
    ArrayList<String> areas;
    String description;
    ArrayList<String> faculties;
    String groupid;
    String status;
    String topic;
    String requestid;

    public RequestQueue() {
    }

    public RequestQueue(ArrayList<String> areas, String description, ArrayList<String> faculties, String groupid, String status, String topic) {
        this.areas = areas;
        this.description = description;
        this.faculties = faculties;
        this.groupid = groupid;
        this.status = status;
        this.topic = topic;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getFaculties() {
        return faculties;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getStatus() {
        return status;
    }

    public String getTopic() {
        return topic;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFaculties(ArrayList<String> faculties) {
        this.faculties = faculties;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setRequestid(String requestid) { this.requestid = requestid; }

    public String getRequestid() { return requestid; }
}