package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

@SpringBootTest
public class BaseIT {

    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    public static Stream<Arguments> getStreamAdminCustomer() {
        return Stream.of(Arguments.of("admin", "admin"),
                Arguments.of("scott", "tiger"));
    }

    public static Stream<Arguments> getStreamAllUsers() {
        return Stream.of(Arguments.of("admin" , "admin"),
                Arguments.of("scott", "tiger"),
                Arguments.of("user", "password"));
    }

    public static Stream<Arguments> getStreamNotAdmin() {
        return Stream.of(Arguments.of("scott", "tiger"),
                Arguments.of("user", "password"));
    }
}
