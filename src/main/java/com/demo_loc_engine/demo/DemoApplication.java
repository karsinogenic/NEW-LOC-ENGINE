package com.demo_loc_engine.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.demo_loc_engine.demo.Controllers.APIMainController;
import com.demo_loc_engine.demo.Models.TerminalMerchant;
import com.demo_loc_engine.demo.Repositories.TerminalMerchantRepository;
import com.demo_loc_engine.demo.Services.AmbilAscend;

import me.xdrop.fuzzywuzzy.FuzzySearch;

@SpringBootApplication
@ComponentScan
public class DemoApplication extends SpringBootServletInitializer {

	// @Autowired
	// public TerminalMerchantRepository terminalMerchantRepository;

	public static void main(String[] args) {
		// AmbilAscend ambilAscend = new AmbilAscend();
		// ambilAscend.queryDatabase();
		SpringApplication.run(DemoApplication.class, args);
		// APIMainController apiMainController = new APIMainController();
		// Optional<TerminalMerchant> list_terminal =
		// apiMainController.terminalMerchantRepository.findByNama("LOC");
		// apiMainController.ipLoc = list_terminal.get().getServerIp();
	}

}