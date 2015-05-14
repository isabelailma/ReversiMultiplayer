package br.com.socket;

import br.com.chat.Cliente;
import br.com.chat.Servidor;
import br.com.jogo.Jogador;
import br.com.jogo.JogoReversi;
import br.com.view.IObserver;

public class Conexao {

	public static Servidor servidor;
	public static Cliente cliente;
	public static  boolean rede = false;
	public static  Jogador amarelo;
	public static  Jogador preto;
	public static  JogoReversi game;
	
	
	

	public static void setConexao(String ip, IObserver o) {
		iniciaServidor(5000, o);
		try {
			iniciaCliente(ip, 5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void iniciaServidor(int porta, IObserver o) {
		servidor = new Servidor(porta, o);
		Thread t = new Thread(servidor);
		t.start();
	}

	public static void iniciaCliente(String ip, int porta) throws Exception {
		cliente = new Cliente(ip, porta);
		sleep();
	}

	public static void sleep() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	

}
