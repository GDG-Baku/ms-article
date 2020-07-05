package az.gdg.msarticle.filter;

import az.gdg.msarticle.exception.WrongDataException;
import az.gdg.msarticle.model.client.auth.UserInfo;
import az.gdg.msarticle.security.UserAuthentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import az.gdg.msarticle.service.MsAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static az.gdg.msarticle.model.client.auth.HttpHeader.X_AUTH_TOKEN;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private MsAuthService msAuthService;

    @Autowired
    public void setAuthenticationClient(MsAuthService msAuthService) {
        this.msAuthService = msAuthService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authToken = request.getHeader(X_AUTH_TOKEN);
            if (authToken != null) {
                UserInfo userInfo = msAuthService.getUserInfo(authToken);
                if (userInfo == null) {
                    throw new WrongDataException("User info is not valid");
                } else {
                    UserAuthentication userAuthentication = new UserAuthentication(userInfo.getUserId(),
                            true);
                    SecurityContextHolder.getContext().setAuthentication(userAuthentication);
                }
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}