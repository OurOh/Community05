package net.musecom.community.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.community.model.Bbs;
import net.musecom.community.model.BbsAdmin;
import net.musecom.community.model.BbsCategory;
import net.musecom.community.model.CustomUserDetails;
import net.musecom.community.model.FileDto;
import net.musecom.community.model.Member;
import net.musecom.community.service.BbsAdminService;
import net.musecom.community.service.BbsService;
import net.musecom.community.service.FileService;

@Controller
@RequestMapping("/bbs")
public class BbsController {

	@Autowired
	private BbsService bbsService;
	
	@Autowired
	private BbsAdminService adminService;
	
	@Autowired
	private FileService fileService;

	@GetMapping("/write")
	public String writeForm(@RequestParam("bbsid") int id, Model model ) {
		
		//인증정보를 이용한 사용자 정보 가져오기
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("권한" + auth);

		Member member = null;
		
		if(auth != null && auth.getPrincipal() instanceof CustomUserDetails) {	
		     CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		     member = userDetails.getMember();
		}
		
        BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(id);
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(id);
        }
        System.out.println("member" + member);
        
        model.addAttribute("member", member);
        model.addAttribute("categories", categories);
		model.addAttribute("adminBbs", bbsAdminDto);
        
		return "bbs.write";
	}
	
	@PostMapping("/write")
	public String writeAction(@ModelAttribute Bbs bbs, Model model) {
		System.out.println("게시판 글쓰기 writeAction()");
		bbsService.getBbsInsert(bbs);
		return "redirect:/";
	}
	
	
	
	@PostMapping("/upload")
	public ResponseEntity<Map<String, Object>> uploadFile(
		@RequestParam("file") MultipartFile file, @RequestParam("bbsid") int bbsid){
        Map<String, Object> result = new HashMap<>();
        
        BbsAdmin bbsAdmin = new BbsAdmin();
        bbsAdmin = adminService.getBbsAdminData(bbsid);
        String path = Integer.toString(bbsid);
        String[] ext = bbsAdmin.getFilechar().split(",");
        long fileSize = bbsAdmin.getFilesize();
        try {
        	
        	
        	long fileId = fileService.uploadFile(file, path, ext, fileSize);
        	result.put("success", true);
        	result.put("fileId", fileId);
        	result.put("fileName", FileDto.getnewfilename());
        	result.put("offFileName", fileDto.getOrfilename());
        	result.put("fileSize",fileDto.getFilesize());
        	result.put("fileUrl", "/res/upload/"+path+"/"+fileDto.getNewfilename());
        	
        }catch(Exception e) {
        	result.put("success" , false);
        	result.put("fileId", e.getMessage());
        }

		return ResponseEntity.ok(result);
		
		
	}

		
	
}
