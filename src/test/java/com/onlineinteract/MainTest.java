package com.onlineinteract;

import java.io.File;

import org.junit.Test;

public class MainTest {
	
	@Test
	public void fileSizeTest() {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("bhc2.mp4").getFile());
		long size = file.length();
		System.out.println(size);
	}
	
}