package controller.file;

import java.io.File;

import javax.swing.filechooser.FileFilter;





public class ImageFilter extends FileFilter {
	private String format;
	
	public ImageFilter(String format) {
		super();
		this.format = format;
	}
	
	public String getDescription() {
		return "*." + format;
	}
	
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		
		String extension = ImageExporter.getExtension(f);
		if (extension != null) {
			return extension.equals(format);
		}
		return false;
	}
}
