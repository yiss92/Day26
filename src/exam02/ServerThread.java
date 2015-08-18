package exam02;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private BufferedWriter bw;
	private BufferedReader br;
	
	private String nicName;
	private MultiChatingServer server;
	
	public ServerThread(MultiChatingServer sever, Socket socket) {
		this.server = sever;
		
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		try {
			while(true){
			nicName = br.readLine();
			server.broadcast("[" + nicName + "]¥‘ ¿‘¿Â«œºÃΩ¿¥œ¥Ÿ.");
			
			String msg = br.readLine();
			server.broadcast("[" + nicName + "]:" + msg);
			}
		} catch (IOException e) {
			server.removeThread(this);
			server.broadcast("[" + nicName + "]¥‘ ≈¿Â«œºÃΩ¿¥œ¥Ÿ.");
		}
		
	}
	
	public void speak(String msg){
		try {
			bw.write(msg+"\n");
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	

}
