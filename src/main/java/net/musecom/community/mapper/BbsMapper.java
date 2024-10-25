package net.musecom.community.mapper;

import java.util.List;

import net.musecom.community.model.Bbs;

public class BbsMapper {
	int insertBbs(Bbs bbs);
	int reInsertBbs(Bbs bbs);
	int updateBbs(Bbs bbs);
	int deleteBbe(int id);
	int selectBbs();
	List<Bbs> selectBbs(int page);
	List<Bbs> searchBbs(String key, String val);
	
	
	Bbs viewBbs(int id);
}
