package net.musecom.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.musecom.community.mapper.BbsMapper;
import net.musecom.community.model.Bbs;

public class BbsServiceImpl implements BbsService{
	
	@Autowired
	private BbsMapper bbsMapper;

	@Override
	public int getBbsInsert(Bbs bbs) {
		return bbsMapper.insertBbs(bbs);
	}

	@Override
	
	
	
	@Override
	public List<Bbs> getbbsList(int page, int recordPerPage) {
		
		return bbsMapper.selectBbs(bbsid, page, recordsPerPage);
	}

	@Override
	public List<Bbs> getSerchbbsList(int page, int recordPerPage, String key, String val) {
		
		return bbsMapper.selectBbs(bbsid, page, recordsPerPage, key, val);
	}
	
	
	
}
