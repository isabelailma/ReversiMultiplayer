package Jogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class Jogada
{
	private Jogador jogador;

	private Celula destino;

	private List<Celula> capturadas = new ArrayList<Celula>();

	public Jogada(Jogador jogador, Celula destino, List<Celula> capturadas)
	{
		this.jogador = jogador;
		this.destino = destino;
		this.capturadas = capturadas;
		
		// adiciona o destino a lista de capturadas
		capturadas.add(destino);
	}
	
	public int pontuacao()
	{
		return capturadas.size();
	}

	public void destacar(boolean destacar)
	{
		Border borda = destacar ? jogador.getBorda() : null;

		for (Celula celula : capturadas)
		{
			celula.destacar(borda);
		}
	}

	public void concretizar(Celula[][] tabuleiro)
	{
		for (Celula celula : capturadas)
		{
			Celula clone = tabuleiro[celula.getLinha()][celula.getColuna()];
			
			clone.destacar(null);
			clone.setarDono(jogador);
		}
	}
}
