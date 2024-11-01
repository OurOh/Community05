package net.musecom.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.musecom.community.model.FileDto;

@Mapper
public interface FileMapper {
  void insertFile(FileDto file);
  void updateFileByBbsId(FileDto file);
  int deleteFile(Long id);
  List<FileDto> selectFileByBbsId(long bbsid);
}
