package org.psoft.ecommerce.util;

import java.io.File;

public class FileUtils extends org.apache.commons.io.FileUtils {
	
	public static File initDir(String path, boolean write){

		File directory = new File(path);

		if (!directory.exists()) {
			try {
				directory.mkdir();
			} catch (Exception e) {
				new RuntimeException("Unable able to create directory, check permissions: " + path,e);
			}
		}

		if (!directory.isDirectory())
			throw new RuntimeException(path + " is not a directory");

		if (!directory.canRead()){
			throw new RuntimeException(path + " is not readable");
		}
		
		if (write && !directory.canWrite()){
			throw new RuntimeException(path + " is not writable");
		}

		return directory;
	}

}
