package com.blackbaud.employeedirectory.security;

import com.blackbaud.employeedirectory.db.Employee;
import com.blackbaud.employeedirectory.db.EmployeeRepository;
import com.blackbaud.employeedirectory.db.EmployeeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

// This class is temporary, because we are not using a real OAuth server
// (with Active Directory or other system for PWD management)..
@Service
public class EmployeeUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmailEquals(email);
        if (employee == null) {
            throw new UsernameNotFoundException(String.format("Username %s not found", email));
        }
        return new User(email, employee.getPassword(), getGrantedAuthorities(employee.getEmployeeType()));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities(EmployeeType employeeType) {
        Collection<? extends GrantedAuthority> authorities;

        if (employeeType.equals(EmployeeType.HR)) {
            authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_BASIC"));
        } else {
            authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_BASIC"));
        }
        return authorities;
    }
}
