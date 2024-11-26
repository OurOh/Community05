<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>       
<div class="p-5 my-4 bg-white">
   <h1 class="text-center">검색 결과 : "${searchVal }"</h1>
   <c:if test="${empty groupedResults }">
      <p class="text-danger">검색 결과가 없습니다.</p>
   </c:if>
   
   <!-- 검색 결과 -->
   <c:forEach var="res" items="${groupedResults }">
     <div class="bbs-group">
        <h3>${res.key }</h3>
        <ul class="list-group">
           <c:forEach var="post" items="${res.value }">
              <li class="list-group-item">
                  <a href="#">${post.title }</a>
                  <span>작성자 : ${post.writer }</span>
                  <span>작성일 : ${post.wdate }</span>
                  <span>조회수 : ${post.hit }</span>
              </li>
           </c:forEach>
        </ul>
     </div>
   </c:forEach>
   

</div>   