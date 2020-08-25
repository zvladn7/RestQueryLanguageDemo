package com.github.RestQueryLanguage.RestQueryDemo;


import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.IUserDAO;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback
@SpringBootTest
public class JPACriteriaQueryTest {

    @Autowired
    private IUserDAO userDAO;

    private User userJohn;

    private User userTom;

    @Before
    public void init() {
        userJohn = new User(
                null,
                "John",
                "Black",
                "john@gmail.com",
                20
        );
        userDAO.save(userJohn);

        userTom = new User(
                null,
                "Tom",
                "Black",
                "tom@gmail.com",
                25
        );
        userDAO.save(userTom);
    }


    @Test
    public void firstAndLastNameEquals() {
        List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("firstName", ":", "John"));
        params.add(new SearchCriteria("lastName", ":", "Black"));

        List<User> results = userDAO.searchUser(params);

        assertTrue(results.contains(userJohn));
        assertFalse(results.contains(userTom));
    }

    @Test
    public void lastNameEquals() {
        List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("lastName", ":", "Black"));

        List<User> results = userDAO.searchUser(params);

        assertTrue(results.contains(userJohn));
        assertTrue(results.contains(userTom));
    }


    @Test
    public void moreThan23Age() {
        List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("age", ">", 23));

        List<User> results = userDAO.searchUser(params);

        assertTrue(results.contains(userTom));
        assertFalse(results.contains(userJohn));
    }

    @Test
    public void emptyResult() {
        List<SearchCriteria> params = new ArrayList<>();
        params.add(new SearchCriteria("firstName", ":", "Vlad"));

        List<User> results = userDAO.searchUser(params);

        assertFalse(results.contains(userJohn));
        assertFalse(results.contains(userTom));
    }

}
