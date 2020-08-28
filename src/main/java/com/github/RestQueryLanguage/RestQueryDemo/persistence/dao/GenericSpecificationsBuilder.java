package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao;

import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchOperation;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SpecificationSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class GenericSpecificationsBuilder<U> {

    private final List<SpecificationSearchCriteria> params;

    public GenericSpecificationsBuilder() {
        this.params = new ArrayList<>();
    }

    public final GenericSpecificationsBuilder<U> with(
            final String key,
            final String operation,
            final Object value,
            final String prefix,
            final String suffix
    ) {
        return with(null, key, operation, value, prefix, suffix);
    }

    private GenericSpecificationsBuilder<U> with(
            final String precedenceIndicator,
            final String key,
            final String operation,
            final Object value,
            final String prefix,
            final String suffix
    ) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            op = SearchOperation.determineOperation(operation, prefix, suffix);
            params.add(new SpecificationSearchCriteria(key, op, value, precedenceIndicator));
        }

        return this;
    }

    public Specification<U> build(
            final Deque<?> postFixedExprStack,
            final Function<SpecificationSearchCriteria, Specification<U>> converter
    ) {
        Deque<Specification<U>> specStack = new LinkedList<>();

        while (!postFixedExprStack.isEmpty()) {
            Object mayBeOperand = postFixedExprStack.pollLast();

            if (!(mayBeOperand instanceof String)) {
                specStack.push(converter.apply((SpecificationSearchCriteria) mayBeOperand));
            } else {
                Specification<U> operand1 = specStack.pop();
                Specification<U> operand2 = specStack.pop();
                if (mayBeOperand.equals(SearchOperation.AND_OPERATOR)) {
                    specStack.push(Specification.where(operand1)
                            .and(operand2));
                } else if (mayBeOperand.equals(SearchOperation.OR_OPERATOR)) {
                    specStack.push(Specification.where(operand1)
                            .or(operand2));
                }
            }
        }

        return specStack.pop();
    }

}
