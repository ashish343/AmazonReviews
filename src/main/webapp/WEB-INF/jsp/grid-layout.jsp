<!DOCTYPE html>

<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en" class="no-js">
	<head>
			
		<meta charset="UTF-8" />
		
		<%@ include file="/WEB-INF/jsp/js/pageJS.jsp" %>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
		<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
		<title>Walmart Review Monitor</title>
		<meta name="description" content="Blueprint: Vertical Icon Menu" />
		<meta name="keywords" content="Vertical Icon Menu, vertical menu, icons, menu, css" />
		<meta name="author" content="Codrops" />
		<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
		
		<link rel="shortcut icon" href="../favicon.ico">
		<script src="/resources/js/modernizr.custom.js" /></script>
		<script src="/resources/js/bootstrap.min.js" /></script>
		<script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
		<script src="http://code.highcharts.com/highcharts.js"></script>
		<script src="http://code.highcharts.com/modules/exporting.js"></script>
		<link href="/resources/css/default.css"  rel="stylesheet">
		<link href="/resources/css/component.css" rel="stylesheet">
		<link type="text/css" href="${path}/resources/css/bootstrap.min.css" rel="stylesheet"/>
		<link type="text/css" href="${path}/resources/css/bootstrap-responsive.min.css" rel="stylesheet"/>
		<style>
		.row{
		padding:5px;
		}</style>
		
	</head>
	<body>
		<div class="container">
			<header class="clearfix">
				
				<h1>Walmart Review Monitor</h1>
							</header>
			<%@ include file="/WEB-INF/jsp/menu.jsp" %>
			<div class="main">
				
				<h2>Products Wall</h2>
				<ul class="list-group">
  

				<c:forEach items="${itemMap}" var="entry" varStatus="val">
					
					<li class="list-group-item">
					<div class="row">
						<a href="/product?id=${entry.id}">
						 <c:choose>
						<c:when test='${(val.count)%2 eq 0}'>
							<div class="span2"><img src ="${entry.img_url }" class="img-responsive img-thumbnail"/></div>
							<div class="span8"><h3>${entry.title}</h3></div>
							</c:when>
							 <c:otherwise>
							<div class="span2"><img src ="${entry.img_url }" class="img-responsive img-thumbnail"/></div>
							<div class="span8"><h3>${entry.title}</h3></div>
							
							</c:otherwise>
							</c:choose>
							
						</a>
					</div>
					</li>
				</c:forEach>
				</ul>
				
				
    	</div>									
	</div>			
			
		
		
	
	</body>
</html>
