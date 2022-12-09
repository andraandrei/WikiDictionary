package com.example.backenddictionary;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DictionaryController 
{
	ArrayList<DictionaryEntity> customDictionary = new ArrayList<>();
  
	@CrossOrigin
	@GetMapping({"/getWord"})
  	public String getInfo(@RequestParam("word") String word) throws IOException, JSONException, InterruptedException, ExecutionException 
	{
		DictionaryEntity obj = new DictionaryEntity();
		JSONArray jarray = new JSONArray();
		JSONObject jobj = new JSONObject();
		String word1 = "";
		JSONArray meaningList = new JSONArray();
		JSONObject meaning = new JSONObject();
		JSONArray definitionList = new JSONArray();
		JSONObject definition = new JSONObject();
		ArrayList<String> arrayDefinition = new ArrayList<>();
		//primero comprueba si palabra existe en base de datos; conexion con BD
		InputStream serviceAccount = new FileInputStream("./serviceAccount.json");
		GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
		FirebaseOptions options = (new FirebaseOptions.Builder()).setCredentials(credentials).build();
		//excepcion para que la conexion no finalice cada vez que devuelve una peticion
		try 
		{
			FirebaseApp.initializeApp(options);
		} 
		catch (Exception e) 
		{
			System.out.println("ya esta conectado");
		} 
			Firestore db = FirestoreClient.getFirestore();
			//comprobar que los espacios del buscador no estan en blanco o contienen numeros
			if( word.isBlank()  ||  Pattern.matches("[a-zA-Z]+", word) == false)
			{
				System.out.println("WORD ESTA VACIO");
				JSONObject errorMessage = new JSONObject();
				errorMessage.append("Error", "please insert ONLY LETTERS and NO WHITESPACES");
				return errorMessage.toString();
			} 
			else 		
			{
				ApiFuture<QuerySnapshot> query = db.collection("dictionary").get();
				QuerySnapshot querySnapshot = (QuerySnapshot)query.get();
				List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
				//recorrer la lista de palabras de dentro del diccionario
				for (QueryDocumentSnapshot document : documents) 
				{
					//comprobar si es igual que la palabra de dentro del diccionario
					String localWord = document.getString("word");
					if (word.toLowerCase().equals(localWord.toLowerCase())) 
					{
						ArrayList<String> localDefinitions = (ArrayList<String>)document.get("definitions");
						obj.setWord(localWord);
						obj.setDefinition(localDefinitions);
						JSONObject j = new JSONObject(obj);
						return j.toString();
					} 
				}
				
				//si no encuentra palabra en diccionario customizado, busca en api externa
				String urlToRead = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;
				URL url = new URL(urlToRead);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					try {
						String line;
						while ((line = reader.readLine()) != null) 
						{
							try 
							{
								jarray = new JSONArray(line);
								jobj = jarray.getJSONObject(0);
								word1 = jobj.getString("word").toLowerCase();
								meaningList = jobj.getJSONArray("meanings".toLowerCase());
								for (int i = 0; i < meaningList.length(); i++) 
								{
									meaning = meaningList.getJSONObject(i);
									definitionList = meaning.getJSONArray("definitions");
									for (int k = 0; k < definitionList.length(); k++) 
									{
										definition = definitionList.getJSONObject(k);
										definition.getString("definition");
										String definitio = definition.getString("definition").replace("\\", "");
										arrayDefinition.add(definitio);
									} 
								} 
								obj.setWord(word1);
								obj.setDefinition(arrayDefinition);
								JSONObject j = new JSONObject(obj);
								String str = j.toString();
								reader.close();
								return str;
							} catch (JSONException je) 
							{
								je.printStackTrace();
							} catch (Exception e) 
							{
								e.printStackTrace();
							} 
						} 
						reader.close();
					} catch (Throwable throwable) {
					try {
						reader.close();
					} catch (Throwable throwable1) {
						throwable.addSuppressed(throwable1);
					} 
					throw throwable;
					} 
				} catch (FileNotFoundException fe) 
				{
					String message = "This is non existing word. Try again";
					JSONObject err = new JSONObject();
					err.put("error", message);
					return err.toString();
				}
			} 
			return word;
	}

  
	@DeleteMapping({"/deleteWord"})
	@ResponseBody
	public String deleteWord(@RequestBody String jsonBody) throws IOException, JSONException, InterruptedException, ExecutionException 
	{
		DictionaryEntity obj = new DictionaryEntity();
		JSONObject jword = new JSONObject(jsonBody);
		String wordIf = jword.getString("word");
		jsonBody.toLowerCase();
		try 
		{
			InputStream serviceAccount = new FileInputStream("./serviceAccount.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = (new FirebaseOptions.Builder()).setCredentials(credentials).build();
			FirebaseApp.initializeApp(options);
		}
		catch (Exception e) 
		{
			System.out.println("ya esta conectado");
		} 
		Firestore db = FirestoreClient.getFirestore();
		if( wordIf.isBlank()  ||  Pattern.matches("[a-zA-Z]+", wordIf) == false)
		{
			System.out.println("WORD ESTA VACIO");
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "please insert ONLY LETTERS and NO WHITESPACES");
			return errorMessage.toString();
		} 
		else 
		{
			ApiFuture<QuerySnapshot> query = db.collection("dictionary").get();
			QuerySnapshot querySnapshot = (QuerySnapshot)query.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot document : documents) 
			{
				String localWord = document.getString("word");
				if ((wordIf.toLowerCase().equals(localWord.toLowerCase())))
				{
					ApiFuture<WriteResult> writeResult = db.collection("dictionary").document(wordIf).delete();
					System.out.println("Update time : " + writeResult.get().getUpdateTime());
					JSONObject successMessage = new JSONObject();
					successMessage.append("Success", "Word " + wordIf + " removed successfully");
					return successMessage.toString();
				} 
			} 		
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "Word "+wordIf+" is not posted in the custom dictionary. ");
			return errorMessage.toString();
		}
	}
  
	@CrossOrigin
	@PostMapping({"/setWord"})
	@ResponseBody
	public String postInfor(@RequestBody String jsonBody) throws IOException, JSONException, InterruptedException, ExecutionException 
	{
		DictionaryEntity obj = new DictionaryEntity();
		JSONObject jword = new JSONObject(jsonBody);
		String wordIf = jword.getString("word");
		String definitionIf = jword.getJSONArray("definitions").getString(0);
		jsonBody.toLowerCase();
		try 
		{
			InputStream serviceAccount = new FileInputStream("./serviceAccount.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = (new FirebaseOptions.Builder()).setCredentials(credentials).build();
			FirebaseApp.initializeApp(options);
		} 
		catch (Exception e) 
		{
			System.out.println("ya esta conectado");
		} 
			Firestore db = FirestoreClient.getFirestore();

		if( wordIf.isBlank()  ||  definitionIf.isBlank() || Pattern.matches("[a-zA-Z]+", wordIf) == false || Pattern.matches("[a-zA-Z]+", definitionIf) == false)
		{
			System.out.println("WORD ESTA VACIO");
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "please insert ONLY LETTERS and NO WHITESPACES");
			return errorMessage.toString();
		} 
		else 
		{
			ApiFuture<QuerySnapshot> query = db.collection("dictionary").get();
			QuerySnapshot querySnapshot = (QuerySnapshot)query.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot document : documents) 
			{
				String localWord = document.getString("word");
				if ((wordIf.toLowerCase()).equals(localWord.toLowerCase())) 
				{
					JSONObject jSONObject = new JSONObject();
					jSONObject.append("Error", "Word already exists");
					return jSONObject.toString();
				} 
			} 
			try 
			{
				ArrayList<String> auxArrList = new ArrayList<>();
				obj.setWord(wordIf);
				JSONArray arr = jword.getJSONArray("definitions");
				for (int i = 0; i < arr.length(); i++)
					auxArrList.add(arr.getString(i).toLowerCase()); 
				obj.setDefinition(auxArrList);
				this.customDictionary.add(obj);
				DocumentReference docRef1 = db.collection("dictionary").document(wordIf);
				Map<String, Object> data = new HashMap<>();
				data.put("word", wordIf.toLowerCase());
				data.put("definitions", auxArrList);
				ApiFuture<WriteResult> result = docRef1.set(data);
				System.out.println("Update time : " + ((WriteResult)result.get()).getUpdateTime());
				JSONObject successMessage = new JSONObject();
				successMessage.append("Success", "Word " + obj.getWord() + " added successfully");
				return successMessage.toString();
			} 
			catch (NumberFormatException ex) 
			{
				System.out.println(ex);
			} catch (Exception e) 
			{
				System.out.println(e.toString());
				JSONObject jSONObject = new JSONObject();
				jSONObject.append("Error", "Word not added successfully");
				return jSONObject.toString();
			} 
				JSONObject errorMessage = new JSONObject();
				errorMessage.append("Error", "Word not added successfully");
				return errorMessage.toString();
		}
	}
  
	@PutMapping(value = "/updateWord")
	@ResponseBody()
	public String updateInfor (@RequestBody String jsonBody) throws IOException, JSONException, InterruptedException, ExecutionException 
	{
		DictionaryEntity obj = new DictionaryEntity();
		JSONObject jword = new JSONObject(jsonBody);
		String wordIf = jword.getString("word");
		String definitionIf = jword.getJSONArray("definitions").getString(0);
		jsonBody.toLowerCase();
		try 
		{
			InputStream serviceAccount = new FileInputStream("./serviceAccount.json");
			GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
			FirebaseOptions options = (new FirebaseOptions.Builder()).setCredentials(credentials).build();
			FirebaseApp.initializeApp(options);
		} 
		catch (Exception e) 
		{
			System.out.println("ya esta conectado");
		} 
		Firestore db = FirestoreClient.getFirestore();

		if( wordIf.isBlank()  ||  definitionIf.isBlank() || Pattern.matches("[a-zA-Z]+", wordIf) == false || Pattern.matches("[a-zA-Z]+", definitionIf) == false)
		{
			System.out.println("WORD ESTA VACIO");
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "please insert ONLY LETTERS and NO WHITESPACES");
			return errorMessage.toString();
		} 
		else 
		{
			ApiFuture<QuerySnapshot> query = db.collection("dictionary").get();
			QuerySnapshot querySnapshot = (QuerySnapshot)query.get();
			List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
			for (QueryDocumentSnapshot document : documents) 
			{
				String localWord = document.getString("word");
				if (wordIf.toLowerCase().equals(localWord.toLowerCase())) 
				{          
					try 
					{	
						ArrayList<String> auxArrList = new ArrayList<>();
						obj.setWord(wordIf.toLowerCase());
						JSONArray arr = jword.getJSONArray("definitions");
						for (int i = 0; i < arr.length(); i++)
							auxArrList.add(arr.getString(i).toLowerCase()); 
						obj.setDefinition(auxArrList);
						this.customDictionary.add(obj);
						DocumentReference docRef1 = db.collection("dictionary").document(wordIf);
						Map<String, Object> data = new HashMap<>();
						// data.replace("word", wordIf);
						// data.replace("definitions", auxArrList.toString());
						data.put("word", wordIf.toLowerCase());
						data.put("definitions", auxArrList);
						ApiFuture<WriteResult> result = docRef1.set(data);
						System.out.println("Update time : " + ((WriteResult)result.get()).getUpdateTime());
						JSONObject successMessage = new JSONObject();
						successMessage.append("Success", "Word "  + wordIf  + " updated successfully");
						return successMessage.toString();
					} 
					catch (NumberFormatException ex) 
					{
						System.out.println(ex);
					} 
					catch (Exception e) 
					{
						System.out.println(e.toString());
						JSONObject jSONObject = new JSONObject();
						jSONObject.append("Error", "Word not updated successfully");
						return jSONObject.toString();
					}
				}   
			} 	
			JSONObject errorMessage = new JSONObject();
			errorMessage.append("Error", "Word "+wordIf+" does not exist in the custom dictionary. ");
			return errorMessage.toString();					
		}
	}
}