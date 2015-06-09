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

import itm.fhj.at.kmucomm.util.Config;
import itm.fhj.at.kmucomm.util.Util;

/**
 * Created by michael.stifter on 09.06.2015.
 */
public class ChatService {

    private static final String TAG = ChatService.class.getName();

    private static ChatService instance;

    public static ChatService getInstance(String username, String password) {
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
           return "User not logged in";
        } catch (SmackException e) {
            Log.e(TAG, e.toString());
            return "User not logged in";
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return "User not logged in";
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return "User not logged in";
        }

        return "User logged in";
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

}
