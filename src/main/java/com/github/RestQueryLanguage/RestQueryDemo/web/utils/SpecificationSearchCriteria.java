package com.github.RestQueryLanguage.RestQueryDemo.web.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationSearchCriteria {

    //used to hold field name - for example: firstName, age, ... etc
    private String key;

    //used to hold the operation - for example: equality, less than, ... etc
    private SearchOperation operation;

    //used to hold the field value - for example: John, 25, .. etc
    private Object value;

    private boolean orPredicate;

    public SpecificationSearchCriteria(String key, SearchOperation operation, Object value, String orPredicate) {
        this.orPredicate = orPredicate != null && orPredicate.equals(SearchOperation.OR_PREDICATE_FLAG);

        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecificationSearchCriteria(
            final String key,
            final String operation,
            final String prefix,
            final String value,
            final String suffix
    ) {
        this.key = key;
        this.operation = SearchOperation.determineOperation(operation, prefix, suffix);
        this.value = value;
    }
}
