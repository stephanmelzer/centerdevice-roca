<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div>
    <h3>      
        <a href="documents">${user.name}</a>
    </h3>
    <c:forEach var="group" items="${user.groups}">
        <a href="documents?groups=${group.id}">${group.name}</a>
        <br/>
    </c:forEach>
</div>