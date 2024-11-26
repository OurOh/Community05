package net.musecom.community.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import net.musecom.community.model.Bbs;

@Mapper
public interface BbsMapper {
  void insertBbs(Bbs bbs);
  void refUpdateById(@Param("ref") long ref, @Param("id") long id);
  int reInsertBbs(Bbs bbs);
  void updateBbs(Bbs bbs);
  int deleteBbs(long id);
  
  
  int selectCountBbs(@Param("bbsid") int bbsid);
  
  int selectSearchCountBbs(@Param("bbsid") int bbsid, 
		                   @Param("key") String key, 
		                   @Param("val") String val);
  
  List<Bbs> selectBbs(@Param("bbsid") int bbsid, 
		              @Param("page") int page, 
		              @Param("recordsPerPage") int recordsPerPage);
  
  List<Bbs> selectSearchBbs(
		  @Param("bbsid") int bbsid, 
		  @Param("page") int page, 
		  @Param("recordsPerPage") int recordsPerPage, 
		  @Param("key") String key, 
		  @Param("val") String val);

  //게시물 비번 확인
  int bbsByIdAndPassword(@Param("id") long id, @Param("password") String password);
  
  //게시물 조회수 등록 및 보기
  void updateHit(long id);
  Bbs viewBbs(long id);
  
  List<Map<String, Object>> selectMainLatestPosts();
  
  //검색
  List<Bbs> searchBbsPostsGrouped(String searchVal);
  void insertSearchKeyword(Map<String, Object> params);
  List<Map<String, Object>> selectPopularKeywords();
}
