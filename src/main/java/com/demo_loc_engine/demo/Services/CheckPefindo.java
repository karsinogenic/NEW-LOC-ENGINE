package com.demo_loc_engine.demo.Services;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo_loc_engine.demo.Models.CustomerParam;

public class CheckPefindo {

    public Boolean pefindo(JSONObject isi, AESComponent component) throws Exception {
        HTTPRequest httpRequest = new HTTPRequest(component);
        String hasil = httpRequest.postRequest(component.getPefindoUrl(), isi.toString());
        JSONObject hasilJsonObject = new JSONObject(hasil);
        Boolean hasilBoolean = hasilJsonObject.getString("status").toLowerCase().contains("not") ? false : true;
        return hasilBoolean;
    }

    public JSONObject validateAndCreate(Map<String, Object> isi) {
        JSONObject jsonToSend = new JSONObject();
        jsonToSend.put("dob", isi.get("dob").toString());
        jsonToSend.put("fullname", isi.get("fullname").toString());
        jsonToSend.put("nik", isi.get("custNumber").toString());
        jsonToSend.put("id", isi.get("id").toString());
        jsonToSend.put("path", isi.get("path").toString());
        return jsonToSend;
    }

    public String cekParamPefindo(Map<String, Object> isi) {
        String[] bodyPefindo = { "dob", "fullname", "custNumber", "id", "path" };
        String hasil = "";
        for (String string : bodyPefindo) {
            try {
                isi.get(string).toString();
            } catch (Exception e) {
                hasil = string;
                break;
            }
        }

        return hasil;
    }

}
