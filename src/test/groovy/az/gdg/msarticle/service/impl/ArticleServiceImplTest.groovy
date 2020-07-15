package az.gdg.msarticle.service.impl

import az.gdg.msarticle.exception.ArticleNotFoundException
import az.gdg.msarticle.exception.NoDraftedArticleExist
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
import az.gdg.msarticle.service.MsAuthService
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

import java.time.LocalDateTime

class ArticleServiceImplTest extends Specification {

    private def articleRepository
    private def mailService
    private def articleServiceImpl
    private MsAuthService msAuthService
    private CommentRepository commentRepository

    void setup() {
        articleRepository = Mock(ArticleRepository)
        mailService = Mock(MailServiceImpl)
        msAuthService = Mock()
        commentRepository = Mock()
        articleServiceImpl = new ArticleServiceImpl(articleRepository, msAuthService, commentRepository, mailService)
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
            articleServiceImpl.publishArticle(articleId)
        then:
            1 * mailService.sendToQueue(_)
            notThrown(exception)
        where:
            exception << [NoSuchArticleException, UnauthorizedAccessException, NoDraftedArticleExist]
    }

    def "should throw UnauthorizedAccessException when not authorized user tries to publish"() {
        given:
            def articleId = "1"
            def articleEntity = new ArticleEntity()
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleEntity.setId(articleId)
            articleEntity.setUserId(3)
            articleRepository.findById(articleId) >> Optional.of(articleEntity)
        when:
            articleServiceImpl.publishArticle(articleId)
        then:
            thrown(UnauthorizedAccessException)
    }

    def "should throw ArticleNotFoundException when article doesn't exist"() {
        given:
            def articleId = "1"
            def userAuthentication = new UserAuthentication("", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleRepository.findById(articleId) >> Optional.empty()
        when:
            articleServiceImpl.publishArticle(articleId)
        then:
            thrown(NoSuchArticleException)
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
            articleServiceImpl.publishArticle(articleId)
        then:
            thrown(NoDraftedArticleExist)
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

    def "add read count if article is found"() {
        given:
            def articleEntity = new ArticleEntity()
            articleEntity.setUserId(3)
            articleEntity.setId("1")
            articleEntity.setReadCount(12)
            def article = Optional.of(articleEntity)
        when:
            articleServiceImpl.addReadCount(articleEntity.getId())

        then:
            1 * articleRepository.findById(articleEntity.getId()) >> article
            1 * msAuthService.addPopularity(articleEntity.getUserId())
            1 * articleRepository.save(articleEntity)
            notThrown(ArticleNotFoundException)
    }

    def "throw ArticleNotFoundException and don't add read count if article is not found"() {
        given:
            def articleEntity = Optional.empty()
            def articleId = "1"

        when:
            articleServiceImpl.addReadCount(articleId)

        then:
            1 * articleRepository.findById(articleId) >> articleEntity
            thrown(ArticleNotFoundException)
    }
}
