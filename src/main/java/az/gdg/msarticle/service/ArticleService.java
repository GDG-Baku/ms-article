package az.gdg.msarticle.service;

import az.gdg.msarticle.model.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {

    List<ArticleDTO> getAllPostsByType(String type, Integer page, Integer size);

}
