package com.demo_loc_engine.demo.Models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class LogAscendResponse {

    private String referenceId;

    private String statusTransfer;

    private String statusTransferDesc;

    private LocalDateTime created_at;

    private String namaFile;

    public LogAscendResponse(LogAscend logAscend) {
        this.referenceId = logAscend.getReferenceId();
        this.namaFile = logAscend.getNamaFile();
        this.created_at = logAscend.getCreated_at();
        this.statusTransfer = logAscend.getStatusTransfer();
        this.statusTransferDesc = logAscend.getStatusTransferDesc();
    }

}
