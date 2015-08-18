package test02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

//클라이언트가 하나 접속할 때 마다 서버가 객체 생성하고 관리 할때 사용되는 클래스
//사용자 하나당 관리하는 클래스
public class ServerThread extends Thread {
	private BufferedReader br;
	private BufferedWriter bw;
	// BufferedWriter bw;
	private String nickname; // 현재 쓰레드가 담당하는 사용자 이름
	private MultiChatingServer server; // 현재 쓰레드는 하나의 사용자만 알고 있음.
										// 전체 사용자 목록을 관리하는 서버 정보 유지

	public ServerThread(MultiChatingServer server, Socket socket) {
		this.server = server;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 현재 쓰레드는 기본적으로 담당하는 클라이언트의 메새지를 계속 수신함
	@Override
	public void run() {

		try {
			while (true) {
				nickname = br.readLine(); // 사용자가 접속하자마자 닉네임을 전송함.
				server.broadcast("[" + nickname + "]님 입장하셨습니다.");

				String msg = br.readLine();
				server.broadcast("[" + nickname + "]:" + msg);

			}
		} catch (IOException e) {
			server.removeThread(this);
			server.broadcast("[" + nickname + "]님 퇴장하셨습니다.");
		}
	}

	// 서버가 요청할 대 담당하는 클라이언트에게 메시지를 전송함.
	public void spaek(String msg) {
		try {
			bw.write(msg + "\n");
			bw.flush();
		} catch (IOException e) {
			System.out.println("해당 클라이언트 메시지 수신불가");
		}
	}

}
