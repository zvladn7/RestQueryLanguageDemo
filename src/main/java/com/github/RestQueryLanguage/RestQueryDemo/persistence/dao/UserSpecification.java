package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.function.BiFunction;

@Data
@AllArgsConstructor
public class UserSpecification implements Specification<User> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<String> key = root.get(criteria.getKey());
        String value = criteria.getValue().toString();
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return build(builder::greaterThanOrEqualTo, key, value);
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return build(builder::lessThanOrEqualTo, key, value);
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return build(builder::like, key, "%" + value + "%");
            } else {
                return build(builder::equal, key, value);
            }
        }

        return null;
    }

    private Predicate build(
            BiFunction<Expression<String>, String, Predicate> method,
            Expression<String> key,
            String value
    ) {
        return method.apply(key, value);
    }
}
