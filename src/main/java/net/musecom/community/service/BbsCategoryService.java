package net.musecom.community.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.musecom.community.model.BbsAdmin;
import net.musecom.community.model.BbsCategory;

@Service
public class BbsCategoryService {

	@Autowired
	private BbsAdminService adminService;
	
	public List<BbsCategory> getCategories(int bbsid){
		BbsAdmin bbsAdminDto = adminService.getBbsAdminData(bbsid);
		return (bbsAdminDto.getCategory() > 0 ) 
				? adminService.getBbsCategoryById(bbsid)
			    : new ArrayList<>();
	}
}
