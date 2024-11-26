package net.musecom.community.service;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.musecom.community.model.FileDto;

@Service
public class FileDeleteService {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ServletContext sc;
	
	public boolean hasFilesToDelete(long id) {
		List<FileDto> fileLists = fileService.getFilesByBbsId(id);
		return fileLists != null && !fileLists.isEmpty();
	}
	
	public boolean deleteFile(long id, int bbsid) {
		try {
			List<FileDto> fileLists = fileService.getFilesByBbsId(id);
		    if(fileLists == null || fileLists.isEmpty()) {
		    	throw new RuntimeException("삭제할 파일이 없습니다.");
		    }
		
		    //파일경로
		    String path = sc.getRealPath("/res/upload/") + bbsid + "/";
		    
		    for(FileDto fileDto : fileLists) {
		    	String fullpath = path + fileDto.getNewfilename();
		    	
		    	//파일객체 생성
		    	File file = new File(fullpath);
		    	if(file.exists() && file.delete()) {
		    		fileService.deleteFile(fileDto.getId());
		    	}
		    }
		    
		    return true;
		    
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
