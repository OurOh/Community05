<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>    
<div class="row">
   <div class='col-md-1'>
      <h1 class="mx-4"><i class="ri-bar-chart-horizontal-line"></i></h1>
   </div>
 
   <form class="form-group col-md-3" action="/community/bbs/search" method="get">
		<div class="input-group mt-3 mb-3">
		  <input type="text" class="form-control" name="searchVal" placeholder="통합검색...">
		  <div class="input-group-append">
            <button class="btn btn-primary" type="button">OK</button>
          </div>
		</div>
   </form>
</div>