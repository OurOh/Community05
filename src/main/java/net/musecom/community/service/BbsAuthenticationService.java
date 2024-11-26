package net.musecom.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import net.musecom.community.model.BbsAdmin;
import net.musecom.community.model.Member;

@Service
public class BbsAuthenticationService {
  
	@Autowired
	private BbsAdminService adminService;
	
	@Autowired
	private MemberService memberService;
	
	public boolean chechAuthorization(int bbsid, Model model) {
		
		BbsAdmin bbsAdminDto = adminService.getBbsAdminData(bbsid);
		//회원정보에서 읽기 권한이 있는지 확인
		if(bbsAdminDto.getLgrade() > 0) {
			Authentication authentication = 
					          SecurityContextHolder.getContext().getAuthentication();
			if(authentication instanceof AnonymousAuthenticationToken) {
				//회원제 게시판, 로그인 안되었을 때 처리
				model.addAttribute("error", "회원제입니다. 로그인 하세요.");
				return false;
			}else {
				//로그인 되었을 때는 권한 확인
				Member member = memberService.getAuthenticatedMember();
				if(member.getGrade() < bbsAdminDto.getLgrade()) {
					model.addAttribute("error", "권한이 없습니다.");
					model.addAttribute("member", member);
					return false;
				}
			}
		}
		
		//익명 사용자 접근 가능한 경우 처리해 주기
		if(!(SecurityContextHolder.getContext().getAuthentication() instanceof  AnonymousAuthenticationToken)) {
			Member member = memberService.getAuthenticatedMember();
			model.addAttribute("member", member);
		}
		
		model.addAttribute("adminBbs", bbsAdminDto);
		return true;
	}
	
}
