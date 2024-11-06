package net.musecom.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import net.musecom.community.model.Bbs;

@Mapper
public interface BbsMapper {
  void insertBbs(Bbs bbs);
  void refUpdateById(@Param("ref") long ref, @Param("id") long id);
  int reInsertBbs(Bbs bbs);
  int updateBbs(Bbs bbs);
  int deleteBbs(long id);
  
  int selectCountBbs(int bbsid);
  int selectSearchCountBbs(@Param("bbsid") int bbsid, 
		  				   @Param("key") String key,
		  				   @Param("val") String val);
  
  List<Bbs> selectBbs(int bbsid, int page, int recordsPerPage);
  
  
  List<Bbs> selectSearchBbs(int bbsid, int page, int recordsPerPage, String key, String val);
  

  Bbs viewBbs(int id);
}
