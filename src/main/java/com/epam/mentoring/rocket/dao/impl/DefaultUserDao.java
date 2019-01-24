package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.UserDao;
import com.epam.mentoring.rocket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Profile("jdbc")
@Repository
public class DefaultUserDao implements UserDao {

    private static final String SELECT_USER_BY_ID = "SELECT * FROM user AS u WHERE u.id = :id;";
    private static final String SELECT_USER_BY_EMAL = "SELECT * FROM user AS u WHERE u.email LIKE :email;";
    private static final String SELECT_ALL_USERS = "SELECT * FROM user;";
    private static final String INSERT_USER = "INSERT INTO user(first_name, last_name, email, password, enabled) VALUES (:firstName, :lastName, :email, :password, :enabled);";
    private static final String UPDATE_USER = "UPDATE user SET first_name = :firstName, last_name = :lastName, email = :email, password = :password, enabled = :enabled WHERE id = :id;";
    private static final String REMOVE_USER = "DELETE FROM user WHERE id = :id;";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_USER_BY_ID, new MapSqlParameterSource("id", id), getRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SELECT_USER_BY_EMAL, new MapSqlParameterSource("email", email), getRowMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USERS, getRowMapper());
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("enabled", user.isEnabled());
        jdbcTemplate.update(INSERT_USER, params, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public void updateUser(User user) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", user.getId());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("enabled", user.isEnabled());
        jdbcTemplate.update(UPDATE_USER, params);
    }

    @Override
    public void removeById(Long id) {
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
            user.setEnabled(rs.getBoolean("enabled"));
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
