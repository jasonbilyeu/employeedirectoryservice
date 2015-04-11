package com.headspring.employeedirectory.db;

import com.headspring.employeedirectory.db.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getById(Long id) {
        return employeeRepository.findOne(id);
    }

    public Page<Employee> getEmployeePage(Integer pageNumber, Integer pageSize, Sort.Direction sortDirection, String sortColumn, String search) {
        PageRequest request =
                new PageRequest(pageNumber, pageSize, sortDirection, sortColumn);
        return employeeRepository.findAll(EmployeeSpecification.fromSearchString(search), request);
    }

    public Employee insert(Employee employee) {
        // This is temporary, because we are not using a real OAuth server
        // (with Active Directory or other system for PWD management), set the password to 'password' on insert
        employee.setPassword("password");
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Long id) {
        employeeRepository.delete(id);
    }
}
