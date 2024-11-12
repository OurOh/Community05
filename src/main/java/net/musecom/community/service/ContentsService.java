package net.musecom.community.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class ContentsService {

	//게시물에서 html 태그 지우고 p태그 대신 br태그로 바꾸기
	public String extractParagraphs(String htmlCode) {
		Document doc = Jsoup.parse(htmlCode);
		Elements paragraphs = doc.select("p");
		
		StringBuilder extractText = new StringBuilder();
		for(Element paragraph : paragraphs) {
			extractText.append(paragraph.text()).append("<br>");
		}
		return extractText.toString();
	}
	
	// 본문 글 자르기
	public String cutParagraph(String content, int chop) {
		return (content.length() > chop) ? content.substring(0, chop)+"..." : content;
		
	}
	
}
