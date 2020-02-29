package helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import beans.CsvFile;

/**
 * Servlet implementation class UploadFile
 * This Servlet will save the uploaded file on server recieved from upload file.jsp
 */
@WebServlet(description = "Servlet for csv file uploading", urlPatterns = { "/uploadFile" })
public class UploadFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		CsvFile csvFile = new CsvFile();
		try 
		{
			csvFile.saveCsvFile(request,response);			// This will save file 
			csvFile.showData(request, response);			// This will show data of a file after successfull upload
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

	
}
