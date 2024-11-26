package net.musecom.community.service;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BbsFileCleanupService {

	@Autowired
	private FileService fileService;

	@Autowired
	private ServletContext sc;
	
	public void cleanFiles(int bbsid) {
		List<String> fileNames = fileService.selectFileWithBbsIdZero();
		if(fileNames != null && !fileNames.isEmpty()) {
			String delFilePath = sc.getRealPath("/res/upload/") + bbsid + "/";
			for(String fileName : fileNames) {
				File file = new File(delFilePath + fileName);
				if(file.exists() && file.delete()) {
					System.out.println(fileName + "을 삭제했습니다.");
				}
			}
			fileService.deleteFileWithBbsIdZero();
		}
	}
}
