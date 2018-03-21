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
    private Integer count;
    private String uid;
    private ArrayList<String> areas;

    public void setNameof(String nameof) {
        this.nameof = nameof;
    }

    public void setPersonalemail(String personalemail) {
        this.personalemail = personalemail;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setAreas(ArrayList<String> areas) {
        this.areas = areas;
    }

    public Faculty(String nameof, String personalemail, String phonenumber, Integer limit, Integer count, String uid, ArrayList<String> areas) {
        this.nameof = nameof;
        this.personalemail = personalemail;
        this.phonenumber = phonenumber;
        this.limit = limit;
        this.count = count;
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

    public String getUid() {
        return uid;
    }
}
