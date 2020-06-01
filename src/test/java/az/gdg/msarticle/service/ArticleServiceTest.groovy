package az.gdg.msarticle.service

import az.gdg.msarticle.exception.ExceedLimitException
import az.gdg.msarticle.exception.NoSuchArticleException
import az.gdg.msarticle.exception.NotValidTokenException
import az.gdg.msarticle.exception.UnauthorizedAccessException
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.model.entity.CommentEntity
import az.gdg.msarticle.model.entity.TagEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.security.UserAuthentication
import az.gdg.msarticle.service.impl.ArticleServiceImpl
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

import java.time.LocalDateTime

class ArticleServiceTest extends Specification {
    ArticleRepository articleRepository
    ArticleServiceImpl articleServiceImpl
    MsAuthService msAuthService
    
    def setup() {
        articleRepository = Mock()
        msAuthService = Mock()
        articleServiceImpl = new ArticleServiceImpl(articleRepository, msAuthService)
    }
    
    def "should use the repository to add article hateCount by id"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def token = "wdasadadada"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def remainingHateCount = 500
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("15", true, "USER")
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.addHateByArticleId(articleId, token)
        
        then:
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            1 * msAuthService.getRemainingHateCount(token) >> remainingHateCount
            1 * articleRepository.save(articleEntity)
            1 * msAuthService.updateRemainingHateCount(token)
            articleEntity.hateCount == 6
    }
    
    def "should throw NoSuchArticleException if no such article"() {
        given:
            def articleId = "dasdpksapdksaop"
            def token = "dsad"
        
        when:
            articleServiceImpl.addHateByArticleId(articleId, token)
        
        then:
            1 * articleRepository.findById(articleId) >> Optional.empty()
            thrown(NoSuchArticleException)
    }
    
    def "should throw NotValidTokenException if not logged"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def token = "wdasadadada"
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.addHateByArticleId(articleId, token)
        
        then:
            thrown(NotValidTokenException)
    }
    
    def "should throw NotValidTokenException if it's own article"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def token = "wdasadadada"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def remainingHateCount = 500
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("41", true, "USER")
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.addHateByArticleId(articleId, token)
        
        then:
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            1 * msAuthService.getRemainingHateCount(token) >> remainingHateCount
            thrown(UnauthorizedAccessException)
    }
    
    def "should throw ExceedLimitException if all hates were used"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def token = "wdasadadada"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def remainingHateCount = 0
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("10", true, "USER")
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.addHateByArticleId(articleId, token)
        
        then:
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            1 * msAuthService.getRemainingHateCount(token) >> remainingHateCount
            thrown(ExceedLimitException)
    }
    
}
