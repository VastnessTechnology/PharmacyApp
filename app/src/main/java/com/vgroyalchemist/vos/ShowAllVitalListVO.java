package com.vgroyalchemist.vos;

public class ShowAllVitalListVO {

    String VitalID;
    String VitalDate;
    String Vital;
    String Unit;
    String Value;
    String Time;
    String VitalTime;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVitalTime() {
        return VitalTime;
    }

    public void setVitalTime(String vitalTime) {
        VitalTime = vitalTime;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getVitalID() {
        return VitalID;
    }

    public void setVitalID(String vitalID) {
        VitalID = vitalID;
    }

    public String getVital() {
        return Vital;
    }

    public void setVital(String vital) {
        Vital = vital;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getVitalDate() {
        return VitalDate;
    }

    public void setVitalDate(String vitalDate) {
        VitalDate = vitalDate;
    }
}
