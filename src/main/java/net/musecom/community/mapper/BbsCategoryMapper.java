package net.musecom.community.mapper;

import org.apache.ibatis.annotations.Mapper;

import net.musecom.community.model.BbsCategory;

@Mapper
public interface BbsCategoryMapper {
   int insertCategory(BbsCategory category);
   int deleteCategory(int id);
   int updateCategory(BbsCategory category);
   
}
