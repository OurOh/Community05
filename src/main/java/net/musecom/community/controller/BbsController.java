package net.musecom.community.controller;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.community.model.Bbs;
import net.musecom.community.model.BbsAdmin;
import net.musecom.community.model.BbsCategory;
import net.musecom.community.model.FileDto;
import net.musecom.community.model.Member;
import net.musecom.community.service.BbsAdminService;
import net.musecom.community.service.BbsAuthenticationService;
import net.musecom.community.service.BbsCategoryService;
import net.musecom.community.service.BbsFileCleanupService;
import net.musecom.community.service.BbsListService;
import net.musecom.community.service.BbsService;
import net.musecom.community.service.ContentsService;
import net.musecom.community.service.FileDeleteService;
import net.musecom.community.service.FileService;
import net.musecom.community.service.MemberService;
import net.musecom.community.service.PagingService;
import net.musecom.community.util.Paging;

@Controller
@RequestMapping("/bbs")
public class BbsController {

	@Autowired
	private BbsService bbsService;
	
	@Autowired
	private BbsAdminService adminService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private ServletContext sc;

	@Autowired
	private ContentsService contentsControll;  //html 태그 정리를 위한 클래스
	
	@Autowired
	private BbsAuthenticationService autthenticationService;
	
	@Autowired
	private BbsFileCleanupService fileCleanupService;
	
	@Autowired
	private BbsCategoryService categoryService;
	
	@Autowired
	private BbsListService listService;
	
	@Autowired
	private PagingService pagingService;
	
	@Autowired
	private FileDeleteService fileDeleteService;
	
	/****************************************************************************
	 * list
	 * @param bbsid
	 * @param page
	 * @param searchKey
	 * @param searchVal
	 * @param model
	 * @return
	 */
	
	/*  
	 * 1. 권한로직 :  BbsAuthenticationService 
       2. 쓰레기파일삭제 : BbsFileCleanupService
       3. 카테고리 : BbsCategoryService 
       4. 게시물조회 및 파일처리 : BbsListService
       5. 페이징처리 : PaginService 
     */
	
	@GetMapping("/list")
	public String List(
		@RequestParam("bbsid") int bbsid, 
		@RequestParam(value="page", defaultValue="1") int page,
		@RequestParam(required=false) String searchKey,
		@RequestParam(required=false) String searchVal,
		Model model) {
			
		//권한검증
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
	  	
		//쓰레기 파일 삭제
		fileCleanupService.cleanFiles(bbsid);
		
		//카테고리 조회
		List<BbsCategory> categories = categoryService.getCategories(bbsid);
        model.addAttribute("categories", categories);
		
		//게시물 조회 및 페이징 처리
	    BbsAdmin bbsAdminDto = (BbsAdmin) model.getAttribute("adminBbs");
		int listCount = bbsAdminDto.getListcount();
		int pageCount = bbsAdminDto.getPagecount();
		int pg = (page -1) * listCount;
		
		int totalRecord = bbsService.getBbsCount(bbsid);
		Paging paging = pagingService.getPaging(totalRecord, listCount, page, pageCount);
		
		List<Bbs> bbslist = listService.getBbsList(bbsid, pg, listCount, searchKey, searchVal);
		listService.processBbsList(bbslist, totalRecord, pg, 100);
			
		model.addAttribute("paging", paging);	
		model.addAttribute("bbslist", bbslist);
	
		//인기검색어 출력
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		
		String skin = bbsAdminDto.getSkin();
		
		switch(skin) {
		   case "gallery":
			  return "gallery.list";
	   	   case "article":
			  return "article.list";
		   case "blog":
			  return "blog.list";
		   default:
			  return "bbs.list"; 
		}
		
		/*
		if(skin.equals("gallery")) {
		   return "gallery.list";
		}else if(skin.equals("article")) {
		   return "article.list";
		}else if(skin.equals("blog")) {
		   return "blog.list";	 
		}else {
			return "bbs.list";
		}
		*/
	}
	
	
	/****************************************************************************
	 * VIEW
	 * @param bbsid
	 * @param id
	 * @param pageF
	 */
	@GetMapping("/view")
	public String views(
	  @RequestParam("bbsid") int bbsid,
	  @RequestParam("id") long id,
	  @RequestParam(value="page", defaultValue="1") int page,
	  Model model
	) { 

		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(bbsid);
		Member member = memberService.getAuthenticatedMember();
				
		//권한검증
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		int sec = bbsView.getSec();
		
		if( sec == 1 && member == null ||
			sec == 1 && member.getUserid() == null ||
			sec == 1 && !"admin".equals(member.getUserid()) &&
			sec == 1 && !member.getUserid().equals(bbsView.getUserid())) {
		    System.out.println("비밀글이므로 pass로 보냄");
			return "redirect: /community/bbs/pass?mode=view&bbsid="+bbsid+"&id="+id+"&page="+page;
		}	
		//조회수 증가
		bbsService.updateCount(id);
		
		//파일업로드 처리
		List<FileDto> files = fileService.getFilesByBbsId(id);
		for(FileDto file : files) {
			System.out.println(file.getNewfilename());		
		}
		
		//카테고리 조회
		List<BbsCategory> categories = categoryService.getCategories(bbsid);
        model.addAttribute("categories", categories);
		
		//인기검색어 출력
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		
		model.addAttribute("files", files);
		model.addAttribute("adminBbs", bbsAdminDto);
	    model.addAttribute("bbsid", bbsid);
	    model.addAttribute("page", page);
		model.addAttribute("bbs", bbsView);
		return "bbs.view";
	}
	
	@GetMapping("/update")
	public String update(
			  @RequestParam("bbsid") int bbsid,
			  @RequestParam("id") long id,
			  @RequestParam(value="page", defaultValue="1") int page,
			  Model model,
			  HttpSession session
		) {
		System.out.println("업데이트");
		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(bbsid);
		Member member = memberService.getAuthenticatedMember();
		
		//세션체크
		String sessionKey = "bbsAuth_" + id;
		Boolean isBbsAuthenticated = (Boolean) session.getAttribute(sessionKey);
		
		//권한검증
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		int sec = bbsView.getSec();
		
		if(isBbsAuthenticated == null || !isBbsAuthenticated) {
			if( member == null || member.getUserid() == null ||
			   !"admin".equals(member.getUserid()) &&
			   !member.getUserid().equals(bbsView.getUserid())) {
			    System.out.println(" pass로 보냄");
				return "redirect: /community/bbs/pass?mode=edit&bbsid="+bbsid+"&id="+id+"&page="+page;
			}	
		}
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(bbsid);
        }
                
		//인기검색어 출력
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
        
        model.addAttribute("categories", categories);
		model.addAttribute("adminBbs", bbsAdminDto);
	    model.addAttribute("bbsid", bbsid);
	    model.addAttribute("page", page);
		model.addAttribute("bbs", bbsView);
		return "bbs.update";
	}
	
	@PostMapping("/update")
	public String updateForm() {
		
		return null;
	}
	
	
	@GetMapping("/pass")
	public String passForm(Model model) {

		return "bbs.pass";
	}
	
	
	/*****************************************************************************]
	 * write get
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/write")
	public String writeForm(@RequestParam("bbsid") int id, Model model ) {
		
		//인증정보를 이용한 사용자 정보 가져오기
		Member member = memberService.getAuthenticatedMember();
		model.addAttribute("member", member);
		
        BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(id);
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(id);
        }
        System.out.println("member" + member);
        
		//인기검색어 출력
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
        
        model.addAttribute("categories", categories);
		model.addAttribute("adminBbs", bbsAdminDto);
        
		return "bbs.write";
	}
	
	
	/***************************************************************************
	 * write post
	 * @param bbsid
	 * @param fileIds
	 * @param title
	 * @param content
	 * @param writer
	 * @param password
	 * @param sec
	 * @param userid
	 * @param category
	 * @param admin
	 * @param model
	 * @return
	 */
	@PostMapping("/write")
	public String writeAction(
		@RequestParam("bbsAdminId") int bbsid,	
        @RequestParam(value="fileId[]", required = false) List<Long> fileIds, 
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam("writer") String writer,
        @RequestParam("password") String password,
        @RequestParam(name = "sec", defaultValue="0") byte sec,
        @RequestParam("userid") String userid,
        @RequestParam(name = "category", required = false) String category,
        Model model) {
		System.out.println("게시판 글쓰기 writeAction()");
		try {
	        Bbs bbs = new Bbs();
	        bbs.setTitle(title);
	        bbs.setContent(content);
	        bbs.setBbsid(bbsid);
	        bbs.setWriter(writer);
	        bbs.setPassword(password);
	        bbs.setSec(sec);	        
	        bbs.setUserid(userid);
	        bbs.setCategory(category);
	        
			bbsService.getBbsInsert(bbs, fileIds);
			
			if(userid.equals("admin") && bbsid==1) {
			     return "redirect:/admin/write";
			}     	
				return "redirect:/bbs/list?bbsid="+bbsid;
			
		}catch(Exception e) {
		    model.addAttribute("error", "글 작성중 오류가 발생했습니다." + e.getMessage());
			if(userid.equals("admin") && bbsid==1) {
			     return "redirect:/admin/write";
			}
			return "redirect:/bbs/list?bbsid="+bbsid;
			
		}
	}
	
	/**********************
	 * 게시물 비번 확인
	 * @Param id
	 * @Param password
	 * @return int
	 */
	@PostMapping("/passwd")
	@ResponseBody
	public String equalPassword(
	   @RequestParam("id") long id,
	   @RequestParam("password") String password,
	   HttpSession session
	) {
		
		int r = bbsService.getBbsPassword(id, password);
		
		if(r > 0) {
			//세션
			try {
				String sessionKey = "bbsAuth_" + id;
				session.setAttribute(sessionKey, true);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		String res = Integer.toString(r);
		return res;
	}
	
	
	/******************************************************************************
	 * upload
	 * @param file
	 * @param bbsid
	 * @return
	 */
	
	@PostMapping("/upload")
	public ResponseEntity<Map<String, Object>> uploadFile(
		@RequestParam("file") MultipartFile file, 
		@RequestParam("bbsid") int bbsid){
        Map<String, Object> result = new HashMap<>();
        
        try {
            BbsAdmin bbsAdmin = new BbsAdmin();
            FileDto fileDto = new FileDto();
            bbsAdmin = adminService.getBbsAdminData(bbsid);
            String path = Integer.toString(bbsid);
            String extFilter = bbsAdmin.getFilechar();
            String[] ext = (extFilter != null && !extFilter.isEmpty()) ?
            		          extFilter.split(",") : null;
            long fileSize = bbsAdmin.getFilesize() * 1024 * 1024;
        	
        	fileDto = fileService.uploadFile(file, path, ext, fileSize);
        	
        	result.put("success", true);
        	result.put("fileId", fileDto.getId());
        	result.put("fileName", fileDto.getNewfilename());
        	result.put("orFileName", fileDto.getOrfilename());
        	result.put("fileSize", fileDto.getFilesize());
        	result.put("fileUrl", "/community/res/upload/"+path+"/"+fileDto.getNewfilename());
        	result.put("ext", fileDto.getExt());
        	
        }catch(Exception e) {
        	result.put("success" , false);
        	result.put("fileId", e.getMessage());
        	System.out.println(Arrays.toString(e.getStackTrace()));
        }

		return ResponseEntity.ok(result);
	}


	//이미지 경로 반환하기
	/*
	@GetMapping("/res/upload/{bbsId}/{fname}")
	@ResponseBody
	public ResponseEntity<Resource> getImage(
			@PathVariable("bbsId") int bbsId,
			@PathVariable("fname") String fname){
		try {
			Path imagePath = Paths.get("/community/res/upload/"+bbsId+"/"+fname);
			Resource resource = new UrlResource(imagePath.toUri().toURL());
			return ResponseEntity.ok()
				   .contentType(MediaType.IMAGE_JPEG)
				   .body(resource);
		}catch (MalformedURLException e) {
	        e.printStackTrace();
	        return ResponseEntity.notFound().build();
	    }
	}
		
	*/
	
	
	@GetMapping("/del")
	public String DeleteForm(
			@RequestParam("bbsid") int bbsid, 
			@RequestParam("id") long id, 
			@RequestParam(value="page", defaultValue="1") int page,
			Model model,
			HttpSession session) {
	
	    //쎄션체크
		String sessionKey = "bbsAuth_" + id;
		Boolean isBbsAuthenticated = (Boolean) session.getAttribute(sessionKey);
		
		/* 관리자 권한이면 무조건 삭제, 
		 * 회원권한이면 아이디가 같은 경우 삭제, 
		 * 그외는 비번을 확인하여 삭제 
		 * -- 1. 파일을 삭제 한 후 2. db를 삭제
		 * */
		Member member = memberService.getAuthenticatedMember();
		//권한검증
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		
		if(isBbsAuthenticated == null || !isBbsAuthenticated) {
			if( member == null || member.getUserid() == null ||
			   !"admin".equals(member.getUserid()) &&
			   !member.getUserid().equals(bbsView.getUserid())) {
			    System.out.println(" pass로 보냄");
				return "redirect: /community/bbs/pass?mode=del&bbsid="+bbsid+"&id="+id+"&page="+page;
			}	
		}
		
		/* 1. 첨부파일이 있는지 확인한 후 있으면 
		   2. 첨부파일 삭제
		   3. 파일db 지우기
		   4. bbs table 삭제
		*/   
		if(fileDeleteService.hasFilesToDelete(id)) {
			//삭제로직
			fileDeleteService.deleteFile(id, bbsid);
		}
		try {
			bbsService.setDeleteById(id);
		}catch(Exception e) {
			e.printStackTrace();
			return "redirect:list?bbsid="+bbsid;
		}
		
		return "redirect:list?bbsid="+bbsid;
	}
	
	
	@PostMapping("/deleteFile")
	@ResponseBody
	public Map<String, Object> deleteFile(
	  @RequestBody Map<String, String> request){
	  Map<String, Object> result = new HashMap<>();
 
	  try {
		 //파일 정보
		 long fileId = Long.parseLong(request.get("fileId"));
	     String bbsId = request.get("bbsId");   
	     FileDto fileDto = fileService.getFile(fileId); 
	     if(fileDto == null) {
	    	 result.put("success", false);
	    	 result.put("message", "파일정보를 찾을 수 없습니다.");
	    	 return result;
	     }
	       String path = "/community/res/upload/" + bbsId + "/";
	       String fullPath = path + fileDto.getNewfilename();
	       File file = new File(fullPath);
	       
	       //파일삭제 
	       if(file.exists() && file.delete()) {
	    	   //db 삭제
	    	   fileService.deleteFile(fileId);
	    	   result.put("success", true);
	    	   result.put("message", "성공적으로 삭제되었습니다.");	      
	       }else{
		       result.put("success", false);
		       result.put("message", "파일삭제에 실패했습니다.");
	       }
	       
	  }catch(Exception e) {
	       result.put("success", false);
	       result.put("message", "파일삭제에 실패했습니다." + e.getMessage());
	  }
	  
	   return result;
	}
	
	
	
	@GetMapping("/download")
	public ResponseEntity<byte[]> downloadFile(
			@RequestParam("fileId") long fileId,
			@RequestParam("bbsId") String bbsid) {
		try {
		 	//파일정보
			FileDto fileDto = (FileDto) fileService.getFile(fileId);
			
			//파일이 있는 경로
			//String filePath = "/community/res/upload/"+ bbsid + "/" + fileDto.getNewfilename();
			String basePath = System.getProperty("catalina.base")+"/wtpwebapps";
			String filePath = basePath + "/community/res/upload/"+ bbsid + "/" + fileDto.getNewfilename();
			//System.out.println(filePath);
			File file = new File(filePath);
			
			//파일이 존재하지 않는 경우 예외처리
			if(!file.exists()) {
				throw new RuntimeException("경로에 파일이 존재하지 않습니다.");
			}
			
			//파일 데이터 읽어오기
			byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
			
			//파일 다운로드를 위한 헤더 설정
			 String originalFileName = 
					 new String(fileDto.getOrfilename().getBytes("UTF-8"), 
							    "ISO-8859-1");
			
			return ResponseEntity.ok()
					.header("Content-Disposition", "attachment;filename=\""+originalFileName+"\"")
					.header("Content-Type", "application/octet-stream")
					.body(fileContent);
					
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/search")
    public String search(@RequestParam("searchVal") String searchVal, Model model) {
		//검색 기록 저장
		bbsService.insertSearchKeyword(searchVal);
		
		//검색 실행
		Map<Integer, List<Bbs>> groupedResults = bbsService.searchBbsPostsGrouped(searchVal);
		model.addAttribute("groupedResults", groupedResults);
		model.addAttribute("searchVal", searchVal);
		
		//인기검색어 출력
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		
		return "bbs.searchGroup";
	}
}
