package com.vgroyalchemist.vos;

public class ServerResponseVO {

    String msg;
    String status;
    Object data;
    String Usrid;
    String EmailId;
    String IsActive;
    String FirstName;
    String LastName;
    String MobileNo;
    String AddressId;
    String CartCount;
    String OrderNo;
    String OrderStatus;
    String CheckOTP;
    String IsVerified;
    String NotiCount;


    public String getNotiCount() {
        return NotiCount;
    }

    public void setNotiCount(String notiCount) {
        NotiCount = notiCount;
    }

    public String getIsVerified() {
        return IsVerified;
    }

    public void setIsVerified(String isVerified) {
        IsVerified = isVerified;
    }

    public String getCheckOTP() {
        return CheckOTP;
    }

    public void setCheckOTP(String checkOTP) {
        CheckOTP = checkOTP;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getCartCount() {
        return CartCount;
    }

    public void setCartCount(String cartCount) {
        CartCount = cartCount;
    }

    public String getAddressId() {
        return AddressId;
    }

    public void setAddressId(String addressId) {
        AddressId = addressId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getUsrid() {
        return Usrid;
    }

    public void setUsrid(String usrid) {
        Usrid = usrid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
