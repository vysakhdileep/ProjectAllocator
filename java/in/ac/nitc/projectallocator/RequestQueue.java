package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Vysakh Dileep on 21-02-2018.
 */

public class RequestQueue {
    ArrayList<String> areas;
    ArrayList<String> faculties;
    String status;
    String groupid;
    String topic;
    String description;
    String requestid;

    public RequestQueue() {
    }


    public RequestQueue(ArrayList<String> areas, ArrayList<String> faculties, String status, String groupid, String topic, String description, String requestid) {
        this.areas = areas;
        this.faculties = faculties;
        this.status = status;
        this.groupid = groupid;
        this.topic = topic;
        this.description = description;
        this.requestid = requestid;
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

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getTopic() {
        return topic;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getRequestid() {

        return requestid;
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
