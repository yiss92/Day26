package exam01;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import exam01.GuiChatingServer.MyListener;

public class GuiChatingClinet extends JFrame {
	private JPanel panel_center;
	private JPanel panel_south;
	private JTextArea textArea;
	private JTextField textFild;
	private JButton jtn;

	private BufferedReader br;
	private BufferedWriter bw;

	public GuiChatingClinet() throws IOException {
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

		Socket socket = new Socket(InetAddress.getByName("203.236.209.86"), 5500);

		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		new Listen().start();
	}

	public static void main(String[] args) throws IOException {
		new GuiChatingClinet();

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
				System.out.println("서버 에러 :" + e);
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
					System.out.println("서버 에러" + e);
					return;
				}

			}
		}

	}

}
