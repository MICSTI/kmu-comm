package itm.fhj.at.kmucomm.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by michael.stifter on 27.05.2015.
 */
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private List<Message> messageList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    /**
     * Returns the message text of the last message in the chat.
     * E.g. for displaying the last message on the chat activity
     */
    public String getLastMessageText() {
        Message lastMessage = messageList.get(messageList.size() - 1);

        return lastMessage.getSender().getName() + ": " + lastMessage.getText();
    }

    /**
     * Returns the timestamp of the last sent message in the chat.
     */
    public long getLastMessageTimestamp() {
        return messageList.get(messageList.size() - 1).getTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;

        Chat chat = (Chat) o;

        if (id != chat.id) return false;
        if (!name.equals(chat.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
