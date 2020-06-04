package az.gdg.msarticle.service.impl

import az.gdg.msarticle.exception.ArticleNotFoundException
import az.gdg.msarticle.exception.InvalidTokenException
import az.gdg.msarticle.exception.NoAccessException
import az.gdg.msarticle.exception.TypeNotFoundException
import az.gdg.msarticle.model.ArticleRequest
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.repository.TagRepository
import az.gdg.msarticle.security.UserAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class ArticleServiceImplTest extends Specification {
    def articleRepository
    def tagRepository
    def mailService
    def articleService

    void setup() {
        articleRepository = Mock(ArticleRepository)
        tagRepository = Mock(TagRepository)
        mailService = Mock(MailServiceImpl)
        articleService = new ArticleServiceImpl(articleRepository, tagRepository, mailService)
    }

    def "should update article"() {
        given:
            def articleId = "1"
            def articleRequest = new ArticleRequest()

            articleRequest.setType("ARTICLE")
            articleRequest.setTags(Collections.emptyList())

            def articleEntity = new ArticleEntity()
            articleEntity.setId(articleId)
            articleEntity.setUserId(2)

            articleRepository.findById(articleId) >> Optional.of(articleEntity)
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            articleService.updateArticle(articleId, articleRequest)
        then:
            1 * articleRepository.save(articleEntity)
            1 * mailService.sendToQueue(_)
    }

    def "should throw ArticleNotFoundException when article doesn't exist with given id"() {
        given:
            def articleId = "1"
            def articleRequest = new ArticleRequest()
            articleRepository.findById(articleId) >> Optional.empty()
            def userAuthentication = new UserAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            articleService.updateArticle(articleId, articleRequest)
        then:
            thrown(ArticleNotFoundException)
    }

    def "should throw NoAccessException when article doesn't belong to user"() {
        given:
            def articleId = "1"
            def articleRequest = new ArticleRequest()

            def articleEntity = new ArticleEntity()
            articleEntity.setUserId(2)

            articleRepository.findById(articleId) >> Optional.of(articleEntity)
            def userAuthentication = new UserAuthentication("3", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            articleService.updateArticle(articleId, articleRequest)
        then:
            thrown(NoAccessException)
    }

    def "should throw InvalidTokenException when user is not authenticated"() {
        given:
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            articleService.getAuthenticatedObject()
        then:
            thrown(InvalidTokenException)
    }

    def "should return authenticated object"() {
        given:
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            articleService.getAuthenticatedObject()
        then:
            notThrown(InvalidTokenException)
    }

    def "should return value of article or forum or news"() {
        given:
            def type = "article"
        when:
            def value = articleService.getValueOfType(type)
        then:
            value == 1
            notThrown(TypeNotFoundException)
    }

    def "should throw TypeNotFoundException when not defined type is requesting"() {
        given:
            def type = ""
        when:
            articleService.getValueOfType(type)
        then:
            thrown(TypeNotFoundException)
    }

}
