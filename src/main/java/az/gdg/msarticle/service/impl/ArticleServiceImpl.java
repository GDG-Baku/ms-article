package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.client.AuthenticationClient;
import az.gdg.msarticle.client.dto.UserDetail;
import az.gdg.msarticle.exception.NotFoundException;
import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static az.gdg.msarticle.mapper.ArticleMapper.getValueOfType;


@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final AuthenticationClient authenticationClient;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              AuthenticationClient authenticationClient) {
        this.articleRepository = articleRepository;
        this.authenticationClient = authenticationClient;
    }

    @Override
    public List<ArticleDTO> getAllPostsByType(String type, Integer page, Integer size) {
        logger.info("ActionLog.getAllPostsByType.start : page {}", page);
        logger.info("ActionLog.getAllPostsByType.start : size {}", size);
        Sort sort = Sort.by("createdAt").descending();
        Pageable paging = PageRequest.of(page, size, sort);
        Page<ArticleEntity> pages = articleRepository
                .getAllByTypeAndIsDraftFalseAndIsApprovedTrue(getValueOfType(type), paging);

        if (!pages.isEmpty()) {
            List<Integer> userIds = new ArrayList<>();
            List<ArticleDTO> articleDTOs = ArticleMapper.INSTANCE.entityListToDtoList(pages.getContent());

            for (ArticleDTO articleDTO : articleDTOs) {
                userIds.add(articleDTO.getUserId());

                if (articleDTO.getType().equals("ARTICLE") ||
                        articleDTO.getType().equals("NEWS")) {
                    articleDTO.setContent(articleDTO.getContent().substring(0, 100));
                } else if (articleDTO.getType().equals("FORUM")) {
                    articleDTO.setContent(null);
                }

            }

            List<UserDetail> userDetails = authenticationClient.getUsersById(userIds);

            for (ArticleDTO articleDTO : articleDTOs) {
                for (UserDetail userDetail : userDetails) {
                    if (articleDTO.getUserId() == userDetail.getId()) {
                        articleDTO.setFirstName(userDetail.getFirstName());
                        articleDTO.setLastName(userDetail.getLastName());
                        articleDTO.setImageUrl(userDetail.getImageUrl());
                    }
                }
            }

            logger.info("ActionLog.getAllPostsByType.stop.success : page {}", page);
            logger.info("ActionLog.getAllPostsByType.stop.success : size {}", size);

            return articleDTOs;

        } else {
            throw new NotFoundException("Page is not found!");
        }
    }
}
