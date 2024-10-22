package net.musecom.community.service;

public interface FileUpload {
	
	//확장자 읽기
	String[] getAllowedExt();
		   
	//확장자 셋팅
	void setAllowedExt(String[] ext);
		   
	//파일크기셋팅
	void setMaxSize(long maxSize);
		   
	//파일크기 가져오기
	long getMaxSize();
		   
	//경로셋팅
	void setAbsolutePath(String path);
		   
	//경로가져오기
	String getAbsolutePath();
		   
	//확장자가져오기
	String getFileExt(String filename);
	
}