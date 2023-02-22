package com.vgroyalchemist.vos;

public class TrackOrderVO {

    String OrderStatusId;
    String OrderStatusName;
    String OrderStatusValue;

    public String getDeliveryboyid() {
        return deliveryboyid;
    }

    public void setDeliveryboyid(String deliveryboyid) {
        this.deliveryboyid = deliveryboyid;
    }

    String deliveryboyid;


    public String getOrderStatusId() {
        return OrderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        OrderStatusId = orderStatusId;
    }

    public String getOrderStatusName() {
        return OrderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        OrderStatusName = orderStatusName;
    }

    public String getOrderStatusValue() {
        return OrderStatusValue;
    }

    public void setOrderStatusValue(String orderStatusValue) {
        OrderStatusValue = orderStatusValue;
    }
}
