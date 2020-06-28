package com.pa.asvblrapi.service;

import com.pa.asvblrapi.entity.Document;
import com.pa.asvblrapi.entity.Player;
import com.pa.asvblrapi.entity.Subscription;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.DocumentNotFoundException;
import com.pa.asvblrapi.exception.UserIsNotPlayerException;
import com.pa.asvblrapi.repository.DocumentRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PlayerService playerService;

    private final String UPLOADED_FOLDER = "src/main/resources/public/documents/";

    public Optional<Document> getDocument(Long id) {
        return this.documentRepository.findById(id);
    }

    public List<Document> getMyDocuments(User user) throws UserIsNotPlayerException {
        Player player = user.getPlayer();
        if (player == null) {
            throw new UserIsNotPlayerException(user.getId());
        }
        Subscription subscription = this.playerService.getLastSubscription(player.getId());

        List<Document> documents = new ArrayList<>();
        documents.add(subscription.getCNI());
        documents.add(subscription.getIdentityPhoto());
        documents.add(subscription.getMedicalCertificate());
        return documents;
    }

    public Document createDocument(MultipartFile file, String username) throws IOException {
        try {
            String originalFilename = file.getOriginalFilename();
            String newFilename = username + "." + FilenameUtils.getExtension(originalFilename);
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + newFilename);
            Files.write(path, bytes);

            Document document = new Document(newFilename);
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
