<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles-extras" prefix="tilesx"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>CenterDevice ROCA prototype</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" href="resources/css/app.css">
    </head>
    <body>
        <!-- Header content -->
        <tiles:insertAttribute name="header"  defaultValue="" />

        <!-- Right Menu (User's Groups) content -->
        <tiles:insertAttribute name="menuRight"  defaultValue="" />
        
        <!-- Page content -->
        <div class="main" role="main">
            <tiles:insertAttribute name="body" defaultValue="" />
        </div>
        
        <!-- End of page content -->
        <tiles:insertAttribute name="footer"  defaultValue="" />
    </body>
</html>