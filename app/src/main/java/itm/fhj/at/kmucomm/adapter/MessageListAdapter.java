package itm.fhj.at.kmucomm.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.model.Message;
import itm.fhj.at.kmucomm.util.CommunicationDatabaseHelper;
import itm.fhj.at.kmucomm.util.Util;

/**
 * Created by michael.stifter on 29.05.2015.
 */
public class MessageListAdapter extends ArrayAdapter<Message> {
    public MessageListAdapter(Context context, List<Message> messageList) {
        super(context, R.layout.item_message_list, messageList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the contact item for this position
        Message message = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message_list, parent, false);
        }

        // get views to add actual data values
        TextView messageContactName = (TextView) convertView.findViewById(R.id.messageContactName);
        TextView messageText = (TextView) convertView.findViewById(R.id.messageText);
        TextView messageTime = (TextView) convertView.findViewById(R.id.messageTime);

        // add the values to the views
        String contactName;

        if (message.getFrom().equals(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("username", ""))) {
            contactName = "You";
        } else {
            contactName = CommunicationDatabaseHelper.getHelper(getContext()).getNameByUsername(message.getFrom());
        }

        messageContactName.setText(contactName);
        messageText.setText(message.getText());

        if (message.getTimestamp() > 0)
            messageTime.setText(Util.getTime(message.getTimestamp()));
        else {
            messageTime.setText("");
        }

        // return the completed view to render on screen
        return convertView;
    }
}
