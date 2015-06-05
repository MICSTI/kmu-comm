package itm.fhj.at.kmucomm.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import itm.fhj.at.kmucomm.R;
import itm.fhj.at.kmucomm.adapter.MessageListAdapter;
import itm.fhj.at.kmucomm.data.DummyDataAccessor;
import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;
import itm.fhj.at.kmucomm.util.Util;

public class ChatDetailActivity extends ActionBarActivity {

    private Chat chat;

    private ListView messageListView;

    private EditText messageText;

    private MessageListAdapter messageListAdapter;

    public static final int REQUEST_CODE_CHAT_DETAIL = 100;

    public static final String EXTRA_MESSAGE = "itm.fhj.at.kmucomm.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);

        // make sure keyboard is not displayed at first
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // get list view
        messageListView = (ListView) findViewById(R.id.message_list);

        // get edit text
        messageText = (EditText) findViewById(R.id.message_text);

        // get chat from intent extra
        chat = (Chat) getIntent().getSerializableExtra(EXTRA_MESSAGE);

        // set activity title to chat name
        setTitle(chat.getName());

        // create adapter
        messageListAdapter = new MessageListAdapter(this, chat.getMessageList());

        // set adapter
        messageListView.setAdapter(messageListAdapter);

        // add onclick listener for adding new message button
        final Button btnSave = (Button) findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();

                Contact contact = DummyDataAccessor.getInstance().getContacts().get(3);

                message.setSender(contact);
                message.setText(messageText.getText().toString());
                message.setTimestamp(Util.getTimestamp());
                message.setId(99);

                DummyDataAccessor.getInstance().addChatMessage(chat, message);

                // clear text view for future input
                messageText.setText("");

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
}
