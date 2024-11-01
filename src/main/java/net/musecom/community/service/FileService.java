package net.musecom.community.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.community.mapper.FileMapper;
import net.musecom.community.model.FileDto;

@Service
public class FileService {

	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	public long uploadFile(
			MultipartFile file, 
			String path, 
			String[] exts,
			long maxSize) throws IOException {
	   fileUploadService.setAbsolutePath(path);
	   fileUploadService.setAllowedExt(exts);
	   fileUploadService.setMaxSize(maxSize);
	   FileDto fileDto = fileUploadService.uploadFile(file);
	   
	   //db ¿˙¿Â
	   fileMapper.insertFile(fileDto);
	   
	   return fileDto.getId();
	}
	
//	public void updateFileWithBbsId(fileId, bbsid) {
//		
//	}
	
}
