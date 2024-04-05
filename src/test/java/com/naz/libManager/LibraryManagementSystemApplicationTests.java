package com.naz.libManager;

import com.naz.libManager.controller.AuthController;
import com.naz.libManager.service.serviceImplementation.AuthImplementation;
import com.naz.libManager.service.serviceImplementation.JwtImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LibraryManagementSystemApplicationTests {

	@MockBean
	JwtImplementation jwtImplementation;

	@MockBean
	UserDetailsService userDetailsService;

	@Test
	void contextLoads() {
	}

}
