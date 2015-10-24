<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

	<div class="panel panel-default container">
			<!-- Default panel contents -->
			<div class="panel-heading">
				<h1>Proprieta Oggetto -> ${document.getName()}</h1>
			</div>
			<br>
			<div class="btn-group" role="group" aria-label="...">
				<button class="btn btn-success" onclick="goBack()"><span class="glyphicon glyphicon-chevron-left"></span></button>
				<button id="homeButton" type="button" class="btn btn-success" onclick="home()"><span class="glyphicon glyphicon-home"></span></button>
				<button id="forwardButton" class="btn btn-success" onclick="goForward()"><span class="glyphicon glyphicon-chevron-right"></span></button>
			</div>
			
			
			<table class="table table-hover">
				<thead>
					<tr>
						<th>Proprietà</th>
						<th>Valore</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${propList}" var="prop">
						<tr>
							<td>${prop.displayName}</td>
							<td style="word-break: break-all;">${prop.value}</td>
						</tr>
						
					</c:forEach>
				</tbody>
			</table>
	</div>
		
	<script type="text/javascript">
	
	$(document).ready(function(){
		document.getElementById("homeButton").disabled = true;
		document.getElementById("forwardButton").disabled = true;
	})
	
	function home() {
		window.location.href = "/mydoc";
	}
	
	function goBack(){
		history.go(-1);
	}
	
	function goForward(){
		history.go(+1);
	}
	
	</script>
</body>
</html>