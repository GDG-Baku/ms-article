package az.gdg.msarticle.service.impl;

import az.gdg.msarticle.mapper.ArticleMapper;
import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.UserArticleDTO;
import az.gdg.msarticle.model.dto.UserDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.repository.ArticleRepository;
import az.gdg.msarticle.service.ArticleService;
import az.gdg.msarticle.service.MsAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);
    private final ArticleRepository articleRepository;
    private final MsAuthService msAuthService;


    public ArticleServiceImpl(ArticleRepository articleRepository, MsAuthService msAuthService) {
        this.articleRepository = articleRepository;
        this.msAuthService = msAuthService;
    }


    @Override
    public UserArticleDTO getArticlesByUserId(int userId, int page) {
        logger.info("ActionLog.getArticlesByUserId.start with userId {}", userId);
        List<ArticleEntity> articleEntities;
        Pageable pageable = PageRequest.of(page, 5, Sort.by("createdAt").descending());
        if(getAuthenticatedObject() != null &&
                Integer.parseInt(getAuthenticatedObject().getPrincipal().toString()) == userId ) {
            articleEntities = articleRepository.getArticleEntitiesByUserId(userId, pageable).getContent();
        } else{
            articleEntities = articleRepository.
                    getArticleEntitiesByUserIdAndIsDraftAndIsApproved(userId, false, true, pageable).getContent();
        }
        List<ArticleDTO> articleDTOs = ArticleMapper.INSTANCE.entityToDtoList(articleEntities);
        UserDTO userDTO = msAuthService.getUserById(userId);

        logger.info("ActionLog.getArticlesByUserId.end with userId {}", userId);
        return UserArticleDTO.builder()
                .articleDTOs(articleDTOs)
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .imageUrl(userDTO.getImageUrl())
                .build();
    }

    private Authentication getAuthenticatedObject() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

}
