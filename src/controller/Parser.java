package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;

import controller.file.ImageExporter;
import view.EditorArea;
import view.ResultPane;
import view.Screen;

public class Parser {
	private Screen screen;
	private EditorArea code;
	private ResultPane result;
	private JSpinner fontSize;
	private JButton colorChooser;
	
	public Parser(Screen screen) {
		this.screen = screen;
		this.code = screen.getCode();
		this.result = screen.getResult();
		this.fontSize = screen.getFontSize();
		this.colorChooser = screen.getColorChooser();
		
		addListeners();
		addKeyStrokes();
		addPopupMenu();
	}
	
	private void addListeners() {
		code.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent evt) {
				result.setCode(code.getText());
				result.updateResult();
			}
			
			@Override
			public void keyPressed(KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_DELETE) {
					code.replaceSelection("");
					return;
				}
				
				char c = evt.getKeyChar();
				int pos = code.getCaretPosition();
				
				if(c == '(') c = ')';
				else if(c == '[') c = ']';
				else if(c == '{') c = '}';
				else {
					try {
						boolean eq = code.getText(pos, 1).equals("" + c);
						if(eq && (c == ')' || c == ']' || c == '}')) {
							//code.setCaretPosition(pos + 1);
							code.select(pos, pos+1);
						}
					} catch (BadLocationException e) {
						// nothing
					}
					return;
				}
				
				code.replaceSelection("" + c);
				code.setCaretPosition(code.getCaretPosition() - 1);
			}
		});
		
		fontSize.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				int size = ((Integer) fontSize.getValue()).intValue();
				result.setFontSize(size);
				result.updateResult();
			}
		});
		
		colorChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Color newColor =JColorChooser.showDialog(screen, "Choose a color", colorChooser.getBackground());
				if(newColor != null) {
					result.setColor(newColor);
					result.updateResult();
					colorChooser.setBackground(newColor);
				}
			}
		});
	}
	
	private void addPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem copy = new JMenuItem("Copy to clipboard");
		JMenuItem save = new JMenuItem("Save as PNG");
		popup.add(copy);
		popup.add(save);
		
		result.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				if(SwingUtilities.isRightMouseButton(evt)) {
					popup.show(evt.getComponent(), evt.getX(), evt.getY());
				}
			}
		});
		
		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				ImageExporter.copyImage(result.getResult());
			}
		});
		
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				ImageExporter.saveImage(screen, result.getResult(), "png");
			}
		});
	}
	
	private void addKeyStrokes() {
		result.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl shift C"), "copy");
		result.getActionMap().put("copy", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				ImageExporter.copyImage(result.getResult());
			}
		});
		result.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ctrl shift S"), "save");
		result.getActionMap().put("save", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				ImageExporter.saveImage(screen, result.getResult(), "png");
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl M"), "pmatrix");
		code.getActionMap().put("pmatrix", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertEnvironment("pmatrix");
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl shift M"), "vmatrix");
		code.getActionMap().put("vmatrix", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertEnvironment("vmatrix");
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl E"), "array");
		code.getActionMap().put("array", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertEnvironment("array");
				code.insert("{ll}", code.getCaretPosition() - 1);
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl P"), "()");
		code.getActionMap().put("()", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertDelimiter("(", ")");
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl shift P"), "(");
		code.getActionMap().put("(", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertDelimiter("(", ".");
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl B"), "{}");
		code.getActionMap().put("{}", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertDelimiter("\\{", "\\}");
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl shift B"), "{");
		code.getActionMap().put("{", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertDelimiter("\\{", ".");
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl F"), "\\frac");
		code.getActionMap().put("\\frac", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertCommand("\\frac", 2);
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl N"), "\\binom");
		code.getActionMap().put("\\binom", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertCommand("\\binom", 2);
			}
		});
		
		code.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ctrl S"), "\\sqrt");
		code.getActionMap().put("\\sqrt", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				insertCommand("\\sqrt", 1);
			}
		});
	}
	
	private void insertEnvironment(String env) {
		String begin = "\\begin{" + env + "}\n\n";
		String end = "\\end{" + env + "}";
		try {
			code.replaceSelection(begin + end);
			int line = code.getLineOfOffset(code.getCaretPosition());
			code.setCaretPosition(code.getLineStartOffset(line - 1));
		} catch (BadLocationException e) {
			// nothing
		}
	}
	
	private void insertCommand(String cmd, int args) {
		for(int i = 0; i < args; i++) cmd += "{}";
		code.replaceSelection(cmd);
		code.setCaretPosition(code.getCaretPosition() - 2*args + 1);
	}
	
	private void insertDelimiter(String l, String r) {
		String left = "\\left" + l + " ";
		String right = " \\right" + r;
		
		code.replaceSelection(left + right);
		int pos = code.getCaretPosition();
		code.setCaretPosition(pos - right.length());
	}
}
