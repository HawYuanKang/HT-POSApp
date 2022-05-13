package Server_Package;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

//Author of this class : ARIEF MUIZZUDDIN BIN KHALID , B032110508

public class Server_Interface {

	public static void main(String[] args) throws IOException 
	{
		// This is server class that will record all the operation of counter.
		
		// Set socket to receive message from client connection.
		Socket socket= new Socket ("localhost",9999);
		
		// Set inputstream to receive data from client side
		DataInputStream InStream=new DataInputStream(socket.getInputStream());
		
		// Create a variable to store message of client.
		String message = "";

		// waiting for clinet to send outputstream and print the message.
		while(true)
		{
			// Read message and print in console.
			message= InStream.readUTF();
			System.out.println(message);
		}
	}
}
