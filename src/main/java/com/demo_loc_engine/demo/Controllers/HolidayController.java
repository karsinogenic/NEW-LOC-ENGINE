package com.demo_loc_engine.demo.Controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.demo_loc_engine.demo.Models.HolidayDate;
import com.demo_loc_engine.demo.Repositories.ChannelRepository;
import com.demo_loc_engine.demo.Repositories.ChannelResponseRepository;
import com.demo_loc_engine.demo.Repositories.HolidayRepository;

@RestController
@RequestMapping("/holiday/")
public class HolidayController {

    @Autowired
    private HolidayRepository holidayRepository;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private ResponseEntity<Map> handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        Map hasil = new HashMap<>();
        hasil.put("rc", "99");
        hasil.put("rd", "format salah untuk parameter '"+ex.getPropertyName()+"' ");
        return new ResponseEntity<>(hasil, null,200);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    private ResponseEntity<Map> handleTypeNotReadable(HttpMessageNotReadableException ex){
        Map hasil = new HashMap<>();
        hasil.put("rc", "97");
        hasil.put("rd", "format salah");
        return new ResponseEntity<>(hasil, null,200);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    private ResponseEntity<Map> handleDataIntegrity(DataIntegrityViolationException ex){
        Map hasil = new HashMap<>();
        if(ex.getMessage().contains("UK_")){
            hasil.put("rc", "98");
            hasil.put("rd", "holiday date telah di input");
        }
        return new ResponseEntity<>(hasil, null,200);
    }

    @GetMapping("get")
    private ResponseEntity<Map> get(@RequestParam(name = "date1",required = false) LocalDate date1,@RequestParam(name = "date2",required = false) LocalDate date2){
        Map hasil = new HashMap<>();
        List<HolidayDate> holidays;
        if(date1 != null && date2 != null){
            holidays = this.holidayRepository.findHolidayBetween(date1, date2);
        }else if(date1 != null || date2 != null){
            holidays = this.holidayRepository.findByHolidayDate(date1!=null?date1:date2);
        }else{
            holidays = this.holidayRepository.findAll();
        }
        this.holidayRepository.findAll();
        hasil.put("rc", "00");
        hasil.put("rd", "OK");
        hasil.put("data", holidays);
        return new ResponseEntity<>(hasil,null,200);
    }

    @PutMapping("update")
    private ResponseEntity<Map> update(@RequestBody HolidayDate hd){
        Map hasil = new HashMap<>();
        List<HolidayDate> opt_hd = this.holidayRepository.findByHolidayDate(hd.getHolidaydate());
        if(opt_hd.size()==0){
            hasil.put("rc", "01");
            hasil.put("rd", "data tidak ditemukan");
            return new ResponseEntity<>(hasil,null,200);
        }
        HolidayDate old_hd = opt_hd.get(0);
        old_hd.setDescription(hd.getDescription());
        old_hd.setIs_active(hd.getIs_active());
        this.holidayRepository.save(old_hd);
        hasil.put("rc", "00");
        hasil.put("rd", "OK");
        hasil.put("data", old_hd);

        return new ResponseEntity<>(hasil,null,200);
    }

    @DeleteMapping("delete")
    private ResponseEntity<Map> delete(@RequestParam(name = "date") LocalDate date){
        Map hasil = new HashMap<>();
        List<HolidayDate> opt_hd = this.holidayRepository.findByHolidayDate(date);
        if(opt_hd.size()==0){
            hasil.put("rc", "01");
            hasil.put("rd", "data tidak ditemukan");
            return new ResponseEntity<>(hasil,null,200);
        }
        this.holidayRepository.deleteById(opt_hd.get(0).getId());
        hasil.put("rc", "00");
        hasil.put("rd", "OK");
        return new ResponseEntity<>(hasil,null,200);
    }
    
    @PostMapping("add")
    private ResponseEntity<Map> add(@RequestBody HolidayDate hd){
        Map hasil = new HashMap<>();
        hd.setIs_active(true);
        this.holidayRepository.save(hd);
        hasil.put("rc", "00");
        hasil.put("rd", "OK");
        hasil.put("data", hd);
        return new ResponseEntity<>(hasil,null,200);
    }
    
    @Autowired
    private ChannelResponseRepository channelRepository;
    // @GetMapping("test")
    // private ResponseEntity<Map> test(){
    //     Map response = new HashMap<>();
    //     String[] refids = {"SMTCL20241205030458H5ThO","SMTLOC202412050633438kfLR","SMTLOC20241205061331D2GS7","naufaltestnew001","naufaltestnew002","SMTLOC20241218092314EsSy9"};
    //     List<String> mfts = this.channelRepository.findAllDistinctKodeChannelMFTS(Arrays.asList(refids));
    //     Map data = new HashMap<>();
    //     for (String string : mfts) {
    //         data.put(string, this.channelRepository.findAllDistinctMFTS(Arrays.asList(refids), string));
    //     }
    //     response.put("rc", "00");
    //     response.put("rd", "OK");
    //     response.put("data", data);
    //     return new ResponseEntity<Map>(response, null, 200);
    // }

}
