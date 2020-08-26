package com.github.RestQueryLanguage.RestQueryDemo.persistence.dao.dao;


import com.github.RestQueryLanguage.RestQueryDemo.web.utils.SearchCriteria;
import com.github.RestQueryLanguage.RestQueryDemo.persistence.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDAO implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUser(List<SearchCriteria> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);

        Root<User> root = query.from(User.class);

        //create an empty predicate
        Predicate predicate = builder.conjunction();
        UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, builder, root);

        params.forEach(searchConsumer);
        predicate = searchConsumer.getPredicate();
        query.where(predicate);

        TypedQuery<User> query1 = entityManager.createQuery(query);
        return query1.getResultList();
    }

    @Override
    @Transactional
    public void save(User entity) {
        entityManager.persist(entity);
    }
}
