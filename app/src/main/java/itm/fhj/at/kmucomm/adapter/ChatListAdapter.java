package itm.fhj.at.kmucomm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.util.Util;

/**
 * Created by michael.stifter on 27.05.2015.
 */
public class ChatListAdapter extends ArrayAdapter<Chat> {
    public ChatListAdapter(Context context, List<Chat> chatList) {
        super(context, R.layout.item_chat_list, chatList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the contact item for this position
        Chat chat = getItem(position);

        // check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_list, parent, false);
        }

        // get views to add actual data values
        ImageView chatImage = (ImageView) convertView.findViewById(R.id.chatImage);
        TextView chatName = (TextView) convertView.findViewById(R.id.chatName);
        TextView chatLastMessageText = (TextView) convertView.findViewById(R.id.chatMessage);
        TextView chatLastMessageTimestamp = (TextView) convertView.findViewById(R.id.chatTime);

        // add the values to the views
        chatName.setText(chat.getName());
        chatLastMessageText.setText(chat.getLastMessageText());
        chatLastMessageTimestamp.setText(Util.getTextualTime(chat.getLastMessageTimestamp()));

        // return the completed view to render on screen
        return convertView;
    }
}
