package test02;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MultiChatingClient extends JFrame {
	private JTextArea chatArea = new JTextArea();
	private JTextField chatField = new JTextField();
	private JScrollPane scroll = new JScrollPane(chatArea); // 스크롤바

	private Socket socket; // 서버와 통신을 위한 socket
	private BufferedReader br; // 서버로부터 메시지 수신용
	private BufferedWriter bw; // 서버에게 메시지 송신용

	// GUI 초기화 작업 수행 및 네트워크 초기화 작업
	public MultiChatingClient() {
		chatArea.setEditable(false);
		chatArea.setBackground(Color.pink);

		chatField.addActionListener(new MyListener());
		
		add(scroll, BorderLayout.CENTER);
		add(chatField, BorderLayout.SOUTH);

		setSize(200, 300);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		startConnection();
		
	}

	// 현재 클랑이언트가 서버와의 연결을 구축하는 메소드
	public void startConnection() {
		try {
			socket = new Socket(InetAddress.getByName("203.236.209.86"), 5500); //81
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			// 서버와 통신 준비 완료

			// 닉네임 입력 다이얼로그 팝업 띄움.
			String nickname = JOptionPane.showInputDialog(this, "대화명을 입력하세요.", JOptionPane.INFORMATION_MESSAGE);

			bw.write(nickname + "\n");
			bw.flush();

			new Listen().start();

		} catch (UnknownHostException e) {
			System.out.println("서버 IP 또는 port 오류" + e);
		} catch (IOException e) {
			System.out.println("서버에 닉네임 전달 오류 " + e);
		}
	}

	/////
	//// chatField에서 Action이벤트 발생시 처리하는 내부 클래스
	private class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String sendMsg = chatField.getText();
				bw.write(sendMsg+"\n");
				bw.flush();
				chatField.setText("");
			} catch (IOException e1) {
				System.out.println("서버에게 메시지 송신 오류");
			}

		}

	}

	///////////////////////////////////////////////////
	// 서버가 보내는 메시지 수신 쓰레드 클래스
	private class Listen extends Thread {
		@Override
		public void run() {
			while (true) {
				String msg;
				try {
					msg = br.readLine();
					chatArea.append(msg + "\n");
					chatArea.setCaretPosition(chatArea.getText().length());
				} catch (IOException e) {
					System.out.println("서버로부터 메시지 수신 오류");
				}

			}
		}
	}

	public static void main(String[] args) {
		new MultiChatingClient();
	}
}
