<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1>Documents</h1>

<h3>Hits: ${documents.hits}</h3>
</br>
<c:forEach var="document" items="${documents.documents}">
    <a href="document/${document.id}" title="${document.title}">${document.filename}</a>
    </br>
    ${document.owner.firstname} ${document.owner.lastname} ${document.formatedUploaddate} ${document.size}
    </br>
</c:forEach>