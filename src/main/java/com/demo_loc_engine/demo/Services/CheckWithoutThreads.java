package com.demo_loc_engine.demo.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.demo_loc_engine.demo.Models.TierKriteria;
import com.demo_loc_engine.demo.Repositories.TierKriteriaRepository;

public class CheckWithoutThreads {
    private Map<String, Object> input;
    private TierKriteriaRepository tierKriteriaRepository;
    private Map result;
    private String cardnum;

    public CheckWithoutThreads(Map<String, Object> jsonInput, TierKriteriaRepository tierKriteriaRepository) {
        this.input = jsonInput;
        this.tierKriteriaRepository = tierKriteriaRepository;
    }

    public Map result() {
        return result;
    }

    public String cardnum() {
        return cardnum;
    }

    public Map runCheck() {
        Map response = new HashMap();
        List<String> kode_hasil = new ArrayList<String>();
        if (input.get("channel") == null) {
            response.put("status_" + input.get("cardnbr"), "channel tidak dimasukan");
        }
        System.out.println("cardnum : " + input.get("cardnbr"));
        cardnum = input.get("cardnbr").toString();

        String[] distinct_kode = this.tierKriteriaRepository.findKodeTierDistinct(input.get("channel").toString());
        for (String isi : distinct_kode) {
            List<TierKriteria> list = this.tierKriteriaRepository.findByKodeTier(isi);
            Boolean status_kode = true;
            for (TierKriteria hasil : list) {
                String key_param = hasil.getKriteria().getNama_kriteria();
                // System.out.println(key_param);
                String value_param = "";
                try {
                    // System.out.println(input.get(key_param).getClass());
                    value_param = input.get(key_param).toString();
                } catch (Exception e) {
                    response.put("status " + input.get("cardnbr"), key_param + " tidak dimasukan");
                    result = response;
                    return result;
                }
                String operator = hasil.getOperator().getNama_operator();
                if (value_param != null) {
                    switch (operator) {
                        case ">=":
                            // System.out.println(Integer.parseInt(value_param) >=
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) >= Integer.parseInt(hasil.getValue()) == false) {
                                System.out.println(key_param);
                                status_kode = false;
                            }

                            break;
                        case "<=":
                            // System.out.println(Integer.parseInt(value_param) <=
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) <= Integer.parseInt(hasil.getValue()) == false) {
                                System.out.println(key_param);
                                status_kode = false;
                            }
                            break;
                        case ">":
                            // System.out.println(Integer.parseInt(value_param) >
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) > Integer.parseInt(hasil.getValue()) == false) {
                                System.out.println(key_param);
                                status_kode = false;
                            }
                            break;
                        case "<":
                            // System.out.println(Integer.parseInt(value_param) <
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) < Integer.parseInt(hasil.getValue()) == false) {
                                System.out.println(key_param);
                                status_kode = false;
                            }
                            break;
                        case "=":
                            // System.out.println(value_param.equals(hasil.getValue()));
                            if (value_param.equals(hasil.getValue()) == false) {
                                System.out.println(key_param);
                                status_kode = false;
                            }
                            break;
                        case "<>":
                            // System.out.println(!value_param.equals(hasil.getValue()));
                            if (!value_param.equals(hasil.getValue()) == false) {
                                System.out.println(key_param);
                                status_kode = false;
                            }
                            break;
                        case "IN":
                            String[] newArr = hasil.getValue().split(",");
                            List<String> newArrList = Arrays.asList(newArr);
                            // for (String string : newArrList) {
                            // System.out.println("list: " + string);
                            // }
                            if (value_param.contains("[")) {
                                // System.out.println(value_param);
                                value_param = value_param.substring(1, value_param.length() - 1);
                                // System.out.println(value_param);
                                // value_param.replaceAll("]", "");
                                String[] new_value_param = value_param.split(",");

                                for (int i = 0; i < new_value_param.length; i++) {
                                    if (new_value_param[i].length() > 1) {
                                        if (i != 0) {
                                            new_value_param[i] = new_value_param[i].substring(1,
                                                    new_value_param[i].length());
                                        }
                                    }
                                }

                                for (String key : newArr) {
                                    // System.out.println();
                                    if (Arrays.asList(new_value_param).contains(key) == true) {
                                        status_kode = true;
                                        System.out.println(key_param + ":" + key);
                                        break;
                                    } else {
                                        status_kode = false;
                                        // break;
                                    }
                                }
                            } else {
                                if (Arrays.asList(newArr).contains(value_param) == false) {
                                    System.out.println(key_param + ":" + value_param + ":" + String.join(",", newArr));
                                    status_kode = false;
                                    System.out.println(key_param + " old");
                                }
                            }

                            break;
                        case "NOT IN":
                            String[] newArr1 = hasil.getValue().split(",");
                            // System.out.println(!Arrays.asList(newArr1).contains(value_param));
                            if (!Arrays.asList(newArr1).contains(value_param) == false) {
                                System.out.println(key_param);
                                status_kode = false;

                            }
                            break;

                    }
                } else {
                    response.put("status " + input.get("cardnbr"), key_param + " tidak dimasukan");
                }
            }
            // System.out.println("status "+input.get("cardnbr") + isi + " : " +
            // status_kode);
            if (status_kode) {
                kode_hasil.add(isi);
            }
        }
        // System.out.println(kode_hasil.toString());

        if (kode_hasil.size() > 1) {
            Collections.sort(kode_hasil);
        }
        if (kode_hasil.size() > 0) {
            response.put("status " + input.get("cardnbr"), "eligible");
        }
        result = response;
        System.out.println(result);
        return result;
    }
}
