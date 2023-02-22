package com.vgroyalchemist.vos;

public class NotificationVO {

    String NotiId;
    String Title;
    String Message;
    String AddedDate;
    String IsRead;


    public String getIsRead() {
        return IsRead;
    }

    public void setIsRead(String isRead) {
        IsRead = isRead;
    }

    public String getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(String addedDate) {
        AddedDate = addedDate;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getNotiId() {
        return NotiId;
    }

    public void setNotiId(String notiId) {
        NotiId = notiId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
