package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.spec;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SpecificationSearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.BiFunction;

@Data
@AllArgsConstructor
public class UserSpecification implements Specification<User> {

    private SpecificationSearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Path<String> key = root.get(criteria.getKey());
        String value = criteria.getValue().toString();

        switch (criteria.getOperation()) {
            case EQUALITY:
                return build(builder::equal, key, value);
            case NEGATION:
                return build(builder::notEqual, key, value);
            case GREATER_THAN:
                return build(builder::greaterThan, key, value);
            case LESS_THAN:
                return build(builder::lessThan, key, value);
            case LIKE:
                return build(builder::like, key, value);
            case STARTS_WITH:
                return build(builder::like, key, value + "%");
            case ENDS_WITH:
                return build(builder::like, key, "%" + value);
            case CONTAINS:
                return build(builder::like, key, "%" + value + "%");
            default:
                return null;
        }
    }

    private Predicate build(
            BiFunction<Expression<String>, String, Predicate> method,
            Expression<String> key,
            String value
    ) {
        return method.apply(key, value);
    }
}
