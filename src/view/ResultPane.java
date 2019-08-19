package view;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import tex.FormulaTeX;

public class ResultPane extends JScrollPane {
	private JLabel result;
	private int fontSize;
	private String texColor;
	private String texCode;
	private String commands;
	
	public ResultPane() {
		setBorder(new LineBorder(null, 3));
		getVerticalScrollBar().setUnitIncrement(10);
		getHorizontalScrollBar().setUnitIncrement(10);
		
		result = new JLabel();
		result.setVerticalAlignment(SwingConstants.TOP);
		result.setHorizontalAlignment(SwingConstants.LEFT);
		getViewport().setView(result);
		
		this.fontSize = 30;
		setColor(Color.black);
		this.texCode = "";
		this.commands = "\\newcommand{\\R}{\\mathbb{R}}\n"
					  + "\\newcommand{\\I}{\\mathbb{I}}\n"
					  + "\\newcommand{\\Q}{\\mathbb{Q}}\n"
					  + "\\newcommand{\\Z}{\\mathbb{Z}}\n"
					  + "\\newcommand{\\N}{\\mathbb{N}}\n"
					  + "\\newcommand{\\C}{\\mathbb{C}}\n";
	}
	
	public JLabel getResult() {
		return result;
	}
	
	public void updateResult() {
		FormulaTeX.createFormulaTeX(result, commands + texColor + texCode, fontSize);
	}
	
	public void setFontSize(int size) {
		fontSize = size;
	}
	
	public void setColor(Color color) {
		double r = (color.getRed() / 255.0);
		double g = (color.getGreen() / 255.0);
		double b = (color.getBlue() / 255.0);
		
		texColor = "\\definecolor{mycolor}{rgb}{" + r + "," + g + "," + b + "}\n";
		texColor += "\\textcolor{mycolor}";
	}
	
	public void setCode(String code) {
		texCode = "{" + code + "}";
	}
}
