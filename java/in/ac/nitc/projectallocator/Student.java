package in.ac.nitc.projectallocator;

/**
 * Created by jsnkrm on 21/02/18.
 */

public class Student {


    private String Name;
    private String email;

    public Student( String Name, String email) {
        //this.mUid = mUid;
        this.Name = Name;
        this.email = email;
    }

    public Student(){

    }


    public String getName() {
        return Name;
    }

    public String getemail() {
        return email;
    }

}
