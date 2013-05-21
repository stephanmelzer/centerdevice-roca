<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<header class="container-fluid">
    <a href="logout">Logout</a>

    <img src="resources/img/cd_logo.png" alt="centerdevice Logo">

    <form class="form-search" action="documents" method="GET">
        <input type="search"
               name="q"
               class="input-xlarge search-query"
               placeholder="Enter search query"
               autofocus>
        <button type="submit" class="btn btn-inverse">Search</button>
    </form>

    <form class="form-upload" action="documents" method="POST" enctype="multipart/form-data">
        <button id="newUploadButton" 
                type="button"
                style="visibility: hidden;" 
                class="btn btn-inverse " 
                onclick="selectFile()">Upload File</button>

        <div id="fallback-input">
            <input id="fileSelector" type="file" name="document">
            <button id="uploadButton" type="submit" class="btn btn-inverse">Upload File</button>
        </div>
    </form>
</header>