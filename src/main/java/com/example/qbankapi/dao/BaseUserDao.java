package com.example.qbankapi.dao;

import com.example.qbankapi.entity.BaseUser;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class BaseUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<BaseUser> findByUsername(String username) {
        List<BaseUser> baseUserList = entityManager.createQuery("SELECT u FROM BaseUser u WHERE u.username = :username", BaseUser.class).setParameter("username", username).getResultList();
        return baseUserList.isEmpty() ? Optional.empty() : Optional.of(baseUserList.getFirst());
    }

    public void save(BaseUser baseUser) {
        entityManager.persist(baseUser);
    }

    public Optional<BaseUser> findByEmail(String email) {
        List<BaseUser> baseUserList = entityManager.createQuery("SELECT u FROM BaseUser u WHERE u.email = :email", BaseUser.class).setParameter("email", email).getResultList();
        return baseUserList.isEmpty() ? Optional.empty() : Optional.of(baseUserList.getFirst());
    }

//    public BaseUserPageViewDto findFilteredUsers(BaseUserFilterDto filter) {
//        StringBuilder sql = new StringBuilder("SELECT b FROM BaseUser b WHERE 1=1");
//        Map<String, Object> parameters = new HashMap<>();
//
//        if (!filter.getRole().equals("ALL")) {
//            sql.append(" AND b.roleValue = :role");
//            parameters.put("role", filter.getRole());
//        }
//
//        if (!filter.getStatus().equals("ALL")) {
//            sql.append(" AND b.status = :status");
//            parameters.put("status", BaseUser.Status.valueOf(filter.getStatus()));
//        }
//
//        if (!filter.getUsernameRegxPattern().equals("")) {
//            sql.append(" AND b.username LIKE :usernamePattern");
//            parameters.put("usernamePattern", filter.getUsernameRegxPattern());
//        }
//
//        sql.append(" ORDER BY b.").append(filter.getSortBy()).append(" ").append(filter.getSortOrder()).append(", b.id ASC");
//
//        TypedQuery<BaseUser> query = entityManager.createQuery(sql.toString(), BaseUser.class);
//        parameters.forEach(query::setParameter);
//
//        query.setFirstResult(filter.getPage() * filter.getPageSize());
//        query.setMaxResults(filter.getPageSize() + 1);
//
//        List<BaseUser> results = query.getResultList();
//
//        boolean hasNext = results.size() > filter.getPageSize();
//
//        if (hasNext) results.remove(results.size() - 1);
//
//        List<BaseUserViewDto> baseUserDetailsDtoList = results.stream().map(user -> BaseUserViewDto.builder().id(user.getId()).username(user.getUsername()).email(user.getEmail()).role(user.getRole()).status(user.getStatus()).build()).toList();
//        BaseUserPageViewDto baseUserViewPage = new BaseUserPageViewDto();
//        baseUserViewPage.setBaseUsers(baseUserDetailsDtoList);
//        baseUserViewPage.setPage(filter.getPage());
//        baseUserViewPage.setPageSize(filter.getPageSize());
//        baseUserViewPage.setHasNextPage(hasNext);
//        return baseUserViewPage;
//    }

    public Optional<BaseUser> findById(Long id) {
        List<BaseUser> userList = entityManager.createQuery("SELECT u FROM BaseUser u WHERE u.id = :id", BaseUser.class).setParameter("id", id).getResultList();
        return userList.isEmpty() ? Optional.empty() : Optional.of(userList.getFirst());
    }

    public void update(BaseUser baseUser) {
        entityManager.merge(baseUser);
    }

}
