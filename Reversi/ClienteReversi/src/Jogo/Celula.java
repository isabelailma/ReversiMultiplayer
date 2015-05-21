package Jogo;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.Border;

public class Celula extends JButton implements Cloneable
{
	private Jogador dono;

	private int linha;

	private int coluna;

	public Jogador obterDono()
	{
		return dono;
	}

	public void setarDono(Jogador player)
	{
		this.dono = player;

		if (dono != null)
		{
			// System.out.println(dono.getCor());
			setBackground(dono.getCor());
		}
	}

	public int getLinha()
	{
		return linha;
	}

	public int getColuna()
	{
		return coluna;
	}

	public Celula(int linha, int coluna)
	{
		this.linha = linha;
		this.coluna = coluna;
		this.setText(this.toString());
	}

	public boolean naoTemDono()
	{
		return dono == null;
	}

	public boolean isDono(Jogador player)
	{
		return player.equals(dono);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + coluna;
		result = prime * result + linha;
		return result;
	}

	@Override
	protected Celula clone()
	{
		Celula cell = new Celula(linha, coluna);

		cell.setarDono(dono);

		return cell;
	}

	public void destacar(Border border)
	{
		setBorder(border);
	}

	@Override
	public boolean equals(Object obj)
	{
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
	public String toString()
	{
		return String.format("(%s,%s)", linha, coluna);
	}
}
