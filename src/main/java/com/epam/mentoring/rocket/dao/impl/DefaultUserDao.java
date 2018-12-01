package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DefaultUserDao implements UserDao {

    private static final String SELECT_USER = "SELECT * FROM user AS u WHERE u.id = :id;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM user;";
    private static final String INSERT_USER = "INSERT INTO user(first_name, last_name, email, password) VALUES (:firstName, :lastName, :email, :password);";
    private static final String UPDATE_USER = "UPDATE user SET first_name = :firstName, last_name = :lastName, email = :email, password = :password WHERE id = :id;";
    private static final String REMOVE_USER = "DELETE FROM user WHERE id = :id;";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public User getUserById(Long id) {
        return jdbcTemplate.queryForObject(SELECT_USER, new MapSqlParameterSource("id", id), getRowMapper());
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(SELECT_ALL_USERS, getRowMapper());
    }

    @Override
    public void insertUser(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        jdbcTemplate.update(INSERT_USER, params);
    }

    @Override
    public void updateUser(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        jdbcTemplate.update(UPDATE_USER, params);
    }

    @Override
    public void removeUser(Long id) {
        jdbcTemplate.update(REMOVE_USER, new MapSqlParameterSource("id", id));
    }

    private RowMapper<User> getRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        };
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
