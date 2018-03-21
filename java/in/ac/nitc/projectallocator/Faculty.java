package in.ac.nitc.projectallocator;

import java.util.ArrayList;

/**
 * Created by User on 27-02-2018.
 */

public class Faculty {

    private int count;
    private int limit;
    private String nameof;
    private String personalemail;
    private String phonenumber;
    private String uid;
    private ArrayList<String> areas;

    public Faculty(int count, int limit, String nameof, String personalemail, String phonenumber, String uid, ArrayList<String> areas) {
        this.count = count;
        this.limit = limit;
        this.nameof = nameof;
        this.personalemail = personalemail;
        this.phonenumber = phonenumber;
        this.uid = uid;
        this.areas = areas;
    }

    public ArrayList<String> getAreas() {
        return areas;
    }

    public Faculty() {
    }

    public int getCount() {
        return count;
    }

    public int getLimit() {
        return limit;
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

    public String getUid() {
        return uid;
    }
}
