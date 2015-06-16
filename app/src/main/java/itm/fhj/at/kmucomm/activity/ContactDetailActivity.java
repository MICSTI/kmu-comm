package itm.fhj.at.kmucomm.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.data.SQLiteData;
import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.util.CommunicationDatabaseHelper;


public class ContactDetailActivity extends ActionBarActivity {

    final Context context = this;

    private Contact contact;

    private SQLiteData db;

    public static final int REQUEST_CODE_CONTACT_DETAIL = 101;

    public static final String EXTRA_MESSAGE = "itm.fhj.at.kmucomm.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // database
        db = new SQLiteData(getApplicationContext());

        // get contact from intent extra
        contact = (Contact) getIntent().getSerializableExtra(EXTRA_MESSAGE);

        // show contact name in action bar
        setTitle(contact.getFullName());

        // get elements to show actual data
        TextView contactDepartment = (TextView) findViewById(R.id.contact_department);
        TextView contactPhone = (TextView) findViewById(R.id.contact_phone);
        TextView contactEmail = (TextView) findViewById(R.id.contact_email);

        // add the values to the views
        if (contact.getDepartment() != null) {
            contactDepartment.setText(getString(R.string.lbl_department) + ": " + contact.getDepartment());
        } else {
            contactDepartment.setVisibility(View.GONE);
        }

        if (contact.getPhone() != null) {
            contactPhone.setText(getString(R.string.lbl_phone) + ": " + contact.getPhone());
        } else {
            contactPhone.setVisibility(View.GONE);
        }

        if (contact.getEmail() != null) {
            contactEmail.setText(getString(R.string.lbl_email) + ": " + contact.getEmail());
        } else {
            contactEmail.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // menu gets only added if contact is not yourself
        if (!contact.getUsername().equals(PreferenceManager.getDefaultSharedPreferences(context).getString("username", "")))
            getMenuInflater().inflate(R.menu.menu_contact_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chat) {
            // start chat with this contact
            List<String> participantList = new ArrayList<String>();
            participantList.add(contact.getUsername());

            int chatId = db.incomingChat(contact.getUsername(), participantList);

            Chat chat = CommunicationDatabaseHelper.getHelper(getApplicationContext()).getChatById(chatId);

            Intent chatDetailIntent = new Intent(this, ChatDetailActivity.class);
            chatDetailIntent.putExtra(ChatDetailActivity.EXTRA_MESSAGE, chat);

            startActivity(chatDetailIntent);
        } else if (id == R.id.action_call) {
            if (contact.getPhone() != null && !contact.getPhone().isEmpty()) {
                String uri = "tel:" + contact.getPhone().trim();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(uri));
                startActivity(callIntent);
            }
        } else if (id == R.id.action_mail) {
            if (contact.getEmail() != null && !contact.getEmail().isEmpty()) {
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.setType("message/rfc822");
                mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {contact.getEmail().trim()});

                try {
                    startActivity(Intent.createChooser(mailIntent, "Send mail..."));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ContactDetailActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT);
                } catch (Exception e) {
                    Toast.makeText(ContactDetailActivity.this, "An unknown error occurred.", Toast.LENGTH_SHORT);
                }
            }
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
