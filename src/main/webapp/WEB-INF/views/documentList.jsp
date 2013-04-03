<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<section class="movie-overview">
    <ul>
        <c:forEach var="document" items="${documents.documents}">
            <li>
                <img src="resources/img/spaltentyp_icon_documents.png"
                     height="17"
                     width="20"
                     alt="Representation of a directory"
                     class="icon">

                <a href="document/${document.id}" title="${document.title}">
                    ${document.filename}
                </a>

                <div class="summary">
                    <span class="author">${document.owner.name}</span>
                    <time datetime="${document.html5Uploaddate}">${document.formatedUploaddate}</time>
                    <span class="filesize">${document.formatedSize}</span>
                </div>
            </li>
        </c:forEach>

    </ul>
</section>