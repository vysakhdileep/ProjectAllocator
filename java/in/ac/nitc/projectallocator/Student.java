package in.ac.nitc.projectallocator;

/**
 * Created by jsnkrm on 21/02/18.
 */

public class Student {


    private String Name;
    private String email;
    private String groupId;
    private String personalemail;
    private String phonenumber;
    private String rollnumber;

    public Student(String Name, String email, String personalemail, String groupId, String phonenumber, String rollnumber) {
        this.Name = Name;
        this.email = email;
        this.personalemail = personalemail;
        this.groupId = groupId;
        this.phonenumber = phonenumber;
        this.rollnumber = rollnumber;
    }

    public Student(){

    }

    public String getPersonalemail() {
        return personalemail;
    }

    public String getEmail() {
        return email;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getRollnumber() {
        return rollnumber;
    }

    public String getName() {
        return Name;
    }


}
