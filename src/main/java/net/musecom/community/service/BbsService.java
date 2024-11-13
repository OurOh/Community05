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
	
	//��ü �Խñ� ����
	List<Map<String, Object>> selectLatestPostsMain();
	
	//��ü �Խñ� ����
	@Override
	public List<Map<String, Object>> selectMainLatestMain(){
		
		return bbsMapper.
	}
}
