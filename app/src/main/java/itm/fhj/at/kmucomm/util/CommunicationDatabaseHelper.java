package itm.fhj.at.kmucomm.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import itm.fhj.at.kmucomm.data.DummyDataAccessor;
import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;

/**
 * Created by michael.stifter on 02.06.2015.
 */
public class CommunicationDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "comm4kmu.db";

    public static final String CONTACT_TABLE_NAME = "contacts";
    public static final String CHAT_TABLE_NAME = "chats";
    public static final String MESSAGE_TABLE_NAME = "messages";
    public static final String CHAT_PARTICIPANTS_TABLE_NAME = "chat_participants";

    // Contacts table
    public static final String CONTACT_KEY_ID = "_id";
    public static final String CONTACT_KEY_USERNAME = "username";
    public static final String CONTACT_KEY_FIRST_NAME = "first_name";
    public static final String CONTACT_KEY_LAST_NAME = "last_name";
    public static final String CONTACT_KEY_PASSWORD = "password";
    public static final String CONTACT_KEY_DEPARTMENT = "department";
    public static final String CONTACT_KEY_PHONE = "phone";
    public static final String CONTACT_KEY_EMAIL = "email";

    private static final String CONTACT_TABLE_CREATE =
            "CREATE TABLE " + CONTACT_TABLE_NAME + " (" +
                    CONTACT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONTACT_KEY_USERNAME + " TEXT, " +
                    CONTACT_KEY_FIRST_NAME + " TEXT, " +
                    CONTACT_KEY_LAST_NAME + " TEXT, " +
                    CONTACT_KEY_PASSWORD + " TEXT, " +
                    CONTACT_KEY_DEPARTMENT + " TEXT, " +
                    CONTACT_KEY_PHONE + " TEXT, " +
                    CONTACT_KEY_EMAIL + " TEXT" +
                    ");";

    // Chats table
    public static final String CHAT_KEY_ID = "_id";
    public static final String CHAT_KEY_NAME = "name";

    private static final String CHAT_TABLE_CREATE =
            "CREATE TABLE " + CONTACT_TABLE_NAME + " (" +
                    CHAT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CHAT_KEY_NAME + " TEXT" +
                    ");";

    // Chat participants table
    public static final String CHAT_PARTICIPANTS_KEY_ID = "_id";
    public static final String CHAT_PARTICIPANTS_FOREIGN_KEY_CHAT_ID = "fk_chat_id";
    public static final String CHAT_PARTICIPANTS_FOREIGN_KEY_CONTACT_ID = "fk_contact_id";

    private static final String CHAT_PARTICIPANTS_CREATE =
            "CREATE TABLE " + CHAT_PARTICIPANTS_TABLE_NAME + " (" +
                    CHAT_PARTICIPANTS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CHAT_PARTICIPANTS_FOREIGN_KEY_CHAT_ID + " INTEGER, " +
                    CHAT_PARTICIPANTS_FOREIGN_KEY_CONTACT_ID + " INTEGER" +
                    ");";

    // Messages table
    public static final String MESSAGES_KEY_ID = "_id";
    public static final String MESSAGES_FOREIGN_KEY_CONTACT_ID = "fk_contact_id";
    public static final String MESSAGES_FOREIGN_KEY_CHAT_ID = "fk_chat_id";
    public static final String MESSAGES_KEY_TEXT = "text";
    public static final String MESSAGES_KEY_TIMESTAMP = "timestamp";

    private static final String MESSAGES_CREATE =
            "CREATE TABLE " + MESSAGE_TABLE_NAME + " (" +
                    MESSAGES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MESSAGES_FOREIGN_KEY_CONTACT_ID + " INTEGER, " +
                    MESSAGES_FOREIGN_KEY_CHAT_ID + " INTEGER, " +
                    MESSAGES_KEY_TEXT + " TEXT, " +
                    MESSAGES_KEY_TIMESTAMP + "INTEGER" +
                    ");";

    Context context;

    private static final int defaultId = -1;

    public CommunicationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create all tables
        db.execSQL(CONTACT_TABLE_CREATE);
        db.execSQL(CHAT_TABLE_CREATE);
        db.execSQL(CHAT_PARTICIPANTS_CREATE);
        db.execSQL(MESSAGES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop all tables
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_PARTICIPANTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGE_TABLE_NAME);

        // create tables from scratch
        onCreate(db);
    }

    public List<Contact> getContacts() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        List<Contact> contacts = new ArrayList<Contact>();

        try {
            db.beginTransaction();

            String order = this.CONTACT_KEY_LAST_NAME + ", " + this.CONTACT_KEY_FIRST_NAME;

            cursor = db.query(this.CONTACT_TABLE_NAME, null, null, null, null, null, order);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(this.CONTACT_KEY_ID);
                int firstNameIndex = cursor.getColumnIndex(this.CONTACT_KEY_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(this.CONTACT_KEY_LAST_NAME);

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

    public int addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(this.CONTACT_KEY_USERNAME, contact.getUsername());
            cv.put(this.CONTACT_KEY_FIRST_NAME, contact.getFirstName());
            cv.put(this.CONTACT_KEY_LAST_NAME, contact.getLastName());
            cv.put(this.CONTACT_KEY_PASSWORD, contact.getPassword());
            cv.put(this.CONTACT_KEY_DEPARTMENT, contact.getDepartment());
            cv.put(this.CONTACT_KEY_PHONE, contact.getPhone());
            cv.put(this.CONTACT_KEY_EMAIL, contact.getEmail());

            lastId = (int) db.insert(this.CONTACT_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return lastId;
    }

    public List<Chat> getChats() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        List<Chat> chats = new ArrayList<Chat>();

        try {
            db.beginTransaction();

            cursor = db.query(this.CONTACT_TABLE_NAME, null, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(this.CHAT_KEY_ID);
                int nameIndex = cursor.getColumnIndex(this.CHAT_KEY_NAME);

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

    public int addChat(Chat chat) {
        SQLiteDatabase db = this.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(this.CHAT_KEY_NAME, chat.getName());

            lastId = (int) db.insert(this.CHAT_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return lastId;
    }

    public List<Message> getChatMessages(Chat chat) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        List<Message> messages = new ArrayList<Message>();

        try {
            db.beginTransaction();

            String where = this.MESSAGES_FOREIGN_KEY_CHAT_ID + " = ?";
            String[] args = {String.valueOf(chat.getId())};
            String order = this.MESSAGES_KEY_TIMESTAMP + " ASC";

            cursor = db.query(this.MESSAGE_TABLE_NAME, null, where, args, null, null, order);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(this.MESSAGES_KEY_ID);
                int fkContactIdIndex = cursor.getColumnIndex(this.MESSAGES_FOREIGN_KEY_CONTACT_ID);
                int timestampIndex = cursor.getColumnIndex(this.MESSAGES_KEY_TIMESTAMP);
                int textIndex = cursor.getColumnIndex(this.MESSAGES_KEY_TEXT);

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

    public int addChatMessage(Chat chat, Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(this.MESSAGES_FOREIGN_KEY_CHAT_ID, chat.getId());
            cv.put(this.MESSAGES_FOREIGN_KEY_CONTACT_ID, message.getSender().getId());
            cv.put(this.MESSAGES_KEY_TEXT, message.getText());
            cv.put(this.MESSAGES_KEY_TIMESTAMP, Util.getTimestamp());

            lastId = (int) db.insert(this.MESSAGE_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return lastId;
    }
}
