<%@page import="beans.CsvFile"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="beans.CsvFile , java.util.ArrayList " %>
<!DOCTYPE html>
<html>
<head>
 	<meta charset="ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<title>File Data</title>
</head>
<body>
<a href="<%= request.getContextPath()%>">Home</a>
<h1 align="center">Csv file data</h1>
<hr><br><br>
<table border="1px" align="center" width="500px"  >
		<thead >
			<tr>
				<th>S.No.</th>
				<th>Name</th>
				<th>Email</th>
				<th>Phone Number</th>
			</tr>
		</thead>
	
<% 
CsvFile csvFile = new CsvFile();
ArrayList<CsvFile> data =csvFile.showData(request, response);
System.out.println(data);

for(int i=0;i<data.size();i++)
{
	CsvFile temp=data.get(i);
	String name = temp.getName();
	String email = temp.getEmail();
	long num = temp.getPhoneNumber();
	//System.out.println("Name : "+name + "   EMail : "+email  +"   num : "+num);
%>
	
	<tbody align="center">
			<tr>
				<td ><%= i+1 %></td>
				<td id="name"><%=name %></td>
				<td id="email"><%=email %></td>
				<td id="phno"><%=num %></td>
			</tr>
<%	
}
%>
</tbody>
	</table>
<a href="<%= request.getContextPath()%>">Home</a>
</body>
</html>