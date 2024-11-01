package net.musecom.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.musecom.community.model.Bbs;

@Mapper
public interface BbsMapper {
  int insertBbs(Bbs bbs);
  int reInsertBbs(Bbs bbs);
  int updateBbs(Bbs bbs);
  int deleteBbs(int id);
  int selectCountBbs(int bbsid);
  List<Bbs> selectBbs(int bbsid, int page, int recordsPerPage);
  List<Bbs> selectSearchBbs(int bbsid, int page, int recordsPerPage, String key, String val);
  
  Bbs viewBbs(int id);
}
