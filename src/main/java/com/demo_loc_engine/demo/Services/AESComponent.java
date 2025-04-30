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

    @Value("${http.timeout}")
    private Integer httpTimeout;

    @Value("${pefindo.url}")
    private String pefindoUrl;

    @Value("${pefindo.path}")
    private String pefindoPath;

    @Value("${default.bic}")
    private String bifastBic;

    @Value("${bifast.url}")
    private String bifastURL;

    @Value("${use.bifast}")
    private Boolean useBifast;


    public Boolean getUseBifast() {
        return useBifast;
    }

    public void setUseBifast(Boolean useBifast) {
        this.useBifast = useBifast;
    }

    public String getBifastURL() {
        return bifastURL;
    }

    public void setBifastURL(String bifastURL) {
        this.bifastURL = bifastURL;
    }

    public String getBifastBic() {
        return bifastBic;
    }

    public void setBifastBic(String bifastBic) {
        this.bifastBic = bifastBic;
    }

    public String getPefindoUrl() {
        return pefindoUrl;
    }

    public void setPefindoUrl(String pefindoUrl) {
        this.pefindoUrl = pefindoUrl;
    }

    public Integer getHttpTimeout() {
        return httpTimeout;
    }

    public void setHttpTimeout(Integer httpTimeout) {
        this.httpTimeout = httpTimeout;
    }

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

    public String getPefindoPath() {
        return pefindoPath;
    }

    public void setPefindoPath(String pefindoPath) {
        this.pefindoPath = pefindoPath;
    }

}
