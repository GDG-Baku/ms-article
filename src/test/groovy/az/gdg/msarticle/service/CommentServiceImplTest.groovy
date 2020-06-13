package az.gdg.msarticle.service

import az.gdg.msarticle.exception.ArticleNotFoundException
import az.gdg.msarticle.exception.CommentNotFoundException
import az.gdg.msarticle.exception.InvalidTokenException
import az.gdg.msarticle.exception.NotAllowedException
import az.gdg.msarticle.mapper.CommentMapper
import az.gdg.msarticle.model.CommentRequest
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.model.entity.CommentEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.repository.CommentRepository
import az.gdg.msarticle.security.UserAuthentication
import az.gdg.msarticle.service.impl.CommentServiceImpl
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Title

@Title("Testing for comment service implementation")
class CommentServiceImplTest extends Specification {

    private ArticleRepository articleRepository
    private CommentRepository commentRepository
    private CommentServiceImpl commentService

    def setup() {
        articleRepository = Mock()
        commentRepository = Mock()
        commentService = new CommentServiceImpl(commentRepository, articleRepository)
    }

    def "save comment if article is found"() {
        given:
            def commentRequest = new CommentRequest("12345", "Hello", "34567")
            String token = "asdfghjklgthth"
            def article = new ArticleEntity()
            def parentComment = new CommentEntity()
            parentComment.setReply(false)
            parentComment.setReplies(new ArrayList<CommentEntity>())
            def articleEntity = Optional.of(article)
            def commentFromDatabase = Optional.of(parentComment)
            def childComment = CommentMapper.INSTANCE.requestToEntity(commentRequest)
            childComment.setUserId(1)
            childComment.setReply(true)
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleRepository.findById(commentRequest.getArticleId()) >> articleEntity
            commentRepository.findById(commentRequest.getParentCommentId()) >> commentFromDatabase

        when:
            commentService.postComment(token, commentRequest)

        then:
            1 * commentRepository.save(childComment)
            1 * commentRepository.save(parentComment)
            notThrown(ArticleNotFoundException)


    }

    def "save comment as parent if parentId which is sent with request is empty"() {
        given:
            def commentRequest = new CommentRequest("12345", "Hello", "")
            String token = "asdfghjklgthth"
            def article = new ArticleEntity()
            article.setComments(new ArrayList<CommentEntity>())
            def articleEntity = Optional.of(article)
            def parentComment = CommentMapper.INSTANCE.requestToEntity(commentRequest)
            parentComment.setUserId(1)
            parentComment.setReply(false)
            parentComment.setReplies(Collections.emptyList())
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleRepository.findById(commentRequest.getArticleId()) >> articleEntity

        when:
            commentService.postComment(token, commentRequest)

        then:
            1 * commentRepository.save(parentComment)
            1 * articleRepository.save(article)
            notThrown(ArticleNotFoundException)


    }


    def "throw ArticleNotFoundException if article is not found"() {
        given:
            def commentRequest = new CommentRequest("12345", "Hello", "34567")
            String token = "asdfghjklgthth"
            def articleEntity = Optional.empty()

        when:
            commentService.postComment(token, commentRequest)

        then:
            1 * articleRepository.findById(commentRequest.getArticleId()) >> articleEntity
            thrown(ArticleNotFoundException)


    }

    def "throw NotAllowedException if comment which is got from database is reply"() {
        given:
            def commentRequest = new CommentRequest("12345", "Hello", "6789")
            String token = "asdfghjklgthth"
            def article = new ArticleEntity()
            def parentComment = new CommentEntity()
            parentComment.setReply(true)
            parentComment.setReplies(Collections.emptyList())
            def articleEntity = Optional.of(article)
            def commentFromDatabase = Optional.of(parentComment)
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleRepository.findById(commentRequest.getArticleId()) >> articleEntity
            commentRepository.findById(commentRequest.getParentCommentId()) >> commentFromDatabase
            parentComment.isReply() >> true
        when:
            commentService.postComment(token, commentRequest)

        then:

            thrown(NotAllowedException)


    }

    def "throw CommentNotFoundException if comment is not found in database"() {
        given:
            def commentRequest = new CommentRequest("12345", "Hello", "6789")
            String token = "asdfghjklgthth"
            def article = new ArticleEntity()
            def articleEntity = Optional.of(article)
            def com = Optional.empty()
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            articleRepository.findById(commentRequest.getArticleId()) >> articleEntity
            commentRepository.findById(commentRequest.getParentCommentId()) >> com

        when:
            commentService.postComment(token, commentRequest)

        then:

            thrown(CommentNotFoundException)


    }

    def "should throw InvalidTokenException if token is invalid"() {
        given:
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)

        when:
            commentService.getAuthenticatedObject()

        then:
            thrown(InvalidTokenException)


    }

    def "get authenticated object if token is valid"() {
        given:
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)

        when:
            commentService.getAuthenticatedObject()

        then:
            SecurityContextHolder.getContext().getAuthentication() == userAuthentication
            notThrown(InvalidTokenException)


    }


}
