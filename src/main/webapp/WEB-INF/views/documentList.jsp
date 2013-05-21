<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<section class="movie-overview">
    <ul>
        <c:forEach var="document" items="${documents}">
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
                    <span class="author">${document.owner.nameHtmlEscaped}</span>
                    <time datetime="${document.html5Uploaddate}">${document.formatedUploaddate}</time>
                    <span class="filesize">${document.formatedSize}</span>
                </div>
            </li>
        </c:forEach>

    </ul>
</section>