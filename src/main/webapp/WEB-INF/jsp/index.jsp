<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<script src="${pageContext.request.contextPath}/js/jquery-2.1.4.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

	<div class="panel panel-default container" id="vecchioDiv">
		<!-- Default panel contents -->
		<div class="panel-heading">
			<h1>I miei Documenti -> ${folder.getName()}</h1>
			<label hidden="true" id="foldLabel">${folder.getId()}</label> <label
				hidden="true" id="foldLabelName">${folder.getName()}</label>

		</div>
		<br>
		<div class="btn-group" role="group" aria-label="...">
			<button class="btn btn-success" onclick="goBack()"><span class="glyphicon glyphicon-chevron-left"></span></button>
			<button id="homeButton" type="button" class="btn btn-success" onclick="home()"><span class="glyphicon glyphicon-home"></span></button>
			<button class="btn btn-success" onclick="goForward()"><span class="glyphicon glyphicon-chevron-right"></span></button>
		</div>
		<div class="btn-group pull-right" role="group">
				<button type="button" class="btn btn-primary dropdown-toggle"
					data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> Create... <span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li data-toggle="modal" data-target="#myModalFolder"><a href="#"> New Folder</a></li>
					<li data-toggle="modal" data-target="#myModalDocument"><a href="#">New Document</a></li>
				</ul>

		</div>




		<!-- Modal Folder-->
		<div id="myModalFolder" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Creazione Cartella</h4>
					</div>
					<form action="/mydoc/newfolder" method="POST">
						<div class="modal-body">
							<input type="text" name="folderName" class="form-control" placeholder="Inserisci il nome..."	aria-describedby="basic-addon1" > 
							<input id="folderID1" type="hidden" name="idFolder" value="${folder.id}">
						</div>
						<div class="modal-footer">
							<button type="submit" class="btn btn-primary">Crea</button>
						</div>
					</form>
				</div>

			</div>
		</div>


		<!-- Modal Document-->
		<div id="myModalDocument" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Creazione Documento</h4>
					</div>
					
					<form action="/mydoc/newdocument" method="POST">
						<div class="modal-body">
							<input type="text" name="documentName" class="form-control" placeholder="Inserisci il nome..." aria-describedby="basic-addon1" /> <br>
							<textarea name="documentText" class="form-control" placeholder="Inserisci il testo..." aria-describedby="basic-addon1"></textarea>
							<input id="folderID2" type="hidden" name="idFolder" value="${folder.id}">
						</div>
	
						<div class="modal-footer">
							<button type="submit" class="btn btn-primary">Crea</button>
						</div>
					</form>
				</div>

			</div>
		</div>
		
		
		<!-- Modal Rename-->
		<div id="myModalRename" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Rinomina</h4>
					</div>
					<form action="/mydoc/rename" method="POST">
						<div class="modal-body">
							<input id="nameText" type="text" name="newName" class="form-control" placeholder="Inserisci il nome..."	aria-describedby="basic-addon1" > 
							<input id="folderID3" type="hidden" name="idFolder" value="${folder.id}">
							<input type="hidden" name="object" id="idObject" value="">
						</div>
						<div class="modal-footer">
							<button type="submit" class="btn btn-primary">Crea</button>
						</div>
					</form>
				</div>

			</div>
		</div>


		<!-- Table -->
		<table class="table table-hover">
			<thead>
				<tr>
					<th>Type</th>
					<th>Name</th>
					<th>Properties</th>
					<th>Options</th>
				</tr>
			</thead>
			<tbody id="tabBody">
				<c:forEach items="${sons}" var="son">
					<c:choose>
						<c:when
							test="${son.getClass().getSimpleName().equals('FolderImpl')}">
							<tr>
								<td><span class="glyphicon glyphicon-folder-open"></span></td>
								<td><a href="/mydoc/detail/${son.id}"> <c:out
											value="${son.name}"></c:out></a></td>
								<td><a href="/mydoc/property/${son.id}"><button
											class="btn btn-primary">Show Properties</button></a></td>
								<td>
									<div class="btn-group" role="group">
										<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> Other... <span class="caret"></span> </button>
										<ul class="dropdown-menu">
											<li><a href="#"> Download as Zip...</a></li>
											<li data-toggle="modal" data-target="#myModalRename"><a href="#" onclick='rename("${son.id}", "${son.name}");'> Rename </a></li>
											<li><a href="/mydoc/delete/${son.id}/${folder.id}"> Delete </a></li>
										</ul>

									</div>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<tr id="row${son.name}">
								<td><span class="glyphicon glyphicon-file"></span></td>
								<td><c:out value="${son.name}"></c:out></td>
								<td><a href="/mydoc/property/${son.id}"><button class="btn btn-primary">Show Properties</button></a></td>
								<td>
									<div class="btn-group" role="group">
										<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"> Other... <span class="caret"></span> </button>
										<ul class="dropdown-menu">
											<li><a href="/mydoc/download/${son.id}"> Download </a></li>
											<li data-toggle="modal" data-target="#myModalRename"><a href="#" onclick='rename("${son.id}", "${son.name}");'> Rename </a></li>
											<li><a href="/mydoc/delete/${son.id}/${folder.id}"> Delete</a></li>
										</ul>

									</div>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</tbody>
		</table>
		
		

<!-- 		<form id="myForm"> -->
<!-- 			<input type="radio" name="radioName" value="1" /> 1 <br /> <input -->
<!-- 				type="radio" name="radioName" value="2" /> 2 <br /> <input -->
<!-- 				type="radio" name="radioName" value="3" /> 3 <br /> -->
<!-- 		</form> -->
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			
			//alert("CIAO")
			
			if('${errore}'!=""){
				alert('${errore}');
			}

// 			$("#folderID1").val('${folder.id}');
// 			$("#folderID2").val('${folder.id}');

			folder = $('#foldLabelName').text();

			if (folder === "documentLibrary") {
				document.getElementById("homeButton").disabled = true;
			}
		});

		/* Non serve perchè viene fatto framite form html
		
			TODO fare la post tramite jquery e farmi tornare la pagina */
		
		/*function send(elem) {

			if (elem.id === "creaFolder") {
				foldName = $('#inputText').val();
				idFolder = $('#foldLabel').text();

				window.location.href = "/mydoc/newfolder/" + idFolder + "/"
						+ foldName;
			} else if (elem.id === "creaDocument") {
				docName = $('#inputTextDocument').val();
				idFolder = $('#foldLabel').text();
				text = $('#textAreaDocument').val();

				window.location.href = "/mydoc/newdocument/" + idFolder + "/"
						+ docName + "/" + text;
			}

		}*/

		function home() {
			window.location.href = "/mydoc";
		}
		
		function goBack(){
			history.go(-1);
		}
		
		function goForward(){
			history.go(+1);
		}
		
		function rename(id, nome){
			
			$('#nameText').val(nome);
			$('#idObject').val(id);
			
		}
		
		function sendRename(){
			
			
			
			$.post("/mydoc/rename", 
				{
					object: $('#idObject').val(),
					idFolder: $('#folderID3').val(),
					newName: $('#nameText').val()
				}).done(function(data){
					
					alert('${data.status}')
					
				/*	if(data.status == "KO"){
						alert(data);
					}else{
						$('#vecchioDiv').replaceWith(data);
					}*/
					
					
				});
		}
		
		
// 		$('#myForm input').on('change', function() {
// 			   alert($('input[name=radioName]:checked', '#myForm').val()); 
// 		});
		
// 		function deleteRow(id){
			
// 			search="#row"+id;
			
// 			$(search).remove();
			
			
			
// 		}
	</script>

</body>
</html>