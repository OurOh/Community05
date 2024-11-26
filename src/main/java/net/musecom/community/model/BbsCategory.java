package net.musecom.community.model;

import lombok.Data;

@Data
public class BbsCategory {
  private int id;  
  private long bbsid;  
  private String categorytext;  
  private int categorynum;  
}
