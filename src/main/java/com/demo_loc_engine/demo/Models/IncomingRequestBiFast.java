package com.demo_loc_engine.demo.Models;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncomingRequestBiFast {

    // @Id

    // @Indexed(unique = true)
    // @Field("ref_id")
    @JsonProperty("ref_id")
    @NotBlank(message = "ref_id is blank")
    private String refId;

    // @Field("ch_id")
    @JsonProperty("ch_name")
    @NotBlank(message = "ch_name is blank")
    private String chName;

    // @Field("amount")
    @JsonProperty("amount")
    @NotNull(message = "amount is null")
    // @NumberFormat
    private Double amount;

    // @Field("accnum")
    @JsonProperty("accnum")
    @NotBlank(message = "accnum is blank")
    private String accnum;

    @JsonProperty("beneficiary_bic")
    @NotBlank(message = "beneficiary_bic is blank")
    private String beneficiary_bic;

}
