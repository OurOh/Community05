package net.musecom.community.dao;

import java.util.List;

import net.musecom.community.model.Member;
import net.musecom.community.model.MemberRole;

public interface MemberDao {
  List<Member> getAllmem();
  Member getMem(int num);
  void insertMem(Member dto);
  void insertMemRole(MemberRole rdto);
  void updateMem(Member dto);
  void delMem(int num);
}
