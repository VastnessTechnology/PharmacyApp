package com.vgroyalchemist.vos;

public class VitalListVO {

    String VitalID;
    String Vital;
    String Unit;


    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public void setVital(String vital) {
        Vital = vital;
    }

    public String getVital() {
        return Vital;
    }

    public String getVitalID() {
        return VitalID;
    }

    public void setVitalID(String vitalID) {
        VitalID = vitalID;
    }
}
