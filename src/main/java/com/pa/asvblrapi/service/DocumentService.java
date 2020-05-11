package com.pa.asvblrapi.service;

import com.pa.asvblrapi.entity.Document;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.exception.DocumentNotFoundException;
import com.pa.asvblrapi.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    private final String UPLOADED_FOLDER = "src/main/resources/public/documents/";

    public Optional<Document> getDocument(Long id) {
        return this.documentRepository.findById(id);
    }

    public Document createDocument(MultipartFile file) throws IOException {
        try {
            String filename = file.getOriginalFilename();
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + filename);
            Files.write(path, bytes);

            Document document = new Document(filename);
            return this.documentRepository.save(document);
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public void deleteDocument(Long id) throws DocumentNotFoundException, IOException {
        Optional<Document> document = this.documentRepository.findById(id);
        if(!document.isPresent()) {
            throw new DocumentNotFoundException(id);
        }
        this.documentRepository.delete(document.get());
        try {
            Files.delete(Paths.get(UPLOADED_FOLDER + document.get().getName()));
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }
}
