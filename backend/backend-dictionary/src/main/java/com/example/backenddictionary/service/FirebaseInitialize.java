package com.example.backenddictionary.service;

import java.io.FileInputStream;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

import org.springframework.core.io.Resource;


public class FirebaseInitialize {
    
	@Value("classpath:serviceAccount.json")
	Resource resourceFile;

	@PostConstruct
    public void initialize() {
		
		try {
			FileInputStream serviceAccount =
					  new FileInputStream("./service/serviceAccount.json");
					
					@SuppressWarnings("deprecation")
					
					FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					//   .setCredentials(GoogleCredentials.fromStream(resourceFile.getInputStream()))
					  .setDatabaseUrl("https://dictionaryapp-b279b-default-rtdb.europe-west1.firebasedatabase.app")
					//   .setServiceAccountId("firebase-adminsdk-pvjnc@dictionaryapp-b279b.iam.gserviceaccount.com")
					  .build();
					
					FirebaseApp.initializeApp(options);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
