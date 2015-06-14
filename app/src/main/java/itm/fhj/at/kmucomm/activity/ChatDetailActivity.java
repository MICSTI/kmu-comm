package itm.fhj.at.kmucomm.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.adapter.ChatListAdapter;
import itm.fhj.at.kmucomm.adapter.MessageListAdapter;
import itm.fhj.at.kmucomm.data.DummyDataAccessor;
import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;
import itm.fhj.at.kmucomm.util.CommunicationDatabaseHelper;
import itm.fhj.at.kmucomm.util.Config;
import itm.fhj.at.kmucomm.util.Util;
import itm.fhj.at.kmucomm.xmpp.ChatService;

public class ChatDetailActivity extends ActionBarActivity {

    private Chat chat;

    private ListView messageListView;

    private EditText messageText;

    private MessageListAdapter messageListAdapter;

    private List<Message> messageList;

    public static final int REQUEST_CODE_CHAT_DETAIL = 100;

    public static final String EXTRA_MESSAGE = "itm.fhj.at.kmucomm.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // make sure keyboard is not displayed at first
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // add reference to ChatService to receive live updates of incoming messages
        ChatService.getInstance().setChatDetailActivity(this);

        // get list view
        messageListView = (ListView) findViewById(R.id.message_list);

        // get edit text
        messageText = (EditText) findViewById(R.id.message_text);

        // get chat from intent extra
        chat = (Chat) getIntent().getSerializableExtra(EXTRA_MESSAGE);

        // set activity title to chat name
        setTitle(Util.getCamelCase(chat.getResource()));

        // message list
        messageList = chat.getMessageList();

        // create adapter
        messageListAdapter = new MessageListAdapter(this, messageList);

        // set adapter
        messageListView.setAdapter(messageListAdapter);

        // add onclick listener for adding new message button
        final Button btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message newMessage = new Message();

                newMessage.setChatId(chat.getId());
                newMessage.setText(messageText.getText().toString());
                newMessage.setFrom(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("username", ""));
                newMessage.setTimestamp(Util.getTimestamp());

                // send message via xmpp
                ChatService chatService = ChatService.getInstance();
                chatService.sendMessage(chat.getParticipantList().get(0), newMessage.getText());

                // add message to local database
                chatService.getDb().addMessage(newMessage);

                // clear text view for future input
                messageText.setText("");

                // update message list
                messageList.add(newMessage);

                // update list view
                ((ArrayAdapter<Object>) messageListView.getAdapter()).notifyDataSetChanged();
            }
        });

        // set focus to edit text
        messageText.requestFocus();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat_detail, menu);
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

    public void updateMessageList() {
        // clear adapter and set it up again
        ((ArrayAdapter<Object>) messageListView.getAdapter()).clear();
        messageListAdapter = new MessageListAdapter(this, CommunicationDatabaseHelper.getHelper(this).getChatMessages(chat.getId()));

        // set adapter
        messageListView.setAdapter(messageListAdapter);
    }
}
