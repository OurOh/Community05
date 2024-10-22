<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h1>aside</h1>

<sec:authorize access="isAuthenticated()">
   <sec:authorize access="hasAnyRole('ROLE_USER', 'ROLE_ADMIN')">
      <p>안녕하세요? ${member.username }회원님 (level: ${member.grade})</p>  
   </sec:authorize> 
   <sec:authorize access="hasRole('ROLE_ADMIN')">
      <p>당신은 관리자 입니다.
         <a href="./admin" target="_blank">관리자모드</a>
      </p>
   </sec:authorize>
   <form action="${pageContext.request.contextPath}/logout" method="post">
      <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
      <button type="submit">로그아웃</button>
   </form>
</sec:authorize>

<sec:authorize access="!isAuthenticated()">
	<form action="./login" method="post">
	   <table>
	      <tr>
	         <td><input type="text" name="userid" id="userid" placeholder="아이디"></td>
	      </tr>
	      <tr>
	         <td><input type="password" name="userpass" id="userpass" placeholder="비밀번호"></td>
	      </tr>
	      <tr>
	         <td class="text-center">
	            <input type="reset" value="취소" /> <input type="submit" value=" 로 그 인 " />
	         </td>
	      </tr>
	      <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
	   </table>
	</form>
</sec:authorize>