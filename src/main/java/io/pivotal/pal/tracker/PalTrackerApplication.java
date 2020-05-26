package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.repository.JdbcTimeEntryRepository;
import io.pivotal.pal.tracker.repository.TimeEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class PalTrackerApplication {

    @Autowired
    DataSource dataSource;



    public static void main(String[] args) {
        SpringApplication.run(PalTrackerApplication.class, args);
    }

    @Bean
    public TimeEntryRepository timeEntryRepository() {
        return new JdbcTimeEntryRepository(dataSource);
    }

    /*
    // This was original code.
    // It injects SPRING_DATASOURCE_URL into a new DataSource

    // Thanks Prajina!!!  It looks like SPRING_DATASOURCE_URL is not autowired by pivotal cloud.
    // Autowiring the dataSource itself instead of creating an instance looks like it works.
    @Bean
    public TimeEntryRepository timeEntryRepository() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(System.getenv("SPRING_DATASOURCE_URL"));
        return new JdbcTimeEntryRepository(dataSource);
    }
     */
}