package com.example.chess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessApplicationTests {

	@Test
	@WithAnonymousUser
	void AnonymousUserTest(@Autowired TestRestTemplate  testRestTemplate) {
		String body = testRestTemplate.getForObject("/test", String.class);
		assertEquals("should be anonymous user",  "anonymousUser", body);
	}
}
