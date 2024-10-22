package net.musecom.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.musecom.community.mapper.MemberMapper;
import net.musecom.community.model.CustomUserDetails;
import net.musecom.community.model.Member;
import net.musecom.community.model.MemberRole;

public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private MemberMapper memberMapper;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Member member = memberMapper.getMemberUserId(username);
		if(member == null) {
			throw new UsernameNotFoundException("회원정보가 없습니다.");
		}
		List<MemberRole> roles = memberMapper.getMemberRole(member.getId());
	
		return new CustomUserDetails(member, roles);
	}

}
