package az.gdg.msarticle.service

import az.gdg.msarticle.exception.UnauthorizedAccessException
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.model.entity.CommentEntity
import az.gdg.msarticle.model.entity.TagEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.repository.CommentRepository
import az.gdg.msarticle.security.UserAuthentication
import az.gdg.msarticle.service.impl.ArticleServiceImpl
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

import java.time.LocalDateTime

class ArticleServiceTest extends Specification {
    ArticleRepository articleRepository
    ArticleServiceImpl articleServiceImpl
    CommentRepository commentRepository
    
    def setup() {
        articleRepository = Mock()
        commentRepository = Mock()
        articleServiceImpl = new ArticleServiceImpl(articleRepository, commentRepository)
    }
    
    def "should use the repository to delete article by id"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("41", true, "USER")
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.deleteArticleById(articleId)
        
        then: "get article"
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            1 * commentRepository.deleteAll(articleEntity.getComments())
            1 * articleRepository.deleteById(articleEntity.id)
    }
    
    def "should throw UnauthorizedAccessException if it's not own article"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("10", true, "USER")
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.deleteArticleById(articleId)
        
        then: "get article"
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            thrown(UnauthorizedAccessException)
    }
    
    
}
