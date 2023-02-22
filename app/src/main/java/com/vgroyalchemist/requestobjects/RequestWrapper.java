package com.vgroyalchemist.requestobjects;

import org.json.JSONObject;


public class RequestWrapper extends ParentRequestVO{

    private ParentRequestVO vo;

    public RequestWrapper(ParentRequestVO vo){
        this.vo = vo;
    }


    @Override
    public JSONObject generateRequest() {

        JSONObject mainRequestObject = new JSONObject();
        try{
            mainRequestObject.put("data", vo.generateRequest());
        }catch (Exception e){
            e.printStackTrace();
        }
        return  mainRequestObject;
    }
}
