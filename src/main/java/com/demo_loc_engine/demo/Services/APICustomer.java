package com.demo_loc_engine.demo.Services;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class APICustomer {
    @Autowired
    public AESComponent aesComponent;

    public Map<String, Object> dataApi(String custNum, String fieldOutput) {
        HTTPRequest httpRequest = new HTTPRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        JSONObject query_string = new JSONObject();
        String[] fields = { "cust-nbr" };
        query_string.put("fields", fields);
        query_string.put("query", "(" + custNum + ")");
        JSONObject query = new JSONObject();
        query.put("query_string", query_string);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("query", query);

        System.out.println(jsonObject.toString());
        System.out.println("coba :" + aesComponent.getElasticUrl());

        String hasil = "";

        try {
            hasil = httpRequest.getRequest("http://" + aesComponent.getElasticUrl(), jsonObject.toString(),
                    aesComponent.getElasticUsername(), aesComponent.getElasticPassword());
        } catch (Exception e) {
            Map<String, Object> invalidReq = new HashMap<>();
            invalidReq.put("rc", "400");
            invalidReq.put("rd", "Invalid Request");
            invalidReq.put("error", "Invalid Request to http://" + aesComponent.getElasticUrl());
            return invalidReq;
        }
        Map<String, Object> response = new HashMap<>();
        try {
            response = objectMapper.readValue(hasil, Map.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject jsonHasil = new JSONObject(hasil);
        JSONObject hits = new JSONObject(jsonHasil.get("hits").toString());
        JSONArray arrayHits = new JSONArray(hits.get("hits").toString());
        JSONObject custData = arrayHits.getJSONObject(0);
        Map<String, Object> isiCustData = custData.getJSONObject("_source").toMap();
        return isiCustData;
    }

}
