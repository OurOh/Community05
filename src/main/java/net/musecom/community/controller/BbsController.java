package net.musecom.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.musecom.community.service.BbsService;



@Controller
@RequestMapping("/bbs")
public class BbsController {
	
	@Autowired
	private BbsService bbsService;
	
	@GetMapping("/write")
	public String writeForm() {
		return "bbs.wrtie";
		
	}
	
	@PostMapping("/write")
	public String writeAction() {
		System.out.println("게시판 글쓰기" + writeAction());
		bbsService.getBbsInsert(bbs);
		return "redirect:/";
	}
}
