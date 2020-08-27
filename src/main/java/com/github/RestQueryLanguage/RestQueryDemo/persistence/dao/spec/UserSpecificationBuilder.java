package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.spec;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchOperation;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SpecificationSearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserSpecificationBuilder {

    private final List<SpecificationSearchCriteria> params;

    public UserSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public UserSpecificationBuilder with(
            final String key,
            final String operation,
            final Object value,
            final boolean orPredicate
    ) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        params.add(new SpecificationSearchCriteria(key, op, value, orPredicate));
        return this;
    }

    public UserSpecificationBuilder with(
            final String orPredicate,
            final String key,
            final String operation,
            final Object value,
            final String prefix,
            final String suffix
    ) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else {
                    op = SearchOperation.STARTS_WITH;
                }
            }

            params.add(new SpecificationSearchCriteria(key, op, value, orPredicate));
        }

        return this;
    }

    public UserSpecificationBuilder with(SpecificationSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

    public Specification<User> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<User>> specs = params.stream()
                .map(UserSpecification::new)
                .collect(Collectors.toList());

        Specification<User> result = specs.get(0);
        for (int i = 1; i < params.size(); ++i) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Objects.requireNonNull(Specification.where(result))
                        .or(specs.get(i))
                    : Objects.requireNonNull(Specification.where(result))
                        .and(specs.get(i));
        }

        return result;
    }


}
