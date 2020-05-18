package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.ArticleDto;
import com.pa.asvblrapi.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface ArticleMapper extends EntityMapper<ArticleDto, Article> {
    ArticleMapper instance = Mappers.getMapper(ArticleMapper.class);

    ArticleDto toDto(Article article);
    List<ArticleDto> toDto(List<Article> articles);
}
