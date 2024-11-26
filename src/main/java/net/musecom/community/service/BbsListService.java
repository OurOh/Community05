package net.musecom.community.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.musecom.community.model.Bbs;
import net.musecom.community.model.FileDto;

@Service
public class BbsListService {

	@Autowired
	private BbsService bbsService;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private ContentsService contentsService;  //html 태그 정리를 위한 클래스
	
    public List<Bbs> getBbsList(int bbsid, int pg, int listCount, String searchKey, String searchVal){
    	
    	return (searchKey != null && !searchKey.isEmpty() && searchVal !=null && !searchVal.isEmpty())?
    			bbsService.getSerchBbsList(bbsid, pg, listCount, searchKey, searchVal) :
    			bbsService.getBbsList(bbsid, pg, listCount);	
    }
    
    public void processBbsList(List<Bbs> bbslist, long totalRecords, int pg, int cut) {
    	long num = totalRecords - pg;
    	
    	for(Bbs bbs: bbslist) {
    		Timestamp dateTime = bbs.getWdate();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		bbs.setFormattedDate(sdf.format(dateTime));
    		
    		//파일조회
    		List<FileDto> files = fileService.getFilesByBbsId(bbs.getId());
    		List<String> fileExts = new ArrayList<>();
    		List<String> filesName = new ArrayList<>();
    		
    		for(FileDto file : files) {
    			fileExts.add(file.getExt());
    			filesName.add(file.getNewfilename());
    		}
    		bbs.setFileExt(fileExts);
    		bbs.setNewfilename(filesName);
    		bbs.setNum(num);
    		num--;
    		String content = contentsService.extractParagraphs(bbs.getContent());
    		bbs.setContent(contentsService.cutParagraph(content, cut));
    	}
    }
	
}
