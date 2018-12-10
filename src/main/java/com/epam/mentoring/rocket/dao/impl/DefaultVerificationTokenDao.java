package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.VerificationTokenDao;
import com.epam.mentoring.rocket.model.User;
import com.epam.mentoring.rocket.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;

@Repository
public class DefaultVerificationTokenDao implements VerificationTokenDao {

    private static final String INSERT_TOKEN = "INSERT INTO token (token, user) VALUES (:token, :userId);";
    private static final String SELECT_BY_TOKEN = "SELECT * FROM token AS t WHERE t.token LIKE :token;";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public VerificationToken getByToken(String token) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_TOKEN, new MapSqlParameterSource("token", token), getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public VerificationToken insertToken(VerificationToken token) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("token", token.getToken())
                .addValue("userId", token.getUser().getId());
        jdbcTemplate.update(INSERT_TOKEN, params, keyHolder);
        token.setId(keyHolder.getKey().longValue());
        return token;
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<VerificationToken> getRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            VerificationToken token = new VerificationToken();
            token.setId(rs.getLong("id"));
            token.setToken(rs.getString("token"));
            User user = new User();
            user.setId(rs.getLong("user"));
            token.setUser(user);
            return token;
        };
    }
}
