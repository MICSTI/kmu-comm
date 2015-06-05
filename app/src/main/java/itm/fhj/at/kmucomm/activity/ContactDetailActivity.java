package itm.fhj.at.kmucomm.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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

        // get elements to show actual data
        ImageView contactImage = (ImageView) findViewById(R.id.contactImage);
        TextView contactName = (TextView) findViewById(R.id.contactName);

        // add the values to the views
        contactName.setText(contact.getFirstName() + " " + contact.getLastName());
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
