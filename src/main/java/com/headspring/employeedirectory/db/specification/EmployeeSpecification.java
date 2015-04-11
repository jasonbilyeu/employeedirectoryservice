package com.headspring.employeedirectory.db.specification;

import com.headspring.employeedirectory.db.Employee;
import com.headspring.employeedirectory.db.EmployeeType;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class EmployeeSpecification  implements Specification<Employee> {

    private SearchCriteria criteria;

    public static Specification<Employee> fromSearchString(String search) {
        EmployeeSpecificationBuilder builder = new EmployeeSpecificationBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }

        return builder.build();
    }

    @Override
    public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return cb.greaterThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return cb.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());
        }
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return cb.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } if (root.get(criteria.getKey()).getJavaType() == List.class) {
                Expression<Collection<String>> collectionItems = root.get(criteria.getKey());
                return cb.isMember((String) criteria.getValue(), collectionItems);
            } if (root.get(criteria.getKey()).getJavaType() == EmployeeType.class) {
                return cb.equal(root.get(criteria.getKey()), EmployeeType.valueOf((String)criteria.getValue()));
            }
            else {
                return cb.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
