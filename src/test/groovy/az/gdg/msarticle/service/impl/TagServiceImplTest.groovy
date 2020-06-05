package az.gdg.msarticle.service.impl

import az.gdg.msarticle.model.TagRequest
import az.gdg.msarticle.model.entity.TagEntity
import az.gdg.msarticle.repository.TagRepository
import spock.lang.Specification
import spock.lang.Title

@Title("Testing for tag service implementation")
class TagServiceImplTest extends Specification {

    TagRepository tagRepository
    TagServiceImpl tagService


    def setup() {
        tagRepository = Mock()
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
