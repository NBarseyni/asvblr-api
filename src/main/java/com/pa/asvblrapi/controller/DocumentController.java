package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.Document;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.DocumentNotFoundException;
import com.pa.asvblrapi.exception.UserIsNotPlayerException;
import com.pa.asvblrapi.service.DocumentService;
import com.pa.asvblrapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable Long id) {
        return this.documentService.getDocument(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    @GetMapping("/my-documents")
    public ResponseEntity<Object> getMyDocuments() {
        try {
            User user = this.userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            List<Document> documents = this.documentService.getMyDocuments(user);
            return ResponseEntity.status(HttpStatus.OK).body(documents);
        } catch (UserIsNotPlayerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}
