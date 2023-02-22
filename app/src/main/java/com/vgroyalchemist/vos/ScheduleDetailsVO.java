package com.vgroyalchemist.vos;

import java.util.ArrayList;

public class ScheduleDetailsVO {

    String ScheduleId;
    String MedicineId;
    String MedicineName;
    String Remark;
    String StartDate;
    String EndDate;
    String SchedulePattener;

    ArrayList<ReminderScheduleDetailsVO> mReminderScheduleDetailsVOS;

    public ArrayList<ReminderScheduleDetailsVO> getmReminderScheduleDetailsVOS() {
        return mReminderScheduleDetailsVOS;
    }

    public void setmReminderScheduleDetailsVOS(ArrayList<ReminderScheduleDetailsVO> mReminderScheduleDetailsVOS) {
        this.mReminderScheduleDetailsVOS = mReminderScheduleDetailsVOS;
    }

    public String getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(String scheduleId) {
        ScheduleId = scheduleId;
    }

    public String getMedicineId() {
        return MedicineId;
    }

    public void setMedicineId(String medicineId) {
        MedicineId = medicineId;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSchedulePattener() {
        return SchedulePattener;
    }

    public void setSchedulePattener(String schedulePattener) {
        SchedulePattener = schedulePattener;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }
}


