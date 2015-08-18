package test01;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GuiChatingServer extends JFrame {
	//GUI ������ �ۼ� ���ú���
	private JPanel panel_center;
	private JPanel panel_south;
	private JTextArea textArea;
	private JTextField textField;
	private JButton btn_send;
	//TCP ��� ���� ����
	private BufferedReader br;
    private BufferedWriter bw;
	
    //������
	public GuiChatingServer() throws IOException{
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
		
		////////////////////////////////////
		//TCP ���� ���� �õ�
		ServerSocket serverSocket = new ServerSocket(5500);
		textArea.append("������ ��ٸ�����");
		Socket socket = serverSocket.accept();
		textArea.append("Ŭ���̾�Ʈ�� ������!\n");
		
		bw = new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream()));
		br = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		
		new Listen().start();
		
	}	
	
	public static void main(String[] args) throws IOException {
		new GuiChatingServer();
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
				System.out.println("Ŭ���̾�Ʈ���� ���� �ҷ�: " +e);
			}
		}		
	}	
	
	class Listen extends Thread{
		@Override
		public void run() {
			while(true){
				try {
					String msg = br.readLine();
					textArea.append("����: " +msg+"\n");
				} catch (Exception e) {
					System.out.println("���� ���� ������. ���� ����: " + e);
					return;
				}
			}
		}
	}

}
