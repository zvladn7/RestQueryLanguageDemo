package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.MyUser;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyUserPredicate {

    private SearchCriteria criteria;

    public BooleanExpression getPredicate() {
        PathBuilder<MyUser> entityPath = new PathBuilder<>(MyUser.class, "myUser");

        if (isNumeric(criteria.getValue().toString())) {
            NumberPath<Integer> path = entityPath.getNumber(criteria.getKey(), Integer.class);
            int value = Integer.parseInt(criteria.getValue().toString());
            switch (criteria.getOperation()) {
                case ":":
                    return path.eq(value);
                case ">":
                    return path.goe(value);
                case "<":
                    return path.loe(value);
            }
        } else {
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }

        return null;
    }

    private boolean isNumeric(final String value) {
        try {
            Integer.parseInt(value);
        } catch (final NumberFormatException ex) {
            return false;
        }

        return true;
    }

}
