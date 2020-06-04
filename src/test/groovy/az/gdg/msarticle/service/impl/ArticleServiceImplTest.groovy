package az.gdg.msarticle.service.impl

import az.gdg.msarticle.exception.ArticleNotFoundException
import az.gdg.msarticle.exception.InvalidTokenException
import az.gdg.msarticle.exception.NoAccessException
import az.gdg.msarticle.exception.NoDraftedArticleExist
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.security.UserAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class ArticleServiceImplTest extends Specification {

    def articleRepository
    def mailService
    def articleService

    void setup() {
        articleRepository = Mock(ArticleRepository)
        mailService = Mock(MailServiceImpl)
        articleService = new ArticleServiceImpl(articleRepository, mailService)
    }

    def "should send email while trying to publish article"() {
        given:
            def articleId = "1"
            def articleEntity = new ArticleEntity()
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleEntity.setId(articleId)
            articleEntity.setUserId(2)
            articleEntity.setDraft(true)
            articleRepository.findById(articleId) >> Optional.of(articleEntity)
        when:
            articleService.publishArticle(articleId)
        then:
            1 * mailService.sendToQueue(_)
            notThrown(exception)
        where:
            exception << [ArticleNotFoundException, NoAccessException, NoDraftedArticleExist]
    }

    def "should throw NoAccessException when not authorized user tries to publish"() {
        given:
            def articleId = "1"
            def articleEntity = new ArticleEntity()
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleEntity.setId(articleId)
            articleEntity.setUserId(3)
            articleRepository.findById(articleId) >> Optional.of(articleEntity)
        when:
            articleService.publishArticle(articleId)
        then:
            thrown(NoAccessException)
    }

    def "should throw ArticleNotFoundException when article doesn't exist"() {
        given:
            def articleId = "1"
            def userAuthentication = new UserAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleRepository.findById(articleId) >> Optional.empty()
        when:
            articleService.publishArticle(articleId)
        then:
            thrown(ArticleNotFoundException)
    }

    def "should throw NoDraftedArticleExist when article is already published"() {
        given:
            def articleId = "1"
            def articleEntity = new ArticleEntity()
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleEntity.setId(articleId)
            articleEntity.setUserId(2)
            articleEntity.setDraft(false)
            articleRepository.findById(articleId) >> Optional.of(articleEntity)
        when:
            articleService.publishArticle(articleId)
        then:
            thrown(NoDraftedArticleExist)
    }

    def "should get authenticated object"() {
        given:
            def userAuthentication = new UserAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            articleService.getAuthenticatedObject()
        then:
            notThrown(InvalidTokenException)
    }

    def "should throw NotValidTokenException when calling getAuthenticatedObject method"() {
        given:
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            articleService.getAuthenticatedObject()
        then:
            0 * SecurityContextHolder.getContext().getAuthentication()
            thrown(InvalidTokenException)
    }
}
