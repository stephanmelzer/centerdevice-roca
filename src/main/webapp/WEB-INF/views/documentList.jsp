<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Documents</h1>

<h1>Hits: ${documents.hits}</h1>
</br>
<c:forEach var="document" items="${documents.documents}">
    ${document.filename}, size: ${document.size}
    </br>
</c:forEach>