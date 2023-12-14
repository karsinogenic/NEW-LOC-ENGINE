package com.demo_loc_engine.demo.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

// import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo_loc_engine.demo.Models.LoginForm;
import com.demo_loc_engine.demo.Models.Users;
import com.demo_loc_engine.demo.Repositories.UsersRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UsersRepository usersRepository;

    @PostMapping("ceklogin")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginForm loginForm) {
        Map<String, Object> response = new HashMap<>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<LoginForm>> violations = validator.validate(loginForm);
        if (!violations.isEmpty()) {
            response.put("rc", 400);
            response.put("rd", "Blank / Null");
            response.put("error", violations.iterator().next().getPropertyPath().toString());
            // throw new ConstraintViolationException(violations);
            // return new ResponseEntity<Map<String, Object>>(response,
            // HttpStatus.BAD_REQUEST);
            return new ResponseEntity<Map<String, Object>>(response, null, 400);
        }

        List<Users> userOptional = this.usersRepository.findByNrik(loginForm.getUsername().toString());
        if (userOptional.isEmpty()) {
            response.put("rc", 400);
            response.put("rd", "Tidak terdaftar");
            // throw new ConstraintViolationException(violations);
            // return new ResponseEntity<Map<String, Object>>(response,
            // HttpStatus.BAD_REQUEST);
            return new ResponseEntity<Map<String, Object>>(response, null, 400);
        }

        try {
            if (!userOptional.get(0).getPassword().equals(loginForm.getPassword())) {
                response.put("rc", 400);
                response.put("rd", "Password Salah");
                // throw new ConstraintViolationException(violations);
                // return new ResponseEntity<Map<String, Object>>(response,
                // HttpStatus.BAD_REQUEST);
                return new ResponseEntity<Map<String, Object>>(response, null, 400);
            }
        } catch (Exception e) {
            if (!userOptional.get(0).getPassword().equals(loginForm.getPassword())) {
                response.put("rc", 400);
                response.put("rd", "Password Salah");
                // throw new ConstraintViolationException(violations);
                // return new ResponseEntity<Map<String, Object>>(response,
                // HttpStatus.BAD_REQUEST);
                return new ResponseEntity<Map<String, Object>>(response, null, 400);
            }
        }

        // List<GrantedAuthority> authorities = new ArrayList<>();
        // authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        UsernamePasswordAuthenticationToken.authenticated(loginForm.getUsername(), loginForm.getPassword(), null);

        response.put("rc", 200);
        response.put("rd", "Authenticated");
        return new ResponseEntity<Map<String, Object>>(response, null, 200);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("mantap", null, 200);
    }

}
