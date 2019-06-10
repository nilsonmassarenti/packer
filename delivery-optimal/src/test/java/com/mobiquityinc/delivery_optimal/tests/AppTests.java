package com.mobiquityinc.delivery_optimal.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.mobiquityinc.delivery_optimal.model.EnumException;
import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.packer.Packer;

class AppTests {

	@Test
	void testMethod01() throws APIException {
		StringBuffer sb = new StringBuffer();
		sb.append("4\n");
		sb.append("-\n");
		sb.append("2,7\n");
		sb.append("8,9");
		File file = new File(getClass().getClassLoader().getResource("mobiquityinc_data").getFile());
		String result = Packer.pack(file.getAbsolutePath());
		assertEquals(sb.toString(), result);
	}

	@Test
	void testMethod02() throws APIException {
		File file = new File(getClass().getClassLoader().getResource("mobiquityinc_data").getFile());
		try {
			Packer.pack(file.getAbsolutePath() + "a");
		} catch (Exception e) {
			assertEquals(EnumException.FILE_NOT_FOUND.getMessage(), e.getMessage());
		} 
		
	}
	
	@Test
	void testMethod03() throws APIException {
		File file = new File(getClass().getClassLoader().getResource("mobiquityinc_data_empty").getFile());
		try {
			Packer.pack(file.getAbsolutePath());
		} catch (Exception e) {
			assertEquals(EnumException.FILE_EMPTY.getMessage(), e.getMessage());
		} 
	}

}
