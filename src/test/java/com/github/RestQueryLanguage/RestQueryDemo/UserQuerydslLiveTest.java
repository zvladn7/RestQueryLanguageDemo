package com.github.RestQueryLanguage.RestQueryDemo;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.MyUserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.MyUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
//@Rollback
@SpringBootTest(classes = RestQueryDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserQuerydslLiveTest {

    private static final String QUERY_URL_START_WITHOUT_PORT = "http://localhost:";
    private static final String QUERY_URL_END_WITHOUT_PORT = "/users/v6";

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

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

    private String getBaseUrl() {
        return QUERY_URL_START_WITHOUT_PORT + port + QUERY_URL_END_WITHOUT_PORT;
    }


    private static List<MyUser> convertList(final List results) {
        List<MyUser> users = new ArrayList<>();
        for (Object o : results) {
            LinkedHashMap map = (LinkedHashMap) o;
            MyUser user = convertToUser(map);
            users.add(user);
        }

        return users;
    }

    private static MyUser convertToUser(final LinkedHashMap map) {
        final long id = (Integer) map.get("id");
        final String firstName = (String) map.get("firstName");
        final String lastName = (String) map.get("lastName");
        final String email = (String) map.get("email");
        final int age = (Integer) map.get("age");
        return new MyUser(
                id,
                firstName,
                lastName,
                email,
                age
        );
    }

    @Test
    public void getAll() {
        List objects = restTemplate.getForObject(
                getBaseUrl(),
                List.class
        );

        assert objects != null;
        List<MyUser> users = convertList(objects);

        assertTrue(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

    @Test
    public void firstNameEquals() {
        List objects = restTemplate.getForObject(
                getBaseUrl() + "?firstName=john",
                List.class
        );

        assert objects != null;
        List<MyUser> users = convertList(objects);

        assertFalse(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

    @Test
    public void partialLastName() {
        List objects = restTemplate.getForObject(
                getBaseUrl() + "?lastName=Bla",
                List.class
        );

        assert objects != null;
        List<MyUser> users = convertList(objects);

        assertTrue(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

    @Test
    public void byEmailEquals() {
        List objects = restTemplate.getForObject(
                getBaseUrl() + "?email=john@gmail.com",
                List.class
        );

        assert objects != null;
        List<MyUser> users = convertList(objects);

        assertFalse(users.contains(userTom));
        assertTrue(users.contains(userJohn));
    }

}
