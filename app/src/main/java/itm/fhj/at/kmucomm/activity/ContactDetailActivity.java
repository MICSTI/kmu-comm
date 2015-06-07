package itm.fhj.at.kmucomm.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.model.Contact;


public class ContactDetailActivity extends ActionBarActivity {

    private Contact contact;

    public static final int REQUEST_CODE_CONTACT_DETAIL = 101;

    public static final String EXTRA_MESSAGE = "itm.fhj.at.kmucomm.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

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
        if (id == R.id.action_settings) {
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
