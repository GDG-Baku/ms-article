package az.gdg.msarticle.service

import az.gdg.msarticle.client.MsAuthClient
import az.gdg.msarticle.exception.ArticleNotFoundException
import az.gdg.msarticle.model.entity.ArticleEntity
import az.gdg.msarticle.repository.ArticleRepository
import az.gdg.msarticle.service.impl.ArticleServiceImpl
import spock.lang.Specification
import spock.lang.Title

@Title("Tests for article service implementation")
class ArticleServiceImplTest extends Specification {

    private ArticleRepository articleRepository
    private MsAuthClient msAuthClient
    private ArticleServiceImpl articleService

    def setup() {
        articleRepository = Mock()
        msAuthClient = Mock()
        articleService = new ArticleServiceImpl(articleRepository, msAuthClient)
    }

    def "add read count if article is found"() {
        given:
            def articleEntity = new ArticleEntity()
            articleEntity.setUserId(3)
            articleEntity.setId("1")
            articleEntity.setReadCount(12)
            def article = Optional.of(articleEntity)


        when:
            articleService.addReadCount(articleEntity.getId())

        then:
            1 * articleRepository.findById(articleEntity.getId()) >> article
            1 * msAuthClient.addPopularity(articleEntity.getUserId())
            1 * articleRepository.save(articleEntity)
            notThrown(ArticleNotFoundException)


    }

    def "throw ArticleNotFoundException and don't add read count if article is not found"() {
        given:
            def articleEntity = Optional.empty()
            def articleId = "1"

        when:
            articleService.addReadCount(articleId)

        then:
            1 * articleRepository.findById(articleId) >> articleEntity
            thrown(ArticleNotFoundException)


    }
}
