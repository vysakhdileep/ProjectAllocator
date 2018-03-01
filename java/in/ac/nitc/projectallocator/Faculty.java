package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by User on 27-02-2018.
 */

public class Faculty {
    private String Name;
    private String Email;
    private String Phoneno;
    private String Limit;
    public ArrayList<String> areas;


    public Faculty(String name, String email, String phoneno, String limit, ArrayList<String> areas) {
        Name = name;
        Email = email;
        Phoneno = phoneno;
        Limit = limit;
        this.areas = areas;
    }

    public Faculty() {

    }

    public String getName() {
        return Name;
    }


    public String getEmail() {
        return Email;
    }


    public String getPhoneno() {
        return Phoneno;
    }


    public String getLimit() {
        return Limit;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }
}
