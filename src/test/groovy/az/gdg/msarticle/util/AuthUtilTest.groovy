package az.gdg.msarticle.util

import az.gdg.msarticle.exception.InvalidTokenException
import az.gdg.msarticle.security.UserAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class AuthUtilTest extends Specification {
    void setup() {
    }

    def "should get authenticated object"() {
        given:
            def userAuthentication = new UserAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            AuthUtil.getAuthenticatedObject()
        then:
            notThrown(InvalidTokenException)
    }

    def "should throw InvalidTokenException when user is not authenticate"() {
        given:
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            AuthUtil.getAuthenticatedObject()
        then:
            thrown(InvalidTokenException)
    }
}
