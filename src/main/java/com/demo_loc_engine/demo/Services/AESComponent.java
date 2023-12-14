package com.demo_loc_engine.demo.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESComponent {

    @Value("${aes.static.iv}")
    private String aesIV;

    @Value("${aes.static.key}")
    private String aesKey;

    @Value("${aes.service.id}")
    private String aesServiceId;

    @Value("${aes.key.id}")
    private String aesKeyId;

    @Value("${aes.channel.id}")
    private String aesChannelId;

    @Value("${elastic.username}")
    private String elasticUsername;

    @Value("${elastic.password}")
    private String elasticPassword;

    @Value("${elastic.url}")
    private String elasticUrl;

    public String getAesIV() {
        return aesIV;
    }

    public void setAesIV(String aesIV) {
        this.aesIV = aesIV;
    }

    public String getAesKey() {
        return aesKey;
    }

    public String getAesServiceId() {
        return aesServiceId;
    }

    public void setAesServiceId(String aesServiceId) {
        this.aesServiceId = aesServiceId;
    }

    public String getElasticUsername() {
        return elasticUsername;
    }

    public void setElasticUsername(String elasticUsername) {
        this.elasticUsername = elasticUsername;
    }

    public String getElasticPassword() {
        return elasticPassword;
    }

    public void setElasticPassword(String elasticPassword) {
        this.elasticPassword = elasticPassword;
    }

    public String getElasticUrl() {
        return elasticUrl;
    }

    public void setElasticUrl(String elasticUrl) {
        this.elasticUrl = elasticUrl;
    }

    public String getAesKeyId() {
        return aesKeyId;
    }

    public void setAesKeyId(String aesKeyId) {
        this.aesKeyId = aesKeyId;
    }

    public String getAesChannelId() {
        return aesChannelId;
    }

    public void setAesChannelId(String aesChannelId) {
        this.aesChannelId = aesChannelId;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

}
