package test01;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GuiChatingClient extends JFrame {
	//GUI 프레임 작성 관련변수
		private JPanel panel_center;
		private JPanel panel_south;
		private JTextArea textArea;
		private JTextField textField;
		private JButton btn_send;
		//TCP 통신 관련 변수
		private BufferedReader br;
	    private BufferedWriter bw;
		
	    //생성자
		public GuiChatingClient() throws IOException{
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
						
			Socket socket = new Socket(InetAddress.getByName("203.236.209.86"),5500);
			
			bw = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			
			new Listen().start();
			
		}	
		
		public static void main(String[] args) throws IOException {
			new GuiChatingClient();
		}
		
		class MyListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = textField.getText();
				textArea.append("ME :" + text + "\n");
				textField.setText("");
				
				try {
					bw.write(text+"\n");
					bw.flush();
				} catch (Exception e1) {
					System.out.println("서버 접속불량: " +e);
				}
			}		
		}	
		
		class Listen extends Thread{
			@Override
			public void run() {
				while(true){
					try {
						String msg = br.readLine();
						textArea.append("상대방: " +msg+"\n");
					} catch (Exception e) {
						System.out.println("서버 연결 끊어짐. 나도 종료: " + e);
						return;
					}
				}
			}
		}

}
