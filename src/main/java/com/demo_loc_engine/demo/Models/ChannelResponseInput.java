package com.demo_loc_engine.demo.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChannelResponseInput {

    @NotBlank
    @JsonProperty("referenceId")
    private String referenceId;

    @NotBlank
    @JsonProperty("cardNo")
    private String cardNo;

    @NotNull
    @JsonProperty("accName")
    private String accName;

    @NotBlank
    @JsonProperty("accNumber")
    private String accNumber;

    @NotNull
    @JsonProperty("amount")
    private Long amount;

    @NotBlank
    @JsonProperty("planCode")
    private String planCode;

    @NotBlank
    @JsonProperty("tierCode")
    private String tierCode;

    @NotBlank
    @JsonProperty("expDate")
    private String expDate;

    @NotBlank
    @JsonProperty("terminalMerchant")
    private String terminalMerchant;

    @NotBlank
    @JsonProperty("mobileNumber")
    private String mobileNumber;

    @JsonProperty("gcn")
    private String gcn;

    // @Size(max = 8)
    @JsonProperty("bic")
    private String bic;
}
