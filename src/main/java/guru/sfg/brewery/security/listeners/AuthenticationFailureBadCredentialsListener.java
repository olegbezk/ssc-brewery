package guru.sfg.brewery.security.listeners;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.LoginFailureRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureBadCredentialsListener {

    private final LoginFailureRepository loginFailureRepository;
    private final UserRepository userRepository;

    @EventListener
    public void listen(AuthenticationFailureBadCredentialsEvent event) {
        log.debug("User With Bad Credentials.");

        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {

            LoginFailure.LoginFailureBuilder builder = LoginFailure.builder();

            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) event.getSource();

            if (token.getPrincipal() instanceof String) {
                Object principal = token.getPrincipal();

                log.debug("Attempted Username: " + principal);

                builder.userName((String) principal);

                userRepository.findByUsername((String) principal).ifPresent(builder::user);
            }

            if (token.getDetails() instanceof WebAuthenticationDetails) {

                WebAuthenticationDetails details = (WebAuthenticationDetails) token.getDetails();

                String remoteAddress = details.getRemoteAddress();

                builder.sourceIp(remoteAddress);

                log.debug("Source IP: " + remoteAddress);
            }

            LoginFailure loginFailure = loginFailureRepository.save(builder.build());

            log.debug("Login failure user ID: " + loginFailure.getId());

            if (loginFailure.getUser() != null) {
                lockUserAccount(loginFailure.getUser());
            }
        }
    }

    private void lockUserAccount(User user) {
        List<LoginFailure> allByUserAndCreatedDateIsAfter = loginFailureRepository.findAllByUserAndCreatedDateIsAfter(
                user, Timestamp.valueOf(LocalDateTime.now().minusDays(1)));

        if (allByUserAndCreatedDateIsAfter.size() > 3) {
            log.debug("Locking User Account...");
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }
}
