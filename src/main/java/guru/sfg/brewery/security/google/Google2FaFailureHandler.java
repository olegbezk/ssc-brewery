package guru.sfg.brewery.security.google;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class Google2FaFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException exception
    ) throws IOException, ServletException {

        log.debug("Forward 2FA");

        httpServletRequest.getRequestDispatcher("/user/verify2fa")
                .forward(httpServletRequest, httpServletResponse);
    }
}
