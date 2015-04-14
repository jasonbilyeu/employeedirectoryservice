package com.headspring.employeedirectory.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"phoneNumbers", "password"})
public class Employee {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String jobTitle;
    private String location;

    @Email
    @NotNull
    @Column(unique=true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty
    private List<String> phoneNumbers;

    @NotNull
    private EmployeeType employeeType;

    // This is temporary, because we are not using a real OAuth server
    // (with Active Directory or other system for PWD management) so password is stored here
    // but not sent or received over the wire..
    @JsonIgnore
    private String password;
}
