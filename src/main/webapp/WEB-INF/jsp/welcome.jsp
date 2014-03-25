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
		    var data = ${token_count}
		    
		    
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
				        .attr("text-anchor", "middle")
				        .attr("transform", function(d) {
				          return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
				        })
				        .text(function(d) { return d.text; });
			  }
			  $("#btn").click(function(){
					window.location.href = "/delivery";
					});
			  
		});
		
		
		</script>
	
		<div class="container">
			<header class="clearfix">
				
				<h1>Walmart Review Monitor</h1>
							</header>
			<%@ include file="/WEB-INF/jsp/menu.jsp" %>
			<div class="main">
				
				<h2>General Review Sentiment Monitor</h2>
				<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
				
				
				
				<span><h2>Delivery Issues</h2><button id="btn" type="button" class="btn btn-success">More</button></span>
					
				<div id='wordcloud' align="center" ></div>
				
				<c:forEach items="${negative_reviews}" var="entry" varStatus="val">
				<div class="row" style="display: none">
				<div class="panel-group" id="accordion">
			  	<div class="panel panel-default">
			    <div class="panel-heading">
			      <h4 class="panel-title">
			        <a data-toggle="collapse" data-parent="#accordion" href="#collapse${val.count}">
			          ${entry.display_text}
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
