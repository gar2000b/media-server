package com.onlineinteract.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class MediaController {

	private long start;
	private long size;
	private long end;
	private long length;
	private File file;
	private long buffer = 102400;

	@RequestMapping(method = RequestMethod.GET, produces = "text/html", value = "/")
	@ResponseBody
	public String dockingPage() {
		return "<!doctype html>\r\n" + 
				"\r\n" + 
				"<html lang=\"en\">\r\n" + 
				"    <head>\r\n" + 
				"      <meta charset=\"utf-8\">\r\n" + 
				"    \r\n" + 
				"      <title>The HTML5 Herald</title>\r\n" + 
				"      <meta name=\"description\" content=\"The HTML5 Herald\">\r\n" + 
				"      <meta name=\"author\" content=\"SitePoint\">\r\n" + 
				"    \r\n" + 
				"      <link rel=\"stylesheet\" href=\"css/styles.css?v=1.0\">\r\n" + 
				"      <style type=\"text/css\">\r\n" + 
				"<!--\r\n" + 
				".style1 {\r\n" + 
				"	font-family: Arial, Helvetica, sans-serif;\r\n" + 
				"	font-weight: bold;\r\n" + 
				"	font-size: 18px;\r\n" + 
				"}\r\n" + 
				"-->\r\n" + 
				"#vid {\r\n" + 
				"	border: 1px solid #000000;\r\n" + 
				"	color:#000000;\r\n" + 
				"	background-color:#000000;\r\n" + 
				"}\r\n" + 
				"      .style2 {\r\n" + 
				"	color: #99CCFF\r\n" + 
				"}\r\n" + 
				"      .style3 {font-size: 28px}\r\n" + 
				"      </style>\r\n" + 
				"</head>\r\n" + 
				"    \r\n" + 
				"<body bgcolor=\"#333333\">\r\n" + 
				"    <p class=\"style1 style2 style3\">Eddie Murphy Showcasey-flix</p>\r\n" + 
				"    <p class=\"style1 style2\">Beverly Hills Cop</p>\r\n" + 
				"	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n" + 
				"  		<source src=\"vid/bhc2\" type=\"video/mp4\">\r\n" + 
				"        Your browser does not support the video tag.\r\n" + 
				"    </video>\r\n" + 
				"    <p class=\"style1 style2\">Eddie Murphy Delirious</p>\r\n" + 
				"	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n" + 
				"  		<source src=\"vid/emd\" type=\"video/mp4\">\r\n" + 
				"        Your browser does not support the video tag.\r\n" + 
				"    </video>\r\n" + 
				"    <p class=\"style1 style2\">The Nutty Professor</p>\r\n" + 
				"	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n" + 
				"  		<source src=\"vid/tnp\" type=\"video/mp4\">\r\n" + 
				"        Your browser does not support the video tag.\r\n" + 
				"    </video>\r\n" + 
				"    <p class=\"style1 style2\">The Golden Child</p>\r\n" + 
				"	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n" + 
				"  		<source src=\"vid/tgc\" type=\"video/mp4\">\r\n" + 
				"        Your browser does not support the video tag.\r\n" + 
				"    </video>\r\n" + 
				"    <p class=\"style1 style2\">Trading Places</p>\r\n" + 
				"	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n" + 
				"  		<source src=\"vid/tp\" type=\"video/mp4\">\r\n" + 
				"        Your browser does not support the video tag.\r\n" + 
				"    </video>\r\n" + 
				"    <p class=\"style1 style2\">48 Hours</p>\r\n" + 
				"	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n" + 
				"  		<source src=\"vid/48hours\" type=\"video/mp4\">\r\n" + 
				"        Your browser does not support the video tag.\r\n" + 
				"    </video>\r\n" + 
				"    <p class=\"style1 style2\">Eddie Murphy Raw</p>\r\n" + 
				"	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n" + 
				"  		<source src=\"vid/emr\" type=\"video/mp4\">\r\n" + 
				"        Your browser does not support the video tag.\r\n" + 
				"    </video>\r\n" + 
				"</body>\r\n" + 
				"</html>";
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "vid/{filename}")
	public void streamVideo(@PathVariable String filename, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		// response.setStatus(206);
		setHeaders(filename, response, request);
		stream(response);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "text/plain", value = "/test")
	@ResponseBody
	public String test() {
		return "APP Running OK :)";
	}
	
	private void stream(HttpServletResponse response) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		long index = start;
		byte[] bytesToWrite = new byte[(int) buffer];
		while(index < end) {
			long bytesToRead = buffer;
			if ((index + bytesToRead) > end)
				bytesToRead = end - index + 1;
			System.out.println("*** index = " + index + " bytesToRead = " + bytesToRead);
			fis.read(bytesToWrite, 0, (int) bytesToRead);
			response.getOutputStream().write(bytesToWrite);
			response.getOutputStream().flush();
			index += bytesToRead;
		}
		// response.flushBuffer();
		fis.close();
		System.out.println("*** Stream segment complete.");
	}

	private void setHeaders(String filename, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		response.addHeader("Content-Type", "video/mp4");
		response.addHeader("Cache-Control", "no-cache, public");
		// response.setContentType("video/mp4");

		start = 0;

		ClassLoader classLoader = getClass().getClassLoader();
		file = new File(classLoader.getResource(filename + ".mp4").getFile());
		size = file.length();

		end = size - 1;

		response.addHeader("Accept-Ranges", "0-" + end);

		/**
		 * Split out range and assign to start and end.
		 */
		if (request.getHeader("Range") != null) {
			String range = request.getHeader("Range").substring(6);
			if (range.contains(",")) {
				response.addHeader("'HTTP/1.1 416 Requested Range Not Satisfiable", null);
				response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + size);
				return;
			}
			String[] rangeArray = range.split("-");
			if (rangeArray != null && rangeArray.length > 0) {
				if (rangeArray[0] != null && rangeArray[0] != "")
					start = Long.valueOf(rangeArray[0]);
				if (rangeArray.length > 1 && rangeArray[1] != null && rangeArray[1] != "")
					end = Long.valueOf(rangeArray[1]);
				length = end - start + 1;
			}
			response.setHeader("header('HTTP/1.1 206 Partial Content');", null);
			response.addHeader("Content-Length", String.valueOf(length));
			response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + size);
			response.addHeader("Connection", "Keep-Alive");
			response.addHeader("Keep-Alive", "timeout=10, max=44");
			response.addHeader("Server", "Apache");
		} else {
			response.addHeader("Content-Length", String.valueOf(size));
		}
	}
}
