<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:choose>
    <c:when test="${isMobileClient}">
        <div id="mobileClient">
            You are visiting this site with your mobile phone. 
            Get the CenterDevice mobile app <a href="${appStoreLink}">here</a>
        </div>
    </c:when>
</c:choose>