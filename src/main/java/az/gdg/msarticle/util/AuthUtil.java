package az.gdg.msarticle.util;

import az.gdg.msarticle.exception.InvalidTokenException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    private AuthUtil() {

    }

    public static Authentication getAuthenticatedObject() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new InvalidTokenException("Token is not valid or it is expired");
        }
        return authentication;
    }
}
