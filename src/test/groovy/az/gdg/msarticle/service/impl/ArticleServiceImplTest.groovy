package az.gdg.msarticle.service.impl

import az.gdg.msarticle.exception.InvalidTokenException
import az.gdg.msarticle.mapper.ArticleMapper
import az.gdg.msarticle.model.ArticleRequest
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.repository.TagRepository
import az.gdg.msarticle.security.UserAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Title

@Title("Testing for article service implementation")
class ArticleServiceImplTest extends Specification {

    ArticleRepository articleRepository
    TagRepository tagRepository
    ArticleServiceImpl articleService

    def setup() {
        articleRepository = Mock()
        tagRepository = Mock()
        articleService = new ArticleServiceImpl(articleRepository, tagRepository)
    }

    def "add draft"() {
        given:
            def token = "dssfffs"
            def articleRequest = new ArticleRequest()
            articleRequest.setType("ARTICLE")
            def draft = ArticleMapper.INSTANCE.requestToEntity(articleRequest)
            draft.setUserId(1)
            draft.setDraft(true)
            draft.setReadCount(0)
            draft.setHateCount(0)
            draft.setQuackCount(0)
            draft.setApproved(false)
            draft.setApproverId(null)
            draft.setComments(Collections.emptyList())
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)

        when:
            articleService.addDraft(token, articleRequest)

        then:
            1 * tagRepository.saveAll(draft.getTags())
            1 * articleRepository.save(draft)


    }

    def "should throw InvalidTokenException if token is invalid"() {
        given:
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)

        when:
            articleService.getAuthenticatedObject()

        then:
            thrown(InvalidTokenException)


    }

    def "get authenticated object if token is valid"() {
        given:
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)

        when:
            articleService.getAuthenticatedObject()

        then:
            SecurityContextHolder.getContext().getAuthentication() == userAuthentication
            notThrown(InvalidTokenException)


    }


}
