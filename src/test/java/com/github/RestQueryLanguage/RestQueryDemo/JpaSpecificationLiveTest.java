package com.github.RestQueryLanguage.RestQueryDemo;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.UserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RestQueryDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JpaSpecificationLiveTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RestTemplate restTemplate;

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

    private static final String BASIC_URL_FOR_TEST_START = "http://localhost:";
    private static final String BASIC_URL_FOR_TEST_END = "/users/v4?search=";
    private static final String BASIC_URL_FOR_TEST_END_WITH_OR = "/users/v5?search=";

    @Test
    public void firstAndLastNameEquals() {
        List objects = restTemplate.getForObject(
                getUrl() + "firstName:Tom",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertTrue(users.contains(userTom));
        assertFalse(users.contains(userJohn));
    }

    @Test
    public void negationTest() {
        List objects = restTemplate.getForObject(
                getUrl() + "firstName!Tom",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertTrue(users.contains(userJohn));
        assertFalse(users.contains(userTom));
    }

    @Test
    public void testGreaterThan() {
        List objects = restTemplate.getForObject(
                getUrl() + "age>25",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertFalse(users.contains(userTom));
        assertFalse(users.contains(userJohn));
    }

    @Test
    public void testStartsWith() {
        List objects = restTemplate.getForObject(
                getUrl() + "firstName:Jo*",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertTrue(users.contains(userJohn));
        assertFalse(users.contains(userTom));
    }

    @Test
    public void testEndsWith() {
        List objects = restTemplate.getForObject(
                getUrl() + "firstName:*om",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertTrue(users.contains(userTom));
        assertFalse(users.contains(userJohn));
    }

    @Test
    public void testContains() {
        List objects = restTemplate.getForObject(
                getUrl() + "firstName:*o*",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertTrue(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

    @Test
    public void testRange() {
        List objects = restTemplate.getForObject(
                getUrl() + "age>19,age<26",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertTrue(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

    @Test
    public void firstNameWithOr() {
        List objects = restTemplate.getForObject(
                getUrlWithFunc() + "firstName=Tom,'firstName=John",
                List.class
        );

        assert objects != null;
        List<User> users = convertList(objects);

        assertTrue(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

    private static List<User> convertList(final List results) {
        List<User> users = new ArrayList<>();
        for (Object o : results) {
            LinkedHashMap map = (LinkedHashMap) o;
            User user = convertToUser(map);
            users.add(user);
        }

        return users;
    }

    private static User convertToUser(final LinkedHashMap map) {
        final long id = (Integer) map.get("id");
        final String firstName = (String) map.get("firstName");
        final String lastName = (String) map.get("lastName");
        final String email = (String) map.get("email");
        final int age = (Integer) map.get("age");
        return new User(
                id,
                firstName,
                lastName,
                email,
                age
        );
    }

    private String getUrl() {
        return BASIC_URL_FOR_TEST_START + port + BASIC_URL_FOR_TEST_END;
    }

    private String getUrlWithFunc() {
        return BASIC_URL_FOR_TEST_START + port + BASIC_URL_FOR_TEST_END_WITH_OR;
    }
}
