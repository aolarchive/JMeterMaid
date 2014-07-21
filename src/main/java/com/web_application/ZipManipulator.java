package com.web_application;

import org.springframework.stereotype.Component;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Component
public class ZipManipulator {
	
	public static byte[] compressZipFile(String sourceDir) throws IOException
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ZipOutputStream zipFile = new ZipOutputStream(stream);
		
		compressDirectoryToZipfile(sourceDir, zipFile);
		
		byte[] byteArray = stream.toByteArray();
		
		
		writeByteToFile(byteArray);

		
		IOUtils.closeQuietly(zipFile);
		
		
		return byteArray;
	}
	
	private static void compressDirectoryToZipfile( String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
		

	    for (File file : new File(sourceDir).listFiles()) {
	        if (!file.isDirectory()) {
	            ZipEntry entry = new ZipEntry(file.getName());
	            out.putNextEntry(entry);

	            FileInputStream in = new FileInputStream(sourceDir + file.getName());
	            IOUtils.copy(in, out);
	            IOUtils.closeQuietly(in);
	        }
	    }
	}
	
	public static void writeByteToFile(byte[] bytes) throws IOException
	{
		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(bytes));
		
		ZipEntry entry = null;
		try
		{
		while ((entry = zipStream.getNextEntry()) != null) {

		    String entryName = entry.getName();
		  
		
		
		    final File parent = new File("/Users/dansbacher14/Documents/workspace/obi_jmx_test_webapp/zip/");
		    if (!parent.mkdirs())
		    {
		       System.err.println("Could not create parent directories ");
		    }
		    
		    FileOutputStream out = new FileOutputStream("/Users/dansbacher14/Documents/workspace/obi_jmx_test_webapp/zip/" + entryName);

		    byte[] byteBuff = new byte[4096];
		    int bytesRead = 0;
		    while ((bytesRead = zipStream.read(byteBuff)) != -1)
		    {
		        out.write(byteBuff, 0, bytesRead);
		    }

		    out.close();
		    zipStream.closeEntry();
		}
		}
		catch (EOFException e)
		{
			
		}
		zipStream.close(); 
	}

	
}
