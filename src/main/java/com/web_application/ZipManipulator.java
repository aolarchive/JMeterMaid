package com.web_application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class ZipManipulator {

	public static byte[] compressZipFile(String sourceDir) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ZipOutputStream zipFile = new ZipOutputStream(stream);

		compressDirectoryToZipfile(sourceDir, zipFile);

		byte[] byteArray = stream.toByteArray();

		IOUtils.closeQuietly(zipFile);
		FileUtils.deleteDirectory(new File(sourceDir));

		return byteArray;
	}

	private static void compressDirectoryToZipfile(String sourceDir,
			ZipOutputStream out) throws IOException, FileNotFoundException {

		for (File file : new File(sourceDir).listFiles()) {
			if (!file.isDirectory()) {
				ZipEntry entry = new ZipEntry(file.getName());
				out.putNextEntry(entry);

				FileInputStream in = new FileInputStream(sourceDir
						+ file.getName());
				IOUtils.copy(in, out);
				IOUtils.closeQuietly(in);
			} else {
				compressDirectoryToZipfile(sourceDir + file.getName() + "/",
						out);
			}
		}
	}

	public static byte[] writeByteToFile(byte[] bytes) throws IOException {
		ZipInputStream zipStream = new ZipInputStream(new ByteArrayInputStream(
				bytes));
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		ZipEntry entry = null;
		try {
			while ((entry = zipStream.getNextEntry()) != null) {

				String entryName = entry.getName();
				System.out.println(entryName);
				if (entryName.equals("summary.html")) {

					IOUtils.copy(zipStream, stream);

				}
				zipStream.closeEntry();
			}
		} catch (EOFException e) {

		}
		zipStream.close();
		return stream.toByteArray();
	}

}
