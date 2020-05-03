package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.exception.ArticleNotFoundException;
import az.gdg.msarticle.mail.service.EmailService;
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
    private final EmailService emailService;

    public ArticleServiceImpl(ArticleRepository articleRepository, TagRepository tagRepository, EmailService emailService) {
        this.articleRepository = articleRepository;
        this.tagRepository = tagRepository;
        this.emailService = emailService;
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
        sendMail(articleId, "update");
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

    private void sendMail(String articleId, String requestType) {
        String mailBody = "Author that has article with id " + articleId + " wants to " + requestType + " it.<br>" +
                "Please review article before " + requestType;
        emailService.sendToQueue(emailService.prepareMail(mailBody));
    }
}
