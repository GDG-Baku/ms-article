package az.gdg.msarticle.service.impl

import az.gdg.msarticle.exception.ArticleNotFoundException
import az.gdg.msarticle.exception.CommentNotFoundException
import az.gdg.msarticle.exception.NotAllowedException
import az.gdg.msarticle.exception.UnauthorizedAccessException
import az.gdg.msarticle.mapper.CommentMapper
import az.gdg.msarticle.model.CommentRequest
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.model.entity.CommentEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.repository.CommentRepository
import az.gdg.msarticle.security.UserAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class CommentServiceImplTest extends Specification {

    private def commentRepository
    private def commentService

    private def articleRepository

    void setup() {
        articleRepository = Mock(ArticleRepository)
        commentRepository = Mock(CommentRepository)
        commentService = new CommentServiceImpl(commentRepository, articleRepository)
    }

    def "should delete child comment"() {
        given:
            def commentId = "1"
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            def commentEntity = new CommentEntity()
            commentEntity.setUserId(2)
            commentEntity.setReply(true)
            def parentComment = new CommentEntity()
            parentComment.setReplies(Collections.emptyList())
            commentRepository.findById(commentId) >> Optional.of(commentEntity)
            commentRepository.findByRepliesContains(commentEntity) >> parentComment
        when:
            commentService.deleteComment(commentId)
        then:
            1 * commentRepository.deleteById(commentId)
            notThrown(exception)
        where:
            exception << [UnauthorizedAccessException, CommentNotFoundException]
    }

    def "should delete parent comment"() {
        given:
            def commentId = "1"
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            def commentEntity = new CommentEntity()
            commentEntity.setUserId(2)
            commentEntity.setReplies(Collections.singletonList("") as List<CommentEntity>)
            commentEntity.setReply(false)
            def referencedArticle = new ArticleEntity();
            referencedArticle.setComments(Collections.emptyList())
            commentRepository.findById(commentId) >> Optional.of(commentEntity)
            articleRepository.findByCommentsContains(commentEntity) >> referencedArticle
        when:
            commentService.deleteComment(commentId)
        then:
            1 * commentRepository.deleteAll(commentEntity.getReplies())
            1 * commentRepository.deleteById(commentId)
            notThrown(exception)
        where:
            exception << [UnauthorizedAccessException, CommentNotFoundException]
    }

    def "should throw UnauthorizedAccessException when deleting comment"() {
        given:
            def commentId = "1"
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            def commentEntity = new CommentEntity()
            commentEntity.setUserId(3)
            commentRepository.findById(commentId) >> Optional.of(commentEntity)
        when:
            commentService.deleteComment(commentId)
        then:
            thrown(UnauthorizedAccessException)
    }

    def "should throw CommentNotFound when deleting comment"() {
        given:
            def commentId = "1"
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            commentRepository.findById(commentId) >> Optional.empty()
        when:
            commentService.deleteComment(commentId)
        then:
            thrown(CommentNotFoundException)
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
}
