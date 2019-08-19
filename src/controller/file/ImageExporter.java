package controller.file;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import view.Screen;

public class ImageExporter {
	public static JFileChooser fc = new JFileChooser();
	
	private static void createFile(Screen screen, BufferedImage image, String format) {
		int ret = fc.showSaveDialog(screen);
		if (ret == JFileChooser.APPROVE_OPTION) {
			try {
				File f = fc.getSelectedFile();
				if (f.exists()) {
					int answer = JOptionPane.showConfirmDialog(screen, "File already exists. Would you like to overwrite it?", "", JOptionPane.YES_NO_OPTION);
					if (answer != JOptionPane.YES_OPTION) {
						createFile(screen, image, format);
						return;
					}
				}
				ImageIO.write(image, format, f);
				
				String extension = getExtension(f);
				if (extension == null || !format.equals(extension)) {
					String filename = f.getName();
					filename += "." + format;
					Path source = f.toPath();
					try {
					     Files.move(source, source.resolveSibling(filename));
					} catch (FileAlreadyExistsException e) {
					     //e.printStackTrace();
						int answer = JOptionPane.showConfirmDialog(screen, "File already exists. Would you like to overwrite it?", "", JOptionPane.YES_NO_OPTION);
						if (answer != JOptionPane.YES_OPTION) {
							createFile(screen, image, format);
						}
						else {
							Files.delete(source.resolveSibling(filename));
							Files.move(source, source.resolveSibling(filename));
						}
					}
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(screen, "File could not be saved", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public static void saveImage(Screen screen, JLabel result, String format) {
		if (result == null) {
			JOptionPane.showMessageDialog(screen, "Nothing to save", "Ooops", JOptionPane.WARNING_MESSAGE);
			return;
		}
		BufferedImage image = extractImage(result);
		
		ImageFilter filter = new ImageFilter(format);
		fc.addChoosableFileFilter(filter);
		fc.setAcceptAllFileFilterUsed(false);
		
		createFile(screen, image, format);
		fc.removeChoosableFileFilter(filter);
	}
	
	public static void copyImage(JLabel result) {
		if (result != null) {
			Image image = extractImage(result);
			TransferableImage transfer = new TransferableImage(image);
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			cb.setContents(transfer, null);
		}
	}
	
	public static BufferedImage extractImage(JLabel result) {
		Icon tex = result.getIcon();
		int w = tex.getIconWidth();
		int h = tex.getIconHeight();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		g.fillRect(0, 0, w, h);
		tex.paintIcon(null, g, 0, 0);
		return image;
	}
	
	public static String getExtension(File f) {
		String extension = null;
		String filename = f.getName();
		
		int i = filename.lastIndexOf('.');
		if (i > 0 && i < filename.length() - 1) {
			extension = filename.substring(i + 1).toLowerCase();
		}
		return extension;
	}
	
    private static class TransferableImage implements Transferable {
    	private Image image;

        public TransferableImage(Image image) {
            this.image = image;
        }

        public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
            if (isDataFlavorSupported(flavor)) {
                return image;
            }
            else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }
    }
}
