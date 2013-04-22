<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<div class="banner"></div>

<header>
    <a href="logout">Logout</a>

    <h1>
        centerdevice - structure without directories
        <img src="resources/img/cd_logo.png" alt="centerdevice Logo">
    </h1>

    <form class="form-search" action="documents" method="GET">
        <input type="search"
               name="q"
               class="input-xlarge search-query"
               placeholder="Enter search query"
               autofocus>
        <button type="submit" class="btn btn-inverse">Search</button>
    </form>

    <form action="documents" method="POST" enctype="multipart/form-data">
        <input type="file" name="document">
        <button type="submit" class="btn btn-inverse">Upload File</button>
    </form>
    
</header>