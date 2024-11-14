<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>       
    
<!DOCTYPE html>
<div class="p-5 my-4 bg-white">
	<h1 class="text=center">${adminBbs.bbstitle }</h1>
	<div class="viewbox">
		<div class="border-bottom d-flex justify-content-between py-2 px-4 align-itmes-end">
			<h3>제목입니다.</h3>
			<div class="writer">홍길동</div>
		</div>
	<div class="infobox d-flex justify-content-end py-3 border-bottom">
		<div class="wdate">2019.11.11 00시 oogn</div>
		<div class="hit">조회수 : 0</div>
	</div>
	<c:if test="${view.fileExt != null && view.fileExt.isEmpty() }">
		<div class="bg-light p-3 text-right mb-3">
			<a href="#">파일다운로드</a>
		</div>
	</c:if>
	<div class="content-box px-4">
		fsdfdsf
		dfsdfdsf
		sdfsdf
	
	</div>
	<div class="btn-box text-right pt-3">
		<a href="#" class="btn btn-primary me-3"> 목 록</a>		
		<a href="#" class="btn btn-warning ms-3">수 정</a>
		<a href="#" class="btn btn-danger me-3">삭 제</a>
		<c:if test="#{adminBbs.lgrade ==0 ||
				(member != null && member.grade!=null && adminbbs.rgrade <= member.grade)} adminBbs.lgrade <=member.grade }">
			<a href= "write?bbsid=${bbsid }" class="btn btn-success">글쓰기</a>
		</c:if>
		<a href="#" class="btn btn-success ms-3">답 변 </a>	
	
	

	</div>
	
	
	</div>

</div>