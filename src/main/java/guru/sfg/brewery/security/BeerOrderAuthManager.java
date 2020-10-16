package guru.sfg.brewery.security;

import guru.sfg.brewery.domain.security.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BeerOrderAuthManager {

    public boolean customerIdMatches(Authentication authentication, UUID customerUuid) {
        User authenticatedUser = (User) authentication.getPrincipal();

        log.debug("Authenticated user customer ID: " +
                authenticatedUser.getCustomer().getId() + " Customer ID: " + customerUuid);

        return authenticatedUser.getCustomer().getId().equals(customerUuid);
    }
}
