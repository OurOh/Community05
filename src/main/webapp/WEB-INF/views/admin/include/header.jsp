<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<h1>관리자 모드</h1>
<nav>
  <a href="admin/mkbbs" class="btn btn-outline-dark mx-2">게시판생성</a>
  <a href="/" class="btn btn-outline-dark mx-2" target="_blank">홈페이지로</a>
  <a href="./logout" class="btn btn-outline-dark mx-2">로그아웃</a>
</nav>
<nav class="d-flex justify-content-between">
	<div>
		<a href="admin/mkbbs class="btn btn-outline-dark mx-2?>공지사항등록</a>
		<a href="admin/mkbbs class="btn btn-outline-dark mx-2?>커뮤니티등록</a>
		<a href="admin/mkbbs class="btn btn-outline-dark mx-2?>커뮤니티관리</a>
		<a href="admin/mkbbs class="btn btn-outline-dark mx-2?>회원관리</a>
	</div>
	<div>
		<a href="/" class="btn btn-outline-dark mx-2" target="_blank">홈페이지로</a>
		<a href="#" data-log="logout" class="btn btn-outline-dark mx-2 logout">로그아웃</a>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" class="csrf" />
	</div>
</nav>

<script>
 $(function(){
	 $(".logout").click(function(){
	
		 const cname=$("#csrf").attr("name");
		 const value=$("#csrf").val();
		 
		 //폼태그를 만든다.
		 let form =$("<form></form>");
		 //폼태그 안에 input 추가하기
		 form.attr('action', "./logout");
		 form.attr('method', 'post');
		 form.append($("<input type='hidden' name="+cname+" value=" +cvalue+ " />"))
		 form.appendTO('body');
		 form.submit();
		 
	 });
	 
 })
</script>