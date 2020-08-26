package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.MyUser;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.QMyUser;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import java.util.List;

public interface MyUserRepository extends JpaRepository<MyUser, Long>,
        QuerydslPredicateExecutor<MyUser> // to use Predicates later to filter search results
        , QuerydslBinderCustomizer<QMyUser> {

    @Override
    default void customize(QuerydslBindings bindings, QMyUser root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.excluding(root.email);
    }

    List<MyUser> findAll(Predicate predicate);

}
