package net.musecom.community.controller;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.community.model.Bbs;
import net.musecom.community.model.BbsAdmin;
import net.musecom.community.model.BbsCategory;
import net.musecom.community.model.FileDto;
import net.musecom.community.model.Member;
import net.musecom.community.service.BbsAdminService;
import net.musecom.community.service.BbsService;
import net.musecom.community.service.ContentsService;
import net.musecom.community.service.FileService;
import net.musecom.community.service.MemberService;
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
	
	
	/****************************************************************************
	 * list
	 * @param bbsid
	 * @param page
	 * @param searchKey
	 * @param searchVal
	 * @param model
	 * @return
	 */
	
	@GetMapping("/list")
	public String List(
		@RequestParam("bbsid") int bbsid, 
		@RequestParam(value="page", defaultValue="1") int page,
		@RequestParam(required=false) String searchKey,
		@RequestParam(required=false) String searchVal,
		Model model) {
		
		BbsAdmin bbsAdminDto = new BbsAdmin();
		bbsAdminDto = adminService.getBbsAdminData(bbsid);
		
		/*** ���� ���� **/
		if(bbsAdminDto.getLgrade() > 0) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(authentication instanceof AnonymousAuthenticationToken) {
				//�͸� ������̸� �Խ����� ȸ������ ���
				model.addAttribute("error", "ȸ�����Դϴ�. �α����ϼ���.");
				return "redirect: /comunity/";
			}else {
				//���������� �̿��� ����� ���� ��������
		        Member member = memberService.getAuthenticatedMember();
	            if(member.getGrade() < bbsAdminDto.getLgrade()) {
	            	model.addAttribute("error", "������ �����ϴ�.");
	            	model.addAttribute("member", member);
	            	return "redirect: /comunity/";
	            }
			}
		}
		
		//�͸� ����� ���� ������ ��� ó���� �ֱ�
		if(!(SecurityContextHolder.getContext().getAuthentication() instanceof  AnonymousAuthenticationToken)) {
			Member member = memberService.getAuthenticatedMember();
			model.addAttribute("member", member);
		}
		      	
		
		/******************************************************************/
		//������ ���� ����
		List<String> fileNames = fileService.selectFileWithBbsIdZero();
		if(fileNames != null && !fileNames.isEmpty()) {
			String delFilePath = sc.getRealPath("/res/upload/") + bbsid + "/";
			System.out.println(delFilePath);
			
			File fileDesk = null;
			for( String fileName : fileNames) {
				System.out.println(fileName);
				fileDesk = new File(delFilePath + fileName);
				
				//������ ������ ������ ����
				if(fileDesk.exists() && fileDesk.delete()) {
					System.out.println(fileDesk + "�����߽��ϴ�.");
				}
			}
				//���� ������ �Ϸ�Ǹ� table�� �÷� ����
				fileService.deleteFileWithBbsIdZero();
		}	
		
		
		List<BbsCategory> categories = null;
        if(bbsAdminDto.getCategory() > 0) {
        	categories = adminService.getBbsCategoryById(bbsid);
        }
          
        model.addAttribute("categories", categories);
		model.addAttribute("adminBbs", bbsAdminDto);
		
		String skin = bbsAdminDto.getSkin();
		int listCount = bbsAdminDto.getListcount();
		int pageCount = bbsAdminDto.getPagecount();
		int pg = (page -1) * listCount;
		
		int totalRecord = ((searchKey != null && !searchKey.isEmpty()) && 
		   (searchVal != null && !searchVal.isEmpty())) ?
		      bbsService.getSearchBbsCount(bbsid, searchKey, searchVal)	
	          :bbsService.getBbsCount(bbsid);
		
		Paging paging = new Paging(totalRecord, listCount, page, pageCount);
		
		List<Bbs> bbslist = ((searchKey != null && !searchKey.isEmpty()) && 
				   (searchVal != null && !searchVal.isEmpty()))?
		     bbsService.getSerchBbsList(bbsid, pg, listCount, searchKey, searchVal)
		     :bbsService.getBbsList(bbsid, pg, listCount);
	
		//�Խù� ��ȣ
		long num = paging.getTotalRecords() - pg;
		
		for(Bbs bbs : bbslist) {
			/*
			 * LocalDateTime dateTime = bbs.getWdate().toLocalDateTime();
			 * bbs.setFormattedDate(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			 */
			Timestamp dateTime = bbs.getWdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			bbs.setFormattedDate(sdf.format(dateTime));
			//�������� ��ȸ
			List<FileDto> files = fileService.getFilesByBbsId(bbs.getId());
		    List<String> fileExts = new ArrayList<>();
		    List<String> filesName = new ArrayList<>();
		    
		    for(FileDto file: files) {
		    	fileExts.add(file.getExt());
		    	filesName.add(file.getNewfilename());
		    }
		    bbs.setFileExt(fileExts);
		    bbs.setNewfilename(filesName);
		    bbs.setNum(num);
		    
		    String contents = contentsControll.extractParagraphs(bbs.getContent());
		    bbs.setContent(contentsControll.cutParagraph(contents, 30));
		    num--;
		}
		
		model.addAttribute("paging", paging);	
		model.addAttribute("bbslist", bbslist);
		
		if(skin.equals("gallery")) {
		   return "gallery.list";
		}else if(skin.equals("article")) {
		   return "article.list";
		}else if(skin.equals("blog")) {
		   return "blog.list";	 
		}else {
			return "bbs.list";
		}
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
        
        
        model.addAttribute("categories", categories);
		model.addAttribute("adminBbs", bbsAdminDto);
        
		return "bbs.write";
	}
	
	@GetMapping("/view")
	public String views(
	@RequestParam("bbsid") int bbsid,
	@RequestParam("id") long id,
	@RequestParam(value="page", defaultValue="1") int page,
	Model model
			
	) {
		
		Bbs bbsView = bbsService.getBbs(id);
		model.addAttribute("bbs", bbsView);
		return "bbs.view";
	}
	
	
	@GetMapping("/pass")
	public String passForm() {
		
		return "bbs.pass";
		
		
	}
	
	
	if(!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuth)
			member = memberService.getAuthenticatedMember();
			model.addAttribute("member", member);
	}
	Bbs bbsView = bbservice.getBbs(id);
	int sec = bbsView.getSec();
	
	if(sec == 1 && member == null ||
	   sec == 1 && 
	   
	   model.addAttribute("adminBbs", bbsAdminDto);
	   model.addAttribute("bbsid", bbsid);
	   model.addAttribute("page	, page");
	   model.addAttribute("bbs", bbsView);
	   
	   
	   
	@GetMapping("/update"){
	public String update() {
		@RequestParam("bbsid")
		return "bbs.update";
		
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
        @RequestParam(name = "sec", required= false ) byte sec,
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
			
			if(userid.equals("admin")) {
			     return "redirect:/admin/write";
			}else {
				return "redirect:/bbs/list?bbsid="+bbsid;
			}
		}catch(Exception e) {
		    model.addAttribute("error", "�� �ۼ��� ������ �߻��߽��ϴ�." + e.getMessage());
			if(userid.equals("admin")) {
			     return "redirect:/admin/write";
			}else {
				return "redirect:/bbs/list?bbsid="+bbsid;
			}
		}
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
        	result.put("fileUrl", "/comunity/res/upload/"+path+"/"+fileDto.getNewfilename());
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
			Path imagePath = Paths.get("/comunity/res/upload/"+bbsId+"/"+fname);
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
}
