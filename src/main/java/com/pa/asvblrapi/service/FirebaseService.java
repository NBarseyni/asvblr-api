package com.pa.asvblrapi.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.entity.User;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {

    public String saveUserDetails(UserDtoFirebase user) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("users").document(user.getUsername()).set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public User getUserDetails(String username) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("users").document(username);
        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot documentSnapshot = future.get();

        User user = null;

        if(documentSnapshot.exists()) {
            user = documentSnapshot.toObject(User.class);
            return user;
        }
        else {
            return null;
        }
    }

    public String updateUserDetails(UserDtoFirebase user) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("users").document(user.getUsername()).set(user);
        return collectionsApiFuture.get().getUpdateTime().toString();
    }

    public String deleteUser(String username) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> writeResultApiFuture = dbFirestore.collection("users").document(username).delete();
        return String.format("Document %s has been deleted", username);
    }
}
