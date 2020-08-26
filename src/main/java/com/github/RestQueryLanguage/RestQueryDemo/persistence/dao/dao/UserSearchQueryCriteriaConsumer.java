package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.dao;

import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Consumer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchQueryCriteriaConsumer implements Consumer<SearchCriteria> {

    private Predicate predicate;
    private CriteriaBuilder builder;
    private Root root;

    @Override
    public void accept(SearchCriteria param) {
        Path path = root.get(param.getKey());
        if (param.getOperation().equalsIgnoreCase(">")) {
            this.predicate = builder.and(predicate, builder.greaterThanOrEqualTo(path, param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase("<")) {
            predicate = builder.and(predicate, builder.lessThanOrEqualTo(path, param.getValue().toString()));
        } else if (param.getOperation().equalsIgnoreCase(":")) {
            if (root.get(param.getKey()).getJavaType() == String.class) {
                predicate = builder.and(predicate, builder.like(path, "%" + param.getValue() + "%"));
            } else {
                predicate = builder.and(predicate, builder.equal(path, param.getValue()));
            }
        }
    }
}
