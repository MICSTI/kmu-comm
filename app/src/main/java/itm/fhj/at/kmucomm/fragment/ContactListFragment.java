package itm.fhj.at.kmucomm.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.adapter.ContactListAdapter;
import itm.fhj.at.kmucomm.data.DummyDataAccessor;
import itm.fhj.at.kmucomm.model.Contact;


public class ContactListFragment extends Fragment {

    private FragmentActivity fActivity;

    private RelativeLayout rLayout;

    private OnContactSelectedListener mCallback;

    private ListView contactList;

    private ContactListAdapter contactListAdapter;

    public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();

        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fActivity = (FragmentActivity) super.getActivity();

        rLayout = (RelativeLayout) inflater.inflate(R.layout.activity_contact_list, container, false);

        // array list containing all contacts to be displayed
        List<Contact> contacts = DummyDataAccessor.getInstance().getContacts();

        // get list view
        this.contactList = (ListView) rLayout.findViewById(R.id.contact_list);

        // add onclick listener for item selection
        this.contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact selectedContact = (Contact) parent.getItemAtPosition(position);

                mCallback.onContactSelected(selectedContact);
            }
        });

        // create adapter
        this.contactListAdapter = new ContactListAdapter(fActivity, contacts);

        // set adapter
        this.contactList.setAdapter(this.contactListAdapter);

        // we must return the loaded layout
        return rLayout;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }*/

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnContactSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnContactSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    public interface OnContactSelectedListener {
        public void onContactSelected(Contact contact);
    }
}
