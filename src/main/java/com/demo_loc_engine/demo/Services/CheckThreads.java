package com.demo_loc_engine.demo.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo_loc_engine.demo.Models.TierKriteria;
import com.demo_loc_engine.demo.Repositories.TierKriteriaRepository;
import com.demo_loc_engine.demo.Repositories.TierRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CheckThreads implements Runnable {

    private Map<String, Object> input;
    private TierKriteriaRepository tierKriteriaRepository;
    private TierRepository tierRepository;
    private Map result;
    private String cardnum;

    public CheckThreads(Map<String, Object> jsonInput, TierKriteriaRepository tierKriteriaRepository,
            TierRepository tierRepository) {
        this.input = jsonInput;
        this.tierKriteriaRepository = tierKriteriaRepository;
        this.tierRepository = tierRepository;
    }

    public Map result() {
        return result;
    }

    public String cardnum() {
        return cardnum;
    }

    @Override
    public void run() {
        Map response = new HashMap();
        List<String> kode_hasil = new ArrayList<String>();
        if (input.get("channel") == null) {
            response.put("status_" + input.get("cardnbr"), "channel tidak dimasukan");
        }
        // System.out.println("cardnum : " + input.get("cardnbr"));
        cardnum = input.get("cardnbr").toString();
        // System.out.println("sout");
        String[] distinct_kode = this.tierRepository.findKodeTierDistinct(input.get("channel").toString());
        for (String isi : distinct_kode) {
            String new_cause = "";
            List<TierKriteria> list = this.tierKriteriaRepository.findByKodeTier(isi);
            Boolean status_kode = true;
            for (TierKriteria hasil : list) {
                String key_param = hasil.getKriteria().getNama_kriteria();
                // System.out.println("cause " + hasil.getKode_tier());
                String value_param = "";
                try {
                    // System.out.println(input.get(key_param).getClass());
                    value_param = input.get(key_param).toString();
                } catch (Exception e) {
                    response.put("status " + input.get("cardnbr"), key_param + " tidak dimasukan");
                    result = response;
                }
                String operator = hasil.getOperator().getNama_operator();
                if (value_param != null) {
                    switch (operator) {
                        case ">=":
                            // System.out.println(Integer.parseInt(value_param) >=
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) >= Integer.parseInt(hasil.getValue()) == false) {
                                // System.out.println(key_param);
                                // response.put("cause " + hasil.getKode_tier(),
                                // hasil.getKriteria().getNama_kriteria());
                                new_cause = hasil.getKriteria().getNama_kriteria();
                                status_kode = false;
                            }
                            break;
                        case "<=":
                            // System.out.println(Integer.parseInt(value_param) <=
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) <= Integer.parseInt(hasil.getValue()) == false) {
                                // System.out.println(key_param);
                                // response.put("cause " + hasil.getKode_tier(),
                                // hasil.getKriteria().getNama_kriteria());
                                new_cause = hasil.getKriteria().getNama_kriteria();
                                status_kode = false;
                            }
                            break;
                        case ">":
                            // System.out.println(Integer.parseInt(value_param) >
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) > Integer.parseInt(hasil.getValue()) == false) {
                                // System.out.println(key_param);
                                // response.put("cause " + hasil.getKode_tier(),
                                // hasil.getKriteria().getNama_kriteria());
                                new_cause = hasil.getKriteria().getNama_kriteria();
                                status_kode = false;
                            }
                            break;
                        case "<":
                            // System.out.println(Integer.parseInt(value_param) <
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) < Integer.parseInt(hasil.getValue()) == false) {
                                // System.out.println(key_param);
                                // response.put("cause " + hasil.getKode_tier(),
                                // hasil.getKriteria().getNama_kriteria());
                                new_cause = hasil.getKriteria().getNama_kriteria();
                                status_kode = false;
                            }
                            break;
                        case "=":
                            // System.out.println(value_param.equals(hasil.getValue()));
                            if (value_param.equals(hasil.getValue()) == false) {
                                // System.out.println(key_param);
                                // response.put("cause " + hasil.getKode_tier(),
                                // hasil.getKriteria().getNama_kriteria());
                                new_cause = hasil.getKriteria().getNama_kriteria();
                                status_kode = false;
                            }
                            break;
                        case "<>":
                            // System.out.println(!value_param.equals(hasil.getValue()));
                            if (!value_param.equals(hasil.getValue()) == false) {
                                // System.out.println(key_param);
                                // response.put("cause " + hasil.getKode_tier(),
                                // hasil.getKriteria().getNama_kriteria());
                                new_cause = hasil.getKriteria().getNama_kriteria();
                                status_kode = false;
                            }
                            break;
                        case "IN":
                            String[] newArr = hasil.getValue().split(",");
                            List<String> newArrList = Arrays.asList(newArr);
                            // for (String string : newArrList) {
                            // System.out.println("list: " + string);
                            // }

                            // System.out.println(value_param);
                            ObjectMapper objectMapper = new ObjectMapper();
                            // value_param = value_param.substring(1, value_param.length() - 1);
                            // System.out.println(value_param);
                            // value_param.replaceAll("]", "");
                            String[] new_value_param = value_param.split(",");
                            JSONArray jsonArray = new JSONArray();
                            List<String> list_value_param = Arrays.asList(new_value_param);

                            if (list_value_param.size() > 0) {
                                for (String value_String : new_value_param) {
                                    // System.out.println(key_param);
                                    // System.out.println("newArr" + Arrays.toString(newArr) + "value:" +
                                    // value_String + ":");
                                    if (Arrays.asList(newArr).contains(value_String) == false
                                            && hasil.getAnd_or().toLowerCase().equals("and")) {
                                        // System.out.println(
                                        // "nih" + "newArr" + Arrays.toString(newArr) + "value:" + value_String + ":");
                                        status_kode = false;
                                        // response.put("cause " + hasil.getKode_tier(),
                                        // hasil.getKriteria().getNama_kriteria());
                                        new_cause = hasil.getKriteria().getNama_kriteria();
                                        // System.out.println("new cause " + new_cause);
                                        break;
                                    } else if (Arrays.asList(newArr).contains(value_String) == false
                                            && hasil.getAnd_or().toLowerCase().equals("or")) {
                                        // System.out.println("new cause " + new_cause);
                                        new_cause = hasil.getKriteria().getNama_kriteria();
                                        status_kode = false;
                                    } else if (Arrays.asList(newArr).contains(value_String) == true
                                            && hasil.getAnd_or().toLowerCase().equals("or")) {
                                        status_kode = true;
                                        new_cause = "";
                                        break;
                                    }
                                }
                            } else {
                                status_kode = false;
                            }

                            break;
                        case "NOT IN":
                            String[] newArr1 = hasil.getValue().split(",");
                            List<String> list_value_param1 = Arrays.asList(newArr1);

                            if (list_value_param1.size() > 0) {
                                // System.out.println(!Arrays.asList(newArr1).contains(value_param));
                                if (!Arrays.asList(newArr1).contains(value_param) == false
                                        && hasil.getAnd_or().toLowerCase().equals("and")) {
                                    // System.out.println(key_param);
                                    status_kode = false;
                                    // response.put("cause " + hasil.getKode_tier(),
                                    // hasil.getKriteria().getNama_kriteria());
                                    new_cause = hasil.getKriteria().getNama_kriteria();
                                    break;
                                } else if (!Arrays.asList(newArr1).contains(value_param) == false
                                        && hasil.getAnd_or().toLowerCase().equals("or")) {
                                    status_kode = false;
                                    new_cause = hasil.getKriteria().getNama_kriteria();
                                } else if (!Arrays.asList(newArr1).contains(value_param) == true
                                        && hasil.getAnd_or().toLowerCase().equals("or")) {
                                    status_kode = true;
                                    new_cause = "";
                                    break;
                                }
                            } else {
                                status_kode = false;
                            }

                    }
                } else {
                    response.put("status", key_param + " tidak dimasukan");
                    // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                // System.out.println("response coba: " + response);
                if (status_kode == false) {
                    break;
                }
            }
            response.put("cause " + isi, new_cause);
            // System.out.println("status " + input.get("cardnbr") + isi + " : " +
            // status_kode);
            if (status_kode) {
                kode_hasil.add(isi);
            }
        }
        // System.out.println(kode_hasil.toString());

        if (kode_hasil.size() > 1)

        {
            Collections.sort(kode_hasil);
        }
        if (kode_hasil.size() > 0) {
            response.put("status " + input.get("cardnbr"), "eligible");
        } else {
            response.put("status " + input.get("cardnbr"), "not eligible");
        }
        response = new TreeMap<>(response);
        result = response;

        // System.out.println("result: " + result);
    }
}
