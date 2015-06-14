package itm.fhj.at.kmucomm.model;

import java.io.Serializable;

/**
 * Created by michael.stifter on 25.05.2015.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int chatId;
    private String from;
    private long timestamp;
    private String text;

    public Message() {

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (id != message.id) return false;
        if (timestamp != message.timestamp) return false;
        if (!from.equals(message.from)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
