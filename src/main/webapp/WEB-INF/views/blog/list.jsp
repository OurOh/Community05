<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>   
<c:if test="${adminBbs.rgrade > member.grade }">
  <script>
   alert("권한이 없습니다.");
   location.href="/comunity";
  </script>  
</c:if>
<div class="p-5 my-4">
   <h1 class="text-center">${adminBbs.bbstitle }</h1>
   <table class="bbslist table-hover">
      <colgroup>
        <col width="10%">
        <col width="50%">
        <col width="15%">
        <col width="15%">
        <col width="10%">
      </colgroup>
      <thead>
       <tr>
         <th>번호</th>
         <th>제목</th>
         <th>작성자</th>
         <th>날짜</th>
         <th>조회수</th> 
       </tr>
      </thead>
      <tbody>
        <c:forEach var="post" items="${bbslist }">
        <!-- 루프 -->
        <tr>
           <td class="text-center">${post.id }</td>
           <td class="ellipsis">
              <a href="view?id=${post.id}&bbsid=${adminBbs.id }&pg=${paging.currentPage}">${post.title }</a> 
                <c:forEach var="ext" items="${post.fileExt }">
                  <span> ${ext } </span>
                </c:forEach>
              </td>
           <td class="ellipsis text-center">${post.writer }</td>
           <td class="text-center">${post.formattedDate }</td>
           <td class="text-center">${post.hit }</td>
        </tr>
        <!-- /루프 -->
        </c:forEach>
      </tbody>
   </table>

   <div class="search-and-button row my-3">
	   <div class="col-md-5 searchbox">
           <form id="searchForm" name="searchForm" method="get">
			<div class="input-group mt-3 mb-3">
			  <div class="input-group-prepend">
			    <button type="button" class="btn btn-outline-secondary dropdown-toggle" 
			    data-toggle="dropdown" id="searchtext">제목검색
			    </button>
			    
			    <div class="dropdown-menu">
			      <a class="dropdown-item" href="#" data-search="title"> 제목검색 </a>
			      <a class="dropdown-item" href="#" data-search="writer"> 이름검색 </a>
			      <a class="dropdown-item" href="#" data-search="contents"> 내용검색 </a>
			    </div>
			  </div>
			  <input type="text" class="form-control" name="searchVal" id="searchInput" placeholder="검색어 입력">
			  <input type="hidden" id="searchKey" name="searchKey" value="title">
			  <input type="hidden" name="bbsid" value="${adminBbs.id }" />
			  <div class="input-group-append">
                 <button class="btn btn-success" id="searchbutton" type="button">검색</button>
              </div>
			</div>
          </form>
       </div>
	   <div class="col-md-7 text-right btn-box">
	     <a href="bbs.jsp" class="btn btn-success">목록</a>  
	     <a href="write?bbsid=${adminBbs.id }" class="btn btn-success">글쓰기</a>
	   </div>  
   </div>
   
   
   	<ul class="pagination justify-content-center">
     	<li class="page-item"><a class="page-link bg-light" href="?bbsid=${adminBbs.id}&page=1&searchKey=${encodeSearchKey}&searchVal=${encodeSearchVal}"><i class="ri-arrow-left-double-line"></i></a></li>
	<!-- 이전그룹 -->
	<c:if test="${paging.startPageOfGroup > 1 }">
	  <li class="page-item"><a class="page-link" href="?bbsid=${adminBbs.id}&page=${paging.startPageOfGroup - 1 }&searchKey=${encodeSearchKey}&searchVal=${encodeSearchVal}"><i class="ri-arrow-left-s-line"></i></a></li>
	</c:if>  
	
	<c:forEach var="i" begin="${paging.startPageOfGroup }" end="${paging.endPageOfGroup}">
	   <li class='page-item <c:if test="${ i == paging.currentPage}">active</c:if>'><a class="page-link" href="?bbsid=${adminBbs.id}&page=${i}&searchKey=${encodeSearchKey}&searchVal=${encodeSearchVal}">${i }</a></li>
	</c:forEach>  
	  
	  
	 <!-- 다음그룹 -->
	<c:if test="${paging.endPageOfGroup < paging.totalPages }">  
	  <li class="page-item"><a class="page-link" href="?bbsid=${adminBbs.id}&page=${paging.endPageOfGroup + 1}&searchKey=${encodeSearchKey}&searchVal=${encodeSearchVal}"><i class="ri-arrow-right-s-line"></i></a></li>
	</c:if>  
	<li class="page-item"><a class="page-link bg-light" href="?bbsid=${adminBbs.id}&page=${paging.totalPages }&searchKey=${encodeSearchKey}&searchVal=${encodeSearchVal}"><i class="ri-arrow-right-double-line"></i></a></li>
	</ul>   
</div>

	<script>
	$(function(){
		$(".dropdown-item").click(function(e){
			e.preventDefault();
			const selectedText = $(this).text();
			const selectedKey = $(this).data("search");
			$("#searchKey").val(selectedKey);
			$("#searchtext").text(selectedText);
		});
		
		$("#searchbutton").click(function(){
			const keyword = $("#searchInput").val().trim();
			if(keyword === ""){
				alert("검색어를 입력하세요.");
				$("#searchInput").focus();
				return false;
			}
			$("#searchForm").submit();
		});
	});
	</script>