package com.krazyhawg.employeedirectory.controllers;

import com.krazyhawg.employeedirectory.db.Employee;
import com.krazyhawg.employeedirectory.db.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.krazyhawg.employeedirectory.controllers.PageConstants.*;

@RestController
@RequestMapping(EmployeeController.EMPLOYEE_PATH)
public class EmployeeController {

    public static final String EMPLOYEE_PATH = "/employee";

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Employee getEmployee(@PathVariable @NotNull Long id) {
        return employeeService.getById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page<Employee> getEmployeePage(@RequestParam( value = PAGE_NUMBER, required = false, defaultValue = "0" ) Integer pageNumber,
                                          @RequestParam( value = PAGE_SIZE, required = false, defaultValue = "50" ) Integer pageSize,
                                          @RequestParam( value = SORT_DIRECTION, required = false, defaultValue = "ASC" ) Sort.Direction sortDirection,
                                          @RequestParam( value = SORT_COLUMN, required = false, defaultValue = "lastName" ) String sortColumn,
                                          @RequestParam( value = SEARCH, required = false) String search) {
        return employeeService.getEmployeePage(pageNumber, pageSize, sortDirection, sortColumn, search);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Employee createEmployee(@RequestBody @NotNull @Valid Employee employee) {
        return employeeService.insert(employee);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
     public Employee updateEmployee(@PathVariable @NotNull Long id, @RequestBody @NotNull @Valid Employee employee) {
        employee.setId(id);
        return employeeService.update(employee);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateEmployee(@PathVariable @NotNull Long id) {
        employeeService.delete(id);
    }
}
