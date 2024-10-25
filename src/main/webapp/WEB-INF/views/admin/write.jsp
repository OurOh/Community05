<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>   
<link rel="stylesheet" href="/community/res/css/summernote-bs5.css" />
<script src="/community/res/js/lang/summernote-ko-KR.js"></script>
<script src="/community/res/js/summernote-bs5.min.js"></script> 
<script>
  $(function(){
	 $("#content").summernote({
		 lange: 'ko-KR',
		 height: 350
	 }); 
  });
</script>
<div class="p-5 m-5">
<form class="row">
    <label class="col-2 text-right py-3 my-3">
       카테고리
    </label>
    <div class="col-10  py-2 my-2">
       <select name="category" class="form-control">
          <c:forEach items="${categories }" var="category">
             <option value="${category.categorytext }">${category.categorytext }</option>
          </c:forEach>
       </select>
    </div>
    <label class="col-2 text-right  py-2 my-2">
       제목
    </label>
    <div class="col-10  py-2 my-2">
       <input type="text" class="form-control" name="title" id="title" />
    </div>   
    <div class="col-12  py-2 my-2">
       <textarea name="content" id="content"></textarea>
    </div>
    <div class="col-12 text-center  py-2 my-2">
    	<input type="hidden" name = "bbsid" value="1"/>
    	<input type="hidden" name = "writer" value="운영자"/>
    	<input type="hidden" name = "password" value="admin"/>
    	<input type="hidden" name = "userid" value="0"/>
       <button type="reset" class="btn btn-danger me-3"> 취 소 </button>
       <button type="submit" class="btn btn-primary ms-3"> 전 송 </button>
    </div>
</form>
</div>




