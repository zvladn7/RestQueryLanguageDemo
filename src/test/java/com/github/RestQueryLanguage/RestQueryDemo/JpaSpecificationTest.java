package com.github.RestQueryLanguage.RestQueryDemo;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.UserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.spec.UserSpecification;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class JpaSpecificationTest {

    @Autowired
    private UserRepository userRepo;

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
        userRepo.save(userJohn);

        userTom = new User(
                null,
                "Tom",
                "Black",
                "tom@gmail.com",
                25
        );
        userRepo.save(userTom);
    }

    @Test
    public void lastNameEquals() {
        UserSpecification specification = new UserSpecification(
                new SearchCriteria("lastName", ":", "Black"));

        List<User> results = userRepo.findAll(specification);

        assertTrue(results.contains(userJohn));
        assertTrue(results.contains(userTom));
    }

    @Test
    public void firstAndLastNameEquals() {
        UserSpecification spec1 = new UserSpecification(
                new SearchCriteria("firstName", ":", "Tom"));
        UserSpecification spec2 = new UserSpecification(
                new SearchCriteria("lastName", ":", "Black"));

        List<User> results = userRepo.findAll(Specification.where(spec1).and(spec2));

        assertFalse(results.contains(userJohn));
        assertTrue(results.contains(userTom));
    }

    @Test
    public void lastNameEqualsAndAgeLess() {
        UserSpecification spec1 = new UserSpecification(
                new SearchCriteria("firstName", ":", "John"));
        UserSpecification spec2 = new UserSpecification(
                new SearchCriteria("age", "<", 23));

        List<User> results = userRepo.findAll(Specification.where(spec1).and(spec2));

        assertFalse(results.contains(userTom));
        assertTrue(results.contains(userJohn));
    }

    @Test
    public void emptyResult() {
        UserSpecification spec1 = new UserSpecification(
                new SearchCriteria("firstName", ":", "Anton"));
        UserSpecification spec2 = new UserSpecification(
                new SearchCriteria("lastName", ":", "Black"));

        List<User> results = userRepo.findAll(Specification.where(spec1).and(spec2));

        assertFalse(results.contains(userJohn));
        assertFalse(results.contains(userTom));
    }

    @Test
    public void givenOnlyPartOfFirstName() {
        UserSpecification spec = new UserSpecification(
                new SearchCriteria("firstName", ":", "To"));

        List<User> results = userRepo.findAll(spec);

        assertTrue(results.contains(userTom));
        assertFalse(results.contains(userJohn));
    }

}
