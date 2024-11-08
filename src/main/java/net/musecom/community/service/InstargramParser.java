package net.musecom.community.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class InstargramParser {
   
   private String stringText;
   String descriptionUrl;
   public String getStringText(String iid) {
      String url = "https://www.instagram.com/p/"+iid;
      try {
         Document doc = Jsoup.connect(url).get();
         Elements metaTags = doc.select("meta[property=og:image]");
         String imageUrl = metaTags.attr("content");
         
         Elements descriptionTags = doc.select("meta[property=og:description]");
         String descriptionUrl = metaTags.attr("content");
         
         stringText = "이미지 URL : " + descriptionTags;
         stringText += "게시물 내용 : " + descriptionUrl;
                  
      }catch(IOException e) {
         e.printStackTrace();
      }
      return descriptionUrl;
   }
   
}
