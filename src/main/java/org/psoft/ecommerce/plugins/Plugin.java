package org.psoft.ecommerce.plugins;

import java.util.Map;

public interface Plugin {

	public String getName();

	/**
	 * A map of key=value settings.  
	 * 
	 * @return
	 */
	public Map<String, Object> getSettings();

	public void saveSetting(String key, String value);
	
	public void processFiles(FileUpload file1, FileUpload file2, FileUpload file3, FileUpload file4);
	
	
	public static class FileUpload {
		
		byte[] data;
		
		String filename;
		
		String contentType;
		
		int size;

		public byte[] getData() {
			return data;
		}

		public void setData(byte[] data) {
			this.data = data;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public int getSize() {
			return size;
		}

		public void setSize(int size) {
			this.size = size;
		}
		
	}

}
