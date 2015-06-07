package itm.fhj.at.kmucomm.api;

import android.os.AsyncTask;

import java.util.ArrayList;

import itm.fhj.at.kmucomm.model.Contact;

/**
 * Created by michael.stifter on 07.06.2015.
 */
public class ContactProvider {

    private static ContactProvider instance;

    private APIRetriever retriever;

    private String json;

    private ContactProvider() {
        json = "{}";
    }

    public static ContactProvider getInstance() {
        if (instance == null) {
            instance = new ContactProvider();
        }

        return instance;
    }

    public void update(ContactActionListener listener) {
        ContactHelperTask helperTask = new ContactHelperTask(listener);
        helperTask.execute("GO");
    }

    public String retrieve() {
        retriever = new APIRetriever();

        return retriever.retrieveContacts();
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