<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div>
    <h3>${user.name}</h3>
    <c:forEach var="group" items="${user.groups}">
        ${group.name}
        <br/>
    </c:forEach>
</div>