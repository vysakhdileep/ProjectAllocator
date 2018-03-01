package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by jsnkrm on 28/02/18.
 */

public class ProjectIdeas {

    private String projectName;
    private String facultyId;
    ArrayList< String> areas;


    public ProjectIdeas() {
    }

    public ProjectIdeas(String projectName, String facultyId, ArrayList<String> areas) {

        this.projectName = projectName;
        this.facultyId = facultyId;
        this.areas = areas;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }
}
