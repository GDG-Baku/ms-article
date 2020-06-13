package az.gdg.msarticle.service.impl


import az.gdg.msarticle.model.TagRequest
import az.gdg.msarticle.model.entity.TagEntity
import az.gdg.msarticle.repository.TagRepository
import spock.lang.Specification

class TagServiceImplTest extends Specification {

    private def tagRepository
    private def tagService

    void setup() {
        tagRepository = Mock(TagRepository)
        tagService = new TagServiceImpl(tagRepository)
    }

    def "should return tag if doesn't exist in database save also"() {
        given:
            def tagEntity = new TagEntity()
            def tagRequest = new TagRequest()
            tagRequest.setName("")
            tagRepository.findByName(tagRequest.getName()) >> tagEntity
        when:
            tagService.saveIfNotExist(tagRequest)
        then:
            0 * tagRepository.save(_)
    }

    def "return tag if exist in database"() {
        given:
            def tagRequest = new TagRequest()
            tagRequest.setName("")
            tagRepository.findByName(tagRequest.getName()) >> null
        when:
            tagService.saveIfNotExist(tagRequest)
        then:
            1 * tagRepository.save(_)
    }
}
