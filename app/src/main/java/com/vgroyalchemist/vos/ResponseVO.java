package com.vgroyalchemist.vos;


public class ResponseVO {
    private String responseId;
    private String pageId;
    private String requestParams;
    private String response;
    private int priority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

       ResponseVO that = (ResponseVO) o;

        return !(responseId != null ? !responseId.equals(that.responseId) : that.responseId != null);

    }

    @Override
    public int hashCode() {
        return responseId != null ? responseId.hashCode() : 0;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {

        this.responseId = responseId;
//        Utility.debugger(" Response ID  :: " + responseId);
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {

        this.pageId = pageId;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
//        Utility.debugger("Response "+response);
        this.response = response;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
