package net.musecom.community.model;

import java.util.List;

import lombok.Data;

@Data
public class BbsAdmin {
  private int id;
  private String bbstitle;
  private String skin;
  private byte category;
  private byte listcount;
  private byte pagecount;
  private byte lgrade;
  private byte rgrade;
  private byte fgrade;
  private byte regrade;
  private byte comgrade;
  private int filesize;
  private int allfilesize;
  private String thimgsize;
  private String imgsize;
  private List<BbsCategory> bbsCategory;
}


