package com.krazyhawg.employeedirectory.db.specification;

import com.krazyhawg.employeedirectory.db.Employee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecificationBuilder {
    private final List<SearchCriteria> params;

    public EmployeeSpecificationBuilder() {
        params = new ArrayList<>();
    }

    public EmployeeSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public Specification<Employee> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Employee>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new EmployeeSpecification(param));
        }

        Specification<Employee> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
