package com.github.RestQueryLanguage.RestQueryDemo;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.UserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.spec.UserSpecification;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.spec.UserSpecificationBuilder;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchOperation;
import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SpecificationSearchCriteria;
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
                new SpecificationSearchCriteria(
                        "lastName", SearchOperation.EQUALITY, "Black", false));

        List<User> results = userRepo.findAll(specification);

        assertTrue(results.contains(userJohn));
        assertTrue(results.contains(userTom));
    }

    @Test
    public void firstNameNotEquals() {
        UserSpecification specification = new UserSpecification(
                new SpecificationSearchCriteria("firstName", SearchOperation.NEGATION, "Tom", false));

        List<User> results = userRepo.findAll(specification);

        assertTrue(results.contains(userJohn));
        assertFalse(results.contains(userTom));
    }

    @Test
    public void firstAndLastNameEquals() {
        UserSpecification spec1 = new UserSpecification(
                new SpecificationSearchCriteria(
                        "firstName", SearchOperation.EQUALITY, "Tom", false));
        UserSpecification spec2 = new UserSpecification(
                new SpecificationSearchCriteria(
                        "lastName", SearchOperation.EQUALITY, "Black", false));

        List<User> results = userRepo.findAll(Specification.where(spec1).and(spec2));

        assertFalse(results.contains(userJohn));
        assertTrue(results.contains(userTom));
    }

    @Test
    public void lastNameEqualsAndAgeLess() {
        UserSpecification spec1 = new UserSpecification(
                new SpecificationSearchCriteria(
                        "firstName", SearchOperation.EQUALITY, "John", false));
        UserSpecification spec2 = new UserSpecification(
                new SpecificationSearchCriteria("age", SearchOperation.LESS_THAN, 23, false));

        List<User> results = userRepo.findAll(Specification.where(spec1).and(spec2));

        assertFalse(results.contains(userTom));
        assertTrue(results.contains(userJohn));
    }

    @Test
    public void emptyResult() {
        UserSpecification spec1 = new UserSpecification(
                new SpecificationSearchCriteria(
                        "firstName", SearchOperation.EQUALITY, "Anton", false));
        UserSpecification spec2 = new UserSpecification(
                new SpecificationSearchCriteria(
                        "lastName", SearchOperation.EQUALITY, "Black", false));

        List<User> results = userRepo.findAll(Specification.where(spec1).and(spec2));

        assertFalse(results.contains(userJohn));
        assertFalse(results.contains(userTom));
    }

    @Test
    public void startWithFirstName() {
        UserSpecification spec = new UserSpecification(
                new SpecificationSearchCriteria("firstName", SearchOperation.STARTS_WITH, "To", false));

        List<User> results = userRepo.findAll(spec);

        assertTrue(results.contains(userTom));
        assertFalse(results.contains(userJohn));
    }

    @Test
    public void endWithFirstName() {
        UserSpecification specification = new UserSpecification(
                new SpecificationSearchCriteria("firstName", SearchOperation.ENDS_WITH, "om", false));

        List<User> results = userRepo.findAll(specification);

        assertTrue(results.contains(userTom));
        assertFalse(results.contains(userJohn));
    }

    @Test
    public void containsInFirstName() {
        UserSpecification specification = new UserSpecification(
                new SpecificationSearchCriteria("firstName", SearchOperation.CONTAINS, "o", false));

        List<User> results = userRepo.findAll(specification);

        assertTrue(results.contains(userTom));
        assertTrue(results.contains(userJohn));
    }

    @Test
    public void ageBetweenValues() {
        UserSpecification spec1 = new UserSpecification(
                new SpecificationSearchCriteria("age", SearchOperation.LESS_THAN, "30", false));

        UserSpecification spec2 = new UserSpecification(
                new SpecificationSearchCriteria("age", SearchOperation.GREATER_THAN, "21", false));

        List<User> results = userRepo.findAll(Specification.where(spec1).and(spec2));

        assertTrue(results.contains(userTom));
        assertFalse(results.contains(userJohn));

    }

    @Test
    public void fistNameEqualsWithOr() {

        SpecificationSearchCriteria spec1 =
                new SpecificationSearchCriteria("firstName", SearchOperation.EQUALITY, "Tom", false);
        SpecificationSearchCriteria spec2 =
                new SpecificationSearchCriteria("firstName", SearchOperation.EQUALITY, "John", "'");

        UserSpecificationBuilder builder = new UserSpecificationBuilder()
                .with(spec1)
                .with(spec2);

        Specification<User> userSpecification = builder.build();

        List<User> results = userRepo.findAll(userSpecification);

        assertTrue(results.contains(userTom));
        assertTrue(results.contains(userJohn));
    }

}
