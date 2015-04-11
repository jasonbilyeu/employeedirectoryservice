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
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"phoneNumbers", "employeeType", "password"})
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
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty
    private List<String> phoneNumbers;

    private EmployeeType employeeType;

    @JsonIgnore
    private String password;
}
