package itm.fhj.at.kmucomm.data;

import java.util.ArrayList;
import java.util.List;

import itm.fhj.at.kmucomm.model.Chat;
import itm.fhj.at.kmucomm.model.Contact;
import itm.fhj.at.kmucomm.model.Message;

/**
 * Created by michael.stifter on 01.06.2015.
 */
public class DummyData implements DataInterface {

    private List<Contact> contacts;
    private List<Chat> chats;

    public static final int OPERATION_SUCCESS = 1;
    public static final int OPERATION_FAILURE = 0;

    public DummyData() {
        // Contacts
        contacts = new ArrayList<Contact>();

        Contact flo = new Contact();
        flo.setId(1);
        flo.setUsername("mayerhofer13");
        flo.setFirstName("Florian");
        flo.setLastName("Mayerhofer");
        flo.setDepartment("ITM");
        flo.setEmail("florian.mayerhofer@edu.fh-joanneum.at");

        Contact andi = new Contact();
        andi.setId(2);
        andi.setUsername("hoeffernig13");
        andi.setFirstName("Andreas");
        andi.setLastName("Höffernig");
        andi.setDepartment("ITM");

        Contact kevin = new Contact();
        kevin.setId(3);
        kevin.setUsername("pfeifer13");
        kevin.setFirstName("Kevin");
        kevin.setLastName("Pfeifer");
        kevin.setDepartment("ITM");

        Contact michi = new Contact();
        michi.setId(4);
        michi.setUsername("stifter13");
        michi.setFirstName("Michael");
        michi.setLastName("Stifter");
        michi.setEmail("michael.stifter2@edu.fh-joanneum.at");
        michi.setPhone("0664/1234567");
        michi.setDepartment("ITM");

        contacts.add(flo);
        contacts.add(andi);
        contacts.add(kevin);
        contacts.add(michi);

        // Chats
        chats = new ArrayList<Chat>();

        Chat kmu = new Chat();
        kmu.setId(1);
        kmu.setName("KMU Group Chat");

        List<Message> kmuMessages = new ArrayList<Message>();

        Message msg1 = new Message();
        msg1.setId(1);
        msg1.setSender(flo);
        msg1.setText("First chat message");
        msg1.setTimestamp(1);

        Message msg2 = new Message();
        msg2.setId(2);
        msg2.setSender(michi);
        msg2.setText("Second chat message");
        msg2.setTimestamp(2);

        kmuMessages.add(msg1);
        kmuMessages.add(msg2);

        kmu.setMessageList(kmuMessages);

        Chat itm = new Chat();
        itm.setId(2);
        itm.setName("ITM13 Chat");

        List<Message> itmMessages = new ArrayList<Message>();

        Message msg3 = new Message();
        msg3.setId(3);
        msg3.setSender(andi);
        msg3.setText("Morgen ist frei!");
        msg3.setTimestamp(3);

        itmMessages.add(msg3);

        itm.setMessageList(itmMessages);

        chats.add(kmu);
        chats.add(itm);
    }

    @Override
    public List<Contact> getContacts() {
        return contacts;
    }

    @Override
    public List<Chat> getChats() {
        return chats;
    }

    @Override
    public List<Message> getChatMessages(Chat chat) {
        return chats.get(chats.indexOf(chat)).getMessageList();
    }

    @Override
    public int addContact(Contact contact) {
        return contacts.add(contact) ? OPERATION_SUCCESS : OPERATION_FAILURE;
    }

    @Override
    public int addChat(Chat chat) {
        return chats.add(chat) ? OPERATION_SUCCESS : OPERATION_FAILURE;
    }

    @Override
    public int addChatMessage(Chat chat, Message message) {
        List<Message> messageList = chat.getMessageList();
        messageList.add(message);

        //chat.setMessageList(messageList);
        chats.get(chats.indexOf(chat)).setMessageList(messageList);

        return OPERATION_SUCCESS;
    }
}
