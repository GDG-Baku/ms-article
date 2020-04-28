package az.gdg.msarticle.mapper;

import az.gdg.msarticle.model.dto.ArticleDTO;
import az.gdg.msarticle.model.dto.CommentDTO;
import az.gdg.msarticle.model.dto.TagDTO;
import az.gdg.msarticle.model.entity.ArticleEntity;
import az.gdg.msarticle.model.entity.Comment;
import az.gdg.msarticle.model.entity.TagEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-04-28T21:40:38+0400",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_181 (Oracle Corporation)"
)
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public ArticleEntity dtoToEntity(ArticleDTO articleDTO) {
        if ( articleDTO == null ) {
            return null;
        }

        ArticleEntity articleEntity = new ArticleEntity();

        articleEntity.setId( articleDTO.getId() );
        articleEntity.setTitle( articleDTO.getTitle() );
        articleEntity.setContent( articleDTO.getContent() );
        articleEntity.setCreatedAt( articleDTO.getCreatedAt() );
        articleEntity.setDraft( articleDTO.isDraft() );
        articleEntity.setTags( tagDTOListToTagEntityList( articleDTO.getTags() ) );
        articleEntity.setComments( commentDTOListToCommentList( articleDTO.getComments() ) );

        return articleEntity;
    }

    @Override
    public ArticleDTO entityToDto(ArticleEntity articleEntity) {
        if ( articleEntity == null ) {
            return null;
        }

        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setId( articleEntity.getId() );
        articleDTO.setTitle( articleEntity.getTitle() );
        articleDTO.setContent( articleEntity.getContent() );
        articleDTO.setCreatedAt( articleEntity.getCreatedAt() );
        articleDTO.setDraft( articleEntity.isDraft() );
        articleDTO.setTags( tagEntityListToTagDTOList( articleEntity.getTags() ) );

        return articleDTO;
    }

    @Override
    public List<ArticleDTO> entityToDtoList(List<ArticleEntity> articleEntities) {
        if ( articleEntities == null ) {
            return null;
        }

        List<ArticleDTO> list = new ArrayList<ArticleDTO>( articleEntities.size() );
        for ( ArticleEntity articleEntity : articleEntities ) {
            list.add( entityToDto( articleEntity ) );
        }

        return list;
    }

    protected TagEntity tagDTOToTagEntity(TagDTO tagDTO) {
        if ( tagDTO == null ) {
            return null;
        }

        TagEntity tagEntity = new TagEntity();

        tagEntity.setName( tagDTO.getName() );

        return tagEntity;
    }

    protected List<TagEntity> tagDTOListToTagEntityList(List<TagDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<TagEntity> list1 = new ArrayList<TagEntity>( list.size() );
        for ( TagDTO tagDTO : list ) {
            list1.add( tagDTOToTagEntity( tagDTO ) );
        }

        return list1;
    }

    protected Comment commentDTOToComment(CommentDTO commentDTO) {
        if ( commentDTO == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setText( commentDTO.getText() );
        List<CommentDTO> list = commentDTO.getReplies();
        if ( list != null ) {
            comment.setReplies( new ArrayList<CommentDTO>( list ) );
        }
        comment.setCreatedAt( commentDTO.getCreatedAt() );

        return comment;
    }

    protected List<Comment> commentDTOListToCommentList(List<CommentDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Comment> list1 = new ArrayList<Comment>( list.size() );
        for ( CommentDTO commentDTO : list ) {
            list1.add( commentDTOToComment( commentDTO ) );
        }

        return list1;
    }

    protected TagDTO tagEntityToTagDTO(TagEntity tagEntity) {
        if ( tagEntity == null ) {
            return null;
        }

        TagDTO tagDTO = new TagDTO();

        tagDTO.setName( tagEntity.getName() );

        return tagDTO;
    }

    protected List<TagDTO> tagEntityListToTagDTOList(List<TagEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<TagDTO> list1 = new ArrayList<TagDTO>( list.size() );
        for ( TagEntity tagEntity : list ) {
            list1.add( tagEntityToTagDTO( tagEntity ) );
        }

        return list1;
    }
}
