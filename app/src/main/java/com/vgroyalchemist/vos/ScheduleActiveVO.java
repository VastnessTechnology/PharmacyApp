package com.vgroyalchemist.vos;

public class ScheduleActiveVO {

    String ScheduleReminderId;
    String ScheduleId;
    String ReminderTime;
    String dosage;
    String MedicineId;
    String MedicineName;
    String action;
    String Time;
    String dateday;
    String colourcode;

    public String getColourcode() {
        return colourcode;
    }

    public void setColourcode(String colourcode) {
        this.colourcode = colourcode;
    }

    public String getReminderTime() {
        return ReminderTime;
    }

    public void setReminderTime(String reminderTime) {
        ReminderTime = reminderTime;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public String getDateday() {
        return dateday;
    }

    public void setDateday(String dateday) {
        this.dateday = dateday;
    }

    public void setScheduleId(String scheduleId) {
        ScheduleId = scheduleId;
    }

    public String getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleReminderId(String scheduleReminderId) {
        ScheduleReminderId = scheduleReminderId;
    }

    public String getScheduleReminderId() {
        return ScheduleReminderId;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTime() {
        return Time;
    }

}
