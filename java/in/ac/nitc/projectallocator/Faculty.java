package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by Arun Joseph on 03-02-2018.
 */

public class Faculty {
    private String nameof;
    private String personalemail;
    private String phonenumber;
    private Integer limit;
    private String uid;
    private ArrayList<String> areas;

    public Faculty(String name, String personalemail, String phonenumber, Integer limit, String uid, ArrayList<String> areas) {
        this.nameof = name;
        this.personalemail = personalemail;
        this.phonenumber = phonenumber;
        this.limit = limit;
        this.uid = uid;
        this.areas = areas;
    }

    public Faculty() {

    }

    public String getNameof() {
        return nameof;
    }

    public String getPersonalemail() {
        return personalemail;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }
}