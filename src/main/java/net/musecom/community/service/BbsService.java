package net.musecom.community.service;

import java.util.List;
import java.util.Map;

import net.musecom.community.model.Bbs;

public interface BbsService {

	void getBbsInsert(Bbs bbs, List<Long> fileIds); 
	
	int getBbsCount(int bbsid);
	int getSearchBbsCount(int bbsid, String key, String val);
	List<Bbs> getBbsList(int bbsid, int page, int recordsPerPage);
	List<Bbs> getSerchBbsList(int bbsid, int page, int recordsPerPage, String key, String val);
	
	Map<Integer, List<Bbs>> searchBbsPostsGrouped(String searchVal);
	void insertSearchKeyword(String keyword);
	List<Map<String , Object>> getPopularKeyword();
	
	void updateCount(long id);
	Bbs getBbs(long id);
	
	//전체 게시글 보기
	List<Map<String, Object>> selectLatestPostsMain();
	
	int getBbsPassword(long id, String password);
	
	//게시물 삭제
	void setDeleteById(long id);
}
