<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>   
<link rel="stylesheet" href="/community/res/css/summernote-bs5.css" />

<script src="/community/res/js/summernote-bs5.min.js"></script>
<script src="/community/res/js/lang/summernote-ko-KR.js"></script> 
<c:if test="${adminBbs.rgrade > member.grade }">
  <script>
   alert("권한이 없습니다.");
   location.href="/community";
  </script>  
</c:if>
<script>
   $(function(){
	 $("#content").summernote({
		 lange: 'ko-KR',
		 height: 350
	 });
  });
</script>
<div class="p-5 m-5">
<c:choose>
  <c:when test="${adminBbs.fgrade > 0}">
     <form class="row" action="/community/bbs/writefile" method="post"  enctype="multipart/form-data">
  </c:when>
  <c:otherwise>
     <form class="row" action="/community/bbs/write" method="post">
  </c:otherwise> 
</c:choose>  
  <c:if test="${adminBbs.category > 0}">
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
   </c:if>
    
    <label class="col-2 text-right  py-2 my-2">
       제목
    </label>
    <div class="col-10  py-2 my-2">
       <input type="text" class="form-control" name="title" id="title" />
    </div>   
    <div class="col-12  py-2 my-2">
       <textarea name="content" id="content"></textarea>
    </div>
    
    <c:if test="${adminBbs.fgrade > 0}">
    <label class="col-2 text-right py-2 my-2">
        파일업로드
    </label>
    <div class="col-10  py-2 my-2">
       <input type="file" class="form-control" name="file" id="file" />
    </div>   
    </c:if>
    
    <div class="col-12 text-center  py-2 my-2">
       <input type="hidden" name="bbsid" value="1" />
       <input type="hidden" name="writer" value="운영자" />
       <input type="hidden" name="password" value="admin" />
       <input type="hidden" name="userid" value="admin" />
       <input type="hidden" name="sec" value="0" />
       <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
       <button type="reset" class="btn btn-danger me-3"> 취 소 </button>
       <button type="submit" class="btn btn-primary ms-3"> 전 송 </button>
    </div>
    
</form>
</div>












