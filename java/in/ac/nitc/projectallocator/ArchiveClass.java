package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Vysakh Dileep on 21-03-2018.
 */

public class ArchiveClass {
    ArrayList<String> areas;
    String description;
    String link;
    String topic;
    ArrayList<StudentArchive> student;
    FacultyArchive faculty;

    public ArchiveClass() {
    }

    public ArchiveClass(ArrayList<String> areas, String description, String link, String topic, ArrayList<StudentArchive> student, FacultyArchive faculty) {
        this.areas = areas;
        this.description = description;
        this.link = link;
        this.topic = topic;
        this.student = student;
        this.faculty = faculty;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<StudentArchive> getStudent() {
        return student;
    }

    public void setStudent(ArrayList<StudentArchive> student) {
        this.student = student;
    }

    public FacultyArchive getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyArchive faculty) {
        this.faculty = faculty;
    }
}
