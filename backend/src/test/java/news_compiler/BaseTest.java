package news_compiler;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for tests.
 * This class contains the MySQL container for testing.
 */
@Testcontainers
public abstract class BaseTest {

    /** The MySQL container for testing. */
    private static final MySQLContainer<?> mySQLContainer;

    /* Starts the MySQL container before any tests are run as a 'singleton'. */
    static {
        mySQLContainer = new MySQLContainer<>("mysql:latest")
                .withDatabaseName("my_test_db")
                .withUsername("my_user")
                .withPassword("my_password");

        mySQLContainer.start();
    }

    /**
     * Sets the properties for the MySQL container.
     * This will allow the application to connect to the MySQL container.
     * @param properties the properties to set for the MySQL container
     */
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry properties){
        properties.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        properties.add("spring.datasource.username", mySQLContainer::getUsername);
        properties.add("spring.datasource.password", mySQLContainer::getPassword);
    }
}
