<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>   
<div class="p-5 my-4 bg-white">
   <div class="pass-box">
      <p class="text-center text-danger">
         비밀번호를 입력하세요.
      </p>
      <form class="bbspass" method="post">
          <div class="alert alert-danger alert-dismissible fade hide">
              <button type="button" class="close"><i class="ri-close-line"></i></button>
              <strong>비밀번호가 틀렸습니다.</strong>
          </div>
          <div class="password">
             <input type="password" name="password" id="bbspassword"
                    class="form-control" placeholder="비밀번호" />
          </div>
          <div class="btn-box">
              <button type="reset" class="btn btn-danger"> 취 소 </button>
              <button type="button" id="submit" class="btn btn-primary"> 전송 </button>
          </div>
          <input type="hidden" name="id" id="id" value="${id }">
          <input type="hidden" name="bbsid" id="bbsid" value="${bbsid }" />
          <input type="hidden" name="page" id="page" value="${page }" />
          <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" /> 
      </form>
   </div>
</div>
<script>
 $(function(){
	 $("#submit").click(function(){
		 const params = $("#bbspass").serialize();
		 $.ajax({
			 url: "/community/bbs/passwd"
			 type: "post",
			 data: params,
			 success: function(data){
				 if(data ==0){
					 alert("비밀번호가 틀렸습니다. 다시 확인하세요.")
				 }else{
					 document.location.href="./"
				 }
			 }
		 })
	 });	 
 })


</script>









