package com.vgroyalchemist.vos;

public class ReminderScheduleDetailsVO {

    String Time;
    String dosage;
    String dateday;
    String ReminderDateTime;

    public String getReminderDateTime() {
        return ReminderDateTime;
    }

    public void setReminderDateTime(String reminderDateTime) {
        ReminderDateTime = reminderDateTime;
    }

    public String getDateday() {
        return dateday;
    }


    public void setDateday(String dateday) {
        this.dateday = dateday;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}
