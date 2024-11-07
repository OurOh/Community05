package net.musecom.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.musecom.community.mapper.BbsMapper;
import net.musecom.community.mapper.FileMapper;
import net.musecom.community.model.Bbs;

@Service
public class BbsServiceImpl implements BbsService {
	
	@Autowired
	private BbsMapper bbsMapper;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Override
	public void getBbsInsert(Bbs bbs, List<Long> fileIds) {
		bbsMapper.insertBbs(bbs);
		long bId = bbs.getId();
		bbsMapper.refUpdateById(bId, bId);
		System.out.println("게시물 아이디: " + bId);
		
		if(fileIds != null && !fileIds.isEmpty()) {
			for (Long fileId : fileIds) {
				System.out.println(fileId);
				fileMapper.updateFileByBbsId(bId, fileId);
			}
		}			
	}

	@Override
	public int getBbsCount(int bbsid) {
		
		return bbsMapper.selectCountBbs(bbsid);
	}


	@Override
	public List<Bbs> getBbsList(int bbsid, int page, int recordsPerPage) {
		return bbsMapper.selectBbs(bbsid, page, recordsPerPage);
	}

	@Override
	public List<Bbs> getSerchBbsList(int bbsid, int page, int recordsPerPage, String key, String val) {	
		return bbsMapper.selectSearchBbs(bbsid, page, recordsPerPage, key, val);
	}
	

	@Override
	public int getSearchBbsCount(int bbsid, String key, String val) {
		
		return bbsMapper.selectSearchCountBbs(bbsid, key, val);
	}


}
