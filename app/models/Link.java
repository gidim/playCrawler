package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class Link extends Model {

	String url; //the url of the page linked from
	public String childUrl;// the ID of the page linked TO
	public String crawler; //name of crawler

	public Link(String url, String childUrl, String crawler){
		this.url = url;
		this.childUrl = childUrl;
		this.crawler = crawler;
		
		
	}
}
