package net.musecom.community.service;

import java.util.List;

public class BbsFileCleanupService {

	
	
	
	public void cleanFiles(int bbsud) {
		List<String> fileNames = fileService.selectFileWithBbsIdZero();
		if(fileNames != null && !fileNames.isEmpty()) {
			String delFilePath = sc.getRealPath("/res/upload/") + bbsid + "/";
			for(String fileName : fileNames) {
				File file = new File(delFilePath + fileName);
				if(file.exists() && file.delete())
			}
		}
				
				
	}
}
