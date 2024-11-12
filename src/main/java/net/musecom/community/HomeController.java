package net.musecom.community;

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
		model.addAttribute("bbsAdminLists", bbsAdminLists );
		model.addAttribute("latestPosts", latestPosts);
		model.addAttribute("userid", userid );
		
		return "main.home";
	}
	
}
