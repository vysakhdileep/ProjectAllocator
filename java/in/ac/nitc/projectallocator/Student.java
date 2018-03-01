package in.ac.nitc.projectallocator;

/**
 * Created by jsnkrm on 21/02/18.
 */

public class Student {


    private String nameof;
    private String groupid;
    private String personalemail;
    private String phonenumber;
    private String rollnumber;
    private String uid;

    public Student(String Name, String personalemail, String groupId, String phonenumber, String rollnumber, String uid) {
        this.nameof = Name;
        this.personalemail = personalemail;
        this.groupid = groupId;
        this.phonenumber = phonenumber;
        this.rollnumber = rollnumber;
        this.uid = uid;
    }

    public Student(){

    }

    public String getNameof() {
        return nameof;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getUid() {
        return uid;
    }

    public String getPersonalemail() {
        return personalemail;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getRollnumber() {
        return rollnumber;
    }



}
