package com.epam.mentoring.rocket.dao.impl;

import com.epam.mentoring.rocket.dao.AuthorityDao;
import com.epam.mentoring.rocket.model.Authority;
import com.epam.mentoring.rocket.model.AuthorityName;
import com.epam.mentoring.rocket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Profile("jdbc")
@Repository
public class DefaultAuthorityDao implements AuthorityDao {

    private static final String SELECT_AUTHORITY_BY_NAME = "SELECT * FROM authority AS a WHERE a.name LIKE :name";

    private static final String SELECT_AUTHORITIES_BY_USER_ID = "SELECT * FROM user_authority AS ua JOIN authority AS a" +
            " ON ua.authority_id = a.id WHERE ua.user_id = :id;";

    private static final String INSERT_AUTHORITY_FOR_USER = "INSERT INTO user_authority (user_id, authority_id) VALUES (:userId, :authorityId);";
    private static final String DELETE_AUTHORITY_FOR_USER = "DELETE FROM user_authority WHERE user_authority.user_id = :userId AND user_authority.authority_id = :authorityId;";

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Authority getAuthorityByName(AuthorityName authorityName) {
        try {
            return jdbcTemplate.queryForObject(SELECT_AUTHORITY_BY_NAME,
                    new MapSqlParameterSource("name", authorityName.name()), getRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Authority> getAuthoritiesByUserId(Long userId) {
        return jdbcTemplate.query(SELECT_AUTHORITIES_BY_USER_ID,
                new MapSqlParameterSource("id", userId), getRowMapper());
    }

    @Override
    public int insertAuthorityForUser(Authority authority, User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", user.getId())
                .addValue("authorityId", authority.getId());
        return jdbcTemplate.update(INSERT_AUTHORITY_FOR_USER, params);
    }

    @Override
    public int removeAuthorityForUser(Authority authority, User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", user.getId())
                .addValue("authorityId", authority.getId());
        return jdbcTemplate.update(DELETE_AUTHORITY_FOR_USER, params);
    }

    private RowMapper<Authority> getRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            Authority authority = new Authority();
            authority.setId(rs.getLong("id"));
            authority.setName(AuthorityName.valueOf(rs.getString("name")));
            return authority;
        };
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
