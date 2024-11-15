<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>   

<div class="p-5 my-4 bg-white">
   <h1 class="text-center">${adminBbs.bbstitle }</h1>
   <div class="viewbox">
      <div class="border-bottom d-flex justify-content-between py-2 px-4 align-items-end">
          <h3>${bbs.title }</h3>
          <div class="writer">${bbs.writer }</div>
      </div>
      <div class="infobox d-flex justify-content-end py-3 border-bottom">
         <div class="wdate">${bbs.wdate }n</div>
         <div class="hit">조회수 : ${bbs.hit }</div>
      </div> 
      <c:if test="${view.fileExt != null && view.fileExt.isEmpty() }">
         <div class="bg-light p-3 text-right mb-3">
             <a href="#">파일다운로드</a>
         </div>
      </c:if>
      <div class="content-box px-4">
        ${bbs.content }
      </div>
      <div class="btn-box text-right pt-5">
           <a href="/community/bbs/list?bbsid=${bbsid }&page=${page}" class="btn btn-primary me-3"> 목 록 </a>
           <a href="/community/bbs/update?bbsid=${bbsid }&page=${page}&id=${bbs.id}" class="btn btn-warning ms-3"> 수 정 </a>
           <a href="#" class="btn btn-danger me-3"> 삭 제 </a>
           <c:if test="${adminBbs.rgrade == 0 || 
                       (member != null && member.grade!=null && adminBbs.rgrade <= member.grade)}">
              <a href="write?bbsid=${bbsid }" class="btn btn-success">글쓰기</a>
           </c:if>
           <c:if test="${adminBbs.regrade == 0 || 
                       (member != null && member.grade!=null && adminBbs.regrade <= member.grade)}">
           <a href="#" class="btn btn-success ms-3"> 답 변 </a>
           </c:if>
      </div>
   </div>
  
</div>   