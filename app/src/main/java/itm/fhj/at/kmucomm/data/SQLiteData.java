package itm.fhj.at.kmucomm.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;
import itm.fhj.at.kmucomm.util.CommunicationDatabaseHelper;
import itm.fhj.at.kmucomm.util.Util;

/**
 * Created by michael.stifter on 02.06.2015.
 */
public class SQLiteData implements DataInterface {

    private static CommunicationDatabaseHelper cdh;

    public static void init(Context context) {
        cdh = CommunicationDatabaseHelper.getHelper(context);
    }

    public SQLiteData(Context context) {
        init(context);
    }

    public int incomingChat(String resource, List<String> participantList) {
        int chatId = cdh.defaultId;

        Chat chat = new Chat(resource);
        chat.setParticipantList(participantList);

        chatId = cdh.chatExists(chat);

        if (chatId == cdh.defaultId) {
            // add chat to local database
            chatId = addChat(chat);
        }

        return chatId;
    }

    public void incomingMessage(int chatId, String from, String text) {
        Message message = new Message();

        message.setTimestamp(Util.getTimestamp());
        message.setChatId(chatId);
        message.setFrom(from);
        message.setText(text);

        addMessage(message);
    }

    @Override
    public List<Contact> getContacts() {
        return cdh.getContacts();
    }

    @Override
    public int addContact(Contact contact) {
        return cdh.addContact(contact);
    }

    @Override
    public List<Chat> getChats() {
        return cdh.getChats();
    }

    @Override
    public int addChat(Chat chat) {
        return cdh.addChat(chat);
    }

    @Override
    public List<Message> getChatMessages(Chat chat) {
        // TODO: Remove and delete from interface!
        return null;
    }

    public List<Message> getChatMessages(int chatId) {
        return cdh.getChatMessages(chatId);
    }

    @Override
    public int addChatMessage(Chat chat, Message message) {
        return cdh.addChatMessage(chat, message);
    }

    public int addMessage(Message message) {
        return cdh.addMessage(message);
    }

    public void addDummyRecords() {
        DummyDataAccessor dda = DummyDataAccessor.getInstance();

        // contacts
        for (Contact c : dda.getContacts()) {
            addContact(c);
        }

        // chats
        for (Chat c : dda.getChats()) {
            addChat(c);
        }
    }

    public List<Message> getAllMessages() {
        return cdh.getAllMessages();
    }
}
