package net.musecom.kdtcom.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.musecom.community.dao.MemberDao;
import net.musecom.kdtcom.model.MemberDto;

@Controller
public class MainController {
  
  @Autowired	
  private FileUploadService fileUpload;	

  @Autowired
  private ClientIpAddress clientIpAddress;
  
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;
  
  @Autowired
  private MemberDao dao;
  
  @GetMapping("/")
  public String main() {
	  return "kdtcom.index";
  }
  
  @GetMapping("/register")
  public String register() {
	  return "kdtcom.register";
  }
  
  @PostMapping("/register")
  public String fomrRegister(
		  @RequestParam("userid") String userid,
		  @RequestParam("userpass") String noopUserpass,
		  @RequestParam("username") String username,
		  @RequestParam("useremail") String useremail,
		  @RequestParam("usertel") String usertel,
		  @RequestParam("zipcode") int zipcode,
		  @RequestParam("address") String address,
		  @RequestParam("detail_address") String detail_address,
		  @RequestParam("extra_address") String extra_address,
		  @RequestParam("userprofile") String userprofile,
		  @RequestParam(value="userimg", required=false) MultipartFile userimg, 
		  HttpServletRequest request,
		  Model model) {

	      MemberDto dto = new MemberDto();
	       //아이피 주소 얻기
	      clientIpAddress.setClientIpAddress(request);
	      String userip = clientIpAddress.getClientIpAddress();
	      
	      //비밀번호 암호화
          String userpass = passwordEncoder.encode(noopUserpass);
          
          dto.setUserid(userid);
          dto.setUserpass(userpass);
          dto.setUsername(username);
          dto.setUseremail(useremail);
          dto.setUsertel(usertel);
          dto.setZipcode(zipcode);
          dto.setAddress(address);
          dto.setDetail_address(detail_address);
          dto.setExtra_address(extra_address);
          dto.setUserprofile(userprofile);
          dto.setUserip(userip);
          
	try {
		//업로드 처리
		if(userimg != null && !userimg.isEmpty()) {
		  fileUpload.setAbsolutePath("members");
		  String[] exts = {"jpg", "gif", "png"};
		  fileUpload.setAllowedExt(exts);
		  String imgname = fileUpload.uploadFile(userimg);
		  dto.setUserimg(imgname);		  
		}
	}catch(Exception e) {
		model.addAttribute("error", "file upload failed: " + e.getMessage());
	}
	      dao.insertMem(dto);
	      
    return "kdtcom.index";
  }
}
