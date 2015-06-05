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

    private static final int defaultId = -1;

    public static void init(Context context) {
        cdh = new CommunicationDatabaseHelper(context);
    }

    @Override
    public List<Contact> getContacts() {
        SQLiteDatabase db = cdh.getReadableDatabase();

        Cursor cursor = null;

        List<Contact> contacts = new ArrayList<Contact>();

        try {
            db.beginTransaction();

            String order = cdh.CONTACT_KEY_LAST_NAME + ", " + cdh.CONTACT_KEY_FIRST_NAME;

            cursor = db.query(cdh.CONTACT_TABLE_NAME, null, null, null, null, null, order);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(cdh.CONTACT_KEY_ID);
                int firstNameIndex = cursor.getColumnIndex(cdh.CONTACT_KEY_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(cdh.CONTACT_KEY_LAST_NAME);

                do {
                    Contact contact = new Contact();

                    int id = cursor.getInt(idIndex);
                    String firstName = cursor.getString(firstNameIndex);
                    String lastName = cursor.getString(lastNameIndex);

                    contact.setId(id);
                    contact.setFirstName(firstName);
                    contact.setLastName(lastName);

                    contacts.add(contact);

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return contacts;
    }

    @Override
    public boolean addContact(Contact contact) {
        boolean success = false;

        SQLiteDatabase db = cdh.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(cdh.CONTACT_KEY_FIRST_NAME, contact.getFirstName());
            cv.put(cdh.CONTACT_KEY_LAST_NAME, contact.getLastName());

            lastId = (int) db.insert(cdh.CONTACT_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();

            if (lastId != defaultId)
                success = true;
        } finally {
            db.endTransaction();
            db.close();
        }

        return success;
    }

    @Override
    public List<Chat> getChats() {
        SQLiteDatabase db = cdh.getReadableDatabase();

        Cursor cursor = null;

        List<Chat> chats = new ArrayList<Chat>();

        try {
            db.beginTransaction();

            cursor = db.query(cdh.CONTACT_TABLE_NAME, null, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(cdh.CHAT_KEY_ID);
                int nameIndex = cursor.getColumnIndex(cdh.CHAT_KEY_NAME);

                do {
                    Chat chat = new Chat();

                    int id = cursor.getInt(idIndex);
                    String name = cursor.getString(nameIndex);

                    chat.setId(id);
                    chat.setName(name);
                    chat.setMessageList(getChatMessages(chat));

                    chats.add(chat);

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return chats;
    }

    @Override
    public boolean addChat(Chat chat) {
        boolean success = false;

        SQLiteDatabase db = cdh.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(cdh.CHAT_KEY_NAME, chat.getName());

            lastId = (int) db.insert(cdh.CHAT_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();

            if (lastId != defaultId)
                success = true;
        } finally {
            db.endTransaction();
            db.close();
        }

        return success;
    }

    @Override
    public List<Message> getChatMessages(Chat chat) {
        SQLiteDatabase db = cdh.getReadableDatabase();

        Cursor cursor = null;

        List<Message> messages = new ArrayList<Message>();

        try {
            db.beginTransaction();

            String where = cdh.MESSAGES_FOREIGN_KEY_CHAT_ID + " = ?";
            String[] args = {String.valueOf(chat.getId())};
            String order = cdh.MESSAGES_KEY_TIMESTAMP + " ASC";

            cursor = db.query(cdh.MESSAGE_TABLE_NAME, null, where, args, null, null, order);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(cdh.MESSAGES_KEY_ID);
                int fkContactIdIndex = cursor.getColumnIndex(cdh.MESSAGES_FOREIGN_KEY_CONTACT_ID);
                int timestampIndex = cursor.getColumnIndex(cdh.MESSAGES_KEY_TIMESTAMP);
                int textIndex = cursor.getColumnIndex(cdh.MESSAGES_KEY_TEXT);

                do {
                    Message message = new Message();

                    int id = cursor.getInt(idIndex);
                    int fkContactId = cursor.getInt(fkContactIdIndex);
                    int timestamp = cursor.getInt(timestampIndex);
                    String text = cursor.getString(textIndex);

                    message.setId(id);
                    message.setSender(null);
                    message.setTimestamp(timestamp);
                    message.setText(text);

                    messages.add(message);

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return messages;
    }

    @Override
    public boolean addChatMessage(Chat chat, Message message) {
        boolean success = false;

        SQLiteDatabase db = cdh.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(cdh.MESSAGES_FOREIGN_KEY_CHAT_ID, chat.getId());
            cv.put(cdh.MESSAGES_FOREIGN_KEY_CONTACT_ID, message.getSender().getId());
            cv.put(cdh.MESSAGES_KEY_TEXT, message.getText());
            cv.put(cdh.MESSAGES_KEY_TIMESTAMP, Util.getTimestamp());

            lastId = (int) db.insert(cdh.MESSAGE_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();

            if (lastId != defaultId)
                success = true;
        } finally {
            db.endTransaction();
            db.close();
        }

        return success;
    }
}
