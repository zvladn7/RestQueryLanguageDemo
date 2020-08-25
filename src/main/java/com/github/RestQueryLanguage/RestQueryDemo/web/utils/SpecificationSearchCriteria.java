package com.github.RestQueryLanguage.RestQueryDemo.web.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationSearchCriteria extends SearchCriteria {

    private boolean orPredicate;

    public SpecificationSearchCriteria(
            final String key,
            final String operation,
            final Object value,
            final boolean orPredicate
    ) {
        super(key, operation, value);
        this.orPredicate = orPredicate;
    }

}
