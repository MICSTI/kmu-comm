package itm.fhj.at.kmucomm.xmpp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

import itm.fhj.at.kmucomm.activity.MainActivity;
import itm.fhj.at.kmucomm.util.Config;
import itm.fhj.at.kmucomm.util.Util;

/**
 * Created by michael.stifter on 09.06.2015.
 */
public class ChatService {

    public static final String TAG = ChatService.class.getName();

    public static final String MSG_SUCCESS = "Success";
    public static final String MSG_CONNECTING = "Trying to connect to Openfire server";
    public static final String MSG_USER_LOGGED_IN = "User successfully logged in as ";
    public static final String MSG_USER_NOT_LOGGED_IN = "User not logged in";

    private static ChatService instance;

    private static MainActivity mActivity;

    public static ChatService getInstance(MainActivity mainActivity, String username, String password) {
        mActivity = mainActivity;
        AUTH_USERNAME = username;
        AUTH_PASSWORD = password;

        return getInstance();
    }

    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }

        return instance;
    }

    public ChatService() {

    }

    private AbstractXMPPConnection connection;

    private static String AUTH_USERNAME = "";
    private static String AUTH_PASSWORD = "";

    private String createConnection() {
        setStatusText(MSG_CONNECTING);

        // Create XMPP connection
        System.setProperty("java.net.preferIPv6Addresses", "false");

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(AUTH_USERNAME, AUTH_PASSWORD)
                .setHost(Config.OPENFIRE_SERVER)
                .setServiceName(Config.OPENFIRE_SERVER)
                .setPort(Config.OPENFIRE_PORT)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();

        connection = new XMPPTCPConnection(config);

        try {
            connection.connect().login();

            // send new status message
            setStatusText(MSG_USER_LOGGED_IN + AUTH_USERNAME);

            // Register listeners for chats and messages
            ChatManager chatManager = ChatManager.getInstanceFor(connection);

            chatManager.addChatListener(new ChatManagerListener() {
                @Override
                public void chatCreated(Chat chat, boolean createdLocally) {
                    chat.addMessageListener(new ChatMessageListener() {
                        @Override
                        public void processMessage(Chat chat, Message message) {

                        }
                    });
                }
            });

            /*
            //ChatManager chatManager = ChatManager.getInstanceFor(connection);
            Chat chat = ChatManager.getInstanceFor(connection).createChat(Config.OF_BOB_USERNAME, new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {

                }
            });

            chat.sendMessage("Openfire Message #2");*/


            //Chat newChat = chatManager.createChat("", new )

            //connection.disconnect();
        } catch (XMPPException e) {
            Log.e(TAG, e.toString());
            setStatusText(MSG_USER_NOT_LOGGED_IN);
            return MSG_USER_NOT_LOGGED_IN;
        } catch (SmackException e) {
            Log.e(TAG, e.toString());
            setStatusText(MSG_USER_NOT_LOGGED_IN);
            return MSG_USER_NOT_LOGGED_IN;
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            setStatusText(MSG_USER_NOT_LOGGED_IN);
            return MSG_USER_NOT_LOGGED_IN;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            setStatusText(MSG_USER_NOT_LOGGED_IN);
            return MSG_USER_NOT_LOGGED_IN;
        }

        return MSG_SUCCESS;
    }

    public void connect(XMPPActionListener listener) {
        XMPPConnectionHelperTask helperTask = new XMPPConnectionHelperTask(listener);
        helperTask.execute("GO");
    }

    public void closeConnection() {
        connection.disconnect();
    }

    class XMPPConnectionHelperTask extends AsyncTask<String, String, String> {

        private XMPPActionListener listener;

        public XMPPConnectionHelperTask(XMPPActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(String... params) {
            return createConnection();
        }

        @Override
        protected void onPostExecute(String s) {
            if (listener != null)
                listener.onUpdated(s);

            super.onPostExecute(s);
        }
    }

    public interface XMPPActionListener {
        void onUpdated(String msg);
    }

    public interface StatusActionListener {
        void onUpdated(String msg);
    }

    private class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            chat.addMessageListener(new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {

                }
            });
        }
    }

    private void setStatusText(final String text) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.setStatusText(text);
            }
        });
    }

}
