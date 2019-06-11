package com.antandbuffalo.homelightrp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("read_ids")
    @Expose
    private List<Object> readIds = null;
    @SerializedName("delivered_ids")
    @Expose
    private List<Object> deliveredIds = null;
    @SerializedName("chat_dialog_id")
    @Expose
    private String chatDialogId;
    @SerializedName("date_sent")
    @Expose
    private Integer dateSent;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("recipient_id")
    @Expose
    private Integer recipientId;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("send_to_chat")
    @Expose
    private Integer sentToChat;

    public Integer getSentToChat() {
        return sentToChat;
    }

    public void setSentToChat(Integer sentToChat) {
        this.sentToChat = sentToChat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public List<Object> getReadIds() {
        return readIds;
    }

    public void setReadIds(List<Object> readIds) {
        this.readIds = readIds;
    }

    public List<Object> getDeliveredIds() {
        return deliveredIds;
    }

    public void setDeliveredIds(List<Object> deliveredIds) {
        this.deliveredIds = deliveredIds;
    }

    public String getChatDialogId() {
        return chatDialogId;
    }

    public void setChatDialogId(String chatDialogId) {
        this.chatDialogId = chatDialogId;
    }

    public Integer getDateSent() {
        return dateSent;
    }

    public void setDateSent(Integer dateSent) {
        this.dateSent = dateSent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }
}
