package com.github.RestQueryLanguage.RestQueryDemo.web.controller;


import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.MyUserPredicatesBuilder;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.dao.IUserDAO;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.MyUserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.UserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.spec.UserSpecificationBuilder;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.MyUser;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchOperation;
import com.google.common.base.Joiner;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    private static final String URL_PARAMETERS_REGEX_MATCHER_STRING = "(\\w+?)([:<>])(\\w+?),";

    @Autowired
    private IUserDAO api;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MyUserRepository myUserRepo;

    @GetMapping("/users/v1")
    public List<User> findAll(@RequestParam(value = "search", required = false) String search) {
        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            Matcher matcher = getMatcher(search);
            while (matcher.find()) {
                params.add(new SearchCriteria(
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(3)
                ));
            }
        }

        return api.searchUser(params);
    }

    @GetMapping("/users/v2")
    public List<User> findAllWithSpec(@RequestParam(value = "search", required = false) String search) {
        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        if (search != null) {
            Matcher matcher = getMatcher(search);
            while (matcher.find()) {
                builder.with(
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(3),
                        false
                );
            }
        }

        Specification<User> specification = builder.build();
        return userRepo.findAll(specification);
    }

    @GetMapping("/users/v3")
    public List<MyUser> findAllWithQuerydsl(@RequestParam(value = "search", required = false) String search) {
        MyUserPredicatesBuilder builder = new MyUserPredicatesBuilder();
        if (search != null) {
            Matcher matcher = getMatcher(search);
            while (matcher.find()) {
                builder.with(
                        matcher.group(1),
                        matcher.group(2),
                        matcher.group(3)
                );
            }
        }

        BooleanExpression booleanExpression = builder.build();
        return myUserRepo.findAll(booleanExpression);
    }

    @GetMapping("/users/v4")
    public List<User> findAllSpecWithMoreOps(@RequestParam(value = "search", required = false) String search) {
        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Matcher matcher = getMatcherV2(search, operationSetExper, UserController::getRegExp);

        while (matcher.find()) {
            builder.with(
                    null,
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5)
            );
        }

        Specification<User> specification = builder.build();
        return userRepo.findAll(specification);
    }

    @GetMapping("/users/v5")
    public List<User> findAllSpecWithOrOperation(@RequestParam(value = "search", required = false) String search) {
        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
        Matcher matcher = getMatcherV2(search, operationSetExper, UserController::getRegExpWithOR);

        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(3),
                    matcher.group(5),
                    matcher.group(4),
                    matcher.group(6)
            );
        }

        Specification<User> specification = builder.build();
        return userRepo.findAll(specification);
    }

    private static Matcher getMatcherV2(
            final String search,
            final String operationSetExper,
            final Function<String, String> regexGet
    ) {
        Pattern pattern = Pattern.compile(regexGet.apply(operationSetExper));
        return pattern.matcher(search + ",");
    }

    private static String getRegExp(final String operationSetExper) {
        return "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),";
    }

    private static String getRegExpWithOR(final String operationSetExper) {
        return "(\\p{Punct}?)(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),";
    }

    private static Matcher getMatcher(final String search) {
        Pattern pattern = Pattern.compile(URL_PARAMETERS_REGEX_MATCHER_STRING);
        return pattern.matcher(search + ",");
    }

}
