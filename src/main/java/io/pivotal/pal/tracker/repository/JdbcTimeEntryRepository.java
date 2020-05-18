package io.pivotal.pal.tracker.repository;

import io.pivotal.pal.tracker.model.TimeEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;


public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    private RowMapper<TimeEntry> rowMapper = (rs, rowNum) -> {
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setId(rs.getLong("id"));
        timeEntry.setProjectId(rs.getLong("project_id"));
        timeEntry.setUserId(rs.getLong("user_id"));
        timeEntry.setDate(rs.getDate("date").toLocalDate());
        timeEntry.setHours(rs.getInt("hours"));
        return timeEntry;
    };

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO time_entries (project_id, user_id, date, hours) " +
                "VALUES(?,?,?,?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());
            return ps;
        }, keyHolder);

        timeEntry.setId(keyHolder.getKey().longValue());
        return timeEntry;
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "UPDATE time_entries " +
                "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, timeEntry.getProjectId());
            ps.setLong(2, timeEntry.getUserId());
            ps.setDate(3, Date.valueOf(timeEntry.getDate()));
            ps.setInt(4, timeEntry.getHours());
            ps.setLong(5, id);
            return ps;
        }, keyHolder);

        return find(id);
    }

    @Override
    public TimeEntry find(Long id) {
        String sql = "SELECT * FROM time_entries WHERE id = ?";

        List<TimeEntry> result = jdbcTemplate.query(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql);
            ps.setLong(1, id);
            return ps;
        }, rowMapper);

        if (result.size() >= 1) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public List<TimeEntry> list() {
        String sql = "SELECT * FROM time_entries";
        return jdbcTemplate.query(sql, rowMapper);
    }


    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id);
    }
}
