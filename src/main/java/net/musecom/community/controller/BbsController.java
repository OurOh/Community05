package net.musecom.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.community.model.BbsAdmin;
import net.musecom.community.service.BbsAdminService;
import net.musecom.community.service.BbsService;



@Controller
@RequestMapping("/bbs")
public class BbsController {
	
	@Autowired
	private BbsService bbsService;
	
	@Autowired
	private BbsAdminService adminService;
	
	
	@GetMapping("/write")
	public String writeForm() {@RequestParam("bbsid") int id, Model model){
		
		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminDate(id);
		List<BbsCategory> categories = null;
		if(bbsAdminDto.getCategory() > 0) {
				categories = bbsAdminDto.getbbsCategory();
		}
		
		System.out.println("categories" + categories);
		
		model.addAttribute("categories", + categories);
		model.addAttribute("adminBbs", bbsAdminDto);
		
		return "bbs.write";
	}
	
	@PostMapping("/writefile"){
		
		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminDate(id);
		
		category = bbsAdminDto.getBbsCategory();
		model.addAttribute("adminBbs", bbsAdminDto);
		
		return "bbs.write";
	}
		System.out.println("게시판 글쓰기 writeAction()");
		bbsService.getBbsInsert();
		
		BbsAdmin bbsAdmin = adminService.getSelectById(id);
		String bbstitle = bbsAdmin.getBbstitle();
		String skin = bbsAdmin.getSkin();
		int category = bbsAdmin.getCategory();
		int listCount = bbsAdmin.getListcount();
		int pageCount = bbsAdmin.getPagecount();
		int lGrade = bbsAdmin.getLgrade();
		int Rgrade = bbsAdmin.getRegrade();
		int fGrade = bbsAdmin.getFgrade();
		int reGrade = bbsAdmin.getRegrade();
		int comGrade = bbsAdmin.getComgrade();
		int fileSize = bbsAdmin.getFilesize();
		int allFileSize = bbsAdmin.getAllfilesize();
		//썸네일 크기
		String thImgSize = bbsAdmin.getThimgsize();
		String thImg = thImgSize.split("\\|");
		String thWidth = thImg[0];
		String thHeight = thImg[1];
		
		//업로드 이미지 크기
		String imgSize = bbsAdmin.getImgsize();
		String img[] = imgSize.split("\\|");
		String imgWidth = img[0];
		String imgHeight = img[1];
		
		
		
		
		
		}
		return "bbs.wrtie";
		
	}
	
	
	
	
	@PostMapping("/writefile")
	public String writeAction() {
		@RequestParam("bbsid") int bbsid,
		@RequestParam("ref") int ref,
		@RequestParam("step") int step,
		@RequestParam("depth") int depth,
		@RequestParmam("title") String title,
		@RequestParam("writer") String writer,
		@RequestParam("password") String password,
		@RequestParam("userid") String userid,
		@RequestParam("content") String content,
		@RequestParam("sec") byte sec,
		@RequestParam("category") String category,
		@RequestParam(value="file", required=false) MultipartFile file
			, Model model){
				System.out.println("게시판 글쓰기 writeAction()");
			
		System.out.println("게시판 글쓰기 writeAction()");
		if(file != null && !file.isEmpty()) {
			//파일업로드 로직
			try {
				
				//fileUpload.setAbsoultePath("bbs")
				//파일 확장자 제한
				//파일 크기 제한
				//전체 파일 크기 제한
				
				
				
				
				
			}catch(Exceptione e)}
				redirectAttribute.addFlashAttribute("error", e.getMessage());
			}
	}
		bbsService.getBbsInsert(bbs);
		return "redirect:/";
	}
}




























