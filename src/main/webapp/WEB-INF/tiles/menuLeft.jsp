<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div>
    <a href="documents">${user.name}</a>
    <br/>
    <c:forEach var="group" items="${user.groups}">
        <a class = "group" href="documents?groups=${group.id}">${group.name}</a>
        <br/>
    </c:forEach>
</div>