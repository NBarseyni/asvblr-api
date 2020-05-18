package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.ArticleDto;
import com.pa.asvblrapi.entity.Article;
import com.pa.asvblrapi.exception.ArticleNotFoundException;
import com.pa.asvblrapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Page<Article> getAllArticle(Pageable pageable) {
        return this.articleRepository.findAllPage(pageable);
    }

    public List<Article> getAllArticle() {
        return this.articleRepository.findAll();
    }

    public Article createArticle(ArticleDto articleDto) {
        Article article = new Article(articleDto.getTitle(), articleDto.getContent());
        return this.articleRepository.save(article);
    }

    public Article updateArticle(Long id, ArticleDto articleDto) throws ArticleNotFoundException {
        Optional<Article> article = this.articleRepository.findById(id);
        if (!article.isPresent()) {
            throw new ArticleNotFoundException(id);
        }
        article.get().setTitle(articleDto.getTitle());
        article.get().setContent(articleDto.getContent());
        article.get().setLastModificationDate(new Date());
        return this.articleRepository.save(article.get());
    }

    public void setVisibleArticle(Long id) throws ArticleNotFoundException {
        Optional<Article> article = this.articleRepository.findById(id);
        if (!article.isPresent()) {
            throw new ArticleNotFoundException(id);
        }
        article.get().setVisible(true);
        this.articleRepository.save(article.get());
    }

    public void setInvisibleArticle(Long id) throws ArticleNotFoundException {
        Optional<Article> article = this.articleRepository.findById(id);
        if (!article.isPresent()) {
            throw new ArticleNotFoundException(id);
        }
        article.get().setVisible(false);
        this.articleRepository.save(article.get());
    }

    public void deleteArticle(Long id) throws ArticleNotFoundException {
        Optional<Article> article = this.articleRepository.findById(id);
        if (!article.isPresent()) {
            throw new ArticleNotFoundException(id);
        }
        this.articleRepository.delete(article.get());
    }
}
