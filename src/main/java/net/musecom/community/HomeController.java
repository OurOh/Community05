package net.musecom.community;

import java.time.LocalDateTime;
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
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("����" + auth);
		if(auth != null && auth.getPrincipal() instanceof CustomUserDetails) {	
		     CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
		     userid = userDetails.getUsername();
		     Member member = userDetails.getMember();
             model.addAttribute("member", member);
		}
		
		List<BbsAdmin> bbsAdminLists = bbsAdminService.getAllBbsList();
		List<Map<String, Object>> latestPosts = bbsService.selectLatestPostsMain();
		
		LocalDateTime now = LocalDateTime.now();
		
		for(Map<String, Object> post : latestPosts) {
			//Timestamp wdate = (Timestamp) post.get("wdate");
			Object wdateObj = post.get("wdate");
			LocalDateTime dateTime = wdate.toLocalDateTime();
			//24시간 이내이면 시:분형식
			if(wdateObj instanceof LocalDateTime) {
				dateTime = (LocalDateTime) wdateObj;
			}else if(wdateObj instanceof Timestamp) {
				dateTime = ((Timestamp) wdateObj).toLocalDateTime();
			}else {
				continue; //
			}
			
			if(dateTime.isAfter(now.minusHours(24))) {
				SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
				post.put("latesttime", dateTime.format(timeFormatter));
			}else {
			//24시간 이후면 년울일 형힉
				post.put("latesttime", dateTime.format(dateFormatter));
				
			}
			
		}
		
		
		
		model.addAttribute("bbsAdminLists", bbsAdminLists );
		model.addAttribute("latestPosts", latestPosts);
		model.addAttribute("userid", userid );
		
		return "main.home";
	}
	
	
	
	
	
}
