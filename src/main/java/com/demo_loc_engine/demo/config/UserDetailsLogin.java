package com.demo_loc_engine.demo.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo_loc_engine.demo.Models.Users;
import com.demo_loc_engine.demo.Repositories.UsersRepository;

@Service
public class UserDetailsLogin implements UserDetails, UserDetailsService {

    @Autowired
    public UsersRepository usersRepository;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPassword'");
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUsername'");
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountNonExpired'");
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAccountNonLocked'");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isCredentialsNonExpired'");
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnabled'");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // System.out.println(username);
        // TODO Auto-generated method stub
        String userName, password = null;
        List<GrantedAuthority> authorities = null;
        List<Users> users = usersRepository.findByNrik(username);
        if (users.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user : "
                    + username);
        } else {
            userName = users.get(0).getNrik();
            password = users.get(0).getPassword();
            authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(users.get(0).getRole()));
        }
        return new User(userName, password, authorities);
        // throw new UnsupportedOperationException("Unimplemented method
        // 'loadUserByUsername'");
    }

    // // public UserDetails newUserDetail(){
    // // UserDetails userDetails = new User
    // // return null;
    // // }

    // public UserDetails loadUserByUsername(String username) {
    // String userName, password = null;
    // List<GrantedAuthority> authorities = null;
    // List<Users> users = usersRepository.findByNrik(username);
    // if (users.size() == 0) {
    // throw new UsernameNotFoundException("User details not found for the user : "
    // + username);
    // } else {
    // userName = users.get(0).getNrik();
    // password = users.get(0).getPassword();
    // authorities = new ArrayList<>();
    // authorities.add(new SimpleGrantedAuthority(users.get(0).getRole()));
    // }
    // return new User(userName, password, authorities);
    // }

}
