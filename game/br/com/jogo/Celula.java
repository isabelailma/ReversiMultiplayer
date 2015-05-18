package br.com.jogo;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

import br.com.socket.Conexao;
import br.com.socket.Serializacao;
import br.com.socket.Transferencia;

public class Celula extends JButton implements Cloneable {
	private Jogador dono;

	private int linha;

	private int coluna;

	public Jogador obterDono() {
		return dono;
	}

	public void setarDono(Jogador player, boolean valido) {
		this.dono = player;

		if (dono != null) {
			setBackground(dono.getCor());
		}

		if (valido && Conexao.rede) {
			try {
				/*
				Transferencia t = new Transferencia();
				t.setPlayer(dono);
				
				Conexao.cliente.enviarObjeto(t);
				*/
				
				
				//Conexao.preto.setValido(false);
				//Conexao.amarelo.setValido(false);

				String sendout = "_JOG_" + "_X_" + getColuna() + "_Y_"
						+ getLinha() + "_JOGCOR_" + dono.getNome() + "!";
				System.out.println(sendout);
				Conexao.cliente.enviar(sendout);
				
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	public Celula(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
		this.setText(this.toString());
	}

	public boolean naoTemDono() {
		return dono == null;
	}

	public boolean isDono(Jogador player) {
		return player.equals(dono);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + coluna;
		result = prime * result + linha;
		return result;
	}

	@Override
	protected Celula clone() {
		Celula cell = new Celula(linha, coluna);

		cell.setarDono(dono, true);

		return cell;
	}

	public void destacar(Border border) {
		setBorder(border);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Celula other = (Celula) obj;
		if (coluna != other.coluna)
			return false;
		if (linha != other.linha)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("(%s,%s)", linha, coluna);
	}
}
