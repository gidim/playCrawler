package controllers;


import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import models.Link;
import models.Page;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;


public class Json {
	
	public static String generateCrawlerJson(String crawler){
		
		List<Page> pages = Page.find("byCrawler", crawler).fetch();
		JSONObject json = new JSONObject();
		
		
		for(Page page : pages){
			
			URL aURL;
			try {
				aURL = new URL(page.url);
				
				//add all children
				for(Link link : page.links){
					JSONObject element = new JSONObject();
					element.put("source",aURL.getPath()); //put the root
					
					aURL = new URL(link.childUrl);
					element.put("target", aURL.getPath());//put the child
					
					json.accumulate("",element);
				}
			

			
			
			
			
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
				

			
			
		}
		
		//cleaning up json
		String str = json.toString();
		str = str.substring(4);
		str =  str.substring(0, str.length()-1);
		
	System.out.println(str);
        return str;

	}
	

	public static String generateCrawlerJsonAlt(String crawler){
		
		List<Page> pages = Page.find("byCrawler", crawler).fetch();
		JSONObject json = new JSONObject(); //return json file
		

		for(Page page : pages){	
			URL aURL;
			try {
				aURL = new URL(page.url);
				JSONObject element = new JSONObject();//single node object
				
				element.put("name", "root.kid."+aURL.getPath());// {"name": "url, 
				element.put("size", page.links.size()*10);// {"name": "url, "size": size, 
				
				//add all children
				int i = 0;
				String lastChild = "";
				for(Link link : page.links){
							
					aURL = new URL(link.childUrl);
					element.accumulate("imports", "root.kid."+aURL.getPath()); //"name": "url, "size": size, ""imports":["child",]
					i++;
					
					lastChild =  "root.kid."+aURL.getPath();
				}
				
				if(i == 1)//one child
					element.accumulate("imports", lastChild);
			
				
				json.accumulate("", element);

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		//cleaning up json
		String str = json.toString();
		json = null; //gc help
		str = str.substring(4);
		str =  str.substring(0, str.length()-1);

        return str;

		
		
		
		
	}

}
