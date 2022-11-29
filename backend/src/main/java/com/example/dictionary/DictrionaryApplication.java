package com.example.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class DictrionaryApplication {

	public static void main(String[] args) throws IOException {

		
		SpringApplication.run(DictrionaryApplication.class, args);
	}

}
