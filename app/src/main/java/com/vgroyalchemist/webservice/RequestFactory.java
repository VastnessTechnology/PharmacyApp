package com.vgroyalchemist.webservice;


import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.ProtocolException;
import com.vgroyalchemist.requestobjects.ParentRequestVO;
import com.vgroyalchemist.requestobjects.RequestWrapper;


import org.json.JSONObject;


public class RequestFactory {
    public JSONObject getFinalRequestObject(ParentRequestVO vo) throws ProtocolException {
        JSONObject jsonObject = null;
        RequestWrapper requestWrapper = new RequestWrapper(vo);
        jsonObject = requestWrapper.generateRequest();
        return jsonObject;
    }
}
