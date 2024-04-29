package com.demo_loc_engine.demo.Services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
// import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.demo_loc_engine.demo.Controllers.APIMainController;
import com.demo_loc_engine.demo.Models.ChannelResponse;
import com.demo_loc_engine.demo.Models.LogAscend;
import com.demo_loc_engine.demo.Models.MftsResponse;
import com.demo_loc_engine.demo.Models.TerminalMerchant;
import com.demo_loc_engine.demo.Models.Channel;
import com.demo_loc_engine.demo.Repositories.ChannelRepository;
import com.demo_loc_engine.demo.Repositories.ChannelResponseRepository;
import com.demo_loc_engine.demo.Repositories.LogAscendRepository;
import com.demo_loc_engine.demo.Repositories.MftsResponseRepository;
import com.demo_loc_engine.demo.Repositories.PlanCodeRepository;
import com.demo_loc_engine.demo.Repositories.TerminalMerchantRepository;

@Service
public class FileReadWrite {

    public static int iter;
    public static int iterPPMERL;
    public static int iterlocTRF;
    public static String oldDate;

    @Autowired
    private TerminalMerchantRepository terminalMerchantRepository;

    @Autowired
    private LogAscendRepository logAscendRepository;

    // @Autowired
    // public ChannelResponseRepository testRepo;

    // public Optional<ChannelResponse> tes() {
    // Optional<ChannelResponse> cr_list =
    // this.testRepo.getByReferenceId("amitest8999");
    // System.out.println(cr_list.get().getReferenceId());
    // return cr_list;
    // }

    public static File getLastModified(String directoryFilePath) {
        // System.out.println(directoryFilePath);
        File directory = new File(directoryFilePath);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;
    }

    public Boolean writeFile(List<LogAscend> list, String nama_path, String nama_file, String[] arr_date,
            ChannelResponseRepository crr, String mId, TerminalMerchantRepository tmr) {
        // File file = new File("src/main/resources/ChannelResponse/write.txt");
        String filler_header = " ".repeat(84);
        String header = "H" + "SPDEXT    " + arr_date[0] + arr_date[1] + arr_date[2] + filler_header;

        String footer = "T000000000000";
        Integer pjgList = list.size();
        // System.out.println(filler_header.length());
        footer = footer.substring(0, footer.length() - pjgList.toString().length()) + pjgList;
        footer = footer + " ".repeat(90);

        Integer i = 1;
        String content = "";
        String date_content = arr_date[0].substring(2) + arr_date[1] + arr_date[2];

        for (LogAscend logAscend : list) {
            String cc_num = "0".repeat(19);
            String approval_code = "0".repeat(6);
            String description = "MEGA LOAN ON CARD" + " ".repeat(23);
            String dana = "0".repeat(12);
            String seq_num = "0".repeat(7);

            // System.out.println("iter :" + iter);
            // System.out.println(logAscend.getReferenceId());
            Optional<ChannelResponse> cr_list = crr.getByReferenceId(logAscend.getReferenceId());
            if (cr_list.isPresent() == false) {
                return false;
            } else {

                try {
                    seq_num = seq_num.substring(0, (seq_num.length() - i.toString().length())) + i;

                } catch (Exception e) {
                    // System.out.println("seq_num");
                    return false;
                }

                // cc_num
                cc_num = cr_list.get().getCardNo() + "   ";

                // dana
                try {
                    String temp_dana = cr_list.get().getAmount().toString() + "00";
                    // System.out.println((temp_dana.length()) + ":" + temp_dana);
                    // System.out.println((dana.length()) + ":" + dana);
                    dana = dana.substring(0, (dana.length() - temp_dana.length())) + temp_dana;
                } catch (Exception e) {
                    // System.out.println("dana");
                    return false;
                }

                // approval_code
                approval_code = logAscend.getAuth_no();

                // mId
                try {
                    Optional<ChannelResponse> cr_mid = crr.getByReferenceId(logAscend.getReferenceId());
                    Optional<TerminalMerchant> tm_mid = tmr.findByNama(cr_mid.get().getTerminalMerchant());
                    mId = tm_mid.get().getMerchantId();
                } catch (Exception e) {
                    // TODO: handle exception
                }

                String constant = "D" + "09" + "2" + seq_num + cc_num + "360" + dana + " " + date_content
                        + approval_code
                        + description + "O" + mId;

                i++;
                content += constant + System.getProperty("line.separator");
            }
        }

        try {
            FileWriter fw = new FileWriter(nama_path + "/" + nama_file, false);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write(header + System.getProperty("line.separator") + content + footer);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println(e.toString());
            return false;
        }

    }

    public Boolean writeFilePPMERL(List<LogAscend> list, String nama_path, String nama_file, String[] arr_date,
            ChannelResponseRepository crr, String mId, PlanCodeRepository pcr) {
        // File file = new File("src/main/resources/ChannelResponse/write.txt");
        String filler_header = " ".repeat(129);
        String header = "H" + "PPMERL    " + arr_date[0] + arr_date[1] + arr_date[2] + filler_header;

        String footer = "T000000000000";
        Integer pjgList = list.size();
        // System.out.println(filler_header.length());
        footer = footer.substring(0, footer.length() - pjgList.toString().length()) + pjgList;
        footer = footer + " ".repeat(131);

        Integer i = 1;
        String content = "";
        String date_content = arr_date[0].substring(2) + arr_date[1] + arr_date[2];

        for (LogAscend logAscend : list) {
            String record_type = "D";
            String prog_code = " ".repeat(8);
            String cc_num = " ".repeat(19);
            Integer year = LocalDate.now().getYear();
            String datePost = year.toString() + logAscend.getDate() + logAscend.getTime() + "00";
            String refNumber = logAscend.getReffno().length() < 23
                    ? logAscend.getReffno() + " ".repeat(23 - logAscend.getReffno().length())
                    : logAscend.getReffno();
            String approval_code = logAscend.getAuth_no();
            String description = " ".repeat(40);
            String dana = "0".repeat(12);
            String dp = "0".repeat(12);
            String tenor = "*000";
            // System.out.println("iter :" + iter);
            System.out.println(logAscend.getReferenceId());
            Optional<ChannelResponse> cr_list = crr.getByReferenceId(logAscend.getReferenceId());

            if (cr_list.isPresent() == false) {
                System.out.println("tidak ada di reference");
                return false;
            } else {

                // prog
                prog_code = cr_list.get().getPlanCode()
                        + prog_code.substring(cr_list.get().getPlanCode().length(), prog_code.length());

                // cc_num
                try {
                    cc_num = cr_list.get().getCardNo()
                            + " ".repeat(cc_num.length() - cr_list.get().getCardNo().length());
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println(e.getMessage() + " : ccnum");
                    return false;
                }

                // dana
                try {
                    String temp_dana = cr_list.get().getAmount().toString() + "00";
                    // System.out.println((temp_dana.length()) + ":" + temp_dana);
                    // System.out.println((dana.length()) + ":" + dana);
                    dana = dana.substring(0, (dana.length() - temp_dana.length())) + temp_dana;
                } catch (Exception e) {
                    System.out.println("dana");
                    return false;
                }

                // approval_code
                approval_code = logAscend.getAuth_no();

                // tenor
                // System.out.println(cr_list.get().getPlanCode());
                try {
                    Integer plan_tenor = pcr.findByPlanCodePPMERL(cr_list.get().getPlanCode()).get(0).getTenor();
                    tenor = tenor.substring(0, (tenor.length() - plan_tenor.toString().length()))
                            + plan_tenor.toString();
                } catch (Exception e) {
                    System.out.println(e.getMessage() + " : tenor");
                    return false;
                }
                String constant = record_type + prog_code + cc_num + mId + datePost + refNumber + approval_code
                        + description + dana + dp + tenor;

                i++;
                content += constant + System.getProperty("line.separator");
            }
        }

        try {
            FileWriter fw = new FileWriter(nama_path + "/" + nama_file, false);
            BufferedWriter writer = new BufferedWriter(fw);

            writer.write(header + System.getProperty("line.separator") + content + footer);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return false;
        }

    }

    public Boolean readFilePPMERL(String base64String, ChannelResponseRepository crr) {

        base64String = new String(Base64.getDecoder().decode(base64String));
        Scanner scanner = new Scanner(base64String);

        // begin buat file
        String nama_folder = LocalDate.now().toString();
        String nama_path = "opt/LOCTRF/" + nama_folder;
        String[] arr_date = nama_folder.split("-");

        String nama_file = "RESPONSE_LOC" + arr_date[0]
                + arr_date[1] + arr_date[2] + "0001.csv";

        FileWriter fw;
        try {
            fw = new FileWriter(nama_path + "/" + nama_file, false);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write(base64String);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // end buat file

        List<String> file = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            file.add(scanner.nextLine());
        }

        return true;
    }

    public void resetHeader(String[] header) {
        for (int i = 0; i < header.length; i++) {
            header[i] = "";
        }
    }

    public void resetContent(String[] content) {
        for (int i = 0; i < content.length; i++) {
            content[i] = "";
        }
    }

    public Boolean writeFilelocTRF(List<LogAscend> list, String nama_path, String nama_file, String[] arr_date,
            ChannelResponseRepository crr, String mId, ChannelRepository cr, TerminalMerchantRepository tmr) {

        String[] content = { "Service code", "Source Branch", "Date DDMMYY", "Reff no", "Amount", "Remark Line 3",
                "Remark Line 4", "REMARK TRX", "BIC BANK PENERIMA", "NAMA PENGIRIM", "REKENING PENGIRIM", "CHARGE 1",
                "CHARGE 2", "Jenis nasabah penerima", "Status penduduk penerima", "Account Credit", "Account Debit",
                "NAMA PENERIMA OVB", "REKENING PENERIMA SKN", "NAMA PENERIMA SKN", "REKENING PENERIMA  RTG",
                "NAMA PENERIMA  RTG", "AlAMAT PENERIMA", "AlAMAT KOTA PENERIMA", "KODE CABANG BANK PENERIMA",
                "NAMA PENERIMA EWALLET", "Reff User", "Company Identifier", "Additional1", "Additional2", "Additional3",
                "Currency Debit", "Currency Credit", "Rate Debit", "Rate Credit", "Amount Credit" };
        // flag
        // System.out.println("panjang :" + String.valueOf(content.length));
        String str_header = "";

        Long amount = Long.valueOf(0);

        String[] header = new String[content.length];
        resetHeader(header);
        String[] str_header_arr = new String[8];
        header[0] = "Flag:";
        header[1] = "N";
        str_header_arr[0] = String.join(",", header) + "\n";

        String acc_debit = tmr.findByNama("LOC").get().getAccDebit();

        // account debit
        header[0] = "Account Debit:";
        header[1] = acc_debit;
        str_header_arr[1] = String.join(",", header) + "\n";

        String str_content = "";
        str_content += String.join(",", content) + "\n";
        resetContent(content);

        String newdate = arr_date[2] + arr_date[1] + arr_date[0].substring(2, arr_date[0].length());

        // String newheader = String.join(",", header);

        try {
            FileWriter fw = new FileWriter(nama_path + "/" + nama_file, false);
            BufferedWriter writer = new BufferedWriter(fw);

            for (LogAscend logAscend : list) {
                Optional<ChannelResponse> list_crr = crr.getByReferenceId(logAscend.getReferenceId());
                // System.out.println("crr: " + list_crr.isPresent());
                Optional<Channel> list_cr = cr.findByKodeChannel(list_crr.get().getKodeChannel());
                content[0] = "OVB";
                content[1] = "801";
                content[2] = newdate;
                content[4] = String.valueOf(list_crr.get().getAmount()) + "00";
                content[5] = "LOAN ON CARD " + list_cr.get().getNama_channel();
                content[6] = logAscend.getReferenceId();
                content[15] = list_crr.get().getAccNumber();
                content[16] = acc_debit;
                content[17] = list_crr.get().getAccName().length() > 20 ? list_crr.get().getAccName().substring(0, 20)
                        : list_crr.get().getAccName();
                content[26] = logAscend.getReferenceId();

                str_content += String.join(",", content) + "\n";

                if (list_crr.isPresent()) {
                    amount += list_crr.get().getAmount();
                } else {
                    System.out.println("ga ada");
                }

            }
            str_content = str_content.substring(0, str_content.length() - 1);

            // total transaction
            header[0] = "Total transaction:";
            header[1] = String.valueOf(list.size());
            str_header_arr[2] = String.join(",", header) + "\n";

            // total transaction amount
            header[0] = "Total transaction amount:";
            header[1] = String.valueOf(amount) + "00";
            str_header_arr[3] = String.join(",", header) + "\n";

            // transaction date
            header[0] = "Transaction date:";
            header[1] = newdate;
            str_header_arr[4] = String.join(",", header) + "\n";

            // User id
            header[0] = "User ID:";
            header[1] = "";
            str_header_arr[5] = String.join(",", header) + "\n";

            header[0] = "";
            header[1] = "";
            str_header_arr[7] = String.join(",", header) + "\n";

            MFTS mfts = new MFTS();
            String temp_str = "";
            for (int i = 0; i < str_header_arr.length; i++) {
                if (i != 6) {
                    temp_str += str_header_arr[i];
                }
            }
            temp_str += str_content;
            // System.out.println("stringCRC= " + temp_str.replaceAll("\n", ""));
            String crc16 = mfts.crc16(temp_str.replaceAll("\n", ""));
            // System.out.println("crc = " + crc16);

            // Code
            header[0] = "Code:";
            header[1] = crc16;
            str_header_arr[6] = String.join(",", header) + "\n";

            for (int i = 0; i < str_header_arr.length; i++) {
                str_header += str_header_arr[i];
            }

            writer.write(str_header + str_content);
            // writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // System.out.println(e.toString());
            return false;
        }
    }

    public String spdext(List<LogAscend> list, ChannelResponseRepository crr, String mId,
            TerminalMerchantRepository tmr) {
        LocalDate date = LocalDate.now();
        if (date.toString().equals(oldDate) == false) {
            iter = 0;
        }
        iter++;
        String nama_folder = date.toString();

        String nama_path = "opt/SPDEXT/" + nama_folder;

        File newFile = getLastModified(nama_path);
        if (newFile != null) {
            String namaFileString = newFile.getName();
            // String[] namaFileStringArr = namaFileString.split(".");
            iter = Integer.parseInt(namaFileString.substring(9, 11));
            iter++;
            // System.out.println(namaFileString.substring(9, 11));
            // System.out.println(namaFileStringArr[0]);
            // for (String string1 : namaFileStringArr) {
            // System.out.println(string1);
            // }
        }

        // find mID
        String[] arr_date = nama_folder.split("-");

        String nama_file = "SPDEXTLOC" + ((iter < 10) ? ("0" + iter) : (iter)) + "." + arr_date[0].substring(2)
                + arr_date[1] + arr_date[2];
        // System.out.println("nama_file = " + nama_file);

        // Optional<ChannelResponse> cr_list = crr.getByReferenceId("amitest8999");
        // System.out.println(cr_list.get().getCardNo());

        try {
            Files.createDirectories(Paths.get(nama_path));
            Boolean wrBool = writeFile(list, nama_path, nama_file, arr_date, crr, mId, tmr);
            if (wrBool == false) {
                return "FAIL";
            }
            // date = date.plusDays(1);
            oldDate = date.toString();
            return "Berhasil membuat file cek pada path:'" + nama_path + "'";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "FAIL";
        }

    }

    public String ppmrl(List<LogAscend> list, ChannelResponseRepository crr, PlanCodeRepository pcr, String mId) {
        LocalDate date = LocalDate.now();
        if (date.toString().equals(oldDate) == false) {
            iterPPMERL = 0;
        }
        iterPPMERL++;
        String nama_folder = date.toString();
        String nama_path = "opt/PPMERL/" + nama_folder;
        String[] arr_date = nama_folder.split("-");

        String nama_file = "PPMERL" + ((iterPPMERL < 10) ? ("0" + iterPPMERL) : (iterPPMERL)) + "."
                + arr_date[0].substring(2)
                + arr_date[1] + arr_date[2];
        // System.out.println("nama_file = " + nama_file);

        // Optional<ChannelResponse> cr_list = crr.getByReferenceId("amitest8999");
        // System.out.println(cr_list.get().getCardNo());

        try {
            Files.createDirectories(Paths.get(nama_path));
            Boolean wrBool = writeFilePPMERL(list, nama_path, nama_file, arr_date, crr, mId, pcr);
            if (wrBool == false) {
                return "FAIL";
            }
            // date = date.plusDays(1);
            oldDate = date.toString();
            return "Berhasil membuat file cek pada path:'" + nama_path + "'";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "FAIL";
        }

    }

    public String locTRF(List<LogAscend> list, ChannelResponseRepository crr, ChannelRepository cr, String mId,
            TerminalMerchantRepository tmr, MftsResponseRepository mftsResponseRepository) {
        LocalDate date = LocalDate.now();
        // if (date.toString().equals(oldDate) == false) {
        // iterlocTRF = 0;
        // }
        // iterlocTRF++;
        Integer seq = this.findSequence(mftsResponseRepository);
        String seq_str = seq.toString();
        String nama_folder = date.toString();
        String nama_path = "opt/LOCTRF/" + nama_folder;
        String[] arr_date = nama_folder.split("-");

        String nama_file = "LOC" + arr_date[0]
                + arr_date[1] + arr_date[2] + "0".repeat(4 - seq_str.length()) + seq_str + ".csv";
        // System.out.println("nama_file = " + nama_file);

        // Optional<ChannelResponse> cr_list = crr.getByReferenceId("amitest8999");
        // System.out.println(cr_list.get().getCardNo());

        try {
            Files.createDirectories(Paths.get(nama_path));
            Boolean wrBool = writeFilelocTRF(list, nama_path, nama_file, arr_date, crr, mId, cr, tmr);
            if (wrBool == false) {
                return "FAIL";
            }
            // date = date.plusDays(1);
            oldDate = date.toString();
            MftsResponse mftsResponse = new MftsResponse();
            Optional<MftsResponse> optData = mftsResponseRepository
                    .findByNamaFile((nama_file.substring(0, nama_file.length() - 4)));
            if (optData.isPresent() && optData.get().getIs_send() == null) {
                mftsResponse = optData.get();
            }
            mftsResponse.setNama_file(nama_file.substring(0, nama_file.length() - 4));
            mftsResponse.setDate_create(LocalDateTime.now());
            mftsResponseRepository.save(mftsResponse);
            for (LogAscend logAscend : list) {
                logAscend.setNamaFile(nama_file.substring(0, nama_file.length() - 4));
                this.logAscendRepository.save(logAscend);
            }
            return "Berhasil membuat file " + nama_file + " cek pada path:'" + nama_path + "'";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "FAIL";
        }

    }

    // public JSONObject fireBase(ChannelResponse cr) {
    // JSONObject main = new JSONObject();
    // main.put("key_id", "AXCVSFDJKAGDKAGDKAFG");
    // main.put("service_id", "LOC_NOTIF");
    // main.put("channel_id", cr.getKodeChannel());
    // main.put("timestamp", LocalDate.now());

    // JSONObject data = new JSONObject();
    // data.put("phone_number", cr.getMobileNumber());
    // data.put("title", "Pengajuan LOC");
    // data.put("konten_value", "");
    // data.put("type_trx_firebase", "");
    // data.put("type_trx_table", "");
    // data.put("key_no", "");
    // data.put("key_noNotif", "0r40NOK0D3NY4");
    // data.put("channel_id", "6969");
    // data.put("body", (cr.getStatusTransfer()
    // ? ("Pengajuan pencairan LOC Anda telah diterima dan dana telah sukses
    // ditransfer ke rekening Bank Mega Anda")
    // : ("Pengajuan pencairan LOC Anda belum dapat disetujui. Info lebih lanjut
    // hubungi Megacall 08041500010")));
    // data.put("notif", (cr.getStatusTransfer()
    // ? ("Pengajuan pencairan LOC Anda telah diterima dan dana telah sukses
    // ditransfer ke rekening Bank Mega Anda")
    // : ("Pengajuan pencairan LOC Anda belum dapat disetujui. Info lebih lanjut
    // hubungi Megacall 08041500010")));

    // AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
    // String encryptString = "";
    // try {
    // encryptString = aesEncryptDecrypt.encrypt(data.toString(),
    // "Random1234567890");
    // } catch (Exception e) {
    // // return "FAIL";
    // }
    // JSONObject msg = new JSONObject();
    // msg.put("msg", encryptString);

    // main.put("data", msg);
    // return main;
    // }

    public Integer findSequence(MftsResponseRepository mftsResponseRepository) {
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        List<MftsResponse> listMfts = mftsResponseRepository.findSequenceData(ldt, ldt.plusDays(1));
        Integer sequence = listMfts.size() + 1;
        return sequence;
    }

}
