package net.musecom.community.model;

import java.util.List;

//import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BbsExtends {
  //private LocalDateTime createDate;
  private String formattedDate;
  private List<String> fileExt;
  private List<String> newfilename;
}