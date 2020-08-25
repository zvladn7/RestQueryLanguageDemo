package com.github.RestQueryLanguage.RestQueryDemo.config;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.IUserDAO;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class StarterConfig {

    private final IUserDAO userDAO;

    @Autowired
    public StarterConfig(IUserDAO userDAO) {
        this.userDAO = userDAO;
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
        };
    }

}
