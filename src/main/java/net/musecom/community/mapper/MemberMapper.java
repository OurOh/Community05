package net.musecom.community.mapper;

import java.util.List;

import net.musecom.community.model.Member;
import net.musecom.community.model.MemberRole;

	
public interface MemberMapper {
   Member getMemberUserId(String userid); // ���̵� ��������
   List<MemberRole> getMemberRole(int membersid);
   
   void setInsertMember(Member member);
   void setInsertRolr(MemberRole memberRole);

}
