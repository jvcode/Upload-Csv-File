package beans;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

/*
  This class contains the fields same as that of csv file
  with the respective getter and setters
 
 */
public class CsvFile {
	
	private int id;
	private String name;
	private String email;
	private long phoneNumber;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return "CsvFile [id=" + id + ", name=" + name + ", email=" + email + ", phoneNumber=" + phoneNumber + "]";
	}
	
	//Method to save file in server recieved from user	
	public void saveCsvFile(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException {
		
		response.setContentType("text/html");  
		PrintWriter out;
		HttpSession session = request.getSession();  //Get current Session
		MultipartRequest m;  //Oreilly Library to handle Multipart request 
		
		String filePath = request.getServletContext().getRealPath("")+"uploads";
		//System.out.println("File Path :"+filePath);
		session.setAttribute("filePath", filePath);
		try {
			out = response.getWriter();
			//System.out.println("Path : "+ request.getServletContext().getRealPath(""));
			
			m = new MultipartRequest(request,filePath);  
			String origFileName = m.getOriginalFileName("userFile");  // Name of a file in a input field
			//System.out.println("Name of File : "+ origFileName);
			session.setAttribute("FileName", origFileName);			// set Attribute in session object
			//System.out.println("Session :"+session.getAttribute("FileName"));
			boolean temp = searchFileNameDb(origFileName, request);   //This method will return boolean value after verification
			if(temp)
			{
				System.out.println("Name of a file Already Exists");
			}
			else
			{
				setFileNameDb(origFileName);
			}
			  
			setCsvData(request,response);
			//out.print("Success");
			request.getRequestDispatcher("/user/show.jsp").forward(request, response);
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		} 
		 
		
	}
	
	//Search the file name database
	private boolean searchFileNameDb(String fileName,HttpServletRequest request)
	{
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		HttpSession session = request.getSession();
		
		try {
			conn = ConnectDb.getConnection();	//Establish a connection
			smt = conn.createStatement();
			smt.execute("SET FOREIGN_KEY_CHECKS=0;");
			rs= smt.executeQuery("select cf_id, file_name from csv_file");
			
			while(rs.next())
			{
				String fileNameDb = rs.getString("file_name");
				System.out.println("File Name in db :"+fileNameDb);
				if(fileName.equals(fileNameDb))
				{
					session.setAttribute("cfId", rs.getInt("cf_id"));
					return true;
				}
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		finally
		{
			try {
				rs.close();
				conn.close();
				smt.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			
		}
		return false;
	}
	
	//Save the file name in database
	private void setFileNameDb(String fileName)
	{
		Connection conn = null;
		PreparedStatement ps = null ;

		try {
		conn =ConnectDb.getConnection();
		ps = conn.prepareStatement("insert into csv_file(file_name) values (?)");
		ps.setString(1, fileName);
		int temp = ps.executeUpdate();
		System.out.println("Record update in csv_file table :"+temp);

		} 
		catch (SQLException e) {
			
			e.printStackTrace();
		}
		finally
		{
			try 
			{
				conn.close();
				ps.close();
			} 
			catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	//Method for Storing the file data in database
	public void setCsvData(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException
	{
		HttpSession session =request.getSession();
		String fileName =(String) session.getAttribute("FileName");   // get filename from session object
		searchFileNameDb(fileName, request);
		String filePath = (String) session.getAttribute("filePath")+"\\"+fileName;
		int cfId = (int) session.getAttribute("cfId");
		//System.out.println("File path to Fetch :"+filePath);
		Connection conn =null;
		PreparedStatement ps =null;
		BufferedReader csvReader = new BufferedReader(new FileReader(filePath));    //This will load file from file path
		
		//System.out.println("SetCsvData Session File Name : "+session.getAttribute("FileName"));
		
		String row;
		String[] data=null;					// data from file will in the form of string array
		conn = ConnectDb.getConnection();
		conn.createStatement().execute("SET FOREIGN_KEY_CHECKS=0;");
		ps=conn.prepareStatement("insert into csv_details(name,email,phone_number,cf_id) values (?,?,?,?)");
		
		while((row = csvReader.readLine())!=null)
		{
			data = row.split(",");
			//System.out.println("Data : "+data);
			int i=1;
			for(String s : data)
			{
				//System.out.println("File data : "+ s);
				if(i==3)
					ps.setLong(i,Long.parseLong(s));
				else
					ps.setString(i, s);
				i++;
			}
			ps.setInt(4, cfId);
			int records = ps.executeUpdate();
			//System.out.println("Records update :"+records);
		}
	}
	
	//Method to retrieve from database and store into ArrayList and it is fetched in show.jsp
	public ArrayList<CsvFile> showData(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException
	{
		HttpSession session = request.getSession();
		int cfId = (int) session.getAttribute("cfId");
		Connection conn = null;
		Statement smt = null;
		ResultSet rs = null;
		
		conn = ConnectDb.getConnection();
		smt = conn.createStatement();
		rs = smt.executeQuery("select cd_id, name,email,phone_number from csv_details where cf_id="+cfId);
		ArrayList<CsvFile> arr= new ArrayList<CsvFile>();
		while(rs.next())
		{
			CsvFile csv= new CsvFile();
			int id = rs.getInt("cd_id");
			String name = rs.getString("name");
			String email = rs.getString("email");
			long phoneNumber = rs.getLong("phone_number");
			csv.setId(id);
			csv.setName(name);
			csv.setEmail(email);
			csv.setPhoneNumber(phoneNumber);
			arr.add(csv);						// adding csv object in arraylist arr
		}
		return arr;   // returning the arraylist object 
	}

}
