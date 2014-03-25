<!DOCTYPE html>

<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
		<script src="/resources/js/d3/lib/d3/d3.js"></script>
		<script src="/resources/js/d3/d3.layout.cloud.js"></script>
		
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

		    var fill = d3.scale.category20();
		    var data = ${tag_cloud}
		    
		    
			  d3.layout.cloud().size([960, 600])
			      .words(data.map(function(d) {
			        return {text: d.text, size: d.frequency *5 +30 };
			      }))
			      .padding(5)
			      .rotate(function() { return ~~(Math.random() * 2) * 90; })
			      .font("Impact")
			      .fontSize(function(d) { return d.size; })
			      .on("end", draw)
			      .start();
		      
			  function draw(words) {
				    d3.select("#wordcloud").append("svg")
				        .attr("width", 960)
				        .attr("height", 600)
				      .append("g")
				        .attr("transform", "translate(480,300)")
				      .selectAll("text")
				        .data(words)
				      .enter().append("text")
				        .style("font-size", function(d) { return d.size + "px"; })
				        .style("font-family", "Impact")
				        .style("fill", function(d, i) { return fill(i); })
				        .style("cursor","pointer" )
				        .attr("text-anchor", "middle")
				        .attr("transform", function(d) {
				          return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
				        })
				        .text(function(d) { return d.text; })
				        .on("click", function(d){
				        	window.location.href = "/productReviews?id=${id}&tag="+d.text;        
					        });
			  }
			  $("#btn").click(function(){
					window.location.href = "/productReviews?id="+${id}+"&tag="+d.text;
					});
		});
		</script>

		
	
	</head>
	<body>
		<div class="container">
			<header class="clearfix">
				<div class="row">
					<div class="span2"><img src='${product_img}' class="img-reponsive img-thumbnail" /></div>
					<div class="span8"><h1>${product_name}</h1></div>
				</div>				
							</header>
			<%@ include file="/WEB-INF/jsp/menu.jsp" %>
			<div class="main">
				<h2>Sentiment Review Monitor</h2>
				<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				<div id='wordcloud'></div>
				
				<h2>Attribute Review Score</h2>
		<table class="table table-striped table-bordered table-condensed">
		
		<c:forEach items="${attribMap}" var="entry" varStatus="val">
		 <c:if test="${entry.value>0}"> 
			<tr >
			<td width="50%">${entry.key}</td>
			<td ><div class="progress">
		  		<div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: ${entry.value*100}%;">
		    	<span class="sr-only">60% Complete</span>
		  		</div>
				</div>
			</td>
			</tr>
		</c:if>
		</c:forEach>
		</table>
		
				<div width = "100%">
				<h2>Positive Reviews</h2>
				<table border ="0" width="100%">
				<c:forEach items="${positive_reviews}" var="entry" varStatus="val">
				 
				 <tr><td style="padding: 3px" class="even">
		 		<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapse${val.count}">
			          ${entry.display_text }
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
					
               		</td></tr>
				 
					
				</c:forEach>
					
				</table>
				</div>
				<c:if test="${fn:length(negative_reviews) > 1 }">
					
				<div>
				
				<h2>Negative Reviews</h2>
				<table border ="0" width="100%">
				<c:forEach items="${negative_reviews}" var="entry" varStatus="val">
				 
				 <tr><td style="padding: 3px" class="even">
		 		<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapse${val.count}">
			          ${entry.display_text }
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
					
               		</td></tr>
				 
					
				</c:forEach>
					
				</table>
				</div>
				</c:if>
				
			<!-- Product Page -->	
				
				
			
		<h2>Delivery Issues</h2>
		
    <div class="row"  style="padding: 10px">
      <div class="span2" ><img src="http://placehold.it/180x120"></div>
      <div class="span8" >Lorem Ipsum, Lorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem IpsumLorem Ipsum
        </div>
    	</div>
    	
		
		
    	</div>									
	</div>			
			
		
		
	
	</body>
</html>
