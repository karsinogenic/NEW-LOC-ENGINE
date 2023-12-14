package com.demo_loc_engine.demo.Services;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.demo_loc_engine.demo.Controllers.APIMainController;
import com.demo_loc_engine.demo.Repositories.MftsResponseRepository;

public class SendMFTSThreads implements Runnable {

    @Autowired
    public MftsResponseRepository mftsResponseRepository;

    private static Map<String, Object> response;

    public SendMFTSThreads() {

    }

    @Override
    public void run() {

    }

}
