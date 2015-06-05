package itm.fhj.at.kmucomm.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String CONTACT_KEY_FIRST_NAME = "first_name";
    public static final String CONTACT_KEY_LAST_NAME = "last_name";

    private static final String CONTACT_TABLE_CREATE =
            "CREATE TABLE " + CONTACT_TABLE_NAME + " (" +
                    CONTACT_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CONTACT_KEY_FIRST_NAME + " TEXT, " +
                    CONTACT_KEY_LAST_NAME + " TEXT" +
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
}
