package br.com.socket;

import java.io.Serializable;

import br.com.jogo.Jogador;

public class Transferencia implements Serializable{
	private String msg;
	private Jogador player;
	
	public Transferencia(){
		
	}

	public Transferencia(String msg, Jogador player) {
		super();
		this.msg = msg;
		this.player = player;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Jogador getPlayer() {
		return player;
	}

	public void setPlayer(Jogador player) {
		this.player = player;
	}
}
