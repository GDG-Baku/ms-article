package az.gdg.msarticle.service

import az.gdg.msarticle.exception.NoSuchArticleException
import az.gdg.msarticle.exception.UnauthorizedAccessException
import az.gdg.msarticle.mapper.ArticleMapper
import az.gdg.msarticle.model.dto.CommentDTO
import az.gdg.msarticle.model.dto.UserDTO
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.model.entity.CommentEntity
import az.gdg.msarticle.model.entity.TagEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.repository.CommentRepository
import az.gdg.msarticle.security.UserAuthentication
import az.gdg.msarticle.service.impl.ArticleServiceImpl
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Title

import java.time.LocalDateTime

@Title("Testing for article service implementation for getArticle method")
class ArticleServiceTest extends Specification {
    ArticleRepository articleRepository
    ArticleServiceImpl articleServiceImpl
    MsAuthService msAuthService
    CommentRepository commentRepository
    
    def setup() {
        articleRepository = Mock()
        msAuthService = Mock()
        commentRepository = Mock()
        articleServiceImpl = new ArticleServiceImpl(articleRepository, msAuthService, commentRepository)
    }
    
    def "should use the repository to fetch article by id"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", type: new Integer(2), userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userDTO = new UserDTO(firstName: "Ali", lastName: "Huseynov", imageUrl: "ali.png")
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            def articleDTO = ArticleMapper.INSTANCE.entityToDto(articleEntity, userDTO)
            articleDTO.setComments(Collections.singletonList(new CommentDTO()))
        
        when:
            def res = articleServiceImpl.getArticleById(articleId)
        
        then: "get article"
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            1 * msAuthService.getUserById(articleEntity.userId) >> userDTO
            
            res == articleDTO
    }
    
    def "should throw NoSuchArticleException if no such article"() {
        given:
            def articleId = "dasdpksapdksaop"
        
        when:
            articleServiceImpl.getArticleById(articleId)
        
        then:
            1 * articleRepository.findById(articleId) >> Optional.empty()
            thrown(NoSuchArticleException)
    }
    
    def "should throw UnauthorizedAccessException if not logged and article is draft"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", type: new Integer(2), userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: true, isApproved: false, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.getArticleById(articleId)
        
        then: "get article"
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            thrown(UnauthorizedAccessException)
    }
    
    def "should throw UnauthorizedAccessException if it's not own article and is draft"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", type: new Integer(2), userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: true, isApproved: false, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("15", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.getArticleById(articleId)
        
        then:
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            thrown(UnauthorizedAccessException)
    }
    
    def "should use the repository to delete article by id"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("41", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.deleteArticleById(articleId)
        
        then: "get article"
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            1 * commentRepository.deleteAll(articleEntity.getComments())
            1 * articleRepository.deleteById(articleId)
    }
    
    def "should throw UnauthorizedAccessException if it's not own article"() {
        given:
            def articleId = "5eac708be7179a42f172de4c"
            def tag = new TagEntity()
            def comment = new CommentEntity()
            def articleEntity = new ArticleEntity(id: "5eac708be7179a42f172de4c", userId: 41, title: "Test Title",
                    content: "Code Block", createdAt: LocalDateTime.now(), updatedAt: LocalDateTime.now(), quackCount: 30,
                    hateCount: 5, readCount: 75, isDraft: false, isApproved: true, approverId: 41, tags: [tag], comments: [comment])
            def userAuthentication = new UserAuthentication("10", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            articleServiceImpl.deleteArticleById(articleId)
        
        then: "get article"
            1 * articleRepository.findById(articleId) >> Optional.of(articleEntity)
            thrown(UnauthorizedAccessException)
    }
    
}
