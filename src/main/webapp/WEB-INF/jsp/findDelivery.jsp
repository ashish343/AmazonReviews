<!DOCTYPE html>

<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html lang="en" class="no-js">
	<head>
			
		<meta charset="UTF-8" />
		
		<%@ include file="/WEB-INF/jsp/js/pageJS.jsp" %>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
		<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
		<title>Amazon Review Monitor</title>
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
		<script src="/resources/js/d3/lib/d3/d3.js"></script>
		<script src="/resources/js/d3/d3.layout.cloud.js"></script>
		<style>
		.row{
		padding:5px;
		}</style>
		

		
	
	</head>
	<body>
	
	
	<script>
	$(function () {
	    $('#container').highcharts({
	        chart: {
	            plotBackgroundColor: null,
	            plotBorderWidth: null,
	            plotShadow: false
	        },
	        title: {
	            text: 'Reviews Sentiment Meter'
	        },
	        tooltip: {
	    	    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	        },
	        plotOptions: {
	            pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                dataLabels: {
	                    enabled: true,
	                    color: '#000000',
	                    connectorColor: '#000000',
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
	                }
	            }
	        },
	        series: [{
	            type: 'pie',
	            name: 'Review Share',
	            data: [
	                ['Positive',  ${map.positive}],
	                ['Neutral',  ${map.neutral}],
	                ['Negative',  ${map.negative}]

	            ]
	        }]
	    });
	});
	    </script>
		<div class="container">
			<header class="clearfix">
				
				<h1>Walmart Review Monitor</h1>
							</header>
			<%@ include file="/WEB-INF/jsp/menu.jsp" %>
			
			<div class="main">
				
				<span><h2>Delivery Issues for <span style="color:black;font-weight:bold">${tag}</span></h2></span>
				<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				
				
				
				<c:if test="${fn:length(positive_reviews) > 0 }">
				<h2>Positive Reviews</h2>
				<c:forEach items="${positive_reviews}" var="entry" varStatus="val">
				<div class="row" style="display: block">
				<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapsea${val.count}">
			          ${entry.display_text}
			        </a>
			      </h4>
			    </div>
			    <div id="collapsea${val.count}" class="panel-collapse collapse ">
			      <div class="panel-body">
			        	${entry.review}
			      </div>
			    </div>
			  </div>
			  </div>
			  </div>	
				</c:forEach>
				</c:if>
				
				
				<c:if test="${fn:length(neutral_reviews) > 0 }">
				<h2>Neutral Reviews</h2>
				<c:forEach items="${neutral_reviews}" var="entry" varStatus="val">
				<div class="row" style="display: block">
				<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapseb${val.count}">
			          ${entry.display_text}
			        </a>
			      </h4>
			    </div>
			    <div id="collapseb${val.count}" class="panel-collapse collapse ">
			      <div class="panel-body">
			        	${entry.review}
			      </div>
			    </div>
			  </div>
			  </div>
			  </div>	
				</c:forEach>
				</c:if>
				
				<c:if test="${fn:length(negative_reviews) > 0 }">
				<h2>Negative Reviews</h2>
				<c:forEach items="${negative_reviews}" var="entry" varStatus="val">
				<div class="row" style="display: block">
				<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapsec${val.count}">
			          ${entry.display_text}
			        </a>
			      </h4>
			    </div>
			    <div id="collapsec${val.count}" class="panel-collapse collapse ">
			      <div class="panel-body">
			        	${entry.review}
			      </div>
			    </div>
			  </div>
			  </div>
			  </div>	
				</c:forEach>
				</c:if>
								
    	</div>									
	</div>			
			
		
		
	
	</body>
</html>
