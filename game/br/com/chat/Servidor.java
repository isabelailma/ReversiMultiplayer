package br.com.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import br.com.jogo.Celula;
import br.com.socket.Conexao;
import br.com.view.IObserver;

public class Servidor implements Runnable, ISubject {

	private ServerSocket socketServidor;
	private IObserver observer;

	public Servidor(int port, IObserver o) {
		this.observer = o;
		try {
			socketServidor = new ServerSocket(port);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Voce esta offline");
		}
	}

	private void check(String jog) {
		String coordX = null;
		Pattern p = Pattern.compile("_X_(.*?)_Y_");
		Matcher m = p.matcher(jog);
		if (m.find()) {
			coordX = m.group(1);
		}

		String coordY = null;
		p = Pattern.compile("_Y_(.*?)_JOGCOR_");
		m = p.matcher(jog);
		if (m.find()) {
			coordY = m.group(1);
		}

		String plyName = null;
		p = Pattern.compile("_JOGCOR_(.*?)!");
		m = p.matcher(jog);
		if (m.find()) {
			plyName = m.group(1);
		}
		int y = Integer.parseInt(coordX);
		int x = Integer.parseInt(coordY);
		if (plyName.equals("Amarelo")) {
			Celula[][] tab = Conexao.game.getTabuleiro();
			tab[x][y].setarDono(Conexao.amarelo, false);

			Conexao.preto.setValido(true);
			Conexao.game.setAtual(Conexao.preto);
		} else {
			Celula[][] tab = Conexao.game.getTabuleiro();
			tab[x][y].setarDono(Conexao.preto, false);

			Conexao.amarelo.setValido(true);
			Conexao.game.setAtual(Conexao.amarelo);
		}
	}

	public void recebe() throws Exception {
		while (true) {
			Socket socketEscuta = socketServidor.accept();
			InputStreamReader streamReader = new InputStreamReader(
					socketEscuta.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			String textoEnviado = reader.readLine();
			System.out.println(textoEnviado);
			

		   if (textoEnviado.contains("_JOG_")) {
				String tempString = textoEnviado.replace("_JOG_", "");
				check(tempString);
			}
		   else{
			   notifyObserver(textoEnviado);
		   }
			reader.close();
		}
	}

	public void fechar() throws IOException {
		socketServidor.close();
	}

	@Override
	public void run() {
		try {
			recebe();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void notifyObserver(String m) {
		observer.updateMensagem(m);

	}
}
