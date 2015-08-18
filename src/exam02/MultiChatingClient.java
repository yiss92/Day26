package exam02;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;


public class MultiChatingClient extends JFrame{
	private TextArea textarea;
	private TextField textfield;
	private JScrollPane scrollpane;
	
	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;	

	public MultiChatingClient(){
		textarea = new TextArea();
		textfield = new TextField();
		scrollpane = new JScrollPane(textarea);
		
		textarea.setEditable(false);
		textarea.setBackground(Color.GREEN);
		
		textfield.addActionListener(new MyListenenr());
		
		add(textarea,BorderLayout.CENTER);
		add(textfield, BorderLayout.SOUTH);
		
		
		setSize(300, 300);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		startconnection();
		
	}
	
	public void startconnection(){
		
		try {
			socket = new Socket(InetAddress.getByName("203.236.209.86"), 5500);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			String nicName = JOptionPane.showInputDialog(this, "¥Î«œ∏Ì: ", JOptionPane.INFORMATION_MESSAGE);
			
			bw.write(nicName+"\n");
			bw.flush();
			
			new Listen().start();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class MyListenenr implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = textfield.getText();
			try {
				bw.write(msg+"\n");
				bw.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	
	private class Listen extends Thread{
		@Override
		public void run() {
			String msg;
			while(true){
				try {
					msg = br.readLine();
					textarea.append(msg + "\n");
					textfield.setText("");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}



	public static void main(String[] args) {
		new MultiChatingClient();

	}

}
