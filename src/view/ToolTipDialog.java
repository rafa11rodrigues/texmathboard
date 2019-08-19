package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ToolTipDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public ToolTipDialog(Screen screen, String text, Point location) {
		super(screen, true);
		setTitle("Information");
		setLocationRelativeTo(screen);
		setLocation(location);
		setResizable(false);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent evt) {
						ToolTipDialog.this.dispose();
					}
				});
			}
		}
		
		JTextArea info = new JTextArea(text);
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		JScrollPane scroll = new JScrollPane(info);
		getContentPane().add(scroll, BorderLayout.CENTER);
	}
}
