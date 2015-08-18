package test01;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyFrame extends JFrame{
	private JPanel panel_center;
	private JPanel panel_south;
	private JTextArea textArea;
	private JTextField textField;
	private JButton btn_send;
	
	public MyFrame(){
		panel_center = new JPanel();
		panel_south = new JPanel();
		textArea = new JTextArea(); //10,30);
		textField = new JTextField();//30);
		btn_send = new JButton("SEND");
		
		btn_send.addActionListener(new MyListener());
		textField.addActionListener(new MyListener());
		
		textArea.setEditable(false);
		
		panel_center.setLayout(new BorderLayout()); 
		panel_center.add(new JScrollPane(textArea));
		
		panel_south.setLayout(new BorderLayout());
		panel_south.add(textField);
		panel_south.add(btn_send, BorderLayout.EAST);
		
		add(panel_center,BorderLayout.CENTER);
		add(panel_south,BorderLayout.SOUTH);
		
		setSize(300,400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}	
	
	public static void main(String[] args) {
		new MyFrame();
	}
	
	class MyListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String text = textField.getText();
			textArea.append("ME :" + text + "\n");
			textField.setText("");			
		}		
	}	
}
