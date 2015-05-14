package br.com.jogo;

public class Resultado
{
	public Jogador vencedor;

	public int pontuacao1;

	public int pontuacao2;

	public Resultado(Jogador vencedor, int pontuacao1, int pontuacao2)
	{
		super();
		this.vencedor = vencedor;
		this.pontuacao1 = pontuacao1;
		this.pontuacao2 = pontuacao2;
	}
}
