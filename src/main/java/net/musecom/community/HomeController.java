package net.musecom.community;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.musecom.community.model.CustomUserDetails;
import net.musecom.community.model.Member;


@Controller
public class HomeController {
	
	@Autowired
	BbsService 
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		String userid = null;
		
		//���������� �̿��� ����� ���� ��������
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
		
		model.addAttribute("latestPosts", latestPosts);
		model.addAttribute("userid", userid );
		
		return "home";
		
		
		
	}
	
}
