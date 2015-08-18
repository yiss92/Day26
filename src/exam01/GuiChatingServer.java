package exam01;

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
import java.util.concurrent.ThreadFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GuiChatingServer extends JFrame {
	private JPanel panel_center;
	private JPanel panel_south;
	private JTextArea textArea;
	private JTextField textFild;
	private JButton jtn;

	private Socket socket;
	private BufferedReader br;
	private BufferedWriter bw;

	public GuiChatingServer() throws IOException {
		textArea = new JTextArea(10, 10);
		textFild = new JTextField(10);
		jtn = new JButton("SEND");
		// setLayout(new FlowLayout());
		panel_center = new JPanel();
		panel_south = new JPanel();

		panel_center.setLayout(new BorderLayout());
		panel_center.add(textArea);

		panel_south.setLayout(new BorderLayout());
		panel_south.add(textFild);
		panel_south.add(jtn, BorderLayout.EAST);

		add(panel_center, BorderLayout.CENTER);
		add(panel_south, BorderLayout.SOUTH);

		textArea.setEditable(false);
		jtn.addActionListener(new MyListener());
		textFild.addActionListener(new MyListener());

		setSize(300, 300);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		ServerSocket serverSocket = new ServerSocket(5500);
		textArea.append("서버 대기중");
		socket = serverSocket.accept();
		textArea.append("클라이언트 접속\n");

		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		new Listen().start();
	}

	public static void main(String[] args) throws IOException {
		new GuiChatingServer();

	}

	class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String text = textFild.getText();
			textArea.append("ME : " + text + "\n");
			textFild.setText("");

			try {
				bw.write(text + "\n");
				bw.flush();
			} catch (Exception e1) {
				System.out.println("연결종료" + e);
			}

		}

	}

	class Listen extends Thread {
		@Override
		public void run() {
			while (true) {
				try {
					String msg = br.readLine();
					textArea.append("상대방:" + msg + "\n");
				} catch (Exception e) {
					System.out.println("연결 끊어짐" + e);
					return;
				}

			}
		}

	}

}
