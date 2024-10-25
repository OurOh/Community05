package net.musecom.community.service;

import java.util.List;

import net.musecom.community.model.Bbs;

public interface BbsService {
	
	int getBbsInsert(Bbs bbs);
	List<Bbs> getbbsList(int page, int recordPerPage);
	List<Bbs> getSerchbbsList(int page, int recordPerPage, String key, String val);
	
}
