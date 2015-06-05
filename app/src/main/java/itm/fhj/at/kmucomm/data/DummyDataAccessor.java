package itm.fhj.at.kmucomm.data;

import java.util.List;

import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;

/**
 * Created by michael.stifter on 01.06.2015.
 */
public class DummyDataAccessor {
    private static DummyDataAccessor ourInstance = new DummyDataAccessor();

    public static DummyDataAccessor getInstance() {
        return ourInstance;
    }

    private DummyData data;

    private DummyDataAccessor() {
        data = new DummyData();
    }

    public List<Contact> getContacts() {
        return data.getContacts();
    }

    public List<Chat> getChats() {
        return data.getChats();
    }

    public List<Message> getChatMessages(Chat chat) {
        return data.getChatMessages(chat);
    }

    public boolean addContact(Contact contact) {
        return data.addContact(contact);
    }

    public boolean addChat(Chat chat) {
        return data.addChat(chat);
    }

    public boolean addChatMessage(Chat chat, Message message) {
        return data.addChatMessage(chat, message);
    }
}
