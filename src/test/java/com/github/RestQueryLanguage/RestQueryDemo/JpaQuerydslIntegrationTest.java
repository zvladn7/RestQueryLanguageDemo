package com.github.RestQueryLanguage.RestQueryDemo;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.MyUserPredicatesBuilder;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.MyUserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.MyUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback
@SpringBootTest
public class JpaQuerydslIntegrationTest {

    @Autowired
    private MyUserRepository repo;

    private MyUser userJohn;

    private MyUser userTom;

    @Before
    public void init() {
        userJohn = new MyUser(
                null,
                "John",
                "Black",
                "john@gmail.com",
                20
        );
        repo.save(userJohn);

        userTom = new MyUser(
                null,
                "Tom",
                "Black",
                "tom@gmail.com",
                25
        );
        repo.save(userTom);
    }

    @Test
    public void lastNameEquals() {
        MyUserPredicatesBuilder builder = new MyUserPredicatesBuilder()
                .with("lastName", ":", "Black");

        BooleanExpression booleanExpression = builder.build();

        List<MyUser> users = repo.findAll(booleanExpression);

        assertTrue(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

    @Test
    public void firstAndLastNameEquals() {
        MyUserPredicatesBuilder builder = new MyUserPredicatesBuilder()
                .with("firstName", ":", "Tom")
                .with("lastName", ":", "Black");

        BooleanExpression booleanExpression = builder.build();

        List<MyUser> users = repo.findAll(booleanExpression);

        assertTrue(users.contains(userTom));
        assertFalse(users.contains(userJohn));
    }

    @Test
    public void lastNameEqualsAndAgeMoreThan() {
        MyUserPredicatesBuilder builder = new MyUserPredicatesBuilder()
                .with("lastName", ":", "Black")
                .with("age", ">", 23);

        BooleanExpression booleanExpression = builder.build();

        List<MyUser> users = repo.findAll(booleanExpression);

        assertTrue(users.contains(userTom));
        assertFalse(users.contains(userJohn));
    }

    @Test
    public void emptyResult() {
        MyUserPredicatesBuilder builder = new MyUserPredicatesBuilder()
                .with("firstName", ":", "Anton")
                .with("lastName", ":", "Black");

        BooleanExpression booleanExpression = builder.build();

        List<MyUser> users = repo.findAll(booleanExpression);

        assertFalse(users.contains(userTom));
        assertFalse(users.contains(userJohn));
    }

}
