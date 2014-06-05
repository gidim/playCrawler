package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import controllers.Fetcher;

import java.net.*;
import java.net.URL;
import java.io.*;

import play.*;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;

import java.net.MalformedURLException;
import java.util.*;

import models.*;
import play.data.validation.*;
import play.db.jpa.Model;

@Entity
public class Page extends Model{
	

	public String crawler;
	@Lob
   // public String html;
    public String url;
    @OneToMany(cascade=CascadeType.ALL)
    public List<Link> links;

	public Page(){
		links = new ArrayList<Link>();
	}
	

    
}
