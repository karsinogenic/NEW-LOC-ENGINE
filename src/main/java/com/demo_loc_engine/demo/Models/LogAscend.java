package com.demo_loc_engine.demo.Models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "log_ascend")
public class LogAscend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String date;

    private String rc;

    private String rd;

    private String reffno;

    @Column(name = "reference_id", nullable = false)
    private String referenceId;

    private String traceno;

    private String time;

    private String statusTransfer;

    @Column(name = "status_transfer_desc")
    private String statusTransferDesc;

    @Column(nullable = true)
    private String auth_no;

    @Column(name = "invoice_num")
    private String invoiceNum;

    @Column(name = "is_generated", columnDefinition = "tinyint(1) default null")
    private Boolean isGenerated;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "is_generated_ppmerl", columnDefinition = "tinyint(1) default null")
    private Boolean isGeneratedPPMERL;

    @Column(name = "generated_at_ppmerl")
    private LocalDateTime generatedAtPPMERL;

    @Column(name = "is_generated_mfts", columnDefinition = "tinyint(1) default null")
    private Boolean isGeneratedMFTS;

    @Column(name = "generated_at_mfts")
    private LocalDateTime generatedAtMFTS;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime created_at;

    @Column(name = "bic", nullable = false)
    private String bic;

    @Column(name = "nama_file")
    private String namaFile;

    @Column(name = "is_voided", nullable = false, columnDefinition = "tinyint(1) default false")
    private Boolean isvoided;

    public Boolean getIsvoided() {
        return isvoided;
    }

    public void setIsvoided(Boolean isvoided) {
        this.isvoided = isvoided;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public Boolean getIsGeneratedPPMERL() {
        return isGeneratedPPMERL;
    }

    public void setIsGeneratedPPMERL(Boolean isGeneratedPPMERL) {
        this.isGeneratedPPMERL = isGeneratedPPMERL;
    }

    public LocalDateTime getGeneratedAtPPMERL() {
        return generatedAtPPMERL;
    }

    public void setGeneratedAtPPMERL(LocalDateTime generatedAtPPMERL) {
        this.generatedAtPPMERL = generatedAtPPMERL;
    }

    public Boolean getIsGeneratedMFTS() {
        return isGeneratedMFTS;
    }

    public void setIsGeneratedMFTS(Boolean isGeneratedMFTS) {
        this.isGeneratedMFTS = isGeneratedMFTS;
    }

    public LocalDateTime getGeneratedAtMFTS() {
        return generatedAtMFTS;
    }

    public void setGeneratedAtMFTS(LocalDateTime generatedAtMFTS) {
        this.generatedAtMFTS = generatedAtMFTS;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public String getStatusTransferDesc() {
        return statusTransferDesc;
    }

    public void setStatusTransferDesc(String statusTransferDesc) {
        this.statusTransferDesc = statusTransferDesc;
    }

    public String getReffno() {
        return reffno;
    }

    public void setReffno(String reffno) {
        this.reffno = reffno;
    }

    public String getTraceno() {
        return traceno;
    }

    public void setTraceno(String traceno) {
        this.traceno = traceno;
    }

    public Boolean getIsGenerated() {
        return isGenerated;
    }

    public void setIsGenerated(Boolean isGenerated) {
        this.isGenerated = isGenerated;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuth_no() {
        return auth_no;
    }

    public void setAuth_no(String auth_no) {
        this.auth_no = auth_no;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getStatusTransfer() {
        return statusTransfer;
    }

    public void setStatusTransfer(String statusTransfer) {
        this.statusTransfer = statusTransfer;
    }

    public String getNamaFile() {
        return namaFile;
    }

    public void setNamaFile(String namaFile) {
        this.namaFile = namaFile;
    }

}
