package com.demo_loc_engine.demo.Models;

import jakarta.persistence.*;
import jakarta.persistence.GeneratedValue;

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

}
