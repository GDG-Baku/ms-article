package az.gdg.msarticle.service

import az.gdg.msarticle.mapper.ArticleMapper
import az.gdg.msarticle.model.dto.UserDTO
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.security.UserAuthentication
import az.gdg.msarticle.service.impl.ArticleServiceImpl
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification
import spock.lang.Title

@Title("Testing for article service implementation for getArticle method")
class ArticleServiceTest extends Specification {
    ArticleRepository articleRepository
    ArticleServiceImpl articleServiceImpl
    MsAuthService msAuthService
    
    def setup() {
        articleRepository = Mock()
        msAuthService = Mock()
        articleServiceImpl = new ArticleServiceImpl(articleRepository, msAuthService)
    }
    
    def "should use the repository to fetch all articles by UserId if it's own account"() {
        given:
            def userId = 41
            def page = 0
            def articleEntity1 = ArticleEntity.builder()
                    .userId(userId)
                    .type(1)
                    .isApproved(false)
                    .isDraft(true)
                    .build()
            def articleEntity2 = ArticleEntity.builder()
                    .userId(userId)
                    .type(1)
                    .isApproved(true)
                    .isDraft(false)
                    .build()
            def userDTO = new UserDTO()
            def articles = Arrays.asList(articleEntity1, articleEntity2)
            def pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending())
            def pageArticles = new PageImpl<ArticleEntity>(articles, pageable, articles.size())
            def articleDTOs = ArticleMapper.INSTANCE.entityToDtoList(articles)
            def userAuthentication = new UserAuthentication("41", true, "USER")
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            def res = articleServiceImpl.getArticlesByUserId(userId, page)
        
        then:
            1 * articleRepository.getArticleEntitiesByUserId(userId, pageable) >> pageArticles
            1 * msAuthService.getUserById(userId) >> userDTO
            
            res.articleDTOs == articleDTOs
            res.userDTO == userDTO
    }
    
    def "should use the repository to fetch non-draft and approved articles by UserId if it's not own account"() {
        given:
            def userId = 41
            def page = 0
            def articleEntity = ArticleEntity.builder()
                    .userId(userId)
                    .type(1)
                    .isApproved(false)
                    .isDraft(true)
                    .build()
            def userDTO = new UserDTO()
            def articles = Arrays.asList(articleEntity)
            def pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending())
            def pageArticles = new PageImpl<ArticleEntity>(articles, pageable, articles.size())
            def articleDTOs = ArticleMapper.INSTANCE.entityToDtoList(articles)
            def userAuthentication = new UserAuthentication("10", true, "USER")
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            def res = articleServiceImpl.getArticlesByUserId(userId, page)
        
        then:
            1 * articleRepository.getArticleEntitiesByUserIdAndIsDraftFalseAndIsApprovedTrue(userId, pageable) >> pageArticles
            1 * msAuthService.getUserById(userId) >> userDTO
            
            res.articleDTOs == articleDTOs
            res.userDTO == userDTO
    }
    
    def "should use the repository to fetch non-draft and approved articles by UserId if not logged"() {
        given:
            def userId = 41
            def page = 0
            def articleEntity = ArticleEntity.builder()
                    .userId(userId)
                    .type(1)
                    .isApproved(false)
                    .isDraft(true)
                    .build()
            def userDTO = new UserDTO()
            def articles = Arrays.asList(articleEntity)
            def pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending())
            def pageArticles = new PageImpl<ArticleEntity>(articles, pageable, articles.size())
            def articleDTOs = ArticleMapper.INSTANCE.entityToDtoList(articles)
            def userAuthentication = null
            SecurityContextHolder.getContext().setAuthentication(userAuthentication)
        
        when:
            def res = articleServiceImpl.getArticlesByUserId(userId, page)
        
        then:
            1 * articleRepository.getArticleEntitiesByUserIdAndIsDraftFalseAndIsApprovedTrue(userId, pageable) >> pageArticles
            1 * msAuthService.getUserById(userId) >> userDTO
            
            res.articleDTOs == articleDTOs
            res.userDTO == userDTO
    }
    
}
