package in.ac.nitc.projectallocator;

/**
 * Created by Vysakh Dileep on 21-03-2018.
 */

class FacultyArchive {
    String nameof;
    String personalemail;
    String phonenumber;

    public FacultyArchive() {
    }

    public FacultyArchive(String nameof, String personalemail, String phonenumber) {
        this.nameof = nameof;
        this.personalemail = personalemail;
        this.phonenumber = phonenumber;
    }

    public String getNameof() {
        return nameof;
    }

    public void setNameof(String nameof) {
        this.nameof = nameof;
    }

    public String getPersonalemail() {
        return personalemail;
    }

    public void setPersonalemail(String personalemail) {
        this.personalemail = personalemail;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
