<!DOCTYPE html>

<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="en" class="no-js">
	<head>
	
	<style>
	tr {
	padding: 10px;
}
td{
vertical-align: top;}
	</style>		
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
		                {
		                    name: 'Negative',
		                    y: ${map.negative},
		                    sliced: true,
		                    selected: true
		                }
		            ]
		        }]
		    });
		});</script>

		
	
	</head>
	<body>
		<div class="container">
			<header class="clearfix">
				
				<h1>${product_name}</h1>
							</header>
			<ul class="cbp-vimenu">
				<li><a href="/	" class="icon-logo">Logo</a></li>
				
				<li><a href="#" class="icon-search">Search</a></li>
				<li><a href="#" class="icon-pencil">Pencil</a></li>
				<!-- Example for active item:
				<li class="cbp-vicurrent"><a href="#" class="icon-pencil">Pencil</a></li>
				-->
				<li><a href="#" class="icon-location">Location</a></li>
				<li><a href="#" class="icon-images">Images</a></li>
				<li><a href="#" class="icon-download">Download</a></li>
			</ul>
			<div class="main">
				<h2>Sentiment Review Monitor</h2>
				<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				
				<h2>Attribute Review Score</h2>
		<table class="table table-striped table-bordered table-condensed">
		<tr >
			<td>Flash</td>
			<td ><div class="progress">
  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%;">
    <span class="sr-only">60% Complete</span>
  </div>
</div></td>
		</tr>
		<tr >
			<td width="50%">Lens</td>
			<td><div class="progress">
  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 89%;">
    <span class="sr-only">60% Complete</span>
  </div>
</div></td>
		</tr>
		<tr >
			<td width="50%">Mega Pixel</td>
			<td><div class="progress">
  	<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 37%;">
    <span class="sr-only">60% Complete</span>
  </div>
</div></td>
		</tr>
		</table>
		
				<div width = "100%">
				<h2>Positive Reviews</h2>
				<table border ="0" width="100%">
				<c:forEach items="${positive_reviews}" var="entry" varStatus="val">
				 <c:if test="${val.count == 1}">
				 <tr><td style="padding: 3px" class="even">
		 		<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne">
			          Canon improved the high ISO performance, providing a 1 to 1.5 stop improvement.
			        </a>
			      </h4>
			    </div>
			    <div id="collapseOne" class="panel-collapse collapse ">
			      <div class="panel-body">
			        	${entry}
			      </div>
			    </div>
			  </div>
			  </div>						
					
               		</td></tr>
				 </c:if>
				 <c:if test="${val.count  != 1}">
				 <tr><td style="padding: 3px" class="odd">
				 				
 				<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapse${val.count}">
			          Summary ${val.count}
			        </a>
			      </h4>
			    </div>
			    <div id="collapse${val.count}" class="panel-collapse collapse ">
			      <div class="panel-body">
			        	${entry}
			      </div>
			    </div>
			  </div>
			  </div>						
			</td></tr>
				 </c:if>
					
				</c:forEach>
					
				</table>
				</div>	
				<div>
				<h2>Negative Reviews</h2>
				<table width="100%">
					<c:forEach items="${negative_reviews}" var="entry" varStatus="val">
						<tr><td>
						<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapse${val.count}">
			          Summary ${val.count}
			        </a>
			      </h4>
			    </div>
			    <div id="collapse${val.count}" class="panel-collapse collapse ">
			      <div class="panel-body">
			        	${entry}
			      </div>
			    </div>
			  </div>
			  </div>	
						
	               		</td></tr>
					</c:forEach>
				</table>
				</div>
				
			<!-- Product Page -->	
				
				
			
		<h2>Delivery Issues</h2>
		
    <div class="row"  style="padding: 10px">
      <div class="span2" ><img src="http://placehold.it/180x120"></div>
      <div class="span8" >asdasdasd as kjahs dkajlkajshadl ksjdhakl sjdhakl sskdksdhals k
      a s;lkdjas ;lkdjas;lkdj as;lkdja ;lskdjal;skjdal;skjdal;ksj da;lksjda;lksj d;alsdj a
       alskjda;sldkjas;lkdja;lskdja;lsk dja;lskdja ;lskdja ;slkdjas;dljka s
        al;skdja;sldkja ;slkdja ;lskdja;slkjda;ls dkjas
        </div>
    	</div>
    	
		
		
    	</div>									
	</div>			
			
		
		
	
	</body>
</html>
