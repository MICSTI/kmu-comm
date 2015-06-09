package itm.fhj.at.kmucomm.data;

import java.util.List;

import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;

/**
 * Created by michael.stifter on 01.06.2015.
 */
public interface DataInterface {
    /**
     * Returns a list containing all available contacts
     * @return
     */
    public List<Contact> getContacts();

    /**
     * Adds a contact
     * @return integer id of the newly assigned id, or CommunicationDatabaseHelper.defaultId if unsuccessful
     */
    public int addContact(Contact contact);

    /**
     * Returns a list containing all chats
     * @return
     */
    public List<Chat> getChats();

    /**
     * Adds a new chat.
     * @param chat
     * @return integer id of the newly assigned id, or CommunicationDatabaseHelper.defaultId if unsuccessful
     */
    public int addChat(Chat chat);

    /**
     * Returns a list containing all messages for the passed chat object
     * @param chat
     * @return
     */
    public List<Message> getChatMessages(Chat chat);

    /**
     * Adds a new message to a chat.
     * @param chat
     * @param message
     * @return integer id of the newly assigned id, or CommunicationDatabaseHelper.defaultId if unsuccessful
     */
    public int addChatMessage(Chat chat, Message message);
}
