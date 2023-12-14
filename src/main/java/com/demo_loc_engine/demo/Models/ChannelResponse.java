package com.demo_loc_engine.demo.Models;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "channel_response")
public class ChannelResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_id", nullable = false, unique = true)
    private String referenceId;

    @Column(name = "terminal_merchant")
    private String terminalMerchant;

    @Column(name = "acc_number")
    private String accNumber;

    @Column(name = "acc_name")
    private String accName;

    @Column(name = "card_no", nullable = false)
    private String cardNo;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "kode_channel", nullable = true)
    private String kodeChannel;

    @Column(name = "plan_code", nullable = false)
    private String planCode;

    @Column(name = "tier_code", nullable = true)
    private String tierCode;

    @Column(name = "gcn", nullable = true)
    private String gcn;

    @Column(name = "exp_date", nullable = false)
    private String expDate;

    @Column(name = "phone_number", nullable = false)
    private String mobileNumber;

    @Column(name = "additional_data")
    private String additionalData;

    @Column(name = "to_firebase", columnDefinition = "boolean default false")
    private Boolean toFirebase;

    @Column(name = "status_transfer", columnDefinition = "boolean default false")
    private Boolean statusTransfer;

    @Lob
    @Column(name = "detail_status_transfer", columnDefinition = "text")
    private String detailStatusTransfer;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public String getGcn() {
        return gcn;
    }

    public void setGcn(String gcn) {
        this.gcn = gcn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getKodeChannel() {
        return kodeChannel;
    }

    public void setKodeChannel(String kodeChannel) {
        this.kodeChannel = kodeChannel;
    }

    public String getPlanCode() {
        return planCode;
    }

    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTierCode() {
        return tierCode;
    }

    public void setTierCode(String tierCode) {
        this.tierCode = tierCode;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getTerminalMerchant() {
        return terminalMerchant;
    }

    public void setTerminalMerchant(String terminalMerchant) {
        this.terminalMerchant = terminalMerchant;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getToFirebase() {
        return toFirebase;
    }

    public void setToFirebase(Boolean toFirebase) {
        this.toFirebase = toFirebase;
    }

    public Boolean getStatusTransfer() {
        return statusTransfer;
    }

    public void setStatusTransfer(Boolean statusTransfer) {
        this.statusTransfer = statusTransfer;
    }

    public String getDetailStatusTransfer() {
        return detailStatusTransfer;
    }

    public void setDetailStatusTransfer(String detailStatusTransfer) {
        this.detailStatusTransfer = detailStatusTransfer;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }

}
