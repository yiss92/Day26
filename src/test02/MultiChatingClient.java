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
	private JScrollPane scroll = new JScrollPane(chatArea); // ��ũ�ѹ�

	private Socket socket; // ������ ����� ���� socket
	private BufferedReader br; // �����κ��� �޽��� ���ſ�
	private BufferedWriter bw; // �������� �޽��� �۽ſ�

	// GUI �ʱ�ȭ �۾� ���� �� ��Ʈ��ũ �ʱ�ȭ �۾�
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

	// ���� Ŭ���̾�Ʈ�� �������� ������ �����ϴ� �޼ҵ�
	public void startConnection() {
		try {
			socket = new Socket(InetAddress.getByName("203.236.209.86"), 5500); //81
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			// ������ ��� �غ� �Ϸ�

			// �г��� �Է� ���̾�α� �˾� ���.
			String nickname = JOptionPane.showInputDialog(this, "��ȭ���� �Է��ϼ���.", JOptionPane.INFORMATION_MESSAGE);

			bw.write(nickname + "\n");
			bw.flush();

			new Listen().start();

		} catch (UnknownHostException e) {
			System.out.println("���� IP �Ǵ� port ����" + e);
		} catch (IOException e) {
			System.out.println("������ �г��� ���� ���� " + e);
		}
	}

	/////
	//// chatField���� Action�̺�Ʈ �߻��� ó���ϴ� ���� Ŭ����
	private class MyListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String sendMsg = chatField.getText();
				bw.write(sendMsg+"\n");
				bw.flush();
				chatField.setText("");
			} catch (IOException e1) {
				System.out.println("�������� �޽��� �۽� ����");
			}

		}

	}

	///////////////////////////////////////////////////
	// ������ ������ �޽��� ���� ������ Ŭ����
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
					System.out.println("�����κ��� �޽��� ���� ����");
				}

			}
		}
	}

	public static void main(String[] args) {
		new MultiChatingClient();
	}
}
