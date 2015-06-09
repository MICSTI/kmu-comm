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
        cdh = new CommunicationDatabaseHelper(context);
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
        return cdh.getChatMessages(chat);
    }

    @Override
    public int addChatMessage(Chat chat, Message message) {
        return cdh.addChatMessage(chat, message);
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
}
