<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${not empty error }">
  <script>
    alert("${error}");
    history.go(-1);
  </script>
</c:if>
<h1>
	Hello world!  
</h1>
<c:if test="${not empty nimg }">
<img src="res/upload/members/${nimg }" />
</c:if>
<p>${test }</p>

