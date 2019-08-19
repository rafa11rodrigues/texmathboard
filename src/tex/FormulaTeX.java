package tex;

import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;


public class FormulaTeX {
	
	public static void createFormulaTeX(JLabel labelTeX, String texCode, int fontSize) {
		TeXFormula teXFormula = new TeXFormula(texCode);
		TeXIcon icon = teXFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSize);
		BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		icon.paintIcon(labelTeX, image.getGraphics(), 0, 0);
		labelTeX.setIcon(icon);
	}
}
