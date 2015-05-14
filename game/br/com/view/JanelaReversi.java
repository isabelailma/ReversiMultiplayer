package br.com.view;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import br.com.jogo.Celula;
import br.com.jogo.JogoReversi;
import br.com.jogo.Resultado;

public class JanelaReversi extends JFrame
{
	private Container container;

	public JanelaReversi(JogoReversi jogo)
	{
		jogo.onGameEnded = this::jogoFinalizado;

		container = getContentPane();

		GridLayout layout = new GridLayout(8, 8, 1, 1);

		container.setLayout(layout);

		Celula[][] board = jogo.getTabuleiro();

		for (Celula[] cells : board)
		{
			for (Celula cell : cells)
			{
				container.add(cell);
			}
		}

		setSize(600, 600);

		setResizable(false);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	void jogoFinalizado(Resultado resultado)
	{
		JOptionPane.showMessageDialog(this,
				String.format("%s Venceu! (%s x %s)", resultado.vencedor.getNome(), resultado.pontuacao1, resultado.pontuacao2));
		
		dispose();
	}
}

