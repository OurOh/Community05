package net.musecom.community.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.musecom.community.model.CustomUserDetails;
import net.musecom.community.model.Member;

@Service
public class MemberService {

	 public Member getAuthenticatedMember() {
		//���������� �̿��� ����� ���� ��������
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		System.out.println("����" + auth);
		
		if(auth != null && auth.getPrincipal() instanceof CustomUserDetails) {	
           CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
           return userDetails.getMember();
        }
		return null;
	 }
}

