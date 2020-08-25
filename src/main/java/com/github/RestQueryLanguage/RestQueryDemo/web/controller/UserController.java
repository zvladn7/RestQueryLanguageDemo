package com.github.RestQueryLanguage.RestQueryDemo.web.controller;


import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.IUserDAO;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.UserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.UserSpecificationBuilder;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    private static final String URL_PARAMETERS_REGEX_MATCHER_STRING = "(\\w+?)([:<>])(\\w+?),";

    @Autowired
    private IUserDAO api;

    @Autowired
    private UserRepository userRepo;

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
        List<SearchCriteria> params = new ArrayList<>();
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

    private static final Matcher getMatcher(String search) {
        Pattern pattern = Pattern.compile(URL_PARAMETERS_REGEX_MATCHER_STRING);
        return pattern.matcher(search + ",");
    }

}
