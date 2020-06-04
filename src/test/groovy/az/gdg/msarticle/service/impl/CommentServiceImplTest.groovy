package az.gdg.msarticle.service.impl

import az.gdg.msarticle.exception.CommentNotFoundException
import az.gdg.msarticle.exception.InvalidTokenException
import az.gdg.msarticle.exception.NoAccessException
import az.gdg.msarticle.model.entity.CommentEntity
import az.gdg.msarticle.repository.CommentRepository
import az.gdg.msarticle.security.UserAuthentication
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class CommentServiceImplTest extends Specification {

    CommentRepository commentRepository
    CommentServiceImpl commentService

    void setup() {
        commentRepository = Mock()
        commentService = new CommentServiceImpl(commentRepository)
    }

    def "should delete child comment"() {
        given:
            def commentId = "1"
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            def commentEntity = new CommentEntity()
            commentEntity.setUserId(2)
            commentRepository.findById(commentId) >> Optional.of(commentEntity)
        when:
            commentService.deleteComment(commentId)
        then:
            1 * commentRepository.deleteById(commentId)
            notThrown(exception)
        where:
            exception << [NoAccessException, CommentNotFoundException]
    }

    def "should delete parent comment"() {
        given:
            def commentId = "1"
            def userAuthentication = new UserAuthentication("2", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
            def commentEntity = new CommentEntity()
            commentEntity.setUserId(2)
            commentEntity.setReplies(Collections.singletonList("") as List<CommentEntity>)
            commentRepository.findById(commentId) >> Optional.of(commentEntity)
        when:
            commentService.deleteComment(commentId)
        then:
            1 * commentRepository.deleteAll(commentEntity.getReplies())
            1 * commentRepository.deleteById(commentId)
            notThrown(exception)
        where:
            exception << [NoAccessException, CommentNotFoundException]

    }

    def "should throw NoAccessException when deleting comment"() {
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
            thrown(NoAccessException)
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


    def "should return authenticated object"() {
        given:
            def userAuthentication = new UserAuthentication("1", true)
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            commentService.getAuthenticatedObject()
        then:
            notThrown(InvalidTokenException)
    }


    def "should throw InvalidTokenException when user is not authenticated"() {
        given:
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        when:
            commentService.getAuthenticatedObject()
        then:
            thrown(InvalidTokenException)

    }

}
