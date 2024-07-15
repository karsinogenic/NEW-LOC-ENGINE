package com.demo_loc_engine.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.rest.webmvc.IncomingRequest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.demo_loc_engine.demo.Controllers.APIMainController;
import com.demo_loc_engine.demo.Models.IncomingRequestBiFast;
import com.demo_loc_engine.demo.Models.TerminalMerchant;
import com.demo_loc_engine.demo.Repositories.TerminalMerchantRepository;
import com.demo_loc_engine.demo.Services.AmbilAscend;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.xdrop.fuzzywuzzy.FuzzySearch;

@SpringBootApplication
@ComponentScan
public class DemoApplication extends SpringBootServletInitializer {

	// @Autowired
	// public TerminalMerchantRepository terminalMerchantRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		// AmbilAscend ambilAscend = new AmbilAscend();
		// ambilAscend.queryDatabase();
		// IncomingRequestBiFast coba = new IncomingRequestBiFast();
		// coba.setAccnum("cobaa");
		// coba.setChName("1cobaa");
		// ObjectMapper objectMapper = new ObjectMapper();
		// Map map = objectMapper.convertValue(coba, Map.class);
		// System.out.println(new JSONObject(map));
		// JaroWinklerDistance jwd = new JaroWinklerDistance();
		// System.out.println(jwd.apply("muhammad naufal", "muhmmad naufl"));
		// APIMainController apiMainController = new APIMainController();
		// Optional<TerminalMerchant> list_terminal =
		// apiMainController.terminalMerchantRepository.findByNama("LOC");
		// apiMainController.ipLoc = list_terminal.get().getServerIp();
	}

}