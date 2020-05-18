package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.dto.ArticleDto;
import com.pa.asvblrapi.dto.ArticleListDto;
import com.pa.asvblrapi.entity.Article;
import com.pa.asvblrapi.exception.ArticleNotFoundException;
import com.pa.asvblrapi.mapper.ArticleListMapper;
import com.pa.asvblrapi.mapper.ArticleMapper;
import com.pa.asvblrapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    public Page<Article> getAllArticlePage(Pageable pageable) {
        return this.articleService.getAllArticle(pageable);
    }

    @GetMapping("/list")
    public List<ArticleListDto> getAllArticleWithoutContent() {
        return ArticleListMapper.instance.toDto(this.articleService.getAllArticle());
    }

    @PostMapping("")
    public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody ArticleDto articleDto) {
        try {
            Article a = this.articleService.createArticle(articleDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ArticleMapper.instance.toDto(a));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleDto articleDto) {
        try {
            Article article = this.articleService.updateArticle(id, articleDto);
            return ResponseEntity.status(HttpStatus.OK).body(ArticleMapper.instance.toDto(article));
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{id}/visible")
    public ResponseEntity<Object> setVisible(@PathVariable Long id) {
        try {
            this.articleService.setVisibleArticle(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{id}/invisible")
    public ResponseEntity<Object> setInvisible(@PathVariable Long id) {
        try {
            this.articleService.setInvisibleArticle(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteArticle(@PathVariable Long id) {
        try {
            this.articleService.deleteArticle(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (ArticleNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
