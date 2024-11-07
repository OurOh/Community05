package net.musecom.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.musecom.community.model.Member;
import net.musecom.community.model.MemberRole;

@Mapper
public interface MemberMapper {
   Member getMemberUserId(String userid);  
   List<MemberRole> getMemberRole(int membersid); 
   
   void setInsertMember(Member member);
   void setInsertRole(MemberRole memberRole);
}
