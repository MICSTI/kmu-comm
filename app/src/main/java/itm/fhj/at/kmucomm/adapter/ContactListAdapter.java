package itm.fhj.at.kmucomm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import itm.fhj.at.kmucomm.model.Contact;

import java.util.List;

import itm.fhj.at.kmucomm.R;

/**
 * Created by michael.stifter on 25.05.2015.
 */
public class ContactListAdapter extends ArrayAdapter<Contact> {

    public ContactListAdapter(Context context, List<Contact> contacts) {
        super(context, R.layout.item_contact_list, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the contact item for this position
        Contact contact = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact_list, parent, false);
        }

        // get views to add actual data values
        ImageView contactImage = (ImageView) convertView.findViewById(R.id.contactImage);
        TextView contactName = (TextView) convertView.findViewById(R.id.contactName);

        // add the values to the views
        contactName.setText(contact.getFirstName() + " " + contact.getLastName());

        // return the completed view to render on screen
        return convertView;
    }

}