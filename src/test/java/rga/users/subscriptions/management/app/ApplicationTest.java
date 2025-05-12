package rga.users.subscriptions.management.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(locations = "/application-test.yml")
@SpringBootTest
class ApplicationTest {

	@Test
	void contextLoads() {
	}

}
