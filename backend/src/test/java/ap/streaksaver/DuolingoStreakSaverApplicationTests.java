package ap.streaksaver;

import eu.alessandropinna.streaksaver.DuolingoStreakSaverApplication;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = DuolingoStreakSaverApplication.class)
class DuolingoStreakSaverApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void contextLoads() {
		assertTrue(ObjectUtils.allNotNull(mockMvc));
	}

}
