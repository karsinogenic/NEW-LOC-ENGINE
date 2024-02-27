package com.demo_loc_engine.demo.Controllers;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
// import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.transform.Source;

import org.apache.catalina.connector.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.groovy.util.Maps;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.sql.exec.ExecutionException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo_loc_engine.demo.Models.AsccendResponse;
import com.demo_loc_engine.demo.Models.Channel;
import com.demo_loc_engine.demo.Models.ChannelResponse;
import com.demo_loc_engine.demo.Models.CustomerParam;
import com.demo_loc_engine.demo.Models.Kriteria;
import com.demo_loc_engine.demo.Models.LogAscend;
import com.demo_loc_engine.demo.Models.LogToAscend;
import com.demo_loc_engine.demo.Models.MftsResponse;
import com.demo_loc_engine.demo.Models.NewAESComponent;
import com.demo_loc_engine.demo.Models.Operator;
import com.demo_loc_engine.demo.Models.PlanCode;
import com.demo_loc_engine.demo.Models.TerminalMerchant;
import com.demo_loc_engine.demo.Models.Tier;
import com.demo_loc_engine.demo.Models.TierKriteria;
import com.demo_loc_engine.demo.Models.Users;
import com.demo_loc_engine.demo.Repositories.ChannelRepository;
import com.demo_loc_engine.demo.Repositories.ChannelResponseRepository;
import com.demo_loc_engine.demo.Repositories.CustomerParamRepository;
import com.demo_loc_engine.demo.Repositories.FirebaseConfigRepository;
import com.demo_loc_engine.demo.Repositories.AESComponentRepository;
import com.demo_loc_engine.demo.Repositories.APIConfigRepository;
import com.demo_loc_engine.demo.Repositories.KriteriaRepository;
import com.demo_loc_engine.demo.Repositories.LogAscendRepository;
import com.demo_loc_engine.demo.Repositories.LogToAscendRepository;
import com.demo_loc_engine.demo.Repositories.MftsResponseRepository;
import com.demo_loc_engine.demo.Repositories.OperatorRepository;
import com.demo_loc_engine.demo.Repositories.PlanCodeRepository;
import com.demo_loc_engine.demo.Repositories.TerminalMerchantRepository;
import com.demo_loc_engine.demo.Repositories.TierKriteriaRepository;
import com.demo_loc_engine.demo.Repositories.TierRepository;
import com.demo_loc_engine.demo.Repositories.UsersRepository;
import com.demo_loc_engine.demo.Services.AESComponent;
import com.demo_loc_engine.demo.Services.AESEncryptDecrypt;
import com.demo_loc_engine.demo.Services.APICustomer;
import com.demo_loc_engine.demo.Services.CheckPefindo;
import com.demo_loc_engine.demo.Services.CheckThreads;
import com.demo_loc_engine.demo.Services.CheckWithoutThreads;
import com.demo_loc_engine.demo.Services.FileReadWrite;
import com.demo_loc_engine.demo.Services.FirebaseService;
import com.demo_loc_engine.demo.Services.HTTPRequest;
import com.demo_loc_engine.demo.Services.LogService;
import com.demo_loc_engine.demo.Services.MFTS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@RestController
@RequestMapping("/api")
public class APIMainController {

    @Autowired
    public TierKriteriaRepository tierKriteriaRepository;
    @Autowired
    public FirebaseConfigRepository firebaseConfigRepository;
    @Autowired
    public TierRepository tierRepository;
    @Autowired
    public KriteriaRepository kriteriaRepository;
    @Autowired
    public ChannelRepository channelRepository;
    @Autowired
    public PlanCodeRepository planCodeRepository;
    @Autowired
    public ChannelResponseRepository channelResponseRepository;
    @Autowired
    public LogAscendRepository logAscendRepository;
    @Autowired
    public LogToAscendRepository logToAscendRepository;
    @Autowired
    public TerminalMerchantRepository terminalMerchantRepository;
    @Autowired
    public MftsResponseRepository mftsResponseRepository;
    @Autowired
    public APIConfigRepository apiConfigRepository;
    @Autowired
    public CustomerParamRepository customerParamRepository;
    @Autowired
    public OperatorRepository operatorRepository;
    @Autowired
    public FileReadWrite fileReadWriteService;

    @Autowired
    public UsersRepository usersRepository;

    @Autowired
    public AESComponentRepository aesComponentRepository;

    @Autowired
    public AESComponent aesComponent;

    // @Autowired
    // public TestAscoreRepository testAscoreRepository;

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = factory.getValidator();
    public static String ipLoc = "10.14.21.65:8084";

    @Autowired
    public LogService logService;

    // @BeforeClass
    // public static void setUpValidator() {
    // ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    // validator = factory.getValidator();
    // }

    // @GetMapping("/cobaFire")
    // public ResponseEntity cobaAsc() {
    // String urlMFTS = this.apiConfigRepository.findIpByNama("MFTS_securitybp");
    // List<LogAscend> logAscend = this.logAscendRepository.findAll();
    // NewAESComponent aesComponentNew =
    // this.aesComponentRepository.findByNama("MFTS_RES");

    // FirebaseService firebaseService = new
    // FirebaseService(channelResponseRepository,
    // logAscend.get(logAscend.size() - 1), firebaseConfigRepository,
    // aesComponentNew, urlMFTS);
    // Boolean coba = firebaseService.sendToFireBase("F");
    // return new ResponseEntity<>(HttpStatus.OK);
    // }

    @GetMapping("/getChannelId")
    public ResponseEntity getChannelId(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id != null) {
                Optional<Channel> channel = this.channelRepository.findById(id);
                response.put("data", channel);
            } else {
                List<Channel> channel = this.channelRepository.findAll();
                response.put("data", channel);
            }
            // Optional<Channel> channel = this.channelRepository.findById(id);
            response.put("rc", 200);
            response.put("rd", "Berhasil");
            // response.put("data", channel);
            return new ResponseEntity<>(response, null, 200);
        } catch (Exception e) {
            response.put("rc", 200);
            response.put("rd", "Error");
            return new ResponseEntity<>(response, null, 400);
        }
    }

    @GetMapping("/getKriteriaId")
    public ResponseEntity getKriteriaId(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id != null) {
                Optional<Kriteria> channel = this.kriteriaRepository.findById(id);
                response.put("data", channel);
            } else {
                List<Kriteria> channel = this.kriteriaRepository.findAll();
                response.put("data", channel);
            }
            // Optional<Kriteria> channel = this.kriteriaRepository.findById(id);
            response.put("rc", 200);
            response.put("rd", "Berhasil");
            // response.put("data", channel);
            return new ResponseEntity<>(response, null, 200);
        } catch (Exception e) {
            response.put("rc", 200);
            response.put("rd", "Error");
            return new ResponseEntity<>(response, null, 400);
        }
    }

    @GetMapping("/getOperatorId")
    public ResponseEntity getOperatorId(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id != null) {
                Optional<Operator> channel = this.operatorRepository.findById(id);
                response.put("data", channel);
            } else {
                List<Operator> channel = this.operatorRepository.findAll();
                response.put("data", channel);
            }
            response.put("rc", 200);
            response.put("rd", "Berhasil");
            return new ResponseEntity<>(response, null, 200);
        } catch (Exception e) {
            response.put("rc", 200);
            response.put("rd", "Error");
            return new ResponseEntity<>(response, null, 400);
        }
    }

    @GetMapping("/getPlanCodeId")
    public ResponseEntity getPlanCodeId(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id != null) {
                Optional<PlanCode> channel = this.planCodeRepository.findById(id);
                response.put("data", channel);
            } else {
                List<PlanCode> channel = this.planCodeRepository.findAll();
                response.put("data", channel);
            }
            response.put("rc", 200);
            response.put("rd", "Berhasil");
            return new ResponseEntity<>(response, null, 200);
        } catch (Exception e) {
            response.put("rc", 200);
            response.put("rd", "Error");
            return new ResponseEntity<>(response, null, 400);
        }
    }

    @GetMapping("/getTierId")
    public ResponseEntity getTierId(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id != null) {
                Optional<Tier> channel = this.tierRepository.findById(id);
                response.put("data", channel);
            } else {
                List<Tier> channel = this.tierRepository.findAll();
                response.put("data", channel);
            }
            response.put("rc", 200);
            response.put("rd", "Berhasil");
            return new ResponseEntity<>(response, null, 200);
        } catch (Exception e) {
            response.put("rc", 200);
            response.put("rd", "Error");
            return new ResponseEntity<>(response, null, 400);
        }
    }

    @GetMapping("/getTierKriteriaId")
    public ResponseEntity getTierKriteriaId(@RequestParam(required = false) Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (id != null) {
                Optional<TierKriteria> channel = this.tierKriteriaRepository.findById(id);
                response.put("data", channel);
            } else {
                List<TierKriteria> channel = this.tierKriteriaRepository.findAll();
                response.put("data", channel);
            }
            response.put("rc", 200);
            response.put("rd", "Berhasil");
            return new ResponseEntity<>(response, null, 200);
        } catch (Exception e) {
            response.put("rc", 200);
            response.put("rd", "Error");
            return new ResponseEntity<>(response, null, 400);
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity addUser(@RequestBody Users users) {
        Map<String, Object> response = new HashMap<>();
        try {
            Users users1 = users;
            users1.setIs_active(true);
            users1.setRole("admin");
            this.usersRepository.save(users1);
            response.put("rc", 200);
            response.put("rd", "Berhasil");
        } catch (Exception e) {
            response.put("rc", 400);
            response.put("rd", "Gagal");
        }

        return new ResponseEntity<Map<String, Object>>(response, null,
                Integer.valueOf(response.get("rc").toString()));
    }

    @PostMapping("/postTierKriteria")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postTierKriteria(@RequestBody TierKriteria input) {
        HashMap map = new HashMap();
        Set<ConstraintViolation<TierKriteria>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            map.put("rc", 400);
            map.put("rd", "Blank / Null");
            // map.put("error", violations.iterator().next().getPropertyPath().toString());
            map.put("error", violations.iterator().next().getMessage());
            // throw new ConstraintViolationException(violations);
            // return new mapEntity<Map<String, Object>>(response,
            // HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        try {
            input.setCreatedAt(Date.from(Instant.now()));
            map.put("rc", 200);
            map.put("rd", "Berhasil Add");
            // //System.out.println(input.getId());
            if (input.getId() != null) {
                map.put("rd", "Berhasil Edit");
            }
            this.tierKriteriaRepository.save(input);
        } catch (Exception e) {
            map.put("rc", 400);
            map.put("rd", "Gagal");
            map.put("error", e.getCause().getCause().getLocalizedMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    public Map<String, Object> validasiInputTierKriteria(Map<String, Object> maps, TierKriteria input) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            if (maps.size() > 0) {
                ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

                @Valid
                TierKriteria tierKriteria = mapper.convertValue(maps, TierKriteria.class);
                tierKriteria.setCreatedAt(Date.from(Instant.now()));
                tierKriteria.setIs_active(true);
                validator.validate(tierKriteria);
            }
            return response;
        } catch (ConstraintViolationException e) {
            List<String> errorMessages = e.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            for (String string : errorMessages) {
                response.put("status", string);
            }
            return response;
        }
    }

    @PostMapping("/postPlanCode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postPlanCode(@RequestBody PlanCode input) {
        HashMap map = new HashMap();
        input.setCreatedAt(Date.from(Instant.now()));
        Set<ConstraintViolation<PlanCode>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            map.put("rc", 400);
            map.put("rd", "Blank / Null");
            // map.put("error", violations.iterator().next().getPropertyPath().toString());
            map.put("error", violations.iterator().next().getMessage());
            // throw new ConstraintViolationException(violations);
            // return new mapEntity<Map<String, Object>>(response,
            // HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }

        try {
            map.put("rc", 200);
            map.put("rd", "Berhasil Add");
            if (input.getId() != null) {
                map.put("rd", "Berhasil Edit");
            }
            this.planCodeRepository.save(input);
        } catch (Exception e) {
            map.put("rc", 400);
            map.put("rd", "Gagal");
            map.put("error", e.getCause().getCause().getLocalizedMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/postTier")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postTier(@RequestBody Tier input) {
        HashMap map = new HashMap();
        try {
            map.put("rc", 200);
            map.put("rd", "Berhasil Add");
            if (input.getId() != null) {
                map.put("rd", "Berhasil Edit");
            }
            input.setCreatedAt(Date.from(Instant.now()));
            input.setIs_active(true);
            input.setKode_tier(input.getKode_channel() + "-" + input.getNama_tier());
            Set<ConstraintViolation<Tier>> violations = validator.validate(input);
            if (!violations.isEmpty()) {
                map.put("rc", 400);
                map.put("rd", "Blank / Null");
                // map.put("error", violations.iterator().next().getPropertyPath().toString());
                map.put("error", violations.iterator().next().getMessage());
                // throw new ConstraintViolationException(violations);
                // return new mapEntity<Map<String, Object>>(response,
                // HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
            }
            this.tierRepository.save(input);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } catch (Exception e) {
            // //System.out.println(e.toString());
            map.put("status", 400);
            map.put("detail", e.getCause().getCause().getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/postKriteria")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postKriteria(@RequestBody Map<String, Object> maps, Kriteria input) {
        HashMap map = new HashMap();
        input.setCreatedAt(Date.from(Instant.now()));
        Set<ConstraintViolation<Kriteria>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            map.put("rc", 400);
            map.put("rd", "Blank / Null");
            // map.put("error", violations.iterator().next().getPropertyPath().toString());
            map.put("error", violations.iterator().next().getMessage());
            // throw new ConstraintViolationException(violations);
            // return new mapEntity<Map<String, Object>>(response,
            // HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        try {
            map.put("rc", 200);
            map.put("rd", "Berhasil Add");
            if (input.getId() != null) {
                map.put("rd", "Berhasil Edit");
            }
            this.kriteriaRepository.save(input);
        } catch (Exception e) {
            map.put("rc", 400);
            map.put("rd", "Gagal");
            map.put("error", e.getCause().getCause().getLocalizedMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/postChannel")
    @ResponseBody
    public ResponseEntity postChannel(@RequestBody Channel input) {
        HashMap map = new HashMap();
        input.setCreatedAt(Date.from(Instant.now()));
        Set<ConstraintViolation<Channel>> violations = validator.validate(input);
        if (!violations.isEmpty()) {
            map.put("rc", 400);
            map.put("rd", "Blank / Null");
            // map.put("error", violations.iterator().next().getPropertyPath().toString());
            map.put("error", violations.iterator().next().getMessage());
            // throw new ConstraintViolationException(violations);
            // return new mapEntity<Map<String, Object>>(response,
            // HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        try {
            map.put("rc", 200);
            map.put("rd", "Berhasil Add");
            if (input.getId() != null) {
                map.put("rd", "Berhasil Edit");
            }
            this.channelRepository.save(input);
        } catch (Exception e) {
            map.put("rc", 400);
            map.put("rd", "Gagal");
            map.put("error", e.getCause().getCause().getLocalizedMessage());
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/eligible")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cekEligible(@RequestBody Map<String, Object> input)
            throws InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> mapList = new ArrayList<>();

        JSONObject jsonObject1 = new JSONObject(input);
        logService.info("/eligible req: " + jsonObject1.toString());

        // System.out.println("pefindo: " + jsonObject1.get("pefindo_data"));
        JSONArray jsonArray = new JSONArray(jsonObject1.get("data").toString());

        List<Thread> lThreads = new ArrayList<>();
        List<CheckThreads> lCheckThreads = new ArrayList<>();

        List<Map> finalList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            // Get each JSONObject from the JSONArray
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            // Convert the JSONObject to a map
            Map<String, Object> map = new HashMap<>();
            map = jsonObject.toMap();

            // CheckWithoutThreads cwt = new CheckWithoutThreads(map,
            // tierKriteriaRepository);
            // finalList.add(cwt.runCheck());

            CheckThreads checkThreads = new CheckThreads(map, this.tierKriteriaRepository, this.tierRepository);
            Thread thread = new Thread(checkThreads);
            // //System.out.println(checkThreads.result());
            lCheckThreads.add(checkThreads);
            lThreads.add(thread);
            thread.start();

            if (map.get("cardnbr") == null) {
                response.put("status ke-" + (i + 1), "crdnbr tidak dimasukan");
            }

        }

        for (Thread thread : lThreads) {
            thread.join();
        }
        for (CheckThreads checkThreads : lCheckThreads) {
            finalList.add(checkThreads.result());
        }
        if (finalList.size() > 0) {

        }
        response.put("data", finalList);

        Map pefindo = pefindoSlikList(finalList, input).getBody();
        // if (pefindo.get("rc").toString().contains("EX")) {
        response.put("rc", pefindo.get("rc"));
        response.put("rd", pefindo.get("rd"));
        // }

        // // response.put("data", mapList);
        // int i = 1;
        // for (Map<String, Object> object : mapList) {
        // CheckThreads checkThreads = new CheckThreads(object, tierKriteriaRepository);
        // // //System.out.println(checkThreads.result());
        // try {
        // checkThreads.start();
        // checkThreads.join();
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // if (object.get("crdnbr") == null) {
        // response.put("status", "crdnbr tidak dimasukan");
        // } else {
        // response.put("data_" + object.get("crdnbr"), checkThreads.result());
        // }
        // }

        // response.put("data", checkThreads.result());

        logService.info("/eligible req: " + response.toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Map> pefindoSlikList(List<Map> finalList, Map<String, Object> input) {
        Map hasil = new HashMap<>();
        Boolean hasil_bool = false;
        for (Map map : finalList) {
            hasil_bool = hasil_bool || (boolean) map.get("bool");
            System.out.println("hasil bool: " + hasil_bool);
        }
        if (!hasil_bool) {
            hasil.put("rc", "EX-00");
            hasil.put("rd", "All Card Not Eligible");
            return new ResponseEntity<>(hasil, null, 200);
        }

        if (!input.containsKey("pefindo_data") && hasil_bool) {
            hasil.put("rc", "01");
            hasil.put("rd", "Eligible Without PefindoData");
            return new ResponseEntity<>(hasil, null, 200);
        }
        // System.out.println("return");

        try {
            JSONObject pefindoData = new JSONObject(input);
            // System.out.println("pefindo data: " + pefindoData);
            CheckPefindo checkPefindo = new CheckPefindo();
            String cekParam = checkPefindo.cekParamPefindo(pefindoData.getJSONObject("pefindo_data").toMap());
            if (cekParam.length() > 0) {
                hasil.put("rc", "EX-03");
                hasil.put("rd", "PefindoData Wrong Format " + cekParam);
                return new ResponseEntity<>(hasil, null, 200);
            }
        } catch (Exception e) {
            hasil.put("rc", "EX-03");
            hasil.put("rd", "PefindoData Wrong Format");
            return new ResponseEntity<>(hasil, null, 200);
        }

        // hit pefindo here
        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        JSONObject hasilJsonObject;
        // String path = "?path=C:/Users/muhmm/Desktop/Bank Mega/Bank
        // Mega/pefindo/pefindo/src/main/resources/static/pefindo.json";
        // path.replace(" ", "%20");
        // path = aesComponent.getPefindoUrl() + path;
        // System.out.println("path: " + path);
        try {
            String hasilHttp = httpRequest.getRequestParamNew(aesComponent.getPefindoUrl(),
                    null, null, null);
            hasilJsonObject = new JSONObject(hasilHttp);
            if (hasilJsonObject.getString("status").contains("Not")) {
                hasil.put("rc", "EX-01");
                hasil.put("rd", "Pefindo Not Eligible");
            } else {
                hasil.put("rc", "00");
                hasil.put("rd", "Eligible");
            }
        } catch (Exception e) {
            hasil.put("rc", "EX-99");
            hasil.put("rd", "Pefindo Timeout / Not Available");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<>(hasil, null, 200);
    }

    @PostMapping("/cek")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cekInput(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(name = "Example", value = """
                            {
                                \"loan\":12500000,
                                \"channel\":"001",
                                \"MOB\":13,
                                \"productcode\":120,
                                \"agingcurrent\":6
                            }
                                """, summary = "Minimal request") }))

            Map<String, Object> input) {
        logService.info("/cek req: " + input.toString());
        Map response = new HashMap();
        List<String> kode_hasil = new ArrayList<String>();
        if (input.get("channel") == null) {
            response.put("status", "channel tidak dimasukan");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else if (input.get("loan") == null) {
            response.put("status", "loan tidak dimasukan");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        Long loan = Long.parseLong(input.get("loan").toString());
        String[] distinct_kode = this.tierRepository.findKodeTierDistinct(input.get("channel").toString());
        for (String each : distinct_kode) {
            // System.out.println(each);
        }
        ;
        for (String isi : distinct_kode) {
            List<TierKriteria> list = this.tierKriteriaRepository.findByKodeTier(isi);
            Boolean status_kode = true;
            for (TierKriteria hasil : list) {
                String key_param = hasil.getKriteria().getNama_kriteria();
                // //System.out.println(key_param);
                String value_param = "";
                try {
                    // //System.out.println(input.get(key_param).getClass());
                    value_param = input.get(key_param).toString();
                } catch (Exception e) {
                    response.put("status", key_param + " tidak dimasukan");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                String operator = hasil.getOperator().getNama_operator();
                if (value_param != null) {
                    switch (operator) {
                        case ">=":
                            // //System.out.println(Integer.parseInt(value_param) >=
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) >= Integer.parseInt(hasil.getValue()) == false) {
                                // //System.out.println(key_param);
                                response.put("cause " + hasil.getKode_tier(), hasil.getKriteria().getNama_kriteria());

                                status_kode = false;
                            }
                            break;
                        case "<=":
                            // //System.out.println(Integer.parseInt(value_param) <=
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) <= Integer.parseInt(hasil.getValue()) == false) {
                                // //System.out.println(key_param);
                                response.put("cause " + hasil.getKode_tier(), hasil.getKriteria().getNama_kriteria());

                                status_kode = false;
                            }
                            break;
                        case ">":
                            // //System.out.println(Integer.parseInt(value_param) >
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) > Integer.parseInt(hasil.getValue()) == false) {
                                // //System.out.println(key_param);
                                response.put("cause " + hasil.getKode_tier(), hasil.getKriteria().getNama_kriteria());

                                status_kode = false;
                            }
                            break;
                        case "<":
                            // //System.out.println(Integer.parseInt(value_param) <
                            // Integer.parseInt(hasil.getValue()));
                            if (Integer.parseInt(value_param) < Integer.parseInt(hasil.getValue()) == false) {
                                // //System.out.println(key_param);
                                response.put("cause " + hasil.getKode_tier(), hasil.getKriteria().getNama_kriteria());

                                status_kode = false;
                            }
                            break;
                        case "=":
                            // //System.out.println(value_param.equals(hasil.getValue()));
                            if (value_param.equals(hasil.getValue()) == false) {
                                // //System.out.println(key_param);
                                response.put("cause " + hasil.getKode_tier(), hasil.getKriteria().getNama_kriteria());

                                status_kode = false;
                            }
                            break;
                        case "<>":
                            // //System.out.println(!value_param.equals(hasil.getValue()));
                            if (!value_param.equals(hasil.getValue()) == false) {
                                // //System.out.println(key_param);
                                response.put("cause " + hasil.getKode_tier(), hasil.getKriteria().getNama_kriteria());
                                status_kode = false;
                            }
                            break;
                        case "IN":
                            String[] newArr = hasil.getValue().split(",");
                            List<String> newArrList = Arrays.asList(newArr);
                            // for (String string : newArrList) {
                            // //System.out.println("list: " + string);
                            // }

                            // //System.out.println(value_param);
                            ObjectMapper objectMapper = new ObjectMapper();
                            // value_param = value_param.substring(1, value_param.length() - 1);
                            // //System.out.println(value_param);
                            // value_param.replaceAll("]", "");
                            String[] new_value_param = value_param.split(",");
                            JSONArray jsonArray = new JSONArray();
                            List<String> list_value_param = Arrays.asList(new_value_param);

                            if (list_value_param.size() > 0) {
                                for (String value_String : new_value_param) {
                                    // System.out.println(key_param);
                                    // //System.out.println("newArr" + Arrays.toString(newArr) + "value:" +
                                    // value_String + ":");
                                    if (Arrays.asList(newArr).contains(value_String) == false
                                            && hasil.getAnd_or().toLowerCase().equals("and")) {
                                        // //System.out.println(
                                        // "nih" + "newArr" + Arrays.toString(newArr) + "value:" + value_String + ":");
                                        status_kode = false;
                                        response.put("cause " + hasil.getKode_tier(),
                                                hasil.getKriteria().getNama_kriteria());
                                        break;
                                    } else if (Arrays.asList(newArr).contains(value_String) == false
                                            && hasil.getAnd_or().toLowerCase().equals("or")) {
                                        status_kode = false;
                                        response.put("cause " + hasil.getKode_tier(),
                                                hasil.getKriteria().getNama_kriteria());
                                    } else if (Arrays.asList(newArr).contains(value_String) == true
                                            && hasil.getAnd_or().toLowerCase().equals("or")) {
                                        status_kode = true;
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
                                // //System.out.println(!Arrays.asList(newArr1).contains(value_param));
                                if (!Arrays.asList(newArr1).contains(value_param) == false
                                        && hasil.getAnd_or().toLowerCase().equals("and")) {
                                    // //System.out.println(key_param);
                                    status_kode = false;
                                    response.put("cause " + hasil.getKode_tier(),
                                            hasil.getKriteria().getNama_kriteria());
                                } else if (!Arrays.asList(newArr1).contains(value_param) == false
                                        && hasil.getAnd_or().toLowerCase().equals("or")) {
                                    status_kode = false;
                                    response.put("cause " + hasil.getKode_tier(),
                                            hasil.getKriteria().getNama_kriteria());
                                } else if (!Arrays.asList(newArr1).contains(value_param) == true
                                        && hasil.getAnd_or().toLowerCase().equals("or")) {
                                    status_kode = true;
                                    break;
                                }
                            } else {
                                status_kode = false;
                            }
                            break;

                    }
                } else {
                    response.put("status", key_param + " tidak dimasukan");
                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                if (status_kode == false) {
                    break;
                }
            }
            // //System.out.println("status " + isi + " : " + status_kode);
            if (status_kode) {
                kode_hasil.add(isi);
            }
        }
        // //System.out.println(kode_hasil.toString());
        if (kode_hasil.size() > 1) {
            Collections.sort(kode_hasil);
        }
        // //System.out.println((kode_hasil.isEmpty() ? "" : kode_hasil.get(0)));
        Map<String, Object> hasil = calculate((kode_hasil.isEmpty() ? "" : kode_hasil.get(0)), loan);
        hasil.put("channel", (input.get("channel")));
        response.putAll(hasil);
        logService.info("/cek res: " + response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);

        // for (Map.Entry mp : input.entrySet()) {
        // String valString = mp.getValue().toString();
        // String keyString = mp.getKey().toString();

        // }
        // return null;
    }

    // public static Map<String, Object> hasilCalculate;
    // static {
    // hasilCalculate = new HashMap<>();
    // }
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public Map<String, Object> calculate(String kodehasil, Long loan) {
        Map<String, Object> hasil = new HashMap<String, Object>();
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        // System.out.println("kodehasil: " + kodehasil);
        // for (String isi : kodehasil) {
        List<PlanCode> list_plan = this.planCodeRepository.findByKodeTier(kodehasil);
        for (PlanCode list : list_plan) {
            Map<String, Object> mapBaru = new HashMap<String, Object>();
            // System.out.println(list.getPlan_code());
            mapBaru.put("plan_code", list.getPlan_code());
            mapBaru.put("tenor", list.getTenor());

            Double cicilan = hitungCicilan(loan, list.getInterest(), list.getInterest_type(), list.getTenor());
            BigDecimal cicilan1 = new BigDecimal(cicilan).setScale(0, RoundingMode.HALF_UP);

            mapBaru.put("cicilan", cicilan1);
            Double interestRate;
            if (list.getInterest_channel().equals("F")) {
                interestRate = hitungInterestRate(cicilan, loan, list.getInterest(), list.getTenor());
            } else {
                interestRate = list.getInterest();
            }

            BigDecimal interestRate1 = new BigDecimal(interestRate).setScale(2, RoundingMode.HALF_UP);

            mapBaru.put("interest", interestRate1);
            mapBaru.put("tier_code", kodehasil);
            listMap.add(mapBaru);
            // //System.out.println(listMap.toString());
        }
        // hasil.put("channel", kodehasil.split("-")[0]);
        // }
        hasil.put("data", listMap);
        if (kodehasil.isBlank()) {
            hasil.put("status", "not eligible");
        } else {
            hasil.put("status", "eligible");
        }
        return hasil;

    }

    public Double hitungCicilan(Long loan, Double oriInterest, String kode, Integer tenor) {
        Double hasil;
        Double y = (1 - (Math.pow((1 + (oriInterest / 100 / 12)), -tenor)));
        if (kode.equals("E")) {
            // //System.out.println("y=" + y);
            hasil = loan * (oriInterest / 100 / 12) / y;
            // //System.out.println("hasil: " + hasil);
        } else {
            hasil = (loan / tenor) + (loan * oriInterest / 100);
        }
        return hasil;
    }

    public Double hitungInterestRate(Double cicilan, Long loan, Double oriInterest, Integer tenor) {
        Double hasil = (cicilan - (loan / tenor)) / loan * 100;
        // //System.out.println("eRate:" + hasil);
        return hasil;
    }

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    @PostMapping(value = "/getChannelResponse", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getChannelResponse(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, examples = {
                    @ExampleObject(name = "Example", value = """
                            {
                                \"referenceId\":\"MEG111111111\",
                                \"cardNo\": \"4714390010000010\",
                                \"amount\": 10000,
                                \"planCode": \"MLS-007\",
                                \"expDate\": \"2511\",
                                \"accName\":\"NaufalTest\",
                                \"accNumber\":\"111111111111\",
                                \"terminalMerchant\": \"LOC\",
                                \"gcn\": \"12345678\"
                            }
                                """, summary = "Minimal request") })) Map<String, Object> input) {
        Map<String, Object> response = new HashMap();
        response = cekInputChannelResponse(input);
        if (response.get("rc").toString().equals("200")) {
            // //System.out.println("test : " + input.get("createdAt"));
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            ChannelResponse responseChannel = mapper.convertValue(input, ChannelResponse.class);
            try {
                channelResponseRepository.save(responseChannel);
            } catch (Exception e) {
                // SqlExceptionHelper sqlEx;
                response.put("rc", 400);
                response.put("detail", "error");
                response.put("info", e.getCause().getCause().getLocalizedMessage());
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            // ke Ascend

            Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository
                    .findByNama(input.get("terminalMerchant").toString());

            // //System.out.println(list_terminal.get().getPoscon());

            try {
                String[] expdate = input.get("expDate").toString().split("-");
                String jsonInput = "{" +
                        "\"referenceId\":\"" + input.get("referenceId") + "\"," +
                        "\"cardNo\": \"" + input.get("cardNo") + "\"," +
                        "\"amount\": \"" + input.get("amount") + "00" + "\"," +
                        "\"expirationDate\": \"" + input.get("expDate").toString() + "\"," +
                        "\"posCondCode\": \"" + list_terminal.get().getPoscon() + "\"," +
                        "\"terminalID\": \"" + list_terminal.get().getTerminalId() + "\"," +
                        "\"merchantID\": \"" + list_terminal.get().getMerchantId() + "\"" +
                        "}";
                JSONObject newJsonObject = new JSONObject(jsonInput);
                // ipLoc = this.apiConfigRepository.findIpByNama("LOC");
                // HttpRequest request = HttpRequest.newBuilder()
                // .uri(URI.create("http://" + ipLoc + "/loc/api/toAscendNew"))
                // // .uri(URI.create("http://127.0.0.1:8081/api/toAscendNew"))
                // .POST(BodyPublishers.ofString(jsonInput))
                // .header("Content-type", "application/json")
                // .header("Authorization", getBasicAuthenticationHeader("7777777", "7777777"))
                // .build();
                // //System.out.println(jsonInput);
                // HttpClient client = HttpClient.newBuilder().build();
                // HttpResponse<?> httpResponse = client.send(request,
                // HttpResponse.BodyHandlers.ofString());
                // //System.out.println(httpResponse.body().toString());
                // Map<String, Object> hasil_map =
                // mapper.readValue(httpResponse.body().toString(),
                // new TypeReference<Map<String, Object>>() {
                // });
                Map<String, Object> hasil_map = this.toAscendNew(null, newJsonObject.toMap()).getBody();
                System.out.println("hasil ascend new: " + new JSONObject(hasil_map).toString());
                if (hasil_map.get("status").toString().equals("200")) {
                    response.put("rc", 200);
                    response.put("authno", hasil_map.get("authno"));
                    response.put("detail", "Berhasil kirim ke Ascend");
                } else {
                    response.put("rc", 400);
                    response = hasil_map;
                }
            } catch (Exception e) {
                response.put("rc", 400);
                response.put("detail", "Gagal ke Ascend");
                // response.put("info", e.ge());
                e.printStackTrace();
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // response.put("detail", "Berhasil menyimpan data");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // return null;
    }

    public ResponseEntity<Map<String, Object>> hitAscend(String dataJson) {
        return null;
    }

    public List<String> findDifference(List<String> first, List<String> second) {
        List<String> diff = new ArrayList<>(first);
        diff.removeAll(second);
        return diff;
    }

    public Map<String, Object> cekInputChannelResponse(Map<String, Object> inputs) {
        Map<String, Object> response = new HashMap();
        String[] param = { "referenceId", "cardNo", "amount", "planCode", "expDate", "tierCode", "accName",
                "accNumber", "terminalMerchant", "gcn", "mobileNumber" };
        String[] param_opsional = { "additionalData" };
        List<String> compareParam = new ArrayList<String>();
        List<String> newParam = new ArrayList<String>(Arrays.asList(param));

        List<String> allTier = this.tierRepository.allTier();
        List<String> allPlanCode = this.planCodeRepository.allPlanCode();

        // //System.out.println(allTier.toString());
        String tierKode = "";

        for (Map.Entry<String, Object> entry : inputs.entrySet()) {
            if (Arrays.asList(param).contains(entry.getKey())
                    || Arrays.asList(param_opsional).contains(entry.getKey())) {
                compareParam.add(entry.getKey());
                response.put("rc", 200);
            } else {
                response.put("rc", 400);
                response.put("detail", "Nama param " + entry.getKey() + " tidak dikenal");
                response.put("info", "Gunakan nama param sebagai berikut " + Arrays.toString(param));
                return response;
            }

            if (entry.getKey().equals("tierCode")) {
                // //System.out.println(entry.getValue());
                // //System.out.println(entry.getValue());
                if (allTier.contains(entry.getValue()) == false) {
                    response.put("rc", 400);
                    response.put("detail", "Value dari  '" + entry.getKey() + "' tidak ditemukan di database");
                    return response;
                } else {
                    tierKode = entry.getValue().toString();
                }

            }

            if (entry.getKey().equals("bankName")) {
                try {
                    if (!inputs.get("bankName").toString().toLowerCase().equals("bank mega")) {
                        response.put("rc", 400);
                        response.put("detail", "accName harus ada isi");
                        return response;
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        }

        try {
            List<String> listPlanCode = this.planCodeRepository.findPlanCodeNameByKodeTier(tierKode);
            // //System.out.println(listPlanCode.toString());
            // //System.out.println(this.planCodeRepository.findByKodeTier(tierKode).get(0).getPlan_code());
            if (!listPlanCode.contains(inputs.get("planCode").toString())) {
                response.put("rc", 400);
                response.put("detail", "Value dari 'planCode' tidak ditemukan di database");
                return response;
            }
        } catch (Exception e) {
            response.put("rc", 400);
            response.put("detail", "Value dari 'planCode' tidak ditemukan di database");
            return response;
        }

        List<String> hasilParam = findDifference(newParam, compareParam);
        // //System.out.println(hasilParam);
        if (hasilParam.size() > 0 && hasilParam.contains(param_opsional) == false) {
            response.put("rc", 400);
            response.put("detail", "Nama param '" + hasilParam.get(0) + "' tidak dimasukan");
        }

        // }
        Date date = new Date();
        LocalDateTime ldt = LocalDateTime.now();
        if (inputs.get("tierCode") != null) {
            String kode_channnel = inputs.get("tierCode").toString().split("-")[0];
            inputs.put("kodeChannel", kode_channnel);
        }
        inputs.put("createdAt", ldt);
        return response;
    }

    // @GetMapping(value = "/tes")
    // public String tes() {
    // Optional<ChannelResponse> cr_list =
    // this.channelResponseRepository.getByReferenceId("asdasd1156");
    // FileReadWrite fileReadWrite = new FileReadWrite();
    // JSONObject jsonObject = fileReadWrite.fireBase(cr_list.get());
    // // //System.out.println(cr_list.get().getReferenceId());
    // return jsonObject.toString();
    // }

    @GetMapping(value = "/testAscend")
    public ResponseEntity<Map<String, Object>> testAscend() {
        Map<String, Object> newMap = new HashMap<String, Object>();
        newMap.put("date", "0213");
        newMap.put("rc", "00");
        newMap.put("rd", "SUCCESS");
        newMap.put("reffno", "000011000618");
        newMap.put("traceno", "000618");
        newMap.put("time", "140207");
        newMap.put("auth_no", "035925");
        return new ResponseEntity<>(newMap, HttpStatus.OK);
    }

    @PostMapping(value = "/toAscend")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toAscend(@RequestBody Map<String, Object> input) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        String refId;
        // //System.out.println("masuk ascend");

        try {
            refId = input.get("referenceId").toString();
            // //System.out.println(refId);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "referenceId belum dimasukan");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.BAD_REQUEST);
        }
        input.remove("referenceId");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String jsonInput;
        String jsonOutput;

        LocalDateTime ldt = LocalDateTime.now();
        LogToAscend logToAscend = new LogToAscend();

        try {
            jsonInput = mapper.writeValueAsString(input);
            logToAscend.setCreatedAt(ldt);
            logToAscend.setInput(jsonInput);
            logToAscend.setReferenceId(refId);
            this.logToAscendRepository.save(logToAscend);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "Gagal menyimpan data ke LogToAscend");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.BAD_REQUEST);

        }

        String ipAscendSale = this.apiConfigRepository.findIpByNama("ASC_sale");
        // System.out.println(ipAscendSale);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                // .uri(URI.create("http://127.0.0.1:8081/api/testAscend"))
                .uri(URI.create("http://" + ipAscendSale))
                .POST(BodyPublishers.ofString(jsonInput))
                .header("Content-type", "application/json")
                .build();
        // HttpRequest request = HttpRequest.newBuilder()
        // .uri(URI.create("http://10.14.20.154:9087/sale"))
        // .POST(BodyPublishers.ofString(jsonInput))
        // .header("Content-type", "application/json")
        // .build();
        // //System.out.println(jsonInput);
        HttpResponse<?> response;
        Map<String, Object> hasil_map = new HashMap<>();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // //System.out.println("coba :" + response.body().toString());
            hasil_map = mapper.readValue(response.body().toString(),
                    new TypeReference<Map<String, Object>>() {
                    });

            if (hasil_map.get("rc").equals("00") == false) {
                newMap.put("status", 400);
                newMap.put("detail", "Balikan Ascend '" + mapper.writeValueAsString(hasil_map) + "'");
                // //System.out.println();
                Map<String, Object> mapAscend = mapper.readValue(response.body().toString(), Map.class);
                jsonOutput = mapper.writeValueAsString(mapAscend);

                List<LogToAscend> findRefId = this.logToAscendRepository.findByRefId(refId);
                logToAscend = findRefId.get(0);
                logToAscend.setOutput(jsonOutput);
                this.logToAscendRepository.save(logToAscend);

                LogAscend logAscend = mapper.convertValue(mapAscend, LogAscend.class);
                logAscend.setReferenceId(refId);
                LocalDateTime ldt4 = LocalDateTime.now();
                logAscend.setCreated_at(ldt4);
                logAscend.setIsGenerated(false);
                logAscend.setIsGeneratedPPMERL(false);
                this.logAscendRepository.save(logAscend);

                return new ResponseEntity<>(newMap, HttpStatus.BAD_REQUEST);
            }
            // //System.out.println("test: " + hasil.get("rc"));
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "Gagal tersambung dengan Ascend");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.BAD_GATEWAY);
        }

        try {
            // System.out.println("test: " + response.body());

            Map<String, Object> mapAscend = mapper.readValue(response.body().toString(), Map.class);
            LocalDateTime ldt2 = LocalDateTime.now();
            mapAscend.put("referenceId", refId);
            mapAscend.put("created_at", ldt2);
            mapAscend.put("isGenerated", null);
            mapAscend.put("isGeneratedPPMERL", null);
            jsonOutput = mapper.writeValueAsString(mapAscend);

            List<LogToAscend> findRefId = this.logToAscendRepository.findByRefId(refId);
            logToAscend = findRefId.get(0);
            logToAscend.setOutput(jsonOutput);
            this.logToAscendRepository.save(logToAscend);

            // mapAscend.entrySet().forEach(entry -> {
            // //System.out.println(entry.getKey() + " : " + entry.getValue());
            // });

            LogAscend logAscend = mapper.convertValue(mapAscend, LogAscend.class);
            this.logAscendRepository.save(logAscend);
            newMap.put("status", 200);
            newMap.put("authno", logAscend.getAuth_no());
            newMap.put("detail", "Berhasil Menyimpan Data Dari Ascend");
            return new ResponseEntity<>(newMap, HttpStatus.OK);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "Balikan Ascend tidak sama dengan Database");
            newMap.put("error", e.toString());
            return new ResponseEntity<>(newMap, HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "/toAscendNew")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toAscendNew(@RequestBody(required = false) Map<String, Object> input,
            Map<String, Object> input1) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        NewAESComponent aesComponentNew = this.aesComponentRepository.findByNama("SALE");
        if (input == null) {
            input = input1;
        }

        String refId;
        // System.out.println("masuk ascend");

        try {
            refId = input.get("referenceId").toString();
            // //System.out.println(refId);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "referenceId belum dimasukan");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.BAD_REQUEST);
        }
        input.remove("referenceId");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String jsonInput;
        String jsonOutput;

        LocalDateTime ldt = LocalDateTime.now();
        LogToAscend logToAscend = new LogToAscend();

        try {
            jsonInput = mapper.writeValueAsString(input);
            logToAscend.setCreatedAt(ldt);
            logToAscend.setInput(jsonInput);
            logToAscend.setReferenceId(refId);
            this.logToAscendRepository.save(logToAscend);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "Gagal menyimpan data ke LogToAscend");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.BAD_REQUEST);

        }

        JSONObject newJsonInput = new JSONObject();
        newJsonInput.put("channel_id", aesComponentNew.getAesChannelId());
        newJsonInput.put("service_id", aesComponentNew.getAesServiceId());
        newJsonInput.put("key_id", aesComponentNew.getAesKeyId());

        JSONObject dataJson = new JSONObject();
        dataJson.put("cardNo", input.get("cardNo"));
        dataJson.put("amount", input.get("amount"));
        dataJson.put("expirationDate", input.get("expirationDate"));
        dataJson.put("terminalID", input.get("terminalID"));
        dataJson.put("merchantID", input.get("merchantID"));

        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
        JSONObject jsonObject = new JSONObject();
        String dataEncrypt = "";
        try {
            dataEncrypt = aesEncryptDecrypt.encrypt(dataJson.toString(), aesComponentNew.getAesKey(),
                    aesComponentNew.getAesIV());
        } catch (Exception e) {

        }
        jsonObject.put("msg", dataEncrypt);
        newJsonInput.put("data", jsonObject);
        // //System.out.println(newJsonInput.toString());

        String ipAscendSale = this.apiConfigRepository.findIpByNama("ASC_sale_new");
        // //System.out.println("siap kirim");
        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        String response = "";
        try {
            System.out.println("request sale :" + newJsonInput.toString());
            response = httpRequest.postRequest("http://" + ipAscendSale, newJsonInput.toString());
            System.out.println("response sale :" + response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Map<String, Object> mapRespon = new HashMap<>();
        try {
            mapRespon = mapper.readValue(response, Map.class);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            // //System.out.println("aesComponent.getAesKey(),aesComponent.getAesIV(): " +
            // aesComponent.getAesKey() + " : " +
            // aesComponent.getAesIV());
            String decryptData = aesEncryptDecrypt.decrypt(mapRespon.get("data").toString(),
                    aesComponentNew.getAesKey(),
                    aesComponentNew.getAesIV());
            // //System.out.println(decryptData.isBlank());
            Map<String, Object> mapAscend = mapper.readValue(decryptData, Map.class);

            if (mapAscend.get("rc").equals("00") == false) {
                // //System.out.println("masuk error");
                List<LogToAscend> findRefId = this.logToAscendRepository.findByRefId(refId);
                logToAscend = findRefId.get(0);
                logToAscend.setOutput(decryptData);
                this.logToAscendRepository.save(logToAscend);

                LogAscend logAscend = mapper.convertValue(mapAscend, LogAscend.class);
                logAscend.setReferenceId(refId);
                LocalDateTime ldt4 = LocalDateTime.now();
                logAscend.setCreated_at(ldt4);
                logAscend.setIsGenerated(null);
                logAscend.setIsGeneratedPPMERL(null);
                mapAscend.put("status", mapAscend.get("rc").toString());
                // System.out.println(new JSONObject(mapAscend).toString());
                this.logAscendRepository.save(logAscend);
                return new ResponseEntity<>(mapAscend, HttpStatus.BAD_REQUEST);
            }

            LocalDateTime ldt2 = LocalDateTime.now();
            mapAscend.put("referenceId", refId);
            mapAscend.put("created_at", ldt2);
            mapAscend.put("isGenerated", null);
            mapAscend.put("isGeneratedPPMERL", null);
            jsonOutput = mapper.writeValueAsString(mapAscend);

            List<LogToAscend> findRefId = this.logToAscendRepository.findByRefId(refId);
            logToAscend = findRefId.get(0);
            logToAscend.setOutput(jsonOutput);
            this.logToAscendRepository.save(logToAscend);

            // mapAscend.entrySet().forEach(entry -> {
            // //System.out.println(entry.getKey() + " : " + entry.getValue());
            // });

            LogAscend logAscend = mapper.convertValue(mapAscend, LogAscend.class);
            this.logAscendRepository.save(logAscend);
            newMap.put("status", 200);
            newMap.put("authno", logAscend.getAuth_no());
            newMap.put("detail", "Berhasil Menyimpan Data Dari Ascend");
            return new ResponseEntity<>(newMap, HttpStatus.OK);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "Balikan Ascend tidak sama dengan Database");
            newMap.put("error", e.toString());
            return new ResponseEntity<>(newMap, HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @GetMapping(value = "/generateFile/SPDEXT")
    public ResponseEntity<Map<String, Object>> generateFile() {
        LocalDateTime ldt = LocalDateTime.now();
        // //System.out.println(ldt);
        Map<String, Object> response = new HashMap<String, Object>();
        FileReadWrite frw = new FileReadWrite();
        // //System.out.println("coba1: " + frw.tes().get().getReferenceId());
        List<LogAscend> list_logAscend = this.logAscendRepository
                .findByIsNotGenerated();

        Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository
                .findByNama("LOC");

        // //System.out.println(list_terminal.get().getMerchantId());

        if (list_logAscend.isEmpty()) {
            response.put("rc", 204);
            response.put("status", "Belum ada data terbaru, tidak membuat file baru");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String spdext = frw.spdext(list_logAscend, this.channelResponseRepository,
                list_terminal.get().getMerchantId().toString(), terminalMerchantRepository);
        // String ppmerl = frw.ppmrl(list_logAscend, this.channelResponseRepository,
        // this.planCodeRepository,
        // list_terminal.get().getMerchantId().toString());
        // Update isGenerated
        if (spdext.contains("FAIL")) {
            response.put("rc", 400);
            response.put("status", "Gagal membuat file SPDEXT");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        for (LogAscend logAscend : list_logAscend) {
            logAscend.setGeneratedAt(ldt);
            logAscend.setIsGenerated(true);
            this.logAscendRepository.save(logAscend);
        }

        response.put("rc", 200);
        response.put("status", spdext);
        // response.put("isi", list_logAscend);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/generateFile/PPMERL")
    public ResponseEntity<Map<String, Object>> generateFilePPMRL() {
        LocalDateTime ldt = LocalDateTime.now();
        // //System.out.println(ldt);
        Map<String, Object> response = new HashMap<String, Object>();
        FileReadWrite frw = new FileReadWrite();
        // //System.out.println("coba1: " + frw.tes().get().getReferenceId());
        List<LogAscend> list_logAscend = this.logAscendRepository
                .findByIsNotGeneratedPPMERL();

        Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository
                .findByNama("LOC");

        // //System.out.println(list_terminal.get().getMerchantId());

        if (list_logAscend.isEmpty()) {
            response.put("rc", 204);
            response.put("status", "Belum ada data terbaru, tidak membuat file baru");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String ppmerl = frw.ppmrl(list_logAscend, this.channelResponseRepository, this.planCodeRepository,
                list_terminal.get().getMerchantId().toString());
        // Update isGenerated
        if (ppmerl.contains("FAIL")) {
            response.put("rc", 400);
            response.put("status", "Gagal membuat file PPMERL");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        for (LogAscend logAscend : list_logAscend) {
            logAscend.setGeneratedAtPPMERL(ldt);
            logAscend.setIsGeneratedPPMERL(true);
            this.logAscendRepository.save(logAscend);
        }

        response.put("rc", 200);
        response.put("status", ppmerl);
        // response.put("isi", list_logAscend);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/generateFile/MFTS")
    public ResponseEntity<Map<String, Object>> generateLOCTRF() {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDate ld = LocalDate.now();
        String ldtnew = (ldt.getYear() + "-" + (String.valueOf(ldt.getMonthValue()).length() == 1
                ? ("0" + String.valueOf(ldt.getMonthValue()))
                : String.valueOf(ldt.getMonthValue())) + "-" + ldt.getDayOfMonth());
        Map<String, Object> response = new HashMap<String, Object>();

        List<LogAscend> list_logAscend = this.logAscendRepository.findLOCTRFData();

        Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository.findByNama("LOC");

        FileReadWrite frw = new FileReadWrite();
        // // //System.out.println(list_terminal.get().getMerchantId());

        for (LogAscend logAscend : list_logAscend) {
            logAscend.setGeneratedAtMFTS(LocalDateTime.now());
            logAscend.setIsGeneratedMFTS(true);
            this.logAscendRepository.save(logAscend);

        }

        if (list_logAscend.isEmpty()) {
            response.put("rc", 204);
            response.put("status", "Belum ada data terbaru, tidak membuat file baru");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String loctrf = fileReadWriteService.locTRF(list_logAscend, this.channelResponseRepository,
                this.channelRepository,
                list_terminal.get().getMerchantId().toString(), this.terminalMerchantRepository,
                this.mftsResponseRepository);
        // Update isGenerated
        if (loctrf.contains("FAIL")) {
            response.put("rc", 400);
            response.put("status", "Gagal membuat file LOCTRF");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("rc", 200);
        response.put("status", loctrf);
        // response.put("isi", list_logAscend);
        // for (LogAscend logAscend : list_logAscend) {
        // logAscend.setGeneratedAtMFTS(LocalDateTime.now());
        // logAscend.setIsGeneratedMFTS(true);
        // }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/cicilanChannel")
    public ResponseEntity<Map<String, Object>> cariCicilan(@RequestBody InputAsccend input) {
        Map<String, Object> response = new HashMap<String, Object>();
        if (input.getCardnum().isEmpty()) {
            response.put("rc", 400);
            response.put("detail", "cardnum tidak dimasukan");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
        if (input.getChannel().isEmpty()) {
            response.put("rc", 400);
            response.put("detail", "channel tidak dimasukan");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
        List<PlanCode> plancode = this.planCodeRepository.findPlanCodeByKodeTier(input.getChannel());

        String newplancode = "[";
        for (int i = 0; i < plancode.size(); i++) {
            newplancode += "\"" + plancode.get(i).getPlan_code() + "\"";
            if (i != plancode.size() - 1) {
                newplancode += ",";
            }
        }
        newplancode += "]";

        // String uri = "http://127.0.0.1:8082/api/SCNMORDP";
        String uri = "http://" + ipLoc + "/asc/api/SCNMORDP";
        String data = "{\"cardnum\":\"" + input.getCardnum() + "\",\"plancode\":" + newplancode + "}";
        // System.out.println(uri);
        // System.out.println(data);
        try {
            HTTPRequest httpRequest = new HTTPRequest(aesComponent);
            String responseHasil = httpRequest.postRequestBasicAuth(uri, data, "7777777", "7777777");
            // System.out.println("response hasil: " + responseHasil);

            ObjectMapper mappers = new ObjectMapper();
            ModelMapper modelMapper = new ModelMapper();
            JSONObject jsonObjHasil = new JSONObject(responseHasil);
            JSONArray jsonArray = jsonObjHasil.getJSONArray("data");
            List<AsccendResponse> listAsccendResponses = new ArrayList<AsccendResponse>();

            for (int i = 0; i < jsonArray.length(); i++) {
                // //System.out.println(jsonArray.get(i).toString());
                AsccendResponse asccendResponse = mappers.readValue(jsonArray.get(i).toString(), AsccendResponse.class);
                Optional<LogAscend> listLogAscend = this.logAscendRepository
                        .findByAuthCode(asccendResponse.getAuthCode());
                // //System.out.println(listLogAscend.toString());
                List<PlanCode> listPlanCode = new ArrayList<>();
                if (listLogAscend.isPresent()) {
                    Optional<ChannelResponse> listChannelResponse = Optional.empty();
                    // //System.out.println("listLogAscend");
                    listChannelResponse = this.channelResponseRepository
                            .getByReferenceId(listLogAscend.get().getReferenceId());

                    if (listChannelResponse.isPresent()) {
                        // //System.out.println("listChannelResponse");
                        listPlanCode = this.planCodeRepository.findByPlanCode(listChannelResponse.get().getPlanCode());
                    }
                    Double interestRate = 0.00;
                    Long loan;
                    if (listPlanCode.size() > 0) {
                        loan = listChannelResponse.get().getAmount();
                        Double cicilan = hitungCicilan(loan, listPlanCode.get(0).getInterest(),
                                listPlanCode.get(0).getInterest_type(), listPlanCode.get(0).getTenor());
                        if (listPlanCode.get(0).getInterest_channel().equals("F")) {
                            interestRate = hitungInterestRate(cicilan, loan,
                                    listPlanCode.get(0).getInterest(),
                                    listPlanCode.get(0).getTenor());
                        } else {
                            interestRate = listPlanCode.get(0).getInterest();
                        }
                    }

                    BigDecimal interestRate1 = new BigDecimal(interestRate).setScale(2,
                            RoundingMode.HALF_UP);

                    asccendResponse.setInterestRate(interestRate1);

                }

                asccendResponse.setCardNumber(input.getCardnum());
                listAsccendResponses.add(asccendResponse);
            }

            response.put("rc", 200);
            response.put("detail", "Success");
            response.put("data", listAsccendResponses);
            // response.put("data1", jsonResponse);
        } catch (Exception e) {
            // System.out.println(e.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/cobaSequece")
    public String cobaSequence() {
        FileReadWrite frw = new FileReadWrite();
        Integer hasil = frw.findSequence(mftsResponseRepository);
        return hasil.toString();
    }

    @GetMapping("/testRead")
    public String testRead() {
        FileReadWrite fileReadWrite = new FileReadWrite();
        String base64 = "RmxhZzosTiwKQWNjb3VudCBEZWJpdDosMTAyMTAwMDMwMDAwNiwKVG90YWwgdHJhbnNhY3Rpb246LDEsClRvdGFsIHRyYW5zYWN0aW9uIGFtb3VudDosMTAwMDAwMCwKVG90YWwgY2hhcmdlOiwwLApUcmFuc2FjdGlvbiBkYXRlOiwwMzA1MjMsClVzZXIgSUQ6LCwKLCwKU2VydmljZSBjb2RlLFNvdXJjZSBCcmFuY2gsRGF0ZSBERE1NWVksUmVmZiBubyxBbW91bnQsUmVtYXJrIExpbmUgMyxSZW1hcmsgTGluZSA0LFJFTUFSSyBUUlgsQklDIEJBTksgUEVORVJJTUEsTkFNQSBQRU5HSVJJTSxSRUtFTklORyBQRU5HSVJJTSxDSEFSR0UgMSxDSEFSR0UgMixKZW5pcyBuYXNhYmFoIHBlbmVyaW1hLFN0YXR1cyBwZW5kdWR1ayBwZW5lcmltYSxBY2NvdW50IENyZWRpdCxBY2NvdW50IERlYml0LE5BTUEgUEVORVJJTUEgT1ZCLFJFS0VOSU5HIFBFTkVSSU1BIFNLTixOQU1BIFBFTkVSSU1BIFNLTixSRUtFTklORyBQRU5FUklNQSAgUlRHLE5BTUEgUEVORVJJTUEgIFJURyxBbEFNQVQgUEVORVJJTUEsQWxBTUFUIEtPVEEgUEVORVJJTUEsS09ERSBDQUJBTkcgQkFOSyBQRU5FUklNQSxOQU1BIFBFTkVSSU1BIEVXQUxMRVQsUmVmZiBVc2VyLFN0YXR1cyxEZXNjU3RhdHVzLENPTVBBTlkgSURFTlRJRklFUixBZGRpdGlvbmFsMSxBZGRpdGlvbmFsMixBZGRpdGlvbmFsMyxDdXJyZW5jeURlYmV0LEN1cnJlbmN5Q3JlZGl0LFJhdGVEZWJpdCxSYXRlQ3JlZGl0LEFtb3VudENyZWRpdApPVkIsMDAxLDAzMDUyMyxMT0MyMDIzMDUwMzAwMDEwMDAwMSwxMDAwMDAwLExPQU4gT04gQ0FSRCBNLVNNSUxFLGxwbTY1MDY4MzI3NiwsLCwsLCwsLDExMTExMTE0MTExMSwxMDIxMDAwMzAwMDA2LE5hdWZhbFRlc3QsLCwsLCwsLCxscG02NTA2ODMyNzYsRixBY2NvdW50IERlYml0L0NyZWRpdCBOb3QgRm91bmQsbnVsbCwsLCwsLCwsLCwK";
        fileReadWrite.readFilePPMERL(base64, channelResponseRepository);
        return null;
    }

    @GetMapping("/createAES")
    public String generateBodyAES(String csv) {
        LocalDate ld = LocalDate.now();
        String tgl_csv = csv.substring(3, csv.length() - 4);
        // System.out.println(tgl_csv.substring(0, 4));
        String[] ld_arr = ld.toString().split("-");
        String[] line;
        String body_new = "";
        String body_final = "";
        String hasil = "";
        String path = "opt/LOCTRF/"
                + tgl_csv.subSequence(0, 4) + "-" + tgl_csv.subSequence(4, 6) + "-" + tgl_csv.subSequence(6, 8);
        String csvFile = csv;
        String ext = ".csv";
        NewAESComponent aesComponentNew = this.aesComponentRepository.findByNama("MFTS_REQ");
        try {

            // //System.out.println(path);
            CSVReader reader = new CSVReader(new FileReader(path + "/" + csvFile + ext));

            // //System.out.println("read: " + reader.readNext().toString());
            while ((line = reader.readNext()) != null) {
                // process each line of CSV data
                for (int i = 0; i < line.length; i++) {
                    body_new += line[i];
                    if (i != line.length - 1) {
                        body_new += ",";
                    }
                }
                body_new += "\n";
            }
            body_new = body_new.substring(0, body_new.length() - 1);
            reader.close();
        } catch (Exception e) {
            // System.out.println(e.getMessage());
            return "Tidak ada file " + csvFile + ext + " dalam direktori " + path;
        }
        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
        body_new = Base64.getEncoder().encodeToString(body_new.getBytes());
        body_final = "{\"filename\":" + "\"" + csvFile + "\"" + ",\"file\":\"" + body_new + "\"}";
        try {
            hasil = aesEncryptDecrypt.encrypt(body_final, aesComponentNew.getAesKey(), aesComponentNew.getAesIV());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hasil;
    }

    @GetMapping("/crctest")
    public String cobaCRC() {
        MFTS mfts = new MFTS();
        return mfts.crc16(
                "Flag:,N,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,Account Debit:,1234567800,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,Total transaction:,4,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,Total transaction amount:,9471560000,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,Transaction date:,110523,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,User ID:,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,Service code,Source Branch,Date DDMMYY,Reff no,Amount,Remark Line 3,Remark Line 4,REMARK TRX,BIC BANK PENERIMA,NAMA PENGIRIM,REKENING PENGIRIM,CHARGE 1,CHARGE 2,Jenis nasabah penerima,Status penduduk penerima,Account Credit,Account Debit,NAMA PENERIMA OVB,REKENING PENERIMA SKN,NAMA PENERIMA SKN,REKENING PENERIMA  RTG,NAMA PENERIMA  RTG,AlAMAT PENERIMA,AlAMAT KOTA PENERIMA,KODE CABANG BANK PENERIMA,NAMA PENERIMA EWALLET,Reff User,Company Identifier,Additional1,Additional2,Additional3,Currency Debit,Currency Credit,Rate Debit,Rate Credit,Amount CreditOVB,801,10052023,,23678900,Descripksi,MFTS_ENGINE,,,,,,,,,45678912113,1234567800,AMY,,,,,,,,,amitest7985,,,,,,,,,OVB,801,10052023,,23678900,Descripksi,MFTS_ENGINE,,,,,,,,,45678912113,1234567800,AMY,,,,,,,,,amitest7985,,,,,,,,,OVB,801,10052023,,23678900,Descripksi,MFTS_ENGINE,,,,,,,,,45678912113,1234567800,AMY,,,,,,,,,amitest7985,,,,,,,,,OVB,801,10052023,,23678900,Descripksi,MFTS_ENGINE,,,,,,,,,45678912113,1234567800,AMY,,,,,,,,,amitest7985,,,,,,,,,");
    }

    @GetMapping("/sendMFTS")
    public ResponseEntity<Map<String, Object>> kirimNFTS() {
        Map<String, Object> response = new HashMap<String, Object>();
        LocalDate ld = LocalDate.now();
        String[] ld_arr = ld.toString().split("-");
        List<MftsResponse> listNotSent = this.mftsResponseRepository.findFileNotSent();
        JSONArray arrayResponse = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        NewAESComponent aesComponentNew = this.aesComponentRepository.findByNama("MFTS_REQ");
        for (MftsResponse mftsResponses : listNotSent) {
            String csvFile = mftsResponses.getNama_file();
            // System.out.println(csvFile);
            String aes = generateBodyAES(csvFile);
            if (aes.contains("Tidak")) {
                jsonObject.put("rc", 400);
                jsonObject.put("rd", "Connection Problem / File Not Found (?)");
                jsonObject.put("filename", csvFile);
                mftsResponses.setIs_send(false);
                this.mftsResponseRepository.save(mftsResponses);
            } else {
                JSONObject data = new JSONObject();
                data.put("msg", aes);

                JSONObject jsonInput = new JSONObject();
                jsonInput.put("channel_id", aesComponentNew.getAesChannelId());
                jsonInput.put("key_id", aesComponentNew.getAesKeyId());
                jsonInput.put("service_id", aesComponentNew.getAesServiceId());
                jsonInput.put("data", data);
                // try {
                // String[] expdate = input.get("expDate").toString().split("-");
                String jsonInputNew = "{" +
                        "\"channel_id\":\"" + aesComponentNew.getAesChannelId() + "\"," +
                        "\"key_id\": \"" + aesComponentNew.getAesKeyId() + "\"," +
                        "\"service_id\": \"" + aesComponentNew.getAesServiceId() + "\"," +
                        "\"data\":{\n    \"msg\":\"" + aes + "\"}" +
                        "}";
                // //System.out.println(jsonInput.toString());
                // response.put("jsonInput", jsonInput.toString());

                HTTPRequest httpRequest = new HTTPRequest(aesComponent);
                String httpRequestResponse;
                String httpRequestResponseOld;
                try {
                    AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
                    String urlMFTS = this.apiConfigRepository.findIpByNama("MFTS_securitybp");
                    httpRequestResponseOld = httpRequest.postRequest("http://" + urlMFTS,
                            jsonInput.toString());
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> newResponse = mapper.readValue(httpRequestResponseOld, Map.class);
                    String dataResponse = "";
                    MftsResponse mftsResponse = new MftsResponse();
                    Optional<MftsResponse> isi = this.mftsResponseRepository.findByNamaFile(csvFile);
                    if (isi.isPresent()) {
                        mftsResponse = isi.get();
                    }
                    Map<String, Object> newNewResponse = new HashMap<>();
                    try {
                        if (newResponse.get("data") != null) {
                            dataResponse = aesEncryptDecrypt.decrypt(newResponse.get("data").toString(),
                                    aesComponentNew.getAesKey(), aesComponentNew.getAesIV());
                            newNewResponse = mapper.readValue(dataResponse, Map.class);
                            mftsResponse.setIsi_file_send(newResponse.get("data").toString());
                            mftsResponse.setRc_send("200");
                            mftsResponse.setIs_send(true);
                        } else {
                            // //System.out.println("masok else");
                            newNewResponse = newResponse;
                            mftsResponse.setRc_send(newNewResponse.get("rc").toString());
                            mftsResponse.setIs_send(true);

                        }
                    } catch (Exception e) {
                        // //System.out.println("masok catch");
                        dataResponse = newResponse.toString();
                    }

                    mftsResponse.setNama_file(csvFile);
                    mftsResponse.setDate_send(LocalDateTime.now());

                    for (Map.Entry<String, Object> entry : newNewResponse.entrySet()) {
                        response.put(entry.getKey(), entry.getValue());
                        if (entry.getKey().equals("rd")) {
                            mftsResponse.setRd_send(String.valueOf(entry.getValue()));
                        }
                    }
                    // response.put("data", httpRequestResponse);
                    // response.put("data", httpRequestResponseOld);
                    this.mftsResponseRepository.save(mftsResponse);
                    if (!newNewResponse.get("rc").equals("00")) {
                        jsonObject = new JSONObject(response);
                        // return new ResponseEntity<Map<String, Object>>(response,
                        // HttpStatus.SERVICE_UNAVAILABLE);
                    }
                    jsonObject = new JSONObject(response);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response.put("rc", 400);
                    response.put("rd", "Connection Problem (?)");
                    MftsResponse mftsResponse = new MftsResponse();
                    Optional<MftsResponse> isi = this.mftsResponseRepository.findByNamaFile(csvFile);
                    if (isi.isPresent()) {
                        mftsResponse = isi.get();
                    }
                    mftsResponse.setNama_file(csvFile);
                    mftsResponse.setDate_send(LocalDateTime.now());
                    mftsResponse.setRc_send("400");
                    mftsResponse.setRd_send("Connection Problem (?)");
                    mftsResponse.setIs_send(false);
                    this.mftsResponseRepository.save(mftsResponse);
                    // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    jsonObject = new JSONObject(response);
                }
            }
            arrayResponse.put(jsonObject);
            // System.out.println(jsonObject.toString());
        }
        response.put("rc", 200);
        response.put("rd", "Berhasil");
        response.put("data", arrayResponse.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/findMFTS")
    public ResponseEntity<Map<String, Object>> findMFTS(@RequestParam(value = "file", required = false) String file,
            String fileOpt) {
        Map<String, Object> response = new HashMap<String, Object>();
        String url = this.apiConfigRepository.findIpByNama("MFTS_securitybp");
        JSONObject jsonObject = new JSONObject();
        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();
        NewAESComponent aesComponentNew = this.aesComponentRepository.findByNama("MFTS_RES");
        String aesFile = "";
        jsonObject.put("filename", (file != null ? file : fileOpt));

        // //System.out.println(jsonObject.get("filename"));

        JSONObject addMSG = new JSONObject();

        try {
            aesFile = aesEncryptDecrypt.encrypt(jsonObject.toString(), aesComponentNew.getAesKey(),
                    aesComponentNew.getAesIV());
            addMSG.put("msg", aesFile);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject bodyResponse = new JSONObject();
        bodyResponse.put("channel_id", aesComponentNew.getAesChannelId());
        bodyResponse.put("service_id", aesComponentNew.getAesServiceId());
        bodyResponse.put("key_id", aesComponentNew.getAesKeyId());
        bodyResponse.put("data", addMSG);

        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        String bodyResponseSent = "";
        try {
            bodyResponseSent = httpRequest.postRequest("http://" + url,
                    bodyResponse.toString());

            // System.out.println("body sent: " + bodyResponse);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        // Map<String, Object> mapBody = new HashMap<>();
        String dataMFTS = "";
        // System.out.println("body: " + bodyResponseSent);
        // //System.out.println("sent_body: " + bodyResponse.toString());
        String decodeDataMFTS = "";
        try {
            Map<String, Object> mapBody = mapper.readValue(bodyResponseSent, Map.class);
            if (mapBody.get("data") != null) {
                // if (mapBody.get("data").toString().length() > 0) {
                dataMFTS = mapBody.get("data").toString();
                decodeDataMFTS = aesEncryptDecrypt.decrypt(dataMFTS, aesComponentNew.getAesKey(),
                        aesComponentNew.getAesIV());

                // }

            } else {
                decodeDataMFTS = bodyResponseSent;
            }
            // for (Map.Entry<String, Object> datax : mapBody.entrySet()) {
            // response.put(datax.getKey(), datax.getValue());
            // // //System.out.println("KEY: " + datax.getKey() + " VALUE: " +
            // datax.getValue());
            // }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }

        try {
            if (decodeDataMFTS.length() > 0) {
                Map<String, Object> dataOut = mapper.readValue(decodeDataMFTS, Map.class);
                for (Map.Entry<String, Object> datax : dataOut.entrySet()) {
                    response.put(datax.getKey(), datax.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        // for (Map.Entry<String, Object> entry : newNewResponse.entrySet()) {
        if (!response.get("rc").equals("00")) {
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/receiveMFTS")
    public ResponseEntity<Map<String, Object>> receiveNFTS() {
        Map<String, Object> response = new HashMap<>();
        List<MftsResponse> mfts = this.mftsResponseRepository.findMFTSFile();
        List<Map<String, Object>> response_list = new ArrayList<>();
        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        for (MftsResponse mftsResponse : mfts) {
            // //System.out.println(mftsResponse.getNama_file());
            ResponseEntity<Map<String, Object>> mapMFTS = findMFTS(null, mftsResponse.getNama_file());
            // //System.out.println(mapMFTS.getBody());
            // mftsResponse.getRc_receive(mapMFTS)
            // if (mapMFTS.getBody().get("rc").equals("00")) {
            mapMFTS.getBody().put("NamaFile", mftsResponse.getNama_file());
            mftsResponse.setRc_receive(mapMFTS.getBody().get("rc").toString());
            mftsResponse.setRd_receive(mapMFTS.getBody().get("rd").toString());
            mftsResponse.setStatus_receive(
                    mapMFTS.getBody().get("Status") != null ? mapMFTS.getBody().get("Status").toString()
                            : mapMFTS.getBody().get("rd").toString());

            mftsResponse.setIsi_file_receive(
                    mapMFTS.getBody().get("resp_file") != null ? mapMFTS.getBody().get("resp_file").toString() : null);

            mftsResponse.setIs_receive(mapMFTS.getBody().get("rc").toString().equals("00") ? true : false);
            mftsResponse.setDate_receive(LocalDateTime.now());
            this.mftsResponseRepository.save(mftsResponse);
            // } else {

            // if (mapMFTS.getBody().get("resp_file") != null) {
            // String bodyFile = new
            // String(Base64.getDecoder().decode(mapMFTS.getBody().get("resp_file").toString()));
            // Map<String, Object> populateDataResponse = this.streamMftsResponse(bodyFile);

            // }

            // }
            // return mapMFTS;
            // response.put(ipLoc, httpRequest);
            response_list.add(mapMFTS.getBody());
        }
        response.put("data", response_list);
        return new ResponseEntity<>(response, HttpStatus.OK);
        // HTTPRequest httpRequest = new HTTPRequest(aesComponent);
    }

    @GetMapping("/cobaElastic")
    public String cobaElastic() throws Exception {
        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        String url = "http://10.14.21.31:9200/asc-custp*/_search";
        String body = """
                {"query":{"query_string":{"query":"(10501601700009)","fields":["cust-nbr"]}}}
                """;
        String username = "elastic";
        String password = "elkbankmega2022@";
        String hasil = httpRequest.getRequest(url, body, username, password);
        return hasil;
    }

    @GetMapping("/readMFTS")
    public ResponseEntity<Map<String, Object>> readMFTS() {
        List<MftsResponse> mfts = this.mftsResponseRepository.findMFTSFileNotRead();
        // //System.out.println(mfts.isEmpty());
        List<Map> isi_response = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        for (MftsResponse mftsResponse : mfts) {
            // //System.out.println(mftsResponse.getNama_file());
            // //System.out.println(new
            // String(Base64.getDecoder().decode(mftsResponse.getIsi_file_receive())));
            Map<String, Object> response1 = streamMftsResponse(
                    new String(Base64.getDecoder().decode(mftsResponse.getIsi_file_receive())),
                    mftsResponse.getNama_file());
            // //System.out.println(response1);
            isi_response.add(response1);
        }
        response.put("rc", 200);
        response.put("rd", "Berhasil");
        response.put("data", isi_response);
        for (MftsResponse mftsResponse1 : mfts) {
            mftsResponse1.setIs_read(true);
            this.mftsResponseRepository.save(mftsResponse1);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/cekElasticCust")
    public Map<String, Object> cobaCek(@RequestParam String custNum, @RequestParam String fieldOutput) {
        APICustomer apiCustomer = new APICustomer();
        return apiCustomer.dataApi(custNum, fieldOutput);
    }

    // private final JwtToken

    @GetMapping("generateToken")
    public String getMethodName() {

        return new String();
    }

    @GetMapping("/getRemoteAddress")
    public String getRemoteAddress(HttpServletRequest request) {
        // Get remote address from HttpServletRequest
        String remoteAddress = request.getRemoteAddr();

        // Print remote address to console
        System.out.println("Remote Address: " + remoteAddress);

        // Return the remote address as a response
        return "Remote Address: " + remoteAddress;
    }

    @Autowired
    private APICustomer apiCustomer;

    @PostMapping("/cekCustomer")
    public ResponseEntity<Map<String, Object>> cekCustomer(@RequestBody Map<String, Object> input) {

        // APICustomer apiCustomer = new APICustomer();
        logService.info("/cekCustomer req: " + input.toString());
        Boolean eligible = true;
        Map<String, Object> response = new HashMap<String, Object>();

        // pefind
        // CheckPefindo checkPefindo = new CheckPefindo();
        // String cekParamPefindo = checkPefindo.cekParamPefindo(input);
        // if (cekParamPefindo.length() > 0) {
        // response.put("rc", "209");
        // response.put("rd", "Param " + cekParamPefindo + " not found");
        // response.put("cause", "Pefindo");
        // logService.info("/cekCustomer res: " + response.toString());
        // return new ResponseEntity<>(response, HttpStatus.OK);
        // }
        // JSONObject pefindoBody = checkPefindo.validateAndCreate(input);
        // try {
        // Boolean hasilPefindo = checkPefindo.pefindo(pefindoBody, aesComponent);
        // if (!hasilPefindo) {
        // response.put("rc", "209");
        // response.put("rd", "Not Eligible");
        // response.put("cause", "Pefindo");
        // logService.info("/cekCustomer res: " + response.toString());
        // return new ResponseEntity<>(response, HttpStatus.OK);
        // }
        // } catch (Exception e) {
        // response.put("rc", "400");
        // response.put("rd", "Other Reason");
        // response.put("cause", e.getMessage());
        // logService.info("/cekCustomer res: " + response.toString());
        // return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        // }

        if (input.get("kodeChannel") == null) {
            response.put("rc", 400);
            response.put("rd", "tidak ada kodeChannel");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        List<CustomerParam> list = this.customerParamRepository.findByKodeChannel(input.get("kodeChannel").toString());

        for (CustomerParam customerParam : list) {
            if (input.get(customerParam.getNama_param()) == null) {
                response.put("rc", 400);
                response.put("rd", customerParam.getNama_param() + " tidak dimasukan");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // //System.out.println("nama param: " + customerParam.getNama_param() + "
            // operator: "
            // + customerParam.getOperator().getNama_operator());

            Long tempLong = Long.valueOf(0);

            try {
                if (customerParam.getColumn_name() != null
                        && customerParam.getColumn_name().equals("cust-maint-date")) {
                    Map<String, Object> isiCustData = apiCustomer.dataApi(input.get("custNumber").toString(),
                            "cust-maint-date");
                    if (isiCustData.containsKey("error")) {
                        response.put("rc", 400);
                        response.put("rd", isiCustData.get("error"));
                        logService.info("/cekCustomer error: " + response.toString());
                        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                    }
                    String[] arrDate = isiCustData.get("cust-maint-date").toString().split("-");
                    LocalDate ldt1 = LocalDate.now();
                    LocalDate ldt2 = LocalDate.of(Integer.parseInt(arrDate[0]), Integer.parseInt(arrDate[1]),
                            Integer.parseInt(arrDate[2]));
                    Long months_diff = ChronoUnit.MONTHS.between(ldt2, ldt1);
                    tempLong = months_diff;
                }
            } catch (Exception e) {
                tempLong = Long.valueOf("999999");
            }

            if (customerParam.getOperator().getNama_operator().equals(">")) {
                if (customerParam.getIs_search() == null) {
                    tempLong = Long.valueOf(input.get(customerParam.getNama_param()).toString());
                }
                if (tempLong > Long.valueOf(customerParam.getValue()) == false) {
                    response.put("cause",
                            customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                    ? customerParam.getColumn_name()
                                    : ""));
                    eligible = false;
                    break;
                }
            }

            if (customerParam.getOperator().getNama_operator().equals(">=")) {
                if (customerParam.getIs_search() == null) {
                    tempLong = Long.valueOf(input.get(customerParam.getNama_param()).toString());
                }
                if (tempLong >= Long.valueOf(customerParam.getValue()) == false) {
                    response.put("cause",
                            customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                    ? customerParam.getColumn_name()
                                    : ""));
                    eligible = false;
                    break;
                }
            }

            if (customerParam.getOperator().getNama_operator().equals("<")) {
                if (customerParam.getIs_search() == null) {
                    tempLong = Long.valueOf(input.get(customerParam.getNama_param()).toString());
                }
                if (tempLong < Long.valueOf(customerParam.getValue()) == false) {
                    response.put("cause",
                            customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                    ? customerParam.getColumn_name()
                                    : ""));
                    eligible = false;
                    break;
                }
            }

            if (customerParam.getOperator().getNama_operator().equals("<=")) {
                if (customerParam.getIs_search() == null) {
                    tempLong = Long.valueOf(input.get(customerParam.getNama_param()).toString());
                }
                if (tempLong <= Long.valueOf(customerParam.getValue()) == false) {
                    response.put("cause",
                            customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                    ? customerParam.getColumn_name()
                                    : ""));
                    eligible = false;
                    break;
                }
            }

            if (customerParam.getOperator().getNama_operator().equals("=")) {
                if (input.get(customerParam.getNama_param()).toString().equals(customerParam.getValue()) == false) {
                    response.put("cause",
                            customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                    ? customerParam.getColumn_name()
                                    : ""));
                    eligible = false;
                    break;
                }
            }
            if (customerParam.getOperator().getNama_operator().equals("<>")) {
                if (!input.get(customerParam.getNama_param()).toString().equals(customerParam.getValue()) == false) {
                    response.put("cause",
                            customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                    ? customerParam.getColumn_name()
                                    : ""));
                    eligible = false;
                    break;
                }
            }
            if (customerParam.getOperator().getNama_operator().equals("IN")) {
                // //System.out.println("masok");
                String[] arrValue = customerParam.getValue().split(",");
                String[] inputValue = {};
                inputValue = input.get(customerParam.getNama_param()).toString().split(",");

                for (String inputVal : inputValue) {
                    if (customerParam.getOr_and().toUpperCase().equals("AND")) {
                        if (Arrays.asList(arrValue).contains(inputVal) == false) {
                            response.put("cause",
                                    customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                            ? customerParam.getColumn_name()
                                            : ""));
                            eligible = false;
                            break;
                        }
                    } else {
                        if (Arrays.asList(arrValue).contains(inputVal)) {
                            eligible = true;
                            response.clear();
                            break;
                        } else {
                            // //System.out.println("masok");
                            response.put("cause",
                                    customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                            ? customerParam.getColumn_name()
                                            : ""));
                            eligible = false;
                        }
                    }

                    // //System.out.println("eligible: " + eligible + " InputVal: " + inputVal);
                }
                if (eligible == false) {
                    break;
                }

            }
            if (customerParam.getOperator().getNama_operator().equals("NOT IN")) {
                String[] arrValue = customerParam.getValue().split(",");
                String[] inputValue = {};
                inputValue = input.get(customerParam.getNama_param()).toString().split(",");

                for (String inputVal : inputValue) {
                    if (customerParam.getOr_and().toUpperCase().equals("AND")) {
                        if (!Arrays.asList(arrValue).contains(inputVal) == false) {
                            response.put("cause",
                                    customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                            ? customerParam.getColumn_name()
                                            : ""));
                            eligible = false;
                            break;
                        }
                    } else {
                        if (!Arrays.asList(arrValue).contains(inputVal)) {

                            response.clear();
                            eligible = true;
                            break;
                        } else {
                            response.put("cause",
                                    customerParam.getNama_param() + " " + (customerParam.getColumn_name() != null
                                            ? customerParam.getColumn_name()
                                            : ""));
                            eligible = false;

                        }
                    }
                    // //System.out.println("eligible: " + eligible + " InputVal: " + inputVal);

                }
                if (eligible == false) {
                    // //System.out.println("/cekCustomer error: " + customerParam.getNama_param());
                    // logService.info("/cekCustomer error: " + customerParam.getNama_param());
                    break;
                }

            }
            // if(eligible == fa)
        }

        if (eligible) {
            response.put("rc", "200");
            response.put("rd", "Eligible");
            logService.info("/cekCustomer res: " + response.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("rc", "209");
            response.put("rd", "Not Eligible");
            logService.info("/cekCustomer res: " + response.toString());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    public Map<String, Object> streamMftsResponse(String fileStr, String namafile) {
        // //System.out.println(namafile);
        Map<String, Object> response = new HashMap<>();

        Scanner scanner = new Scanner(fileStr);
        List<String> hasil = new ArrayList<>();

        JSONArray dataMaps = new JSONArray();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            hasil.add(line);
        }

        if (hasil.size() < 9) {
            response.put("rc", 400);
            response.put("rd", "Tidak ada transaksi, File body kosong");
            response.put("filename", namafile);
            return response;
        }

        for (int i = 9; i < hasil.size(); i++) {
            JSONObject tempData = new JSONObject();
            tempData.put(
                    "refUser", hasil.get(i).split(",")[26]);
            tempData.put(
                    "status", hasil.get(i).split(",")[27]);
            tempData.put(
                    "desc", hasil.get(i).split(",")[28]);
            dataMaps.put(tempData);
            // //System.out.println(hasil.get(i).split(",")[26] + " : " +
            // hasil.get(i).split(",")[27] + " : "
            // + hasil.get(i).split(",")[28]);
            // //System.out.println(dataMaps.get(i - 9).toString());
            // //System.out.println(dataMaps.get(0).toString());
        }

        String urlMFTS = this.apiConfigRepository.findIpByNama("MFTS_securitybp");
        NewAESComponent aesComponentNew = this.aesComponentRepository.findByNama("FIREBASE");

        for (int i = 0; i < dataMaps.length(); i++) {
            Optional<LogAscend> transactionDatas = this.logAscendRepository
                    .findByRefId(dataMaps.getJSONObject(i).getString("refUser"));
            FirebaseService firebaseService = new FirebaseService(channelResponseRepository, transactionDatas.get(),
                    firebaseConfigRepository, aesComponentNew, urlMFTS, aesComponent);
            // //System.out.println(dataMaps.getJSONObject(i).toString());
            if (transactionDatas.isEmpty() == false) {
                String status_transfer = "";
                try {
                    status_transfer = dataMaps.getJSONObject(i).getString("status");
                } catch (Exception e) {
                    status_transfer = "null";
                }
                LogAscend tempTransactionData = transactionDatas.get();
                tempTransactionData.setStatusTransfer(status_transfer);
                Boolean status_firebase = false;
                if (status_transfer.equals("S")) {
                    status_firebase = firebaseService.sendToFireBase("S");
                } else if (status_transfer.equals("null")) {

                } else {
                    status_firebase = firebaseService.sendToFireBase("F");
                }
                // tempTransactionData.set
                // tempTransactionData.set(dataMaps.getJSONObject(i).get("status"));
                this.logAscendRepository.save(tempTransactionData);
                response.put("rc", 200);
                response.put("rd", "Berhasil Update Status");
                response.put("filename", namafile);
            }
        }
        return response;
    }
}
