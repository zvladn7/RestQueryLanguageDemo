package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.repo;

import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

//We're extending the JpaSpecificationExecutor to get the new Specification APIs
//by the specification which we provided in UserSpecification class
public interface UserRepository
        extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
