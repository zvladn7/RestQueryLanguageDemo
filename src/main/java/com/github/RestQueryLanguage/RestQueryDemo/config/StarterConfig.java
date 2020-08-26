package com.github.RestQueryLanguage.RestQueryDemo.config;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.dao.IUserDAO;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo.MyUserRepository;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.MyUser;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StarterConfig {

    private final IUserDAO userDAO;
    private final MyUserRepository repo;

    @Autowired
    public StarterConfig(
            final IUserDAO userDAO,
            final MyUserRepository repo
    ) {
        this.userDAO = userDAO;
        this.repo = repo;
    }

    @Bean
    CommandLineRunner runner() throws InterruptedException {
        Thread.sleep(2000);
        return args -> {
            userDAO.save(new User(
                    null,
                    "John",
                    "Black",
                    "john@gmail.com",
                    20
            ));

            userDAO.save(new User(
                    null,
                    "Tom",
                    "Black",
                    "tom@gmail.com",
                    25
            ));


            repo.save(new MyUser(
                    null,
                    "John",
                    "Black",
                    "john@gmail.com",
                    20
            ));

            repo.save(new MyUser(
                    null,
                    "Tom",
                    "Black",
                    "tom@gmail.com",
                    25
            ));
        };
    }

}
