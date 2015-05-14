package br.com.chat;

import java.io.PrintWriter;
import java.net.Socket;

import br.com.socket.Conexao;

public class Cliente {

	private Socket socketCliente;
	private String curIp;
	private int curPort;

	public Cliente(String ip, int port) throws Exception {

		curIp = ip;
		curPort = port;
		Conexao.rede = true;

	}

	public void enviar(String str) throws Exception {
		socketCliente = new Socket(curIp, curPort);
		PrintWriter escritor = new PrintWriter(socketCliente.getOutputStream());
		escritor.write(str);
		escritor.close();
	}

}
