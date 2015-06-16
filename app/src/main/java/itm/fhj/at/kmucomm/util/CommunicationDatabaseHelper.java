package itm.fhj.at.kmucomm.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;

/**
 * Created by michael.stifter on 02.06.2015.
 */
public class CommunicationDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 16;

    public static final String DATABASE_NAME = "comm4kmu";

    public static final String CONTACT_TABLE_NAME = "contacts";
    public static final String CHAT_TABLE_NAME = "chats";
    public static final String MESSAGE_TABLE_NAME = "messages";
    public static final String CHAT_PARTICIPANTS_TABLE_NAME = "chat_participants";

    // Contacts table
    public static final String CONTACT_KEY_USERNAME = "username";
    public static final String CONTACT_KEY_FIRST_NAME = "first_name";
    public static final String CONTACT_KEY_LAST_NAME = "last_name";
    public static final String CONTACT_KEY_PASSWORD = "password";
    public static final String CONTACT_KEY_DEPARTMENT = "department";
    public static final String CONTACT_KEY_PHONE = "phone";
    public static final String CONTACT_KEY_EMAIL = "email";

    private static final String CONTACT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CONTACT_TABLE_NAME + " (" +
                    CONTACT_KEY_USERNAME + " TEXT PRIMARY KEY, " +
                    CONTACT_KEY_FIRST_NAME + " TEXT, " +
                    CONTACT_KEY_LAST_NAME + " TEXT, " +
                    CONTACT_KEY_PASSWORD + " TEXT, " +
                    CONTACT_KEY_DEPARTMENT + " TEXT, " +
                    CONTACT_KEY_PHONE + " TEXT, " +
                    CONTACT_KEY_EMAIL + " TEXT" +
                    ");";

    // Chats table
    public static final String CHAT_KEY_ID = "_id";
    public static final String CHAT_KEY_RESOURCE = "resource";

    private static final String CHAT_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CHAT_TABLE_NAME + " (" +
                    CHAT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CHAT_KEY_RESOURCE + " TEXT" +
                    ");";

    // Chat participants table
    public static final String CHAT_PARTICIPANTS_KEY_ID = "_id";
    public static final String CHAT_PARTICIPANTS_FOREIGN_KEY_CHAT_ID = "fk_chat_id";
    public static final String CHAT_PARTICIPANTS_FOREIGN_KEY_CONTACT_USERNAME = "fk_contact_username";

    private static final String CHAT_PARTICIPANTS_CREATE =
            "CREATE TABLE IF NOT EXISTS " + CHAT_PARTICIPANTS_TABLE_NAME + " (" +
                    CHAT_PARTICIPANTS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CHAT_PARTICIPANTS_FOREIGN_KEY_CHAT_ID + " INTEGER, " +
                    CHAT_PARTICIPANTS_FOREIGN_KEY_CONTACT_USERNAME + " TEXT" +
                    ");";

    // Messages table
    public static final String MESSAGES_FOREIGN_KEY_CHAT_ID = "fk_chat_id";
    public static final String MESSAGES_FOREIGN_KEY_USERNAME = "fk_username";
    public static final String MESSAGES_KEY_TEXT = "text";
    public static final String MESSAGES_KEY_TIMESTAMP = "timestamp";

    private static final String MESSAGES_CREATE =
            "CREATE TABLE IF NOT EXISTS " + MESSAGE_TABLE_NAME + " (" +
                    MESSAGES_FOREIGN_KEY_CHAT_ID + " INTEGER, " +
                    MESSAGES_FOREIGN_KEY_USERNAME + " TEXT, " +
                    MESSAGES_KEY_TEXT + " TEXT, " +
                    MESSAGES_KEY_TIMESTAMP + " INTEGER" +
                    ");";

    Context context;

    public static final int defaultId = -1;

    private static CommunicationDatabaseHelper instance;

    public static synchronized CommunicationDatabaseHelper getHelper(Context context) {
        if (instance == null)
            instance = new CommunicationDatabaseHelper(context);

        return instance;
    }

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

                int usernameIndex = cursor.getColumnIndex(this.CONTACT_KEY_USERNAME);
                int firstNameIndex = cursor.getColumnIndex(this.CONTACT_KEY_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(this.CONTACT_KEY_LAST_NAME);
                int departmentIndex = cursor.getColumnIndex(this.CONTACT_KEY_DEPARTMENT);
                int emailIndex = cursor.getColumnIndex(this.CONTACT_KEY_EMAIL);
                int phoneIndex = cursor.getColumnIndex(this.CONTACT_KEY_PHONE);

                do {
                    Contact contact = new Contact();

                    String username = cursor.getString(usernameIndex);
                    String firstName = cursor.getString(firstNameIndex);
                    String lastName = cursor.getString(lastNameIndex);
                    String email = cursor.getString(emailIndex);
                    String phone = cursor.getString(phoneIndex);
                    String department = cursor.getString(departmentIndex);

                    contact.setUsername(username);
                    contact.setFirstName(firstName);
                    contact.setLastName(lastName);
                    contact.setEmail(email);
                    contact.setPhone(phone);
                    contact.setDepartment(department);

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

    public String getNameByUsername(String username) {
        String name = "";

        SQLiteDatabase db = this.getReadableDatabase();

        String where = this.CONTACT_KEY_USERNAME + " = ?";
        String[] args = {username};

        Cursor cursor = null;

        try {
            db.beginTransaction();

            cursor = db.query(this.CONTACT_TABLE_NAME, null, where, args, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int firstNameIndex = cursor.getColumnIndex(this.CONTACT_KEY_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(this.CONTACT_KEY_LAST_NAME);

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    name = cursor.getString(firstNameIndex) + " " + cursor.getString(lastNameIndex);
                }

                db.setTransactionSuccessful();
            }
        }  finally {
            db.endTransaction();
            db.close();
        }

        return name;
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
            //cv.put(this.CONTACT_KEY_PASSWORD, contact.getPassword());
            cv.put(this.CONTACT_KEY_DEPARTMENT, contact.getDepartment());
            cv.put(this.CONTACT_KEY_PHONE, contact.getPhone());
            cv.put(this.CONTACT_KEY_EMAIL, contact.getEmail());

            db.insert(this.CONTACT_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return lastId;
    }

    public int updateContact(Contact contact) {
        int result = defaultId;

        SQLiteDatabase db = this.getWritableDatabase();

        String where = this.CONTACT_KEY_USERNAME + " = ?";
        String[] args = {contact.getUsername()};

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(this.CONTACT_KEY_FIRST_NAME, contact.getFirstName());
            cv.put(this.CONTACT_KEY_LAST_NAME, contact.getLastName());
            cv.put(this.CONTACT_KEY_DEPARTMENT, contact.getDepartment());
            cv.put(this.CONTACT_KEY_PHONE, contact.getPhone());
            cv.put(this.CONTACT_KEY_EMAIL, contact.getEmail());

            db.update(this.CONTACT_TABLE_NAME, cv, where, args);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return result;
    }

    public List<Chat> getChats() {
        List<Chat> chats = new ArrayList<Chat>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try {
            db.beginTransaction();

            cursor = db.query(this.CHAT_TABLE_NAME, null, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(this.CHAT_KEY_ID);
                int resourceIndex = cursor.getColumnIndex(this.CHAT_KEY_RESOURCE);

                do {
                    Chat chat = new Chat();

                    int id = cursor.getInt(idIndex);
                    String resource = cursor.getString(resourceIndex);

                    chat.setId(id);
                    chat.setResource(resource);

                    // get participants
                    chat.setParticipantList(getParticipants(chat));

                    // get chat messages
                    chat.setMessageList(getChatMessages(id));

                    chats.add(chat);

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            db.setTransactionSuccessful();
        }  finally {
            db.endTransaction();
            db.close();
        }

        //List<Chat> chats = new ArrayList<Chat>();

        //chats.add(new Chat("abcdefg"));

        return chats;
    }

    public Chat getChatById(int chatId) {
        Chat chat = new Chat();

        SQLiteDatabase db = this.getReadableDatabase();

        String where = this.CHAT_KEY_ID + " = ?";
        String[] args = {String.valueOf(chatId)};

        Cursor cursor = null;

        try {
            db.beginTransaction();

            cursor = db.query(this.CHAT_TABLE_NAME, null, where, args, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(this.CHAT_KEY_ID);
                int resourceIndex = cursor.getColumnIndex(this.CHAT_KEY_RESOURCE);

                int id = cursor.getInt(idIndex);
                String resource = cursor.getString(resourceIndex);

                chat.setId(id);
                chat.setResource(resource);

                // get participants
                chat.setParticipantList(getParticipants(chat));

                // get chat messages
                chat.setMessageList(getChatMessages(id));
            }

            db.setTransactionSuccessful();
        }  finally {
            db.endTransaction();
            db.close();
        }

        return chat;
    }

    private List<String> getParticipants(Chat chat) {
        List<String> participantsList = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String where = this.CHAT_PARTICIPANTS_FOREIGN_KEY_CHAT_ID + " = ?";
        String[] args = {String.valueOf(chat.getId())};

        Cursor cursor = null;

        try {
            db.beginTransaction();

            cursor = db.query(this.CHAT_PARTICIPANTS_TABLE_NAME, null, where, args, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int contactUsernameIndex = cursor.getColumnIndex(this.CHAT_PARTICIPANTS_FOREIGN_KEY_CONTACT_USERNAME);

                do {
                    participantsList.add(cursor.getString(contactUsernameIndex));

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            participantsList.add(e.toString());
        } finally {
            db.endTransaction();
            //db.close();
        }

        return participantsList;
    }

    public int addChat(Chat chat) {
        SQLiteDatabase db = this.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            // Add chat to database
            ContentValues cv = new ContentValues();

            cv.put(this.CHAT_KEY_RESOURCE, chat.getResource());

            lastId = (int) db.insert(this.CHAT_TABLE_NAME, null, cv);

            // Add participants to database
            for (String participant : chat.getParticipantList()) {
                ContentValues cvParticipants = new ContentValues();

                cvParticipants.put(this.CHAT_PARTICIPANTS_FOREIGN_KEY_CHAT_ID, lastId);
                cvParticipants.put(this.CHAT_PARTICIPANTS_FOREIGN_KEY_CONTACT_USERNAME, participant);

                db.insert(this.CHAT_PARTICIPANTS_TABLE_NAME, null, cvParticipants);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            //db.close();
        }

        return lastId;
    }

    public List<Message> getChatMessages(int chatId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        List<Message> messages = new ArrayList<Message>();

        try {
            db.beginTransaction();

            String where = this.MESSAGES_FOREIGN_KEY_CHAT_ID + " = ?";
            String[] args = {String.valueOf(chatId)};
            String order = this.MESSAGES_KEY_TIMESTAMP + " ASC";

            cursor = db.query(this.MESSAGE_TABLE_NAME, null, where, args, null, null, order);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int fromIndex = cursor.getColumnIndex(this.MESSAGES_FOREIGN_KEY_USERNAME);
                int timestampIndex = cursor.getColumnIndex(this.MESSAGES_KEY_TIMESTAMP);
                int textIndex = cursor.getColumnIndex(this.MESSAGES_KEY_TEXT);

                do {
                    Message message = new Message();

                    long timestamp = cursor.getLong(timestampIndex);
                    String text = cursor.getString(textIndex);
                    String from = cursor.getString(fromIndex);

                    message.setTimestamp(timestamp);
                    message.setText(text);
                    message.setFrom(from);

                    messages.add(message);

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return messages;
    }

    public Chat findOrCreateChatByResource(String resource) {
        Chat chat = new Chat();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String where = this.CHAT_KEY_RESOURCE + " = ?";
        String[] args = {resource};

        try {
            db.beginTransaction();

            cursor = db.query(this.CHAT_TABLE_NAME, null, where, args, null, null, null);

            if (cursor.getCount() > 0) {
                // find chat
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(this.CHAT_KEY_ID);

                int id = cursor.getInt(idIndex);

                chat.setId(id);
                chat.setResource(resource);

                // get participants
                chat.setParticipantList(getParticipants(chat));

                // get chat messages
                chat.setMessageList(getChatMessages(id));
            } else {
                // create chat
                chat.setId(addChat(chat));
                chat.setResource(resource);
                chat.setMessageList(new ArrayList<Message>());
                chat.setParticipantList(getParticipants(chat));
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return chat;
    }

    public int addMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(this.MESSAGES_FOREIGN_KEY_CHAT_ID, message.getChatId());
            cv.put(this.MESSAGES_FOREIGN_KEY_USERNAME, message.getFrom());
            cv.put(this.MESSAGES_KEY_TEXT, message.getText());
            cv.put(this.MESSAGES_KEY_TIMESTAMP, Util.getTimestamp());

            db.insert(this.MESSAGE_TABLE_NAME, null, cv);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return lastId;
    }

    public int addChatMessage(Chat chat, Message message) {
        SQLiteDatabase db = this.getWritableDatabase();

        int lastId = defaultId;

        try {
            db.beginTransaction();

            ContentValues cv = new ContentValues();

            cv.put(this.MESSAGES_FOREIGN_KEY_CHAT_ID, chat.getId());
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

    public int chatExists(Chat chat) {
        int chatId = defaultId;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String where = this.CHAT_KEY_RESOURCE + " = ?";
        String[] args = {chat.getResource()};

        try {
            db.beginTransaction();

            cursor = db.query(this.CHAT_TABLE_NAME, null, where, args, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int idIndex = cursor.getColumnIndex(this.CHAT_KEY_ID);

                chatId = cursor.getInt(idIndex);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return chatId;
    }

    public boolean contactExists(Contact contact) {
        boolean exists = false;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String where = this.CONTACT_KEY_USERNAME + " = ?";
        String[] args = {contact.getUsername()};

        try {
            db.beginTransaction();

            cursor = db.query(this.CONTACT_TABLE_NAME, null, where, args, null, null, null);

            if (cursor.getCount() > 0) {
                exists = true;
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return exists;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<Message>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        try {
            db.beginTransaction();

            cursor = db.query(this.MESSAGE_TABLE_NAME, null, null, null, null, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                int chatIdIndex = cursor.getColumnIndex(this.MESSAGES_FOREIGN_KEY_CHAT_ID);
                int fromIndex = cursor.getColumnIndex(this.MESSAGES_FOREIGN_KEY_USERNAME);
                int timestampIndex = cursor.getColumnIndex(this.MESSAGES_KEY_TIMESTAMP);
                int textIndex = cursor.getColumnIndex(this.MESSAGES_KEY_TEXT);

                do {
                    Message message = new Message();

                    int chatId = cursor.getInt(chatIdIndex);
                    String from = cursor.getString(fromIndex);
                    long timestamp = cursor.getLong(timestampIndex);
                    String text = cursor.getString(textIndex);

                    message.setChatId(chatId);
                    message.setFrom(from);
                    message.setTimestamp(timestamp);
                    message.setText(text);

                    messages.add(message);

                    cursor.moveToNext();
                } while (!cursor.isAfterLast());
            }

            db.setTransactionSuccessful();
        }  finally {
            db.endTransaction();
            db.close();
        }

        return messages;
    }
}
