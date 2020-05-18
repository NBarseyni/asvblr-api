package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.ArticleListDto;
import com.pa.asvblrapi.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface ArticleListMapper extends EntityMapper<ArticleListDto, Article> {
    ArticleListMapper instance = Mappers.getMapper(ArticleListMapper.class);

    ArticleListDto toDto(Article article);
    List<ArticleListDto> toDto(List<Article> articles);
}
