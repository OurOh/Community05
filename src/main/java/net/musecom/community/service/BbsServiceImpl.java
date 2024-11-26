package net.musecom.community.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@Override
	public List<Map<String, Object>> selectLatestPostsMain() {
		
		return bbsMapper.selectMainLatestPosts();
	}

	@Override
	public void updateCount(long id) {
		bbsMapper.updateHit(id);
	}

	@Override
	public Bbs getBbs(long id) {
		return bbsMapper.viewBbs(id);
	}

	@Override
	public int getBbsPassword(long id, String password) {
		return bbsMapper.bbsByIdAndPassword(id, password);
	}

	@Override
	public void setDeleteById(long id) {
		bbsMapper.deleteBbs(id);		
	}

	@Override
	public Map<Integer, List<Bbs>> searchBbsPostsGrouped(String searchVal) {
		List<Bbs> searchResults = bbsMapper.searchBbsPostsGrouped(searchVal);
		//Stream 을 이용해 searchResults의 결과값 리스트를 bbsid (그룹) 별로 그룹화
		return searchResults.stream().collect(Collectors.groupingBy(Bbs::getBbsid));
	}

	@Override
	public void insertSearchKeyword(String keyword) {
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", keyword);
		bbsMapper.insertSearchKeyword(params);		
	}

	@Override
	public List<Map<String, Object>> getPopularKeyword() {
		// TODO Auto-generated method stub
		return bbsMapper.selectPopularKeywords();
	}


}
