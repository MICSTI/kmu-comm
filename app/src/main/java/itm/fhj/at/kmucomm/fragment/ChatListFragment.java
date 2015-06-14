package itm.fhj.at.kmucomm.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.adapter.ChatListAdapter;
import itm.fhj.at.kmucomm.data.DummyDataAccessor;
import itm.fhj.at.kmucomm.data.SQLiteData;
import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Message;
import itm.fhj.at.kmucomm.util.CommunicationDatabaseHelper;
import itm.fhj.at.kmucomm.xmpp.ChatService;


public class ChatListFragment extends Fragment {

    private FragmentActivity fActivity;

    private RelativeLayout rLayout;

    private OnChatSelectedListener mCallback;

    private ListView chatListView;

    private List<Chat> chatList;

    private ChatListAdapter chatListAdapter;

    public static ChatListFragment newInstance() {
        ChatListFragment fragment = new ChatListFragment();

        Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fActivity = (FragmentActivity) super.getActivity();

        rLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_chat_list, container, false);

        // get list view
        chatListView = (ListView) rLayout.findViewById(R.id.chat_list);

        // add onclick listener for item selection
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Chat selectedChat = (Chat) parent.getItemAtPosition(position);

                mCallback.onChatSelected(selectedChat);
            }
        });

        //chatList = CommunicationDatabaseHelper.getHelper(fActivity.getApplicationContext()).getChats();

        // create adapter
        chatListAdapter = new ChatListAdapter(fActivity, CommunicationDatabaseHelper.getHelper(fActivity.getApplicationContext()).getChats());

        // set adapter
        chatListView.setAdapter(chatListAdapter);

        // we must return the loaded layout
        return rLayout;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnChatSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChatSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_list, menu);
        return true;
    }*/

    public void updateFragment() {
        // clear adapter and set it up again
        ((ArrayAdapter<Object>) chatListView.getAdapter()).clear();
        chatListAdapter = new ChatListAdapter(fActivity, CommunicationDatabaseHelper.getHelper(fActivity.getApplicationContext()).getChats());

        // set adapter
        chatListView.setAdapter(chatListAdapter);
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

    public interface OnChatSelectedListener {
        public void onChatSelected(Chat chat);
    }
}
