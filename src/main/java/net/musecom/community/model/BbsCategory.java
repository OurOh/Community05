package net.musecom.community.model;

import lombok.Data;

@Data
public class BbsCategory {
  private int id;  //기본 값
  private int bbsid;  //bbs의 아이디 (외래키)
  private String categorytext;  //카테고리 내용
  private int categorynum;  //카테고리 순서
}
