package Jogo;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import javax.swing.JOptionPane;

public class JogoReversi
{
	private Jogador player1;
	private Jogador player2;
	private Jogador atual;

	boolean iniciado;

	public Consumer<Resultado> onGameEnded;

	private Celula[][] tabuleiro = new Celula[8][8];

	public Jogador getPlayer1()
	{
		return player1;
	}

	public Jogador getPlayer2()
	{
		return player2;
	}

	public Jogador getAtual()
	{
		return atual;
	}

	public Jogador adversario(Jogador player)
	{
		return player == player1 ? player2 : player1;
	}

	public Celula[][] getTabuleiro()
	{
		return tabuleiro;
	}

	public JogoReversi(Jogador player1, Jogador player2)
	{
		super();

		tabuleiro = new Celula[8][8];

		for (int i = 0; i < tabuleiro.length; i++)
		{
			for (int j = 0; j < tabuleiro[i].length; j++)
			{
				tabuleiro[i][j] = new Celula(i, j);
				tabuleiro[i][j].addActionListener(this::cellClicked);
				tabuleiro[i][j].addMouseListener(new BoardCellMouseEvents(this));
			}
		}
		
		tabuleiro[3][3].setarDono(player1);
		tabuleiro[4][4].setarDono(player1);
		tabuleiro[3][4].setarDono(player2);
		tabuleiro[4][3].setarDono(player2);

		this.player1 = player1;
		this.player2 = player2;
		this.atual = player1;

		player1.setJogo(this);
		player2.setJogo(this);
		
		if(!player1.isHumano())
		{
			atual = player2;
			proxima();
		}
	}

	void cellClicked(ActionEvent event)
	{

		Celula destino = (Celula) event.getSource();

		if (atual.isHumano())
		{
			if (atual.fazerJogada(tabuleiro, destino))
			{
				proxima();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Jogada inválida");
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Espere pela sua vez");
		}
	}

	private void finalizarJogo()
	{
		int p1 = player1.obterPontucao(tabuleiro);
		int p2 = player2.obterPontucao(tabuleiro);

		Jogador vencedor = null;

		if (p1 > p2)
		{
			vencedor = player1;
		}
		else if (p1 < p2)
		{
			vencedor = player2;
		}

		onGameEnded.accept(new Resultado(vencedor, p1, p2));
	}

	public void proxima()
	{
		atual = atual == player2 ? player1 : player2;

		if (!atual.temJogada(tabuleiro))
		{
//			JOptionPane.showMessageDialog(null, atual.getNome() + " não tem mais jogadas");

			finalizarJogo();
			
			return;
		}

		if (!atual.isHumano())
		{
			new Thread(() -> {
				try
				{
					Thread.sleep(500);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				atual.fazerMelhorJogada(tabuleiro, true);

				proxima();
			}).start();
		}
	}
}

class BoardCellMouseEvents extends MouseAdapter
{
	private JogoReversi jogo;

	private Jogada jogadaDestacada;

	// private List<Celula> hightLightedCells = new ArrayList<Celula>();

	public BoardCellMouseEvents(JogoReversi jogo)
	{
		super();
		this.jogo = jogo;
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		Celula destino = (Celula) e.getSource();

		Jogador atual = jogo.getAtual();

		if (atual.isHumano())
		{
			Jogada jogada = atual.obterJogada(jogo.getTabuleiro(), destino);

			if (jogada != null)
			{
				jogadaDestacada = jogada;
				jogada.destacar(true);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if (jogadaDestacada != null)
		{
			jogadaDestacada.destacar(false);
			jogadaDestacada = null;
		}
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		mouseExited(event);
	}
}
