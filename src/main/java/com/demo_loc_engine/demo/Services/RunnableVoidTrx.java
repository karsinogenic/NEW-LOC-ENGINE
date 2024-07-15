package com.demo_loc_engine.demo.Services;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;

import com.demo_loc_engine.demo.Models.ChannelResponse;
import com.demo_loc_engine.demo.Models.LogAscend;
import com.demo_loc_engine.demo.Models.NewAESComponent;
import com.demo_loc_engine.demo.Models.TerminalMerchant;
import com.demo_loc_engine.demo.Repositories.AESComponentRepository;
import com.demo_loc_engine.demo.Repositories.APIConfigRepository;
import com.demo_loc_engine.demo.Repositories.ChannelResponseRepository;
import com.demo_loc_engine.demo.Repositories.TerminalMerchantRepository;

public class RunnableVoidTrx implements Runnable {

    public LogAscend logasc;

    public NewAESComponent aescomp;
    public AESComponent oldaes;

    public JSONObject jsonhasil;

    public ChannelResponseRepository channelResponseRepository;

    public TerminalMerchantRepository terminalMerchantRepository;

    public LogService logService;

    public APIConfigRepository apiConfigRepository;

    public AESEncryptDecrypt aesEncryptDecrypt;

    public int th_num;

    public RunnableVoidTrx(LogAscend logasc, NewAESComponent aescomp,
            ChannelResponseRepository channelResponseRepository,
            TerminalMerchantRepository terminalMerchantRepository, int th_num, AESComponent oldaes,
            APIConfigRepository apiConfigRepository) {
        this.logasc = logasc;
        this.aescomp = aescomp;
        this.channelResponseRepository = channelResponseRepository;
        this.terminalMerchantRepository = terminalMerchantRepository;
        this.th_num = th_num;
        this.oldaes = oldaes;
        this.apiConfigRepository = apiConfigRepository;
    }

    @Override
    public void run() {
        Optional<ChannelResponse> chres = this.channelResponseRepository.getByReferenceId(logasc.getReferenceId());
        Optional<TerminalMerchant> termmerch = this.terminalMerchantRepository
                .findByNama(chres.get().getTerminalMerchant());
        logService = new LogService();

        if (!chres.isPresent()) {
            jsonhasil.put("rc", "LOC-96");
            jsonhasil.put("rd", "Cannot find Existing ChannelResponse");
            logService.info(jsonhasil.toString());
        } else if (!termmerch.isPresent()) {
            jsonhasil.put("rc", "LOC-95");
            jsonhasil.put("rd", "Cannot find Existing TerminalMerchant");
            logService.info(jsonhasil.toString());
        } else {
            JSONObject body = generateBody(chres.get(), termmerch.get());
            JSONObject request = callVoidSale(body, chres.get().getReferenceId());
            jsonhasil = request;
        }

    }

    public JSONObject hasil() {
        return jsonhasil;
    }

    public JSONObject generateBody(ChannelResponse chresp, TerminalMerchant tmerc) {
        aesEncryptDecrypt = new AESEncryptDecrypt();
        String dummy = """
                {
                    "cardNo" : "4714390010000010",
                    "amount" : "100000",
                    "expirationDate" : "2511",
                    "posCondCode" : "08",
                    "reffno" : "001481000400",
                    "terminalID" : "30007319",
                    "merchantID" : "042600000006756",
                    "invoiceNum" : "000085"
                  }
                    """;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardNo", chresp.getCardNo());
        jsonObject.put("amount", chresp.getAmount().toString() + "00");
        jsonObject.put("expirationDate", chresp.getExpDate());
        jsonObject.put("posCondCode", tmerc.getPoscon());
        jsonObject.put("reffno", logasc.getReffno());
        jsonObject.put("terminalID", tmerc.getTerminalId());
        jsonObject.put("merchantID", tmerc.getMerchantId());
        jsonObject.put("invoiceNum", logasc.getInvoiceNum());
        logService.info("Thread" + th_num + ": " + jsonObject.toString());
        // logService.info("Thread" + th_num + ": " + jsonObject.toString() + "\n" +
        // "=".repeat(150));

        JSONObject main = new JSONObject();
        main.put("channel_id", aescomp.getAesChannelId());
        main.put("service_id", aescomp.getAesServiceId());
        main.put("key_id", aescomp.getAesKeyId());
        JSONObject data = new JSONObject();

        try {
            String dec = aesEncryptDecrypt.encrypt(jsonObject.toString(), aescomp.getAesKey(), aescomp.getAesIV());
            data.put("msg", dec);
            main.put("data", data);
            logService.info("Input | Thread" + th_num + ": " + main.toString());
            // logService.info("Thread" + th_num + ": " + main.toString() + "\n" +
            // "=".repeat(150));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return main;
    }

    public JSONObject callVoidSale(JSONObject input, String refid) {
        HTTPRequest httpRequest = new HTTPRequest(oldaes);
        String url = apiConfigRepository.findIpByNama("ASC_void");

        JSONObject hasil = new JSONObject();
        try {
            String response = httpRequest.postRequest("http://" + url, input.toString());
            JSONObject hasilJson = new JSONObject(response);
            JSONObject jsonObject = new JSONObject(
                    aesEncryptDecrypt.decrypt(hasilJson.getString("data"), aescomp.getAesKey(), aescomp.getAesIV()));
            logService.info("Output | Thread" + th_num + ": " + jsonObject.toString());

            jsonObject.put("refid", refid);

            if (jsonObject.get("rc").toString().toLowerCase().contains("00")) {
                hasil.put("rc", "00");
                hasil.put("rd", "OK");
                hasil.put("data", jsonObject);
            } else {
                hasil = jsonObject;
            }

            return hasil;

        } catch (Exception e) {
            System.out.println("something wrong with securitybp");
            hasil.put("rc", "LOC-97");
            hasil.put("rd", "something wrong with securitybp");
            return hasil;
        }
    }

}
