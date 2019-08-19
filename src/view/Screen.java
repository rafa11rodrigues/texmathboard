package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controller.Parser;

public class Screen extends JFrame {

	private JPanel contentPane;
	private EditorArea code;
	private ResultPane result;
	private JSpinner fontSize;
	private JButton colorChooser;

	public Screen() {
		setTitle("TeXMathBoard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1024, 728));
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{1024, 0};
		gbl_contentPane.rowHeights = new int[]{34, 350, 34, 350, 0};
		contentPane.setLayout(gbl_contentPane);
		setContentPane(contentPane);
		
		initCodeEditor();
		initResultPane();
	}
	
	private void initCodeEditor() {
		JPanel p1 = new JPanel();
		p1.setLayout(null);;		
		GridBagConstraints gbc_p1 = new GridBagConstraints();
		gbc_p1.fill = GridBagConstraints.BOTH;
		gbc_p1.gridwidth = GridBagConstraints.REMAINDER;
		gbc_p1.weightx = 1.0;
		gbc_p1.gridx = 0;
		gbc_p1.gridy = 0;
		contentPane.add(p1, gbc_p1);
		
		JLabel lblCode = new JLabel("Code:");
		lblCode.setHorizontalAlignment(SwingConstants.LEFT);
		lblCode.setBounds(5, 5, 33, 20);
		Font normalCodeFont = lblCode.getFont();
		lblCode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent evt) {
				Font font = lblCode.getFont();
				Map<TextAttribute,Object> attributes = new HashMap<>(font.getAttributes());
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				lblCode.setFont(font.deriveFont(attributes));
			}
			@Override
			public void mouseExited(MouseEvent evt) {
				lblCode.setFont(normalCodeFont);
			}
			@Override
			public void mouseClicked(MouseEvent evt) {
				String text = "All LaTeX commands inserted here are within a math environment, so it's not necessary to use $.\n"
							+ "It's a limited LaTeX parser, thus some commands may not work properly.\n"
							+ "You can use \"\\newcommand\" to define macros.\n"
							+ "The numerical sets can be inserted using \\N, \\Z, \\Q, \\I, \\R and \\C.\n"
							+ "Blank lines do not break the line. Use \"\\\\\" instead.\n\n"
							+ ""
							+ "Some key strokes:\n"
							+ "    Ctrl+M\t    pmatrix\n"
							+ "    Ctrl+Shift+M\t    vmatrix\n"
							+ "    Ctrl+E\t    array\n"
							+ "    Ctrl+P\t    \\left( ... \\right)\n"
							+ "    Ctrl+Shift+P\t    \\left( ... \\right.\n"
							+ "    Ctrl+B\t    \\left\\{ ... \\right\\}\n"
							+ "    Ctrl+Shift+B\t    \\left\\{ ... \\right.\n"
							+ "    Ctrl+F\t    \\frac{}{}\n"
							+ "    Ctrl+S\t    \\sqrt{}\n"
							+ "    Ctrl+N\t    \\binom{}{}";
				ToolTipDialog d = new ToolTipDialog(Screen.this, text, SwingUtilities.convertPoint(lblCode, evt.getPoint(), Screen.this));
				d.setSize(300, 400);
				d.setVisible(true);
			}
		});
		lblCode.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblCode.setToolTipText("Click for more information.");
		p1.add(lblCode);
		
		code = new EditorArea();
		GridBagConstraints gbc_p2 = new GridBagConstraints();
		gbc_p2.fill = GridBagConstraints.BOTH;
		gbc_p2.gridwidth = GridBagConstraints.REMAINDER;
		gbc_p2.weightx = 1.0;
		gbc_p2.gridx = 0;
		gbc_p2.gridy = 1;
		contentPane.add(code.getScrollPane(), gbc_p2);
		
		lblCode.setLabelFor(code);
	}
	
	private void initResultPane() {
		JPanel p3 = new JPanel();
		p3.setLayout(null);
		GridBagConstraints gbc_p3 = new GridBagConstraints();
		gbc_p3.fill = GridBagConstraints.BOTH;
		gbc_p3.gridwidth = GridBagConstraints.REMAINDER;
		gbc_p3.weightx = 1.0;
		gbc_p3.gridx = 0;
		gbc_p3.gridy = 2;
		contentPane.add(p3, gbc_p3);
		
		JLabel lblResult = new JLabel("Result:");
		lblResult.setHorizontalAlignment(SwingConstants.LEFT);
		lblResult.setBounds(5, 5, 40, 20);
		Font normalResultFont = lblResult.getFont();
		lblResult.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent evt) {
				Font font = lblResult.getFont();
				Map<TextAttribute,Object> attributes = new HashMap<>(font.getAttributes());
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				lblResult.setFont(font.deriveFont(attributes));
			}
			@Override
			public void mouseExited(MouseEvent evt) {
				lblResult.setFont(normalResultFont);
			}
			@Override
			public void mouseClicked(MouseEvent evt) {
				String text = "The result of code parsing will be shown here with the selected font size and color.\n"
						+ "The color is applied with \"\\textcolor\" command.\n"
						+ "You can right click on the result to open a popup menu to copy or save it.\n\n"
						+ ""
						+ "Some key strokes:\n"
						+ "    Ctrl+Shift+C\t    Copy result to clipboard as an image\n"
						+ "    Ctrl+Shift+S\t    Save result as PNG";
				ToolTipDialog d = new ToolTipDialog(Screen.this, text, SwingUtilities.convertPoint(lblResult, evt.getPoint(), Screen.this));
				d.setSize(350, 220);
				d.setVisible(true);
			}
		});
		lblResult.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblResult.setToolTipText("Click for more information");
		p3.add(lblResult);
		
		fontSize = new JSpinner(new SpinnerNumberModel(30, 5, Integer.MAX_VALUE, 1));
		fontSize.setBounds(600, 5, 50, 25);
		p3.add(fontSize);
		
		colorChooser = new JButton();
		colorChooser.setBounds(655, 7, 20, 20);
		colorChooser.setBackground(Color.black);
		p3.add(colorChooser);
		
		result = new ResultPane();
		GridBagConstraints gbc_p4 = new GridBagConstraints();
		gbc_p4.fill = GridBagConstraints.BOTH;
		gbc_p4.gridwidth = GridBagConstraints.REMAINDER;
		gbc_p4.weightx = 1.0;
		gbc_p4.weighty = 1.0;
		gbc_p4.gridx = 0;
		gbc_p4.gridy = 3;
		contentPane.add(result, gbc_p4);
		
		new Parser(this);
	}

	public EditorArea getCode() {
		return code;
	}

	public ResultPane getResult() {
		return result;
	}

	public JSpinner getFontSize() {
		return fontSize;
	}

	public JButton getColorChooser() {
		return colorChooser;
	}
}
