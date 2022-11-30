package com.example.backenddictionary.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.example.backenddictionary.DictionaryEntity;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class FirebaseService {
    public String saveWordDetails(DictionaryEntity word) throws InterruptedException, ExecutionException{
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> collectionApiFuture = dbFirestore.collection("dictionary").document(word.getWord()).set(word);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    public String getWordDetails(String word) throws InterruptedException, ExecutionException{
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("dictionary").document(word);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        
        DictionaryEntity dictionary = null;
        
        if (document.exists()){
            dictionary = document.toObject(DictionaryEntity.class);
            return dictionary.toString();
        } else {
            return null;
        }
    }

}
