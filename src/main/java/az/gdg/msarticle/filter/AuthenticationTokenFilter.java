package az.gdg.msarticle.filter;

import az.gdg.msarticle.Client.AuthenticationClient;
import az.gdg.msarticle.model.Client.Auth.UserInfo;
import az.gdg.msarticle.security.UserAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static az.gdg.msarticle.model.Client.Auth.HttpHeader.X_AUTH_TOKEN;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private AuthenticationClient authenticationClient;

    @Autowired
    public void setAuthenticationClient(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authToken = request.getHeader(X_AUTH_TOKEN);
            if (authToken != null) {
                UserInfo userInfo = authenticationClient.validateToken(authToken);
                if (userInfo == null) {
                    throw new RuntimeException("User info is not valid");
                } else {
                    UserAuthentication userAuthentication = new UserAuthentication(userInfo.getUserId(),
                            true,
                            userInfo.getRole());
                    SecurityContextHolder.getContext().setAuthentication(userAuthentication);
                }
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
