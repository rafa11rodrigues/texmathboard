package view;

import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class EditorArea extends JTextArea {
	private JScrollPane scrollPane;
	
	public EditorArea() {
		scrollPane = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		scrollPane.setBorder(new LineBorder(null, 3));
		
		setFont(new Font("Monospaced", Font.PLAIN, 18));
		
	}
	
	JScrollPane getScrollPane() {
		return this.scrollPane;
	}
}
