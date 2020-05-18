package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.ArticleDto;
import com.pa.asvblrapi.dto.ArticleListDto;
import com.pa.asvblrapi.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ArticleMappingTests {

    @Test
    public void should_map_article_to_dto() {
        Article article = new Article((long)1, "Title", "content", new Date(), new Date(), false);

        ArticleDto articleDto = ArticleMapper.instance.toDto(article);

        assertThat(articleDto).isNotNull();
        assertThat(articleDto.getId()).isEqualTo(article.getId());
        assertThat(articleDto.getTitle()).isEqualTo(article.getTitle());
        assertThat(articleDto.getContent()).isEqualTo(article.getContent());
        assertThat(articleDto.getCreationDate()).isEqualTo(article.getCreationDate());
        assertThat(articleDto.getLastModificationDate()).isEqualTo(article.getLastModificationDate());
        assertThat(articleDto.isVisible()).isEqualTo(article.isVisible());
    }

    @Test
    public void should_map_article_to_list_dto() {
        Article article = new Article((long)1, "title", "content", new Date(), new Date(), false);

        ArticleListDto articleListDto = ArticleListMapper.instance.toDto(article);

        assertThat(articleListDto).isNotNull();
        assertThat(articleListDto.getId()).isEqualTo(article.getId());
        assertThat(articleListDto.getTitle()).isEqualTo(article.getTitle());
        assertThat(articleListDto.getCreationDate()).isEqualTo(article.getCreationDate());
        assertThat(articleListDto.getLastModificationDate()).isEqualTo(article.getLastModificationDate());
        assertThat(articleListDto.isVisible()).isEqualTo(article.isVisible());
    }
}
