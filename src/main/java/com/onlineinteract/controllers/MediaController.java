package com.onlineinteract.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class MediaController {

	private static final String[] VIDS = { "bhc2.mp4", "48hours.mp4", "tp.mp4", "emd.mp4", "emr.mp4", "tgc.mp4",
			"tnp.mp4" };
	private long start;
	private long size;
	private long end;
	private long length;
	private long buffer = 102400;
	private static HashMap<String, byte[]> videoStore;

	@PostConstruct
	public void loadVidsToMemory() throws IOException, URISyntaxException {
		System.out.println("*** Loading videos to memory.");

		videoStore = new HashMap<>();
		String[] fileList = VIDS;
		List<String> files = new ArrayList<>(Arrays.asList(fileList));

		for (String fileName : files) {
			InputStream resource = MediaController.class.getResourceAsStream("/" + fileName);
			System.out.println(resource.available());
			byte[] data = new byte[resource.available()];
			resource.read(data);
			videoStore.put(fileName, data);
			System.out.println("*** " + fileName + "loaded...");
		}

		System.out.println("*** Videos loaded to memory.");
	}

	@RequestMapping(method = RequestMethod.GET, produces = "text/html", value = "/")
	@ResponseBody
	public String dockingPage() {
		String randomUUID = UUID.randomUUID().toString();
		return "<!doctype html>\r\n" + "\r\n" + "<html lang=\"en\">\r\n" + "    <head>\r\n"
				+ "      <meta charset=\"utf-8\">\r\n" + "    \r\n" + "      <title>The HTML5 Herald</title>\r\n"
				+ "      <meta name=\"description\" content=\"The HTML5 Herald\">\r\n"
				+ "      <meta name=\"author\" content=\"SitePoint\">\r\n" + "    \r\n"
				+ "      <style type=\"text/css\">\r\n" + "<!--\r\n" + ".style1 {\r\n"
				+ "	font-family: Arial, Helvetica, sans-serif;\r\n" + "	font-weight: bold;\r\n"
				+ "	font-size: 18px;\r\n" + "}\r\n" + "-->\r\n" + "#vid {\r\n" + "	border: 1px solid #000000;\r\n"
				+ "	color:#000000;\r\n" + "	background-color:#000000;\r\n" + "}\r\n" + "      .style2 {\r\n"
				+ "	color: #99CCFF\r\n" + "}\r\n" + "      .style3 {font-size: 28px}\r\n" + "      </style>\r\n"
				+ "</head>\r\n" + "    \r\n" + "<body bgcolor=\"#333333\">\r\n"
				+ "    <p class=\"style1 style2 style3\">Eddie Murphy Showcasey-flix</p>\r\n"
				+ "    <p class=\"style1 style2\">Beverly Hills Cop</p>\r\n"
				+ "	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n"
				+ "  		<source src=\"vid?filename=bhc2&uuid=" + randomUUID + "\" type=\"video/mp4\">\r\n"
				+ "        Your browser does not support the video tag.\r\n" + "    </video>\r\n"
				+ "    <p class=\"style1 style2\">Eddie Murphy Delirious</p>\r\n"
				+ "	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n"
				+ "  		<source src=\"vid?filename=emd&uuid=" + randomUUID + "\" type=\"video/mp4\">\r\n"
				+ "        Your browser does not support the video tag.\r\n" + "    </video>\r\n"
				+ "    <p class=\"style1 style2\">The Nutty Professor</p>\r\n"
				+ "	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n"
				+ "  		<source src=\"vid?filename=tnp&uuid=" + randomUUID + "\" type=\"video/mp4\">\r\n"
				+ "        Your browser does not support the video tag.\r\n" + "    </video>\r\n"
				+ "    <p class=\"style1 style2\">The Golden Child</p>\r\n"
				+ "	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n"
				+ "  		<source src=\"vid?filename=tgc&uuid=" + randomUUID + "\" type=\"video/mp4\">\r\n"
				+ "        Your browser does not support the video tag.\r\n" + "    </video>\r\n"
				+ "    <p class=\"style1 style2\">Trading Places</p>\r\n"
				+ "	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n"
				+ "  		<source src=\"vid?filename=tp&uuid=" + randomUUID + "\" type=\"video/mp4\">\r\n"
				+ "        Your browser does not support the video tag.\r\n" + "    </video>\r\n"
				+ "    <p class=\"style1 style2\">48 Hours</p>\r\n"
				+ "	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n"
				+ "  		<source src=\"vid?filename=48hours&uuid=" + randomUUID + "\" type=\"video/mp4\">\r\n"
				+ "        Your browser does not support the video tag.\r\n" + "    </video>\r\n"
				+ "    <p class=\"style1 style2\">Eddie Murphy Raw</p>\r\n"
				+ "	<video id=\"vid\" width=\"640\" height=\"480\" preload=\"none\" controls>\r\n"
				+ "  		<source src=\"vid?filename=emr&uuid=" + randomUUID + "\" type=\"video/mp4\">\r\n"
				+ "        Your browser does not support the video tag.\r\n" + "    </video>\r\n" + "</body>\r\n"
				+ "</html>";
	}

	@RequestMapping(method = RequestMethod.GET, value = "vid")
	public void streamVideo(@RequestParam(name = "filename") String filename, HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		System.out.println("** Filename is: " + filename);
		setHeaders(filename, response, request);
		stream(filename, response);
	}

	@RequestMapping(method = RequestMethod.GET, produces = "text/plain", value = "/test")
	@ResponseBody
	public String test() {
		return "APP Running OK :)";
	}

	private void stream(String filename, HttpServletResponse response) throws IOException {
		byte[] data = videoStore.get(filename + ".mp4");

		long index = start;
		byte[] bytesToWrite = new byte[(int) buffer];
		System.out.println("*** Starting new stream from: " + index + " to: " + end);
		while (index < end) {
			long bytesToRead = buffer;
			if ((index + bytesToRead) > end)
				bytesToRead = end - index + 1;
			System.out.println("*** index = " + index + " bytesToRead = " + bytesToRead);
			// fis.read(bytesToWrite, 0, (int) bytesToRead);
			// from index to end...
			bytesToWrite = Arrays.copyOfRange(data, (int) index, (int) end);
			try {
				response.getOutputStream().write(bytesToWrite);
			} catch (IOException e) {
				System.out.println("*** Caught exception - cleaning up.");
				e.printStackTrace();
				break;
			}
			response.getOutputStream().flush();
			index += bytesToRead;
		}
		response.flushBuffer();
		// fis.close();
		System.out.println("*** Stream segment complete.");
	}

	private void setHeaders(String filename, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		response.addHeader("Content-Type", "video/mp4");
		response.addHeader("Cache-Control", "no-cache, public");

		if (filename.equals("tgc") || filename.equals("bhc2") || filename.equals("tnp"))
			response.setStatus(206);
		else
			response.setStatus(200);

		start = 0;
		size = videoStore.get(filename + ".mp4").length;
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
			response.addHeader("Content-Length", String.valueOf(length));
			response.addHeader("Content-Range", "bytes " + start + "-" + end + "/" + size);
			response.addHeader("Connection", "Keep-Alive");
			response.addHeader("Keep-Alive", "timeout=100, max=400");
		} else {
			response.addHeader("Content-Length", String.valueOf(size));
		}
	}
}
