<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty errorMessage }">
   <script>
     alert('${errorMessage}');
   </script>
</c:if>
<h2 class="text-center mt-4 mb-5">회원 로그인</h2>
<form action="./login" method="post">
   <table>
      <tr>
         <td>아이디 : </td>
         <td><input type="text" name="userid" id="userid"></td>
      </tr>
      <tr>
         <td>비밀번호 : </td>
         <td><input type="password" name="userpass" id="userpass"></td>
      </tr>
      <tr>
         <td colspan="2" class="text-center">
            <input type="submit" value=" 로 그 인 " />
         </td>
      </tr>
      <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" />
   </table>
</form>