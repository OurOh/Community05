package net.musecom.community.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class Member {
	  private int id;
	  private String userid;
	  private String userpass;
	  private String username;
	  private String useremail;
	  private String usertel;
	  private int zipcode;
	  private String address;
	  private String detailAddress;
	  private String extraAddress;
	  private String userimg;
	  private String oruserimg;
	  private String userprofile;
	  private Timestamp createAt;
	  private Timestamp editAt;
	  private String userip;
	  private int grade;
	  
}
