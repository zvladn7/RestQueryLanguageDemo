package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao;

import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;

import java.util.List;

public interface IUserDAO {

    List<User> searchUser(List<SearchCriteria> params);

    void save(User entity);


}
