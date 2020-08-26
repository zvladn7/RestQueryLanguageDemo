package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.spec;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
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
        params.add(new SpecificationSearchCriteria(key, operation, value, orPredicate));
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
