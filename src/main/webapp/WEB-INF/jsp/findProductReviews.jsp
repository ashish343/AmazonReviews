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
		            text: 'Sentiment Review Meter'
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
		                {
		                    name: 'Negative',
		                    y: ${map.negative},
		                    sliced: true,
		                    selected: true
		                }
		            ]
		        }]
		    });});
	    </script>
		<div class="container">
			<header class="clearfix">
				
				<h1>Walmart Review Monitor</h1>
							</header>
			<%@ include file="/WEB-INF/jsp/menu.jsp" %>
			
			<div class="main">
				
				<span><h2>Product Reviews for <a href="/product?id=${id} "><span style="color:black;font-weight:bold">${id}</span></a> with <span style="color:black;font-weight:bold">${tag}</span></h2></span>
				<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				<c:forEach items="${negative_reviews}" var="entry" varStatus="val">
				<div class="row" style="display: block">
				<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapse${val.count}">
			        <div class="row">
			        <div class="span9">${entry.display_text}</div>
			        <div class="span1"><c:choose>
			          <c:when test="${entry.categ=='1'}">
			          	<span class="label label-success">Positive</span>
			          </c:when>
			          <c:when test="${entry.categ=='-1'}">
			          	<span class="label label-danger">Negative</span>
			          </c:when>
			          <c:otherwise>
			          	<span class="label label-info">Neutral</span>
			          </c:otherwise>
			          </c:choose>
			        </div>
   			        </div>
			        
			          
			        </a>
			      </h4>
			    </div>
			    <div id="collapse${val.count}" class="panel-collapse collapse ">
			      <div class="panel-body">
			        	${entry.review}
			      </div>
			    </div>
			  </div>
			  </div>
			  </div>	
				</c:forEach>
								
    	</div>									
	</div>			
			
		
		
	
	</body>
</html>
