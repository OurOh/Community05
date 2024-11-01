package net.musecom.community.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Bbs {
  private Long id;
  private int bbsid;
  private int ref;
  private int step;
  private int depth;
  private String title;
  private String writer;
  private String password;
  private String userid;
  private String content;
  private int comment;
  private Timestamp wdate;
  private byte sec;
  private String category;
}
