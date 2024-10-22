package net.musecom.community.service;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService implements FileUpload {
	
	//확장자목록, 파일제한크기 필드 설정
	private String[] allowedExt;
	private long maxSize;
	
	//저장경로 필드
	private String path;
	private String orPath;
	
	//써블릿 콘텍스트 빈에서 꺼네기
	@Autowired
	private ServletContext sc;

	@Override
	public String[] getAllowedExt() {
		return allowedExt;
	}

	@Override
	public void setAllowedExt(String[] allowedExt) {
        this.allowedExt = allowedExt;
	}

	@Override
	public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
	}

	@Override
	public long getMaxSize() {
		return maxSize;
	}

	@Override
	public void setAbsolutePath(String path) {
		this.path =sc.getRealPath("/res/upload/") + path + "/";
		this.orPath = path;
	}

	@Override
	public String getAbsolutePath() {
		return path;
	}

	
	//파일 확장자 이름 가져오는 함수
	@Override
	public String getFileExt(String filename) {
		if(filename == null || filename.isEmpty()) {
			return "";
		}
		int dotIndex = filename.lastIndexOf(".");
		return (dotIndex != -1 && dotIndex < filename.length() - 1) ? filename.substring(dotIndex + 1) :"";
	}
	
	//파일업로드 처리
	public String[] uploadFile(MultipartFile file) throws IOException {
		
		String fileExt = getFileExt(file.getOriginalFilename());
		
		if(file == null || file.isEmpty()) {
			throw new IllegalArgumentException("파일이 없습니다.");
		}
		
		//파일크기 체크
		if(maxSize > 0 && file.getSize() > maxSize) {
			throw new IllegalArgumentException("파일 크기는 " + maxSize + "보다 작아야 합니다.");
		}
		
		if(allowedExt != null && allowedExt.length > 0) {
			boolean isFileOk = false;
			for(String ext : allowedExt) {
				if(fileExt.equals(ext)) {
					isFileOk = true;
					break;
				}
			}
			
			if(!isFileOk) {
				throw new IllegalArgumentException("허용되지 않는 확장자 입니다.");
			}
		}
		
		//저장경로 + 파일이름
		String orFilename = file.getOriginalFilename();
		String newFilename = System.currentTimeMillis() + "-"+ orPath +"." + fileExt;
		File dest = new File(path, newFilename);
		
		//파일저장
		file.transferTo(dest);
		String[] filesname = {orFilename, newFilename};
		
		return filesname;
	}

}
