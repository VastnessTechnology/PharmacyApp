package com.vgroyalchemist.vos;

public class LegalDetailsVO {

    String PrivacyPolicy;
    String ReturnRefundPolicy;
    String TermsCondition;

    public String getPrivacyPolicy() {
        return PrivacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        PrivacyPolicy = privacyPolicy;
    }

    public void setReturnRefundPolicy(String returnRefundPolicy) {
        ReturnRefundPolicy = returnRefundPolicy;
    }

    public String getReturnRefundPolicy() {
        return ReturnRefundPolicy;
    }

    public void setTermsCondition(String termsCondition) {
        TermsCondition = termsCondition;
    }

    public String getTermsCondition() {
        return TermsCondition;
    }
}
