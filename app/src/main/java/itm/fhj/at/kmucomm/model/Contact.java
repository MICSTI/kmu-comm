package itm.fhj.at.kmucomm.model;

import java.io.Serializable;

/**
 * Created by michael.stifter on 25.05.2015.
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String firstName;
    private String lastName;

    public Contact() {

    }

    public String getName() {
        // TODO: Maybe show only first name if possible (i.e. if there is only one contact in the chat with the same first name)
        return getFirstName();
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;

        Contact contact = (Contact) o;

        if (id != contact.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
