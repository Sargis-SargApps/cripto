package com.sargapps.cripto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controlador {
	@GetMapping("/bitcoin")
	public String bitcoin(@RequestParam(value="tipo",defaultValue="USD") String tipo) {
		String url = "https://blockchain.info/ticker";
		String respuesta = null;
		try {
			respuesta =peticionHttpGet(url,tipo).toString();
			
			System.out.println("La respuesta es:\n" + respuesta);
			return respuesta;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return respuesta;
		
	}
	
	public static JSONObject peticionHttpGet(String urlParaVisitar,String tipo) throws Exception {
		
		  StringBuilder resultado = new StringBuilder();	 
		  URL url = new URL(urlParaVisitar);
		  HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		  conexion.setRequestMethod("GET");
		  BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
		  String linea;
		  while ((linea = rd.readLine()) != null) {
		    resultado.append(linea);
		  }
		  rd.close();
		  
		 return parsearJSON(resultado.toString(),tipo);
		}
	
	public static JSONObject parsearJSON(String json,String tipo) {
	
		JSONObject objetoJson = new JSONObject(json);
		JSONObject dolar = objetoJson.getJSONObject(tipo);
		double precio = dolar.getDouble("15m");
		double last = dolar.getDouble("last");
		double buy = dolar.getDouble("buy");
		double sell = dolar.getDouble("sell");
		String symbol = dolar.getString("symbol");
		//String simbolo = objetoJson.getString("EUR");
		Bitcoin bitcoin = new Bitcoin(precio,last,sell,symbol);
		JSONObject jsonO=new JSONObject(bitcoin);
		System.out.println(jsonO);
		return jsonO;
	}
	
	
}
