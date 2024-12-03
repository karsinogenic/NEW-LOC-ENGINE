package com.demo_loc_engine.demo.Services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.demo_loc_engine.demo.Models.ChannelResponse;
import com.demo_loc_engine.demo.Models.FirebaseConfig;
import com.demo_loc_engine.demo.Models.LogAscend;
import com.demo_loc_engine.demo.Models.NewAESComponent;
import com.demo_loc_engine.demo.Repositories.ChannelResponseRepository;
import com.demo_loc_engine.demo.Repositories.FirebaseConfigRepository;

public class FirebaseService {

    private FirebaseConfigRepository fcr;
    private ChannelResponseRepository crr;
    private LogAscend logAscend;
    private NewAESComponent aesComponent;
    private AESComponent aesComponent2;
    private String url;

    public FirebaseService(ChannelResponseRepository crr, LogAscend logAscend, FirebaseConfigRepository fcr,
            NewAESComponent aesComponent, String url, AESComponent aesComponent2) {
        this.crr = crr;
        this.logAscend = logAscend;
        this.fcr = fcr;
        this.aesComponent = aesComponent;
        this.url = url;
        this.aesComponent2 = aesComponent2;
    }

    public JSONObject createJsonFirebase(ChannelResponseRepository crr, LogAscend logAscend, String status) {
        ChannelResponse channelResponse = crr.getByReferenceId(logAscend.getReferenceId()).get();
        FirebaseConfig firebaseConfig = fcr.findNotifLoc(status);
        JSONObject jsonObject = new JSONObject(firebaseConfig);
        jsonObject.remove("id");
        jsonObject.remove("notif_type");
        jsonObject.put("phone_number", channelResponse.getMobileNumber());
        jsonObject.put("key_noNotif", channelResponse.getReferenceId());
        return jsonObject;
    }

    public Boolean sendToFireBase(String status) {
        JSONObject fireObject = this.createJsonFirebase(crr, logAscend, status);
        // System.out.println("fire_object: " + fireObject.toString());
        String AESstring = this.makeAES(fireObject, aesComponent);
        Boolean sendJson = this.sendJson(AESstring, aesComponent);
        return sendJson;
    }

    public String makeAES(JSONObject jsonObject, NewAESComponent aesComponent) {
        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
        String isi = jsonObject.toString();
        isi = isi.replaceAll("\n", "");
        String encrypted = "";
        try {
            encrypted = aesEncryptDecrypt.encrypt(isi, aesComponent.getAesKey(), aesComponent.getAesIV());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(encrypted);
        return encrypted;
    }

    public Boolean sendJson(String aes, NewAESComponent aesComponent) {
        Map hasil = new HashMap<>();
        ChannelResponse channelResponse = crr.getByReferenceId(logAscend.getReferenceId()).get();
        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
        HTTPRequest httpRequest = new HTTPRequest(aesComponent2);
        LocalDate localDate = LocalDate.now();

        JSONObject msg = new JSONObject();
        msg.put("msg", aes);

        JSONObject main = new JSONObject();
        main.put("key_id", aesComponent.getAesKeyId());
        main.put("service_id", aesComponent.getAesServiceId());
        main.put("channel_id", aesComponent.getAesChannelId());
        main.put("timestamp", localDate.toString());
        main.put("data", msg);

        String hasil_req = "";
        // System.out.println("main json: " + main.toString());
        try {
            hasil_req = httpRequest.postRequest("http://" + url, main.toString());
            // System.out.println(hasil_req);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (hasil_req.length() > 0) {
            JSONObject json_req = new JSONObject(hasil_req);
            String dataResponse = "";
            try {
                if (json_req.get("data") != null) {
                    dataResponse = aesEncryptDecrypt.decrypt(json_req.get("data").toString(),
                            aesComponent.getAesKey(), aesComponent.getAesIV());
                    JSONObject data_req = new JSONObject(dataResponse);
                    // System.out.println(data_req.toString());
                    if (data_req.getString("RC").equals("00")) {
                        channelResponse.setToFirebase(true);
                    } else {
                        channelResponse.setToFirebase(false);
                        return false;
                    }
                    crr.save(channelResponse);

                }
            } catch (Exception e) {
                channelResponse.setToFirebase(false);
                crr.save(channelResponse);
                return false;
            }
        }

        return true;
    }

}
