package exam02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiChatingServer{
	private ArrayList<ServerThread> list= new ArrayList<>();
	private ServerSocket serversocket;
	private Socket socket;
	
	public MultiChatingServer(){
		try {
			serversocket = new ServerSocket(5500);			
			
			while(true){
				socket = serversocket.accept();
				ServerThread thread = new ServerThread(this, socket);
				list.add(thread);
				thread.start();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void broadcast(String msg){
		for(ServerThread t: list){
			t.speak(msg);
		}
	}
	
	public void removeThread(ServerThread exitUser){
		list.remove(exitUser);
	}

	public static void main(String[] args) {
		new MultiChatingServer();

	}

}
