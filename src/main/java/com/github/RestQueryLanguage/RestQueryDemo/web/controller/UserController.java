package com.github.RestQueryLanguage.RestQueryDemo.web.controller;


import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.IUserDAO;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @Autowired
    private IUserDAO api;

    @GetMapping("/users")
    public List<User> findAll(@RequestParam(value = "search", required = false) String search) {
        List<SearchCriteria> params = new ArrayList<>();
        if (search != null) {
            String stringPattern = "(\\w+?)([:<>])(\\w+?),";
            Pattern pattern = Pattern.compile(stringPattern);
            Matcher matcher = pattern.matcher(search + ",");
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

}
