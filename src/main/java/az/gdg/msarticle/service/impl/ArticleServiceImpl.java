package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.mapper.TagMapper;
import az.gdg.msarticle.model.ArticleRequest;
import az.gdg.msarticle.model.TagRequest;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.TagEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.repository.TagRepository;
import az.gdg.msarticle.service.ArticleService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository, TagRepository tagRepository) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public String updateArticle(String articleId, ArticleRequest articleRequest) {
        ArticleEntity articleEntity = articleRepository.findById(articleId).orElseThrow(() ->
                new ArticleNotFoundException("Article doesn't exist with this id " + articleId));

        articleEntity.setTitle(articleRequest.getTitle());
        articleEntity.setContent(articleRequest.getContent());
        articleEntity.setQuackCount(articleRequest.getQuackCount());
        articleEntity.setReadCount(articleRequest.getReadCount());
        articleEntity.setTags(getTagsFromRequest(articleRequest.getTags()));
        articleEntity.setDraft(true);

        articleRepository.save(articleEntity);

        return "Article is updated";
    }

    private List<TagEntity> getTagsFromRequest(List<TagRequest> tagRequests) {
        List<TagEntity> tags = new ArrayList<>();
        for (TagRequest tagRequest : tagRequests) {
            TagEntity tagEntity = tagRepository.findByName(tagRequest.getName());
            if (tagEntity == null) {
                tagEntity = tagRepository.save(TagMapper.INSTANCE.requestToEntity(tagRequest));
            }
            tags.add(tagEntity);
        }
        return tags;
    }
}
