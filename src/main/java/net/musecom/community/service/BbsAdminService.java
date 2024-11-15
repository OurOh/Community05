package net.musecom.community.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.musecom.community.mapper.BbsAdminMapper;
import net.musecom.community.mapper.BbsCategoryMapper;
import net.musecom.community.model.BbsAdmin;
import net.musecom.community.model.BbsCategory;

@Service
public class BbsAdminService {

   @Autowired
   private BbsAdminMapper bbsAdminMapper;
   
   @Autowired
   private BbsCategoryMapper bbsCategoryMapper;
   
   public int editBbsAdmin(BbsAdmin bbsAdmin) {
      return bbsAdminMapper.updateBbsAdmin(bbsAdmin);
   }
   
    public BbsAdmin getSelectById(int id) {
       return bbsAdminMapper.selectById(id);
    }
   
   public List<BbsAdmin> getAllBbsList(){
      return bbsAdminMapper.selectList();
   }
   
   public List<BbsCategory> getBbsCategoryById(int id) {
      return bbsAdminMapper.selectCategoryByBbsId(id);
   }
   
   public int bbsCategoryDelete(int categoryId) {
      return bbsCategoryMapper.deleteCategory(categoryId);
   }
   
   public int bbsCategoryUpdate(BbsCategory category) {
       return bbsCategoryMapper.updateCategory(category);   
   }
   
   public int bbsCategoryInsert(BbsCategory category) {
       return bbsCategoryMapper.insertCategory(category);   
   }
   
   public BbsAdmin getBbsAdminData(int id) {

      BbsAdmin bbsAdmin = getSelectById(id);
      
      BbsAdmin dto = new BbsAdmin();

      dto.setBbstitle(bbsAdmin.getBbstitle());
      dto.setSkin(bbsAdmin.getSkin());
      dto.setCategory(bbsAdmin.getCategory());   
      dto.setListcount(bbsAdmin.getListcount());   
      dto.setPagecount(bbsAdmin.getPagecount());
      dto.setLgrade(bbsAdmin.getLgrade());
      dto.setRgrade(bbsAdmin.getRgrade());   
      dto.setFgrade(bbsAdmin.getFgrade());   
      dto.setRegrade(bbsAdmin.getRegrade());   
      dto.setComgrade(bbsAdmin.getComgrade());   
      dto.setFilesize(bbsAdmin.getFilesize());   
      dto.setAllfilesize(bbsAdmin.getAllfilesize());   
      dto.setThimgsize(bbsAdmin.getThimgsize());   
      dto.setFilechar(bbsAdmin.getFilechar());  
      dto.setId(id);

        return dto;
   }
}
