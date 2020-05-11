package com.pa.asvblrapi.controller;

import com.pa.asvblrapi.entity.Document;
import com.pa.asvblrapi.exception.DocumentNotFoundException;
import com.pa.asvblrapi.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @GetMapping("/{id}")
    public Document getDocument(@PathVariable Long id) {
        return this.documentService.getDocument(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }
}
