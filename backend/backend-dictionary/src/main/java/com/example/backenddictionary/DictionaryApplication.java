package com.example.backenddictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;

@SpringBootApplication
@ComponentScan(basePackages = {"java.com.example.backenddictionary"})
@ComponentScan(basePackages = {"java.com.example.backenddictionary.service"})
@ComponentScan 
public class DictionaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DictionaryApplication.class, args);

	}

}
