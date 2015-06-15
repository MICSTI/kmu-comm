package itm.fhj.at.kmucomm.api;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import itm.fhj.at.kmucomm.data.DummyDataAccessor;
import itm.fhj.at.kmucomm.data.SQLiteData;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.util.CommunicationDatabaseHelper;

/**
 * Created by michael.stifter on 07.06.2015.
 */
public class ContactProvider {

    private static ContactProvider instance;

    private APIRetriever retriever;

    private List<Contact> contacts;

    private Context context;

    // constants for JSON data exchange
    public static final String CONTACT_USERNAME = "username";
    public static final String CONTACT_EMAIL = "email";
    public static final String CONTACT_FIRST_NAME = "firstName";
    public static final String CONTACT_LAST_NAME = "lastName";
    public static final String CONTACT_PASSWORD = "password";
    public static final String CONTACT_PHONE = "telNr";
    public static final String CONTACT_DEPARTMENT = "department";

    private ContactProvider(Context ctxt) {
        context = ctxt;

        contacts = CommunicationDatabaseHelper.getHelper(context).getContacts();
    }

    public static ContactProvider getInstance(Context context) {
        if (instance == null) {
            instance = new ContactProvider(context);
        }

        return instance;
    }

    public void update(ContactActionListener listener) {
        ContactHelperTask helperTask = new ContactHelperTask(listener);
        helperTask.execute("GO");
    }

    public String retrieve() {
        retriever = new APIRetriever();

        String response = retriever.retrieveContacts();

        // parse response and add contacts to the contact list
        parseResponse(response);

        // add contacts to database
        for (Contact contact : contacts) {
            if (CommunicationDatabaseHelper.getHelper(context).contactExists(contact)) {
                // update contact in database
                CommunicationDatabaseHelper.getHelper(context).updateContact(contact);
            } else {
                // create contact in database
                CommunicationDatabaseHelper.getHelper(context).addContact(contact);
            }
        }

        return response;
    }

    private void parseResponse(String response) {
        try {
            contacts.clear();

            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                Contact contact = new Contact();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                contact.setUsername(jsonObject.getString(CONTACT_USERNAME));
                contact.setFirstName(jsonObject.getString(CONTACT_FIRST_NAME));
                contact.setLastName(jsonObject.getString(CONTACT_LAST_NAME));
                contact.setDepartment(jsonObject.getString(CONTACT_DEPARTMENT));
                contact.setEmail(jsonObject.getString(CONTACT_EMAIL));
                //contact.setPassword(jsonObject.getString(CONTACT_PASSWORD));
                contact.setPhone(jsonObject.getString(CONTACT_PHONE));

                contacts.add(contact);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    class ContactHelperTask extends AsyncTask<String, String, String> {

        private ContactActionListener listener;

        public ContactHelperTask(ContactActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            return retrieve();
        }

        @Override
        protected void onPostExecute(String s) {
            if (listener != null)
                listener.onUpdated(s);

            super.onPostExecute(s);
        }
    }

    public interface ContactListener {
        void onContactsDelivered(ArrayList<Contact> contactArrayList);
    }

    public interface ContactActionListener {
        void onUpdated(String msg);
    }
}