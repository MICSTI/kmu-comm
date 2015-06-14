package itm.fhj.at.kmucomm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michael.stifter on 27.05.2015.
 */
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String resource;
    private List<String> participantList;
    private List<Message> messageList;

    public Chat() {
        participantList = new ArrayList<String>();
        messageList = new ArrayList<Message>();
    }

    public Chat(String resource) {
        setResource(resource);
        participantList = new ArrayList<String>();
        messageList = new ArrayList<Message>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<String> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<String> participantList) {
        this.participantList = participantList;
    }

    /**
     * Returns the message text of the last message in the chat.
     * E.g. for displaying the last message on the chat activity
     */
    public String getLastMessageText() {
        if (messageList.size() > 0) {
            Message lastMessage = messageList.get(messageList.size() - 1);

            return lastMessage.getFrom() + ": " + lastMessage.getText();
        }

        return "no message";
    }

    /**
     * Returns the timestamp of the last sent message in the chat.
     */
    public long getLastMessageTimestamp() {
        if (messageList.size() > 0) {
            return messageList.get(messageList.size() - 1).getTimestamp();
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;

        Chat chat = (Chat) o;

        if (id != chat.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
