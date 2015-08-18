package test02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//Ŭ���̾�Ʈ�� �ϳ� ������ �� ���� ������ ��ü �����ϰ� ���� �Ҷ� ���Ǵ� Ŭ����
//����� �ϳ��� �����ϴ� Ŭ����
public class ServerThread extends Thread {
	private BufferedReader br;
	private BufferedWriter bw;
	// BufferedWriter bw;
	private String nickname; // ���� �����尡 ����ϴ� ����� �̸�
	private MultiChatingServer server; // ���� ������� �ϳ��� ����ڸ� �˰� ����.
										// ��ü ����� ����� �����ϴ� ���� ���� ����

	public ServerThread(MultiChatingServer server, Socket socket) {
		this.server = server;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ���� ������� �⺻������ ����ϴ� Ŭ���̾�Ʈ�� �޻����� ��� ������
	@Override
	public void run() {

		try {
			while (true) {
				nickname = br.readLine(); // ����ڰ� �������ڸ��� �г����� ������.
				server.broadcast("[" + nickname + "]�� �����ϼ̽��ϴ�.");

				String msg = br.readLine();
				server.broadcast("[" + nickname + "]:" + msg);

			}
		} catch (IOException e) {
			server.removeThread(this);
			server.broadcast("[" + nickname + "]�� �����ϼ̽��ϴ�.");
		}
	}

	// ������ ��û�� �� ����ϴ� Ŭ���̾�Ʈ���� �޽����� ������.
	public void spaek(String msg) {
		try {
			bw.write(msg + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("�ش� Ŭ���̾�Ʈ �޽��� ���źҰ�");
		}
	}

}
