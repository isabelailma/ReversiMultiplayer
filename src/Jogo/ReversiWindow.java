package Jogo;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ReversiWindow extends JFrame
{
	private Container container;

	public ReversiWindow(JogoReversi jogo)
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

		setSize(500, 500);

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

