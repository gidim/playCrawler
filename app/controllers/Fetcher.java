package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import play.libs.F;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.mvc.Controller;

public class Fetcher  {

	
	
	   public static String getPageContent(String urlToRead) {
		   
 
		   HttpResponse res = WS.url(urlToRead).followRedirects(false).get();
		   return res.getString();
		
	   }
}

