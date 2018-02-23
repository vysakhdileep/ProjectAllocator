package in.ac.nitc.projectallocator;

/**
 * Created by jsnkrm on 21/02/18.
 */

public class Student {


    private String Name;
    private String email;
    private String personalemail;

    public Student(String name, String email, String personalemail) {
        Name = name;
        this.email = email;
        this.personalemail = personalemail;
    }

    public Student(){

    }

    public String getPersonalemail() {
        return personalemail;
    }

    public String getName() {
        return Name;
    }

    public String getemail() {
        return email;
    }

}
