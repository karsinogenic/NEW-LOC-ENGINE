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
import java.util.TreeMap;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.demo_loc_engine.demo.Models.AsccendResponse;
import com.demo_loc_engine.demo.Models.Channel;
import com.demo_loc_engine.demo.Models.ChannelResponse;
import com.demo_loc_engine.demo.Models.ChannelResponseInput;
import com.demo_loc_engine.demo.Models.CustomerParam;
import com.demo_loc_engine.demo.Models.HolidayDate;
import com.demo_loc_engine.demo.Models.IncomingRequestBiFast;
import com.demo_loc_engine.demo.Models.Kriteria;
import com.demo_loc_engine.demo.Models.LogAscend;
import com.demo_loc_engine.demo.Models.LogAscendResponse;
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
import com.demo_loc_engine.demo.Repositories.HolidayRepository;
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
import com.demo_loc_engine.demo.Services.RunnableVoidTrx;
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

    @Autowired
    public HolidayRepository holidayRepository;

    // @Autowired
    // public TestAscoreRepository testAscoreRepository;

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = factory.getValidator();
    // public static String ipLoc = "10.14.21.65:8084";

    @Autowired
    public LogService logService;

    @Value("${use.dummy}")
    public Boolean useDummy;

    // @BeforeClass
    // public static void setUpValidator() {
    // ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    // validator = factory.getValidator();
    // }

    @GetMapping("/voidtrx")
    public ResponseEntity<Map> voidTrx(@RequestParam(required = false) String refid) throws InterruptedException {
        Map hasil = new HashMap<>();
        List<LogAscend> la = this.logAscendRepository.findByStatusTransferAndAuth_noNotNull("F");
        if (refid != null) {
            Optional<LogAscend> la_opt = this.logAscendRepository.findByRefId2(refid);
            la = new ArrayList<>();
            if (!la_opt.isPresent()) {
                hasil.put("rc", "LOC-98");
                hasil.put("rd", "cannot find the given refid");
                return new ResponseEntity<Map>(hasil, null, 200);
            }
            if (la_opt.get().getIsvoided()) {
                hasil.put("rc", "LOC-97");
                hasil.put("rd", "the given refid already been voided");
                return new ResponseEntity<Map>(hasil, null, 200);
            }
            la.add(la_opt.get());
        }
        List<Thread> ltreadh = new ArrayList<>();
        List<RunnableVoidTrx> runnableVoidTrxs = new ArrayList<>();
        LogService logService = new LogService();
        NewAESComponent newaes = this.aesComponentRepository.findByNama("VOID");
        if (newaes == null) {
            hasil.put("rc", "LOC-99");
            hasil.put("rd", "cannot find encrypt component named 'VOID' ");
            return new ResponseEntity<Map>(hasil, null, 200);
        }
        logService.info("Start void batch with total of " + la.size() + (la.size() > 1 ? " datas"
                : " data") + "\n" + "=".repeat(150));
        int th_num = 0;
        for (LogAscend lasc : la) {
            RunnableVoidTrx rx = new RunnableVoidTrx(lasc, newaes, channelResponseRepository,
                    terminalMerchantRepository, th_num, aesComponent, apiConfigRepository);
            Thread thread = new Thread(rx);
            ltreadh.add(thread);
            runnableVoidTrxs.add(rx);
            thread.start();
            th_num++;
        }

        for (Thread thread1 : ltreadh) {
            thread1.join();
        }

        JSONArray joinhasil = new JSONArray();
        List<String> refid_list = new ArrayList<>();
        List<String> rc_list = new ArrayList<>();
        for (RunnableVoidTrx checkThreads : runnableVoidTrxs) {
            if(checkThreads.hasil().isNull("data")){
                logService.info("no data :"+checkThreads.hasil());
            }else{
                joinhasil.put(checkThreads.hasil().getJSONObject("data"));
                if (!checkThreads.hasil().getJSONObject("data").isNull("refid")) {
                    refid_list.add(checkThreads.hasil().getJSONObject("data").getString("refid"));
                }
                rc_list.add(checkThreads.hasil().getString("rc"));
            }
        }
        List<LogAscend> ls = this.logAscendRepository.findByListRefId(refid_list);
        for (int i = 0; i < ls.size(); i++) {
            // System.out.println("=".repeat(20) + "\n" + ls.get(i).getReferenceId() + " ==
            // " + refid_list.get(i));
            ls.get(i).setIsvoided(rc_list.get(i).equals("00") ? true : false);
        }
        this.logAscendRepository.saveAll(ls);

        if(refid!=null){
            if(joinhasil.toList().isEmpty()){
                hasil.put("rc", "99");
                hasil.put("rd", "Fail To Void");
            }else{
                hasil.put("rc", "00");
                hasil.put("rd", "OK");
            }
        }else{
            hasil.put("rc", "00");
            hasil.put("rd", "OK");
            hasil.put("data", joinhasil.toList());
        }
        logService.info("Finished void batch with total of " + la.size() + (la.size() > 1 ? " datas"
                : " data") + joinhasil.toList() + "\n" + "=".repeat(150));

        return new ResponseEntity<>(hasil, null, 200);
    }

    public JSONObject dummyVoid() {
        return new JSONObject("""
                {
                    "accountNum" : "4714390010000010",
                    "amount" : "100000",
                    "expirationDate" : "2511",
                    "posCondCode" : "08",
                    "reffno" : "001481000400",
                    "terminalID" : "30007319",
                    "merchantID" : "042600000006756",
                    "invoiceNum" : "000085"
                  }
                    """);
    }

    @GetMapping("/status")
    public ResponseEntity<Map> status(@RequestParam String refid) {
        Map hasil = new HashMap<>();
        Optional<LogAscend> isi = logAscendRepository.findByRefId(refid);
        if (!isi.isPresent()) {
            hasil.put("rc", "EX-69");
            hasil.put("rd", "Cannot find data of the given refid");
        }
        if (!isi.get().getStatusTransfer().isEmpty()) {
            switch (isi.get().getStatusTransfer()) {
                case "F":

                    break;
                case "S":

                    break;

                default:
                    break;
            }
        }
        return new ResponseEntity<Map>(hasil, null, 200);
    }

    public JSONObject checkBiFast(String refid) throws Exception {
        HTTPRequest req = new HTTPRequest(aesComponent);
        String hasilreq = req.getRequestParamNew(aesComponent.getBifastURL() + "/api/status?refid=" + refid, null, null,
                null);
        return new JSONObject(hasilreq);
    }

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
            // HttpStatus.OK);
            return new ResponseEntity<>(map, HttpStatus.OK);
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
            return new ResponseEntity<>(map, HttpStatus.OK);
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
            // HttpStatus.OK);
            return new ResponseEntity<>(map, HttpStatus.OK);
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
            return new ResponseEntity<>(map, HttpStatus.OK);
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
                // HttpStatus.OK);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            this.tierRepository.save(input);
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
        } catch (Exception e) {
            // //System.out.println(e.toString());
            map.put("status", 400);
            map.put("detail", e.getCause().getCause().getLocalizedMessage());
            return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
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
            // HttpStatus.OK);
            return new ResponseEntity<>(map, HttpStatus.OK);
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
            return new ResponseEntity<>(map, HttpStatus.OK);
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
            // HttpStatus.OK);
            return new ResponseEntity<>(map, HttpStatus.OK);
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
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/eligible")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cekEligible(@RequestBody Map<String, Object> input,
            HttpServletRequest req)
            throws InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> mapList = new ArrayList<>();

        JSONObject jsonObject1 = new JSONObject(input);
        // logService.info("/eligible req: " + jsonObject1.toString());
        logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Request", req.getLocalAddr(),
                req.getRemoteAddr(), "LOC Engine", "eligible", "cek account", null, null, jsonObject1);

        Map pefindo = pefindoSlikList(input).getBody();
        if (!pefindo.get("rc").toString().equals("00")) {
            logService.info2("", String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "eligible", "cek account", pefindo.get("rc").toString(),
                    null,
                    new JSONObject(pefindo));
            return new ResponseEntity<Map<String, Object>>(pefindo, null, 200);
        }

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

        response.put("data", finalList);

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

        logService.info2("", String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                req.getRemoteAddr(), "LOC Engine", "eligible", "cek account", null,
                null,
                new JSONObject(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Map> pefindoSlikList(Map<String, Object> input) {
        Map hasil = new HashMap<>();
        if (!input.containsKey("pefindo_data")) {
            hasil.put("rc", "00");
            hasil.put("rd", "Eligible Without PefindoData");
            return new ResponseEntity<>(hasil, null, 200);
        }
        // System.out.println("return");

        JSONObject pefindoData = new JSONObject(input);
        try {
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
            String hasilHttp = httpRequest.postRequest(aesComponent.getPefindoUrl(),
                    pefindoData.getJSONObject("pefindo_data").toString());
            hasilJsonObject = new JSONObject(hasilHttp);
            if (hasilJsonObject.getString("status").toLowerCase().contains("not")) {
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

            Map<String, Object> input, HttpServletRequest req) {
        // logService.info("/cek req: " + input.toString());
        logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Request", req.getLocalAddr(),
                req.getRemoteAddr(), "LOC Engine", "cek", "cek card", null, null, new JSONObject(input));

        Map response = new HashMap();
        List<String> kode_hasil = new ArrayList<String>();
        if (input.get("channel") == null) {
            response.put("status", "channel tidak dimasukan");

            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "cek", "cek card", null,
                    null, new JSONObject(response));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (input.get("loan") == null) {
            response.put("status", "loan tidak dimasukan");

            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "cek", "cek card", null,
                    null, new JSONObject(response));
            return new ResponseEntity<>(response, HttpStatus.OK);
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
                    logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                            req.getRemoteAddr(), "LOC Engine", "cek", "cek card", null,
                            null, new JSONObject(response));

                    return new ResponseEntity<>(response, HttpStatus.OK);
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
                    logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                            req.getRemoteAddr(), "LOC Engine", "cek", "cek card", null,
                            null, new JSONObject(response));
                    return new ResponseEntity<>(response, HttpStatus.OK);
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

        logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                req.getRemoteAddr(), "LOC Engine", "cek", "cek card", null,
                null, new JSONObject(response));
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

    // @ResponseStatus(HttpStatus.OK)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Map> unexpected(MethodArgumentNotValidException ex) {
    // Map hasil = new HashMap<>();
    // String errorMessage = ex.getAllErrors().get(0).getDefaultMessage();

    // hasil.put("RC", "EX-00");
    // hasil.put("RD", errorMessage);
    // return new ResponseEntity<Map>(hasil, null, 200);
    // }

    public String sknCheck(ChannelResponseInput input){
        if(input.getAccName()==null && !input.getBic().equals("MEGAIDJA")){
            return "accName null";
        }else if(input.getAccNumber()==null  && !input.getBic().equals("MEGAIDJA")){
            return "accNumber null";
        }else if(input.getAccName().isBlank() && !input.getBic().equals("MEGAIDJA")){
            return "accName blank";
        }else if(input.getAccNumber().isBlank() && !input.getBic().equals("MEGAIDJA")){
            return "accNumber blank";
        }else{
            return "OK";
        }

    }

    @PostMapping(value = "/getChannelResponse")
    public ResponseEntity<Map<String, Object>> getChannelResponse(@Valid @RequestBody ChannelResponseInput inputnew,
            HttpServletRequest req)
            throws Exception {
        Map<String, Object> response = new HashMap();
        response.put("rc_trf", null);
        response.put("rd_trf", null);
        response.put("data_trf", null);
        if(inputnew.getBic() == null){
            inputnew.setBic("MEGAIDJA");
        }

        logService.info("========================CH Response========================");

        logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Request", req.getLocalAddr(),
                req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend", null,
                null, new JSONObject(inputnew));
        String skncek = sknCheck(inputnew);
        if(!skncek.equals("OK")){
            response.put("rc", 400);
                response.put("rd", "error");
                response.put("info", skncek);
                return new ResponseEntity<>(new TreeMap<>(response), HttpStatus.OK);
        }

        ObjectMapper mapper = new ObjectMapper();

        List<PlanCode> lists_plancode = this.planCodeRepository.findByPlanCode(inputnew.getPlanCode());
        if (lists_plancode.size() == 0) {
            response.put("rc", 400);
            response.put("detail", "error");
            response.put("info", "plan code not exist");
            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend",
                    response.get("rc").toString(),
                    null, new JSONObject(response));
                    logService.info("========================CH Response========================");

            return new ResponseEntity<>(new TreeMap<>(response), HttpStatus.OK);
        } else {
            boolean flag = false;
            for (PlanCode planCode : lists_plancode) {
                flag = flag || planCode.getKode_tier().equals(inputnew.getTierCode());
            }
            if (!flag) {
                response.put("rc", 400);
                response.put("detail", "error");
                response.put("info", "tier code not exist");
                logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                        req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend",
                        response.get("rc").toString(),
                        null, new JSONObject(response));
                        logService.info("========================CH Response========================");

                return new ResponseEntity<>(new TreeMap<>(response), HttpStatus.OK);
            }
        }

        // //System.out.println("test : " + input.get("createdAt"));
        mapper.registerModule(new JavaTimeModule());
        ChannelResponse responseChannel = mapper.convertValue(inputnew,
                ChannelResponse.class);
        responseChannel
                .setBic(responseChannel.getBic() == null ? aesComponent.getBifastBic() : responseChannel.getBic());
        responseChannel.setCreatedAt(LocalDateTime.now());

        List<Tier> listT = this.tierRepository.findByKode_tier(inputnew.getTierCode());
        if(listT.isEmpty()){
            response.put("rc", "LOC-90");
            response.put("detail", "tier code tidak ditemukan");
            return new ResponseEntity<Map<String,Object>>(response, null,200);
        }
        // Optional<Channel> newch =this.channelRepository.findByKodeChannel(listT.get(0).getKode_channel());
        responseChannel.setKodeChannel(listT.get(0).getKode_channel());
        // if(list.size()==0){}

        Optional<ChannelResponse> oldchres = this.channelResponseRepository.getByReferenceId(responseChannel.getReferenceId());
        Optional<LogAscend> logres = this.logAscendRepository.findByRefId(responseChannel.getReferenceId());
        Long logid = null;
        if(!logres.isPresent()&&oldchres.isPresent()){
            System.out.println("tiban response ch");
            responseChannel.setId(oldchres.get().getId());
        }
        if(logres.isPresent() && oldchres.isPresent()){
            if(!logres.get().getRc().equals("00")){
                System.out.println("tiban response ch");

                responseChannel.setId(oldchres.get().getId());
                logid = logres.get().getId();
            }else{
                response.put("rc", "01");
                response.put("detail", "Grab ascend sebelumnya telah berhasil");
                logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend",
                    response.get("rc").toString(),
                    null, new JSONObject(response));
                    logService.info("========================CH Response========================");

                return new ResponseEntity<Map<String,Object>>(response, null, 200);
            }
        }
        // responseChannel
        try {
            channelResponseRepository.save(responseChannel);
        } catch (Exception e) {
            // SqlExceptionHelper sqlEx;
            response.put("rc", 400);
            response.put("detail", "error");
            response.put("info", e.getCause().getCause().getLocalizedMessage());
            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend",
                    response.get("rc").toString(),
                    null, new JSONObject(response));
                    logService.info("========================CH Response========================");

            return new ResponseEntity<>(new TreeMap<>(response), HttpStatus.OK);
        }
        // ke Ascend
        Map input = mapper.convertValue(inputnew, Map.class);

        Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository
                .findByNama(input.get("terminalMerchant").toString());

        // //System.out.println(list_terminal.get().getPoscon());
        String authnox = null;
        try {
            String[] expdate = input.get("expDate").toString().split("-");
            String jsonInput = "{" +
                    "\"referenceId\":\"" + input.get("referenceId") + "\"," +
                    "\"cardNo\": \"" + input.get("cardNo") + "\"," +
                    "\"amount\": \"" + input.get("amount") + "00" + "\"," +
                    "\"bic\": \"" + responseChannel.getBic() + "\"," +
                    "\"expirationDate\": \"" + input.get("expDate").toString() + "\"," +
                    "\"posCondCode\": \"" + list_terminal.get().getPoscon() + "\"," +
                    "\"terminalID\": \"" + list_terminal.get().getTerminalId() + "\"," +
                    "\"merchantID\": \"" + list_terminal.get().getMerchantId() + "\"" +
                    "}";
            JSONObject newJsonObject = new JSONObject(jsonInput);

            Map<String, Object> hasil_map = this.toAscendNew(null,
                    newJsonObject.toMap()).getBody();
            // System.out.println("hasil ascend new: " + new
            // JSONObject(hasil_map).toString());
            if (hasil_map.get("status").toString().equals("200")) {
                response.put("rc", 200);
                response.put("rc_grab", "00");
                response.put("rd_grab", "Berhasil kirim ke Ascend");
                response.put("authno", hasil_map.get("authno"));
                response.put("data_grab", hasil_map);

            } else {
                response.put("rc", 400);
                response.put("rc_grab", "ASC-" + hasil_map.get("rc"));
                response.put("rd_grab", hasil_map.get("rd"));
                response.put("rc_trf", null);
                response.put("rd_trf", null);
                response.put("data_trf", null);
                response.put("data_grab", hasil_map);
                // response = hasil_map;
                logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                        req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend",
                        response.get("rc").toString(),
                        null, new JSONObject(response));
                        logService.info("========================CH Response========================");

                return new ResponseEntity<>(new TreeMap<>(response), HttpStatus.OK);
            }
        } catch (Exception e) {
            response.put("rc", 400);
            response.put("rc_grab", "ASC-99");
            response.put("rd_grab", "Gagal ke Ascend");
            response.put("info", e);
            // e.printStackTrace();
            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend",
                    response.get("rc").toString(),
                    null, new JSONObject(response));
            logService.info("========================CH Response========================");

            return new ResponseEntity<>(new TreeMap<>(response), HttpStatus.OK);
        }
        System.out.println("curr res: " + response.toString());

        // do BIFAST
        if (!responseChannel.getBic().equals(aesComponent.getBifastBic())&&aesComponent.getUseBifast()) {
            JSONObject outbifast = this.doBiFast(responseChannel);
            System.out.println(outbifast);
            // response.put("data", outbifast.toMap());
            // if (outbifast.getString("rc").equals("00")) {
            // try {
            response.put("rc_trf", outbifast.getString("rc"));
            response.put("rd_trf", outbifast.getString("rd"));
            response.put("data_trf",
                    !outbifast.isNull("data") ? outbifast.getJSONObject("data").toMap() : null);
            // } catch (Exception e) {get
            // response.put("rc_trf", null);
            // response.put("rd_trf", null);
            // response.put("data_trx", null);

            // // TODO: handle exception
            // }

            // } else {
            // response.put("rc_trf", outbifast.getString("rc"));
            // }
            // System.out.println(response.toString());
            Optional<LogAscend> repost = this.logAscendRepository
                    .findByAuthCode(response.get("authno").toString());
            Optional<ChannelResponse> repost_cr = this.channelResponseRepository
                    .getByReferenceId(repost.get().getReferenceId());
            // if(response.get("rc_trf").toString().equals("BIF-00")){
            // Log
            // }else
            if (outbifast.isNull("data")) {
                try {
                    repost.get().setStatusTransfer("F");
                    repost_cr.get().setStatusTransfer("F");
                } catch (Exception e) {
                    repost.get().setStatusTransfer("F");
                    repost_cr.get().setStatusTransfer("F");
                }
                
            } else if (outbifast.getJSONObject("data").getString("trx_status").equals("ACTC")) {
                repost.get().setStatusTransfer("S");
                repost_cr.get().setStatusTransfer("S");
            } else {
                repost.get().setStatusTransfer("F");
                repost_cr.get().setStatusTransfer("F");
            }
            this.logAscendRepository.save(repost.get());
            this.channelResponseRepository.save(repost_cr.get());

        }

        // // response.put("detail", "Berhasil menyimpan data");
        response = new TreeMap<>(response);
        logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                req.getRemoteAddr(), "LOC Engine", "getChannelResponse", "Grab Ascend", response.get("rc").toString(),
                null, new JSONObject(response));
        logService.info("========================CH Response========================");

        return new ResponseEntity<>(response, HttpStatus.OK);

        // return null;
    }

    public JSONObject doBiFast(ChannelResponse responseChannel) {
        IncomingRequestBiFast inputdata = new IncomingRequestBiFast();
        inputdata.setAccnum(responseChannel.getAccNumber());
        inputdata.setAmount(Double.valueOf(responseChannel.getAmount().toString()));
        inputdata.setBeneficiary_bic(responseChannel.getBic());
        inputdata.setChName("TELE1");
        inputdata.setRefId(responseChannel.getReferenceId());
        AESEncryptDecrypt aesEncryptDecrypt = new AESEncryptDecrypt();

        ObjectMapper objectMapper = new ObjectMapper();
        Map inputmap = objectMapper.convertValue(inputdata, Map.class);

        JSONObject inputjson = new JSONObject(inputmap);
        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        // System.out.println("input: " + inputjson.toString());
        try {
            String reqbifast = httpRequest.postRequestBasicAuth(aesComponent.getBifastURL() + "/api/transfer",
                    inputjson.toString(), "loc",
                    aesEncryptDecrypt.encrypt("MEG@202404NAUFAL", "49UffL7GyUKz5gK2",
                            "r22sQb8ygfDyY9Gu"));
            System.out.println("bifast out: " + reqbifast);
            JSONObject outbifast = new JSONObject(reqbifast);
            return outbifast;
        } catch (Exception e) {
            JSONObject outbifast = new JSONObject("""
                {
                    "rc":"BIF-45",
                    "rd":"Something wrong with bifast engine"
                }
                    """);
            return outbifast;
        }
    }

    public ResponseEntity<Map<String, Object>> hitAscend(String dataJson) {
        return null;
    }

    public List<String> findDifference(List<String> first, List<String> second) {
        List<String> diff = new ArrayList<>(first);
        diff.removeAll(second);
        return diff;
    }

    // public Map<String, Object> cekInputChannelResponse(Map<String, Object>
    // inputs) {
    // Map<String, Object> response = new HashMap();
    // String[] param = { "referenceId", "cardNo", "amount", "planCode", "expDate",
    // "tierCode", "accName",
    // "accNumber", "terminalMerchant", "gcn", "mobileNumber", "bic" };
    // String[] param_opsional = { "additionalData" };
    // List<String> compareParam = new ArrayList<String>();
    // List<String> newParam = new ArrayList<String>(Arrays.asList(param));

    // List<String> allTier = this.tierRepository.allTier();
    // List<String> allPlanCode = this.planCodeRepository.allPlanCode();

    // // //System.out.println(allTier.toString());
    // String tierKode = "";

    // for (Map.Entry<String, Object> entry : inputs.entrySet()) {
    // if (Arrays.asList(param).contains(entry.getKey())) {
    // compareParam.add(entry.getKey());
    // response.put("rc", 200);
    // } else {
    // response.put("rc", 400);
    // response.put("detail", "Nama param " + entry.getKey() + " tidak dikenal");
    // response.put("info", "Gunakan nama param sebagai berikut " +
    // Arrays.toString(param));
    // return response;
    // }

    // if (entry.getKey().equals("tierCode")) {
    // // //System.out.println(entry.getValue());
    // // //System.out.println(entry.getValue());
    // if (allTier.contains(entry.getValue()) == false) {
    // response.put("rc", 400);
    // response.put("detail", "Value dari '" + entry.getKey() + "' tidak ditemukan
    // di database");
    // return response;
    // } else {
    // tierKode = entry.getValue().toString();
    // }

    // }

    // if (entry.getKey().equals("bankName")) {
    // try {
    // if (!inputs.get("bankName").toString().toLowerCase().equals("bank mega")) {
    // response.put("rc", 400);
    // response.put("detail", "accName harus ada isi");
    // return response;
    // }
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    // }

    // try {
    // List<String> listPlanCode =
    // this.planCodeRepository.findPlanCodeNameByKodeTier(tierKode);
    // // //System.out.println(listPlanCode.toString());
    // //
    // //System.out.println(this.planCodeRepository.findByKodeTier(tierKode).get(0).getPlan_code());
    // if (!listPlanCode.contains(inputs.get("planCode").toString())) {
    // response.put("rc", 400);
    // response.put("detail", "Value dari 'planCode' tidak ditemukan di database");
    // return response;
    // }
    // } catch (Exception e) {
    // response.put("rc", 400);
    // response.put("detail", "Value dari 'planCode' tidak ditemukan di database");
    // return response;
    // }

    // // List<String> hasilParam = findDifference(newParam, compareParam);
    // // // //System.out.println(hasilParam);
    // // if (hasilParam.size() > 0 && hasilParam.contains(param_opsional) == false)
    // {
    // // response.put("rc", 400);
    // // response.put("detail", "Nama param '" + hasilParam.get(0) + "' tidak
    // // dimasukan");
    // // }

    // // }
    // Date date = new Date();
    // LocalDateTime ldt = LocalDateTime.now();
    // if (inputs.get("tierCode") != null) {
    // String kode_channnel = inputs.get("tierCode").toString().split("-")[0];
    // inputs.put("kodeChannel", kode_channnel);
    // }
    // inputs.put("createdAt", ldt);
    // return response;
    // }

    // // @GetMapping(value = "/tes")
    // public String tes() {
    // Optional<ChannelResponse> cr_list =
    // this.channelResponseRepository.getByReferenceId("asdasd1156");
    // FileReadWrite fileReadWrite = new FileReadWrite();
    // JSONObject jsonObject = fileReadWrite.fireBase(cr_list.get());
    // // //System.out.println(cr_list.get().getReferenceId());
    // return jsonObject.toString();
    // }

    @Deprecated
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
            return new ResponseEntity<>(newMap, HttpStatus.OK);
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
            return new ResponseEntity<>(newMap, HttpStatus.OK);

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

                return new ResponseEntity<>(newMap, HttpStatus.OK);
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
            // newMap.put("error", e.toString());
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
        String bic;
        List<LogToAscend> lta;
        Optional<LogAscend> la;
        // System.out.println("masuk ascend");
        
        try {
            refId = input.get("referenceId").toString();
            lta = this.logToAscendRepository.findByRefId(refId);
            la = this.logAscendRepository.findByRefId(refId);
            // //System.out.println(refId);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "referenceId belum dimasukan");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.OK);
        }
        try {
            bic = input.get("bic").toString();
            // //System.out.println(refId);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "bic belum dimasukan");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.OK);
        }
        input.remove("referenceId");
        input.remove("bic");

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
            if(!lta.isEmpty()){
                System.out.println("tiban log to ascend");

                logToAscend.setId(lta.get(0).getId());
            }
            this.logToAscendRepository.save(logToAscend);
        } catch (Exception e) {
            newMap.put("status", 400);
            newMap.put("detail", "Gagal menyimpan data ke LogToAscend");
            newMap.put("error", e.getLocalizedMessage());
            return new ResponseEntity<>(newMap, HttpStatus.OK);

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
            System.out.println("gagal encrypt");
        }
        jsonObject.put("msg", dataEncrypt);
        newJsonInput.put("data", jsonObject);
        // System.out.println(newJsonInput.toString());

        String ipAscendSale = this.apiConfigRepository.findIpByNama("ASC_sale_new");
        // //System.out.println("siap kirim");
        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        String response = null;
        try {
            System.out.println("request sale :" + newJsonInput.toString());
            response = httpRequest.postRequest("http://" + ipAscendSale, newJsonInput.toString());
            System.out.println("response sale :" + response);
        } catch (Exception e) {
            System.out.println("timeout sale loc");
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }

        JSONObject mapRespon = response != null ? new JSONObject(response) : null;
        if (mapRespon.isNull("data")) {
            System.out.println("no data return");
            Map newmaps = mapRespon.toMap();
            newmaps.put("status", 400);
            return new ResponseEntity<>(mapRespon.toMap(), HttpStatus.OK);

        }

        String decryptData = null;
        try {
            // //System.out.println("aesComponent.getAesKey(),aesComponent.getAesIV(): " +
            // aesComponent.getAesKey() + " : " +
            // aesComponent.getAesIV());
            decryptData = aesEncryptDecrypt.decrypt(mapRespon.get("data").toString(),
                    aesComponentNew.getAesKey(),
                    aesComponentNew.getAesIV());
        } catch (Exception e) {
            System.out.println("decrypt failed");
            // e.printStackTrace();
            // newMap = mapRespon;
            // newMap.put("status", 400);
            // newMap.put("detail", "Grab Ascend Gagal, Lakukan Grab Ulang");
            // // newMap.put("data", mapAscend);
            // // newMap.put("new rc", "EX-00");
            // // newMap.put("detail", "Balikan Ascend tidak sama dengan Database");
            // // newMap.put("error", e.toString());
            // return new ResponseEntity<>(newMap, HttpStatus.NOT_ACCEPTABLE);
        }

        JSONObject mapAscend = new JSONObject(decryptData);
        System.out.println("hasil asc: " + mapAscend);

        if (!mapAscend.get("rc").equals("00")) {
            // //System.out.println("masuk error");
            List<LogToAscend> findRefId = this.logToAscendRepository.findByRefId(refId);
            logToAscend = findRefId.get(0);
            logToAscend.setOutput(decryptData);
            this.logToAscendRepository.save(logToAscend);

            LogAscend logAscend = mapper.convertValue(mapAscend.toMap(), LogAscend.class);
            logAscend.setReferenceId(refId);
            LocalDateTime ldt4 = LocalDateTime.now();
            logAscend.setCreated_at(ldt4);
            logAscend.setIsGenerated(null);
            logAscend.setIsGeneratedPPMERL(null);
            logAscend.setIsvoided(false);
            logAscend.setBic(bic);
            mapAscend.put("status", mapAscend.get("rc").toString());
            // System.out.println(new JSONObject(mapAscend).toString());
            if(la.isPresent()){
                if(!la.get().getRc().equals("00")){
                    System.out.println("tiban log ascend");
                    logAscend.setId(la.get().getId());
                }
            }
            this.logAscendRepository.save(logAscend);
            return new ResponseEntity<>(mapAscend.toMap(), HttpStatus.OK);
        }

        LocalDateTime ldt2 = LocalDateTime.now();
        mapAscend.put("referenceId", refId);
        // mapAscend.put("bic", bic);
        mapAscend.put("created_at", ldt2);

        List<LogToAscend> findRefId = this.logToAscendRepository.findByRefId(refId);
        logToAscend = findRefId.get(0);
        logToAscend.setOutput(mapAscend.toString());
        this.logToAscendRepository.save(logToAscend);

        // mapAscend.entrySet().forEach(entry -> {
        // //System.out.println(entry.getKey() + " : " + entry.getValue());
        // });

        LogAscend logAscend = mapper.convertValue(mapAscend.toMap(), LogAscend.class);
        if(la.isPresent()){
            if(!la.get().getRc().equals("00")){
                System.out.println("tiban log ascend");
                logAscend.setId(la.get().getId());
            }
        }
        logAscend.setBic(bic);
        logAscend.setIsvoided(false);
        this.logAscendRepository.save(logAscend);
        newMap.put("status", 200);
        newMap.put("authno", logAscend.getAuth_no());
        newMap.put("detail", "Berhasil Menyimpan Data Dari Ascend");
        return new ResponseEntity<>(newMap, HttpStatus.OK);
        // } catch (Exception e) {
        // // newMap = mapRespon;
        // newMap.put("status", 400);
        // newMap.put("detail", "Grab Ascend Gagal, Lakukan Grab Ulang");
        // // newMap.put("data", mapAscend);
        // // newMap.put("new rc", "EX-00");
        // // newMap.put("detail", "Balikan Ascend tidak sama dengan Database");
        // // newMap.put("error", e.toString());
        // return new ResponseEntity<>(newMap, HttpStatus.NOT_ACCEPTABLE);
        // }

    }

    @GetMapping("regrabAscend")
    public ResponseEntity<Map> regrab(@RequestParam(required = true) String refid) {
        Map newMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        NewAESComponent aesComponentNew = this.aesComponentRepository.findByNama("SALE");

        JSONObject newJsonInput = new JSONObject();
        newJsonInput.put("channel_id", aesComponentNew.getAesChannelId());
        newJsonInput.put("service_id", aesComponentNew.getAesServiceId());
        newJsonInput.put("key_id", aesComponentNew.getAesKeyId());
        JSONObject dataJson = new JSONObject();
        LogToAscend lta = new LogToAscend();
        try {
            lta = this.logToAscendRepository.findByRefId(refid).get(0);
            if (lta.getOutput() != null) {
                newMap.put("status", 400);
                newMap.put("detail", "Hasil grab sudah didapat");
                newMap.put("new rc", "EX-02");
                newMap.put("data", new JSONObject(lta.getOutput()).toMap());
                return new ResponseEntity<Map>(newMap, null, 200);
            }
            lta.setRedialAt(LocalDateTime.now());
            this.logToAscendRepository.save(lta);
            dataJson = new JSONObject(lta.getInput());
        } catch (Exception e) {
            System.out.println();
        }

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

        String ipAscendSale = this.apiConfigRepository.findIpByNama("ASC_sale_new");
        // //System.out.println("siap kirim");
        HTTPRequest httpRequest = new HTTPRequest(aesComponent);
        String response = "";
        try {
            // System.out.println("request sale :" + newJsonInput.toString());
            response = httpRequest.postRequest("http://" + ipAscendSale, newJsonInput.toString());
            // System.out.println("response sale :" + response);
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

                this.logToAscendRepository.save(lta);

                LogAscend logAscend = mapper.convertValue(mapAscend, LogAscend.class);
                logAscend.setReferenceId(refid);
                LocalDateTime ldt4 = LocalDateTime.now();
                logAscend.setCreated_at(ldt4);
                logAscend.setIsGenerated(null);
                logAscend.setIsGeneratedPPMERL(null);
                mapAscend.put("status", mapAscend.get("rc").toString());
                // System.out.println(new JSONObject(mapAscend).toString());
                this.logAscendRepository.save(logAscend);
                return new ResponseEntity<>(mapAscend, HttpStatus.OK);
            }

            String jsonOutput;

            LocalDateTime ldt2 = LocalDateTime.now();
            mapAscend.put("referenceId", refid);
            mapAscend.put("created_at", ldt2);
            mapAscend.put("isGenerated", null);
            mapAscend.put("isGeneratedPPMERL", null);
            jsonOutput = mapper.writeValueAsString(mapAscend);

            lta.setOutput(jsonOutput);
            this.logToAscendRepository.save(lta);

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
            newMap = mapRespon;
            newMap.put("status", 400);
            newMap.put("detail", "Grab Ascend Gagal, Lakukan Grab Ulang");
            newMap.put("new rc", "EX-00");
            // newMap.put("detail", "Balikan Ascend tidak sama dengan Database");
            // newMap.put("error", e.toString());
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
                .findByIsNotGeneratedOld();

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
            return new ResponseEntity<>(response, HttpStatus.OK);
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
                .findByIsNotGeneratedPPMERLOld();

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
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping(value = "/generateFile/PAYEXT")
    public ResponseEntity<Map<String, Object>> generateFilePAYEXT() {
        LocalDateTime ldt = LocalDateTime.now();
        // //System.out.println(ldt);
        Map<String, Object> response = new HashMap<String, Object>();
        FileReadWrite frw = new FileReadWrite();
        // //System.out.println("coba1: " + frw.tes().get().getReferenceId());
        String [] arrChres = this.terminalMerchantRepository.findByPayext(true);
        List<String> listChres = this.channelResponseRepository.findAllPayextRef(Arrays.asList(arrChres));
        // for (String string : listChres) {
        //     System.out.println("=================\n"+string);
        // }
        List<LogAscend> list_logAscend = this.logAscendRepository
                .findByRefIdList(listChres);

        Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository
                .findByNama("LOC");

        // //System.out.println(list_terminal.get().getMerchantId());

        if (list_logAscend.isEmpty()) {
            response.put("rc", 204);
            response.put("status", "Belum ada data terbaru, tidak membuat file baru");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String payext = frw.payext(list_logAscend, this.channelResponseRepository, this.planCodeRepository,
                list_terminal.get().getMerchantId().toString(),terminalMerchantRepository);
        // Update isGenerated
        if (payext.contains("FAIL")) {
            response.put("rc", 400);
            response.put("status", "Gagal membuat file PPMERL");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        for (LogAscend logAscend : list_logAscend) {
            logAscend.setGeneratedAtPAYEXT(ldt);
            logAscend.setIsGeneratedPAYEXT(true);
            this.logAscendRepository.save(logAscend);
        }

        response.put("rc", 200);
        response.put("status", payext);
        // response.put("isi", list_logAscend);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/generateFile/MFTS/OVB")
    public ResponseEntity<Map<String, Object>> generateLOCTRF() throws JsonProcessingException {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDate ld = LocalDate.now();
        String ldtnew = (ldt.getYear() + "-" + (String.valueOf(ldt.getMonthValue()).length() == 1
                ? ("0" + String.valueOf(ldt.getMonthValue()))
                : String.valueOf(ldt.getMonthValue())) + "-" + ldt.getDayOfMonth());
        Map<String, Object> response = new HashMap<String, Object>();

        List<LogAscend> list_logAscend = this.logAscendRepository.findLOCTRFDataAllOVB(); 
        // this.logAscendRepository.findLOCTRFData(aesComponent.getBifastBic());
        // if(!aesComponent.getUseBifast()){
        //     // System.out.println("findall");
        //     list_logAscend = 
        // }

        Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository.findByNama("LOC");

        FileReadWrite frw = new FileReadWrite();
        // // //System.out.println(list_terminal.get().getMerchantId());
        List<String> ids = new ArrayList<>();
        for (LogAscend logAscend : list_logAscend) {
            ids.add(logAscend.getReferenceId());
            // logAscend.setGeneratedAtMFTS(LocalDateTime.now());
            // logAscend.setIsGeneratedMFTS(true);
            // this.logAscendRepository.save(logAscend);

        }

        if (list_logAscend.isEmpty()) {
            response.put("rc", 204);
            response.put("status", "Belum ada data terbaru, tidak membuat file baru");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        List<Map> list_response = new ArrayList<>();
        List<String> chs = this.channelResponseRepository.findAllDistinctKodeChannelMFTS(ids);
        System.out.println(new ObjectMapper().writeValueAsString(chs));
        Boolean flag_response = true;
        for (String ch_code : chs) {
            Map temp_res = new HashMap<>();
            Optional<Channel> channel = this.channelRepository.findByKodeChannelOVBSKN(ch_code);
            if(channel.isEmpty()){
                temp_res.put("rc", 400);
                temp_res.put("status", "Gagal membuat file LOCTRF Untuk channel "+null);
            }else if(channel.get() == null && channel.get() == null){
                temp_res.put("rc", 400);
                temp_res.put("status", "Gagal membuat file LOCTRF Untuk channel "+ch_code);
            }else{
                
                List<String> new_ids = this.channelResponseRepository.findAllReferenceIDMFTS(ids, ch_code);
                System.out.println(new ObjectMapper().writeValueAsString("ids: "+new_ids));
                List<LogAscend> new_LogAscends = this.logAscendRepository.findByListRefId(new_ids);
                System.out.println(new ObjectMapper().writeValueAsString("logasc: "+new_LogAscends));
                String loctrf = fileReadWriteService.locTRF(new_LogAscends, this.channelResponseRepository,
                        this.channelRepository,
                        list_terminal.get().getMerchantId().toString(), this.terminalMerchantRepository,
                        this.mftsResponseRepository,channel.get().getOvbStart(),channel.get().getOvbEnd());
    
                if (loctrf.contains("FAIL")) {
                    temp_res.put("rc", 400);
                    temp_res.put("status", "Gagal membuat file LOCTRF Untuk channel "+ch_code);
                    list_response.add(temp_res);
                    flag_response = false || flag_response;
                    // return new ResponseEntity<>(response, HttpStatus.OK);
                }else{
                    temp_res.put("rc", 200);
                    temp_res.put("status", loctrf);
                    list_response.add(temp_res);
                    flag_response = true || flag_response;
                    for (LogAscend logAscend : new_LogAscends) {
                        // ids.add(logAscend.getReferenceId());
                        logAscend.setGeneratedAtMFTS(LocalDateTime.now());
                        logAscend.setIsGeneratedMFTS(true);
                        this.logAscendRepository.save(logAscend);
            
                    }
                }   

            }
           
            
        }
        // Update isGenerated
        
        response.put("rc", flag_response ? 200:400);
        response.put("rd", flag_response ? "OK":"ERROR");
        response.put("data", list_response);
        // response.put("isi", list_logAscend);
        // for (LogAscend logAscend : list_logAscend) {
        // logAscend.setGeneratedAtMFTS(LocalDateTime.now());
        // logAscend.setIsGeneratedMFTS(true);
        // }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/generateFile/MFTS/SKN")
    public ResponseEntity<Map<String, Object>> generateLOCTRFSKN() throws JsonProcessingException {
        LocalDateTime ldt = LocalDateTime.now();
        LocalDate ld = LocalDate.now();
        Map<String, Object> response = new HashMap<String, Object>();
        // System.out.println(ld);
        Optional<HolidayDate> holiday = this.holidayRepository.findByHolidayDateActive(ld);
        // System.out.println(holiday);
        if(holiday.isPresent()){
            response.put("rc", "LOC-25");
            response.put("rd", "It's Holiday,Happy Holiday :)");
            response.put("data", holiday);
            return new ResponseEntity<>(response,null,200);
        }
        String ldtnew = (ldt.getYear() + "-" + (String.valueOf(ldt.getMonthValue()).length() == 1
                ? ("0" + String.valueOf(ldt.getMonthValue()))
                : String.valueOf(ldt.getMonthValue())) + "-" + ldt.getDayOfMonth());

        List<LogAscend> list_logAscend = this.logAscendRepository.findLOCTRFData(aesComponent.getBifastBic());
        if(!aesComponent.getUseBifast()){
            // System.out.println("findall");
            list_logAscend = this.logAscendRepository.findLOCTRFDataAllSKN();
        }else{
            response.put("rc", "LOC-77");
            response.put("rd", "Current Setting is Using BIFAST");
            // response.put("data", holiday);
            return new ResponseEntity<>(response,null,200);
        }

        Optional<TerminalMerchant> list_terminal = this.terminalMerchantRepository.findByNama("LOC");

        FileReadWrite frw = new FileReadWrite();
        // // //System.out.println(list_terminal.get().getMerchantId());
        List<String> ids = new ArrayList<>();
        
        if (list_logAscend.isEmpty()) {
            response.put("rc", 204);
            response.put("status", "Belum ada data terbaru, tidak membuat file baru");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        for (LogAscend logAscend : list_logAscend) {
            ids.add(logAscend.getReferenceId());
            System.out.println(logAscend.getReferenceId());
            // logAscend.setGeneratedAtMFTS(LocalDateTime.now());
            // logAscend.setIsGeneratedMFTS(true);
            // this.logAscendRepository.save(logAscend);

        }

        List<Map> list_response = new ArrayList<>();
        List<String> chs = this.channelResponseRepository.findAllDistinctKodeChannelMFTS(ids);
        System.out.println(new ObjectMapper().writeValueAsString(chs));
        
        Boolean flag_response = true;
        for (String ch_code : chs) {
            Map temp_res = new HashMap<>();
            Optional<Channel> channel = this.channelRepository.findByKodeChannelOVBSKN(ch_code);
            // System.out.println(new ObjectMapper().writeValueAsString(channel.get()));
            List<String> new_ids = this.channelResponseRepository.findAllReferenceIDMFTS(ids, channel.get().getKode_channel());
            System.out.println(new ObjectMapper().writeValueAsString("ids: "+new_ids));

            List<LogAscend> new_LogAscends = this.logAscendRepository.findByListRefId(new_ids);
            String loctrf = fileReadWriteService.locTRFSKN(new_LogAscends, this.channelResponseRepository,
                    this.channelRepository,
                    list_terminal.get().getMerchantId().toString(), this.terminalMerchantRepository,
                    this.mftsResponseRepository,channel.get().getSknStart(),channel.get().getSknEnd());

        if (loctrf.contains("FAIL")) {
            temp_res.put("rc", 400);
            temp_res.put("status", "Gagal membuat file LOCTRF");
            list_response.add(temp_res);
            flag_response = false || flag_response;
            // return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            temp_res.put("rc", 200);
            temp_res.put("status", loctrf);
            list_response.add(temp_res);
            flag_response = true || flag_response;
            for (LogAscend logAscend : new_LogAscends) {
                // System.out.println(logAscend.getReferenceId());
                logAscend.setGeneratedAtMFTS(LocalDateTime.now());
                logAscend.setIsGeneratedMFTS(true);
                
                this.logAscendRepository.save(logAscend);
            }
        }   

            
        }
        // Update isGenerated
        
        response.put("rc", flag_response ? 200:400);
        response.put("rd", flag_response ? "OK":"ERROR");
        response.put("data", list_response);
        // response.p

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Value("${ascend.url}")
    private String ascendurl;

    @PostMapping(value = "/cicilanChannel")
    public ResponseEntity<Map<String, Object>> cariCicilan(@RequestBody InputAsccend input) {
        Map<String, Object> response = new HashMap<String, Object>();
        if (input.getCardnum().isEmpty()) {
            response.put("rc", 400);
            response.put("detail", "cardnum tidak dimasukan");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        if (input.getChannel().isEmpty()) {
            response.put("rc", 400);
            response.put("detail", "channel tidak dimasukan");
            return new ResponseEntity<>(response, HttpStatus.OK);

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
        String uri = ascendurl+"/SCNMORDP";
        String data = "{\"cardnum\":\"" + input.getCardnum() + "\",\"plancode\":" + newplancode + "}";
        System.out.print("uri: "+uri+" || ");
        System.out.println(data);
        try {
            HTTPRequest httpRequest = new HTTPRequest(aesComponent);
            String responseHasil = httpRequest.postRequestBasicAuth(uri, data, "7777777", "7777777");
            System.out.println("response hasil: " + responseHasil);

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
            System.out.println(e.getMessage());
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
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
                    // System.out.println(mapper.writeValueAsString(newResponse));
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
                    // return new ResponseEntity<>(response, HttpStatus.OK);
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
            System.out.println(mapper.writeValueAsString(mapBody));
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
            e.printStackTrace();
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
            return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<Map<String, Object>> cekCustomer(@RequestBody Map<String, Object> input,
            HttpServletRequest req) {

        // APICustomer apiCustomer = new APICustomer();
        logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Request", req.getLocalAddr(),
                req.getRemoteAddr(), "LOC Engine", "cekCustomer", "Cek Customer", null,
                null, new JSONObject(input));
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

            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "cekCustomer", "Cek Customer",
                    response.get("rc").toString(),
                    null, new JSONObject(response));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        List<CustomerParam> list = this.customerParamRepository.findByKodeChannel(input.get("kodeChannel").toString());

        for (CustomerParam customerParam : list) {
            if (input.get(customerParam.getNama_param()) == null) {
                response.put("rc", 400);
                response.put("rd", customerParam.getNama_param() + " tidak dimasukan");

                logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                        req.getRemoteAddr(), "LOC Engine", "cekCustomer", "Cek Customer",
                        response.get("rc").toString(),
                        null, new JSONObject(response));
                return new ResponseEntity<>(response, HttpStatus.OK);
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
                        return new ResponseEntity<>(response, HttpStatus.OK);
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
            // logService.info("/cekCustomer res: " + response.toString());
            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "cekCustomer", "Cek Customer",
                    response.get("rc").toString(),
                    null, new JSONObject(response));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("rc", "209");
            response.put("rd", "Not Eligible");
            // logService.info("/cekCustomer res: " + response.toString());
            logService.info2(null, String.valueOf(Instant.now().toEpochMilli()), "Response", req.getLocalAddr(),
                    req.getRemoteAddr(), "LOC Engine", "cekCustomer", "Cek Customer",
                    response.get("rc").toString(),
                    null, new JSONObject(response));
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
            String refuser = hasil.get(i).split(",")[26].isBlank() ? hasil.get(i).split(",")[6]:hasil.get(i).split(",")[26];
            tempData.put(
                    "refUser", refuser);
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
            Optional<ChannelResponse> chResDatas = this.channelResponseRepository
                    .getByReferenceId(dataMaps.getJSONObject(i).getString("refUser"));
            FirebaseService firebaseService = new FirebaseService(channelResponseRepository, transactionDatas.get(),
                    firebaseConfigRepository, aesComponentNew, urlMFTS, aesComponent);
            // //System.out.println(dataMaps.getJSONObject(i).toString());
            if (transactionDatas.isEmpty() == false) {
                String status_transfer = "";
                String status_transfer_desc = "";
                try {
                    status_transfer = dataMaps.getJSONObject(i).getString("status");
                    status_transfer_desc = dataMaps.getJSONObject(i).getString("desc");
                } catch (Exception e) {
                    status_transfer = "null";
                }
                ChannelResponse chResData = chResDatas.get();
                chResData.setStatusTransfer(status_transfer);
                chResData.setDetailStatusTransfer(status_transfer_desc);

                LogAscend tempTransactionData = transactionDatas.get();
                tempTransactionData.setStatusTransfer(status_transfer);
                tempTransactionData.setStatusTransferDesc(status_transfer_desc);
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
                this.channelResponseRepository.save(chResData);
                response.put("rc", 200);
                response.put("rd", "Berhasil Update Status");
                response.put("filename", namafile);
            }
        }
        return response;
    }

    @GetMapping("/findTxnResponse")
    public ResponseEntity<Map> findTxnResponse(@RequestParam(name = "tanggal", required = false) String tanggal,
            @RequestParam(name = "refId", required = false) String refId) {
        Map hasil = new HashMap<>();
        List<LogAscendResponse> logAscends = new ArrayList<>();
        if (tanggal == null && refId == null) {
            hasil.put("rc", "99");
            hasil.put("rd", "param missing");
            return new ResponseEntity<>(hasil, null, 200);
        } else if (tanggal != null && refId == null) {
            String newTgl = tanggal + "T00:00:00";
            List<LogAscend> list = this.logAscendRepository.findByCreated_at(LocalDateTime.parse(newTgl),
                    LocalDateTime.parse(newTgl).plusDays(1));
            for (LogAscend logAscend : list) {
                LogAscendResponse lResponse = new LogAscendResponse(logAscend);
                logAscends.add(lResponse);
            }
        } else if (tanggal == null && refId != null) {
            LogAscend log = this.logAscendRepository.findByRefId(refId).get();
            LogAscendResponse lResponse = new LogAscendResponse(log);
            logAscends.add(lResponse);
        }
        hasil.put("rc", "00");
        hasil.put("rd", "OK");
        hasil.put("data", logAscends);
        // this.logAscendRepository.findByRefId(refId);
        return new ResponseEntity<>(hasil, null, 200);
    }

}
