<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="p-4">
	<div class="row main-row">
	   <c:set var="ct" value="0" />
	   <c:forEach var="post" items="${latestPosts }">
	      <c:choose>
          <c:when test="${post.bbsadmin_id != ct }">
             <c:if test="${ct != 0 }">
                </ul>
                   </div>       
             </c:if>
             <!-- 새 그룹 시작 -->
             <div class="col-md-4">
                <h5 class="text-center mt-5 mb-3">${post.bbstitle }</h5>
                <c:choose>
                   <c:when test="${post.skin == 'gallery' }">
                     <ul class="gallery row">
                   </c:when>
                   <c:otherwise>
                      <ul class="list-group ">
                   </c:otherwise>
                </c:choose>
                
          </c:when>
          </c:choose>
          
          
          <c:choose>
          <c:when test="${post.skin == 'gallery' }">
              <li class="col-4">이미지</li>
          </c:when>
               <c:otherwise>          
                   <li class="list-group-item list-group-item-dashed">${post.title}</li>
               </c:otherwise>
           </c:choose>
                
           <!-- ct 갱신 -->
           <c:set var="ct" value="${post.bbsadmin_id }" />
    	   </c:forEach>     
    	     
	</div>
</div>