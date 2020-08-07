package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncodingTest {

    private static final String PASSWORD = "password";

    @Test
    void testBCrypt15() {
        PasswordEncoder bCrypt = new BCryptPasswordEncoder(15);

        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode("tiger"));
    }

    @Test
    void testBCrypt() {
        PasswordEncoder bCrypt = new BCryptPasswordEncoder();

        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode("tiger"));
    }

    @Test
    void testSha256() {
        PasswordEncoder encoder = new StandardPasswordEncoder();

        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode(PASSWORD));
        System.out.println(encoder.encode("tiger"));
    }

    @Test
    void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode("tiger"));

        String encode = ldap.encode(PASSWORD);
        assertTrue(ldap.matches(PASSWORD, encode));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void hashingExample() {
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        var salted = PASSWORD + "ThisIsMySALT";
        System.out.println(DigestUtils.md5DigestAsHex(salted.getBytes()));
    }
}
