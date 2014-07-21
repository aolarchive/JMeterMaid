package com.web_application;

import org.springframework.stereotype.Component;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Component
public class ZipManipulator {
	
	public static void compressZipFile(String sourceDir, String outputFile) throws IOException
	{
		ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
		compressDirectoryToZipfile(sourceDir, sourceDir, zipFile);
		IOUtils.closeQuietly(zipFile);
	}
	
	private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
	    for (File file : new File(sourceDir).listFiles()) {
	        if (file.isDirectory()) {
	            compressDirectoryToZipfile(rootDir, sourceDir + file.getName() + File.separator, out);
	        } else {
	            ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName());
	            out.putNextEntry(entry);

	            FileInputStream in = new FileInputStream(sourceDir + file.getName());
	            IOUtils.copy(in, out);
	            IOUtils.closeQuietly(in);
	        }
	    }
	}
//	public byte[] getZippedFile()
//	{
//		
//	}

}
