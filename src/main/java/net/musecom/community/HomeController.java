package net.musecom.community;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.musecom.community.model.BbsAdmin;
import net.musecom.community.model.CustomUserDetails;
import net.musecom.community.model.FileDto;
import net.musecom.community.model.Member;
import net.musecom.community.service.BbsAdminService;
import net.musecom.community.service.BbsService;
import net.musecom.community.service.FileService;


@Controller
public class HomeController {

	@Autowired
	BbsService bbsService;
	
	@Autowired
	BbsAdminService bbsAdminService;
	
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		String userid = null;
		
		//인증정보를 이용한 사용자 정보 가져오기
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("권한" + auth);
		if(auth != null && auth.getPrincipal() instanceof CustomUserDetails) {	
		     CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		     userid = userDetails.getUsername();
		     Member member = userDetails.getMember();
             model.addAttribute("member", member);
		}
		
		List<BbsAdmin> bbsAdminLists = bbsAdminService.getAllBbsList();
		List<Map<String, Object>> latestPosts = bbsService.selectLatestPostsMain();
		
		LocalDateTime now = LocalDateTime.now();
		
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		for(Map<String, Object> post : latestPosts) {
			Object wdateObj = post.get("wdate");
			LocalDateTime dateTime; 
			if(wdateObj instanceof LocalDateTime) {
				dateTime = (LocalDateTime) wdateObj;
			}else if(wdateObj instanceof Timestamp) {
				dateTime = ((Timestamp) wdateObj).toLocalDateTime();
			}else {
				continue; //타입이 않맞으면 다음으로 
			}
			
			//24시간 이내이면 시:분형식
			if(dateTime.isAfter(now.minusHours(24))){
			   post.put("latesttime", dateTime.format(timeFormatter));
			}else {			
			//24시간 이후면 년월일 형힉
				post.put("latesttime", dateTime.format(dateFormatter));
			}	
			
			//이미지 뽑기 (각 id의 첫번째 이미지만 뽑음)
			if("gallery".equals(post.get("skin"))) {
				long bbsid = (long) post.get("bbs_id");
				List<FileDto> files = fileService.getFilesByBbsId(bbsid);
				if(!files.isEmpty()) {
					post.put("fileFirst", files.get(0));
				}
			}
			
		}
		
		model.addAttribute("bbsAdminLists", bbsAdminLists );
		model.addAttribute("latestPosts", latestPosts);
		model.addAttribute("userid", userid );
		
		return "main.home";
	}
	
}
