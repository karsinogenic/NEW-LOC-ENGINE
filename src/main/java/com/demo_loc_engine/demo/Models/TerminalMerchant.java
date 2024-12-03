package com.demo_loc_engine.demo.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "terminal_merchant")

public class TerminalMerchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nama;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "poscon")
    private String poscon;

    @Column(name = "server_ip")
    private String serverIp;

    @Column(name = "acc_debit")
    private String accDebit;

    @Column(name = "acc_name")
    private String accName;

    @Column(name = "payext", nullable = false,columnDefinition = "tinyint(1) default 0")
    private Boolean payext;

    @Column(name = "payment_fee",nullable = false,columnDefinition = "BIGINT(20) default 0")
    private Long paymentFee;

    @Column(name = "spdext_desc")
    private String spdextDesc;

    public String getSpdextDesc() {
        return spdextDesc;
    }

    public void setSpdextDesc(String spdextDesc) {
        this.spdextDesc = spdextDesc;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    @Column(name = "payment_fee_desc",length = 40)
    private String paymentFeeDesc;

    public Long getPaymentFee() {
        return paymentFee;
    }

    public void setPaymentFee(Long paymentFee) {
        this.paymentFee = paymentFee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getPoscon() {
        return poscon;
    }

    public void setPoscon(String poscon) {
        this.poscon = poscon;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getAccDebit() {
        return accDebit;
    }

    public void setAccDebit(String accDebit) {
        this.accDebit = accDebit;
    }

    public String getPaymentFeeDesc() {
        return paymentFeeDesc;
    }

    public void setPaymentFeeDesc(String paymentFeeDesc) {
        this.paymentFeeDesc = paymentFeeDesc;
    }

    public Boolean getPayext() {
        return payext;
    }

    public void setPayext(Boolean payext) {
        this.payext = payext;
    }

}
