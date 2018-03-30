package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by jsnkrm on 28/02/18.
 */

public class ProjectIdeas {

    private String topic;
    private String facultyid;
    ArrayList< String> areas;
    String description;

    public ProjectIdeas(String topic, String facultyid, ArrayList<String> areas, String description) {
        this.topic = topic;
        this.facultyid = facultyid;
        this.areas = areas;
        this.description = description;
    }


    public ProjectIdeas() {
    }

    public String getTopic() {
        return topic;
    }

    public String getFacultyid() {
        return facultyid;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public String getDescription() {
        return description;
    }
}
