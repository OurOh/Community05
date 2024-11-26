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
	private ContentsService contentsControll;  //html �±� ������ ���� Ŭ����
	
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
	 * 1. ���ѷ��� :  BbsAuthenticationService 
       2. ���������ϻ��� : BbsFileCleanupService
       3. ī�װ� : BbsCategoryService 
       4. �Խù���ȸ �� ����ó�� : BbsListService
       5. ����¡ó�� : PaginService 
     */
	
	@GetMapping("/list")
	public String List(
		@RequestParam("bbsid") int bbsid, 
		@RequestParam(value="page", defaultValue="1") int page,
		@RequestParam(required=false) String searchKey,
		@RequestParam(required=false) String searchVal,
		Model model) {
			
		//���Ѱ���
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
	  	
		//������ ���� ����
		fileCleanupService.cleanFiles(bbsid);
		
		//ī�װ� ��ȸ
		List<BbsCategory> categories = categoryService.getCategories(bbsid);
        model.addAttribute("categories", categories);
		
		//�Խù� ��ȸ �� ����¡ ó��
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
	
		//�α�˻��� ���
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
				
		//���Ѱ���
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		int sec = bbsView.getSec();
		
		if( sec == 1 && member == null ||
			sec == 1 && member.getUserid() == null ||
			sec == 1 && !"admin".equals(member.getUserid()) &&
			sec == 1 && !member.getUserid().equals(bbsView.getUserid())) {
		    System.out.println("��б��̹Ƿ� pass�� ����");
			return "redirect: /community/bbs/pass?mode=view&bbsid="+bbsid+"&id="+id+"&page="+page;
		}	
		//��ȸ�� ����
		bbsService.updateCount(id);
		
		//���Ͼ��ε� ó��
		List<FileDto> files = fileService.getFilesByBbsId(id);
		for(FileDto file : files) {
			System.out.println(file.getNewfilename());		
		}
		
		//ī�װ� ��ȸ
		List<BbsCategory> categories = categoryService.getCategories(bbsid);
        model.addAttribute("categories", categories);
		
		//�α�˻��� ���
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
		System.out.println("������Ʈ");
		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(bbsid);
		Member member = memberService.getAuthenticatedMember();
		
		//����üũ
		String sessionKey = "bbsAuth_" + id;
		Boolean isBbsAuthenticated = (Boolean) session.getAttribute(sessionKey);
		
		//���Ѱ���
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		int sec = bbsView.getSec();
		
		if(isBbsAuthenticated == null || !isBbsAuthenticated) {
			if( member == null || member.getUserid() == null ||
			   !"admin".equals(member.getUserid()) &&
			   !member.getUserid().equals(bbsView.getUserid())) {
			    System.out.println(" pass�� ����");
				return "redirect: /community/bbs/pass?mode=edit&bbsid="+bbsid+"&id="+id+"&page="+page;
			}	
		}
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(bbsid);
        }
                
		//�α�˻��� ���
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
		
		//���������� �̿��� ����� ���� ��������
		Member member = memberService.getAuthenticatedMember();
		model.addAttribute("member", member);
		
        BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(id);
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(id);
        }
        System.out.println("member" + member);
        
		//�α�˻��� ���
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
		System.out.println("�Խ��� �۾��� writeAction()");
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
		    model.addAttribute("error", "�� �ۼ��� ������ �߻��߽��ϴ�." + e.getMessage());
			if(userid.equals("admin") && bbsid==1) {
			     return "redirect:/admin/write";
			}
			return "redirect:/bbs/list?bbsid="+bbsid;
			
		}
	}
	
	/**********************
	 * �Խù� ��� Ȯ��
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
			//����
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


	//�̹��� ��� ��ȯ�ϱ�
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
	
	    //���üũ
		String sessionKey = "bbsAuth_" + id;
		Boolean isBbsAuthenticated = (Boolean) session.getAttribute(sessionKey);
		
		/* ������ �����̸� ������ ����, 
		 * ȸ�������̸� ���̵� ���� ��� ����, 
		 * �׿ܴ� ����� Ȯ���Ͽ� ���� 
		 * -- 1. ������ ���� �� �� 2. db�� ����
		 * */
		Member member = memberService.getAuthenticatedMember();
		//���Ѱ���
		if(!autthenticationService.chechAuthorization(bbsid, model)) {
			return "redirect: /community/";
		}
		
		Bbs bbsView = bbsService.getBbs(id);
		
		if(isBbsAuthenticated == null || !isBbsAuthenticated) {
			if( member == null || member.getUserid() == null ||
			   !"admin".equals(member.getUserid()) &&
			   !member.getUserid().equals(bbsView.getUserid())) {
			    System.out.println(" pass�� ����");
				return "redirect: /community/bbs/pass?mode=del&bbsid="+bbsid+"&id="+id+"&page="+page;
			}	
		}
		
		/* 1. ÷�������� �ִ��� Ȯ���� �� ������ 
		   2. ÷������ ����
		   3. ����db �����
		   4. bbs table ����
		*/   
		if(fileDeleteService.hasFilesToDelete(id)) {
			//��������
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
		 //���� ����
		 long fileId = Long.parseLong(request.get("fileId"));
	     String bbsId = request.get("bbsId");   
	     FileDto fileDto = fileService.getFile(fileId); 
	     if(fileDto == null) {
	    	 result.put("success", false);
	    	 result.put("message", "���������� ã�� �� �����ϴ�.");
	    	 return result;
	     }
	       String path = "/community/res/upload/" + bbsId + "/";
	       String fullPath = path + fileDto.getNewfilename();
	       File file = new File(fullPath);
	       
	       //���ϻ��� 
	       if(file.exists() && file.delete()) {
	    	   //db ����
	    	   fileService.deleteFile(fileId);
	    	   result.put("success", true);
	    	   result.put("message", "���������� �����Ǿ����ϴ�.");	      
	       }else{
		       result.put("success", false);
		       result.put("message", "���ϻ����� �����߽��ϴ�.");
	       }
	       
	  }catch(Exception e) {
	       result.put("success", false);
	       result.put("message", "���ϻ����� �����߽��ϴ�." + e.getMessage());
	  }
	  
	   return result;
	}
	
	
	
	@GetMapping("/download")
	public ResponseEntity<byte[]> downloadFile(
			@RequestParam("fileId") long fileId,
			@RequestParam("bbsId") String bbsid) {
		try {
		 	//��������
			FileDto fileDto = (FileDto) fileService.getFile(fileId);
			
			//������ �ִ� ���
			//String filePath = "/community/res/upload/"+ bbsid + "/" + fileDto.getNewfilename();
			String basePath = System.getProperty("catalina.base")+"/wtpwebapps";
			String filePath = basePath + "/community/res/upload/"+ bbsid + "/" + fileDto.getNewfilename();
			//System.out.println(filePath);
			File file = new File(filePath);
			
			//������ �������� �ʴ� ��� ����ó��
			if(!file.exists()) {
				throw new RuntimeException("��ο� ������ �������� �ʽ��ϴ�.");
			}
			
			//���� ������ �о����
			byte[] fileContent = java.nio.file.Files.readAllBytes(file.toPath());
			
			//���� �ٿ�ε带 ���� ��� ����
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
		//�˻� ��� ����
		bbsService.insertSearchKeyword(searchVal);
		
		//�˻� ����
		Map<Integer, List<Bbs>> groupedResults = bbsService.searchBbsPostsGrouped(searchVal);
		model.addAttribute("groupedResults", groupedResults);
		model.addAttribute("searchVal", searchVal);
		
		//�α�˻��� ���
		List<Map<String, Object>> popularKeywords = bbsService.getPopularKeyword();
		model.addAttribute("popularKeywords", popularKeywords);
		
		return "bbs.searchGroup";
	}
}
