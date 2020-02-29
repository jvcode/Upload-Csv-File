<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-3.4.1.min.js"></script>
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<title>Home</title>
</head>

<body>
<h1>Upload File</h1>
<form action="uploadFile" method="post" id="uploadForm"   enctype="multipart/form-data" >

	Select a file (*.csv) : 
	<input type="file"  id = "file"  name="userFile" accept=".csv" required><br><br>
	<input type= "submit" name="Upload" value="Upload File" id="upload">
	<br>
	
	<br>
</form>

<script >
	$(document).ready(function(){
		$('a').hide('fast');
		$('#show').hide('fast');
		$('#file').change(function(){
		var fileNameExt = $('#file').val().split(".").pop().toLowerCase();
			 if (fileNameExt!='csv') 
			 {        
				 $('#uploadForm').trigger('reset');
			  	 alert('You must select an .CSV file only');   
			  	 return false;
			 }
			 else
				 return true;
		});
		
		$('#upload').click(function(){
			var formData = new FormData( $('#uploadForm')[0]);
			
			$.ajax({
				url:'<%=request.getContextPath()%>/ uploadFile',
				type:'post',
				processData:false,
				data:{formData:formData},
				enctype: 'multipart/form-data',
				cache:false,
				success:function(data){
					
				}
			});
		});
		
	});

</script> 
 
 </body>
</html>