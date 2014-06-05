package controllers;

import play.*;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;

import java.net.MalformedURLException;
import java.util.*;

import models.*;
import play.data.validation.*;
import play.db.jpa.JPA;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

import javax.persistence.Query;

public class Application extends Controller {

	public static Queue <Link> urlQueue;
	
	public static final int MAX_PAGES = 1000;
	
	
    public static void index() {
        render();
    }
    
  
    public static void initCrawl(@Required String url) throws Exception{
    	int numOfPages = 0;
    	
    	if(validation.hasErrors()){
    		flash.error("fill field");
    		index();
    	}
    	
    	//queue of links waiting for inspection
    	urlQueue = new LinkedList<Link>();
    	
    	//get the root
    	getPage(url,url);

    	while(!urlQueue.isEmpty()){
    		
    		//get the link for inspection
    		Link linkToInspect = urlQueue.poll();
    		
    		//check that we don't have it already
    		List <Page> pl = Page.find("byUrl", linkToInspect.childUrl).fetch(1);
    		if(!pl.isEmpty()) //already got it
    			continue;
    		
    		getPage(linkToInspect.childUrl, url); //get the child
    		numOfPages++;
    	
        	
        	if(numOfPages > MAX_PAGES - 1)//already checked max amount of pages
        		break;
    	}
    	
    	
    	
    	render(url,numOfPages);
    }

    
    private static void getPage(String url, String crawler) 
    {
    	
    	Page p = new Page();
    	p.url = url;
    	p.crawler = crawler;
    	
    	//get the html
    	String html = Fetcher.getPageContent(url);
    	//p.html = html;
    	
    	//get html links
		Document doc = Jsoup.parse(html);
		doc.setBaseUri(crawler);
		Elements links = doc.select("a");
    	
    	//add links to Page
		for (Element link : links) {
        	
        	String childUrl = link.attr("abs:href");
        	
        	if(childUrl.isEmpty())// empty href
        		continue;

        	if(url.equals(childUrl))//same page!
        		continue;
        	
        	try{
        	URL aURL = new URL(childUrl);
        	URL cURL = new URL(crawler);
        	
        	if(!(aURL.getHost().equals(cURL.getHost()))) //ignore links to pages outside the domain
        		continue;
        	
        	Link l = new Link(url, childUrl, crawler);
        	urlQueue.add(l); // add to the queue of links waiting for inspection
        	p.links.add(l); //add to the list the page links to

        	
        	}
        	catch (Exception e)
        	{
        		// show error here
        		response.print(e);
        	}
        	
		}
		
		p.save();
    	
    	
    }
    
    public static void displayCrawl(String crawler){
    	
    	if(crawler == null)
    		displayListOfCrawlers();

    	
    	render(crawler);
    }    
    
    public static void displayCrawlAlt(String crawler){
    	
    	if(crawler == null)
    		displayListOfCrawlers();

    	
    	render(crawler);
    }
    
    public static void displayListOfCrawlers(){
    	
    	
    	//List<Page> crawlers = Page.find("select distinct crawler from Page");
        Query query = JPA.em().createQuery("select distinct crawler from Page");
        List<Page> crawlers = query.getResultList();
        

        render(crawlers);
    	
    }
    
    
    public static void pagesJson(String crawler){
    	String pages = Json.generateCrawlerJson(crawler);
    	
    	request.format = "json"; 
    	render(pages);
    }


	public static void pagesJsonAlt(String crawler){
		String pages = Json.generateCrawlerJsonAlt(crawler);
		
		request.format = "json"; 
		render(pages);
	}
}
