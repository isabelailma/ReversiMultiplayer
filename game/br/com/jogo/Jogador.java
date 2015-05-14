package br.com.jogo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.Border;

public class Jogador {
	private String nome;

	private Color cor;

	private Border borda;

	private boolean humano;
	private boolean valido;

	private JogoReversi jogo;

	public Jogador(String name, boolean human, Color color, Border borda) {
		super();
		this.nome = name;
		this.cor = color;
		this.humano = human;
		this.borda = borda;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jogador other = (Jogador) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nome;
	}

	public List<Celula> minhasCelulas(Celula[][] board) {
		List<Celula> toReturn = new ArrayList<Celula>();

		for (Celula[] cellRow : board) {
			for (Celula cell : cellRow) {
				// se essa celula � minha
				if (cell.isDono(this)) {
					toReturn.add(cell);
				}
			}
		}

		return toReturn;
	}

	public List<Celula> vazias(Celula[][] board) {
		List<Celula> toReturn = new ArrayList<Celula>();

		for (Celula[] cellRow : board) {
			for (Celula cell : cellRow) {
				// se essa celula � minha
				if (cell.naoTemDono()) {
					toReturn.add(cell);
				}
			}
		}

		return toReturn;
	}

	public int obterPontucao(Celula[][] board) {
		return minhasCelulas(board).size();
	}

	private Celula[][] clonarTabuleiro(Celula[][] tabuleiro) {
		Celula[][] clone = new Celula[8][8];

		for (int i = 0; i < clone.length; i++) {
			for (int j = 0; j < clone[i].length; j++) {
				clone[i][j] = tabuleiro[i][j].clone();
			}
		}

		return clone;
	}

	public void fazerMelhorJogada(Celula[][] tabuleiro,
			boolean simularProximaJogada) {
		List<Jogada> jogadas = todasJogadas(tabuleiro);

		int melhorPontuacao = 0;

		Jogada melhorJogada = null;

		// obtem a melhor jogada simulando tamb�m a melhor jogada do advers�rio
		// na proxima jogada
		for (Jogada jogada : jogadas) {
			Celula[][] tabuleiroClone = clonarTabuleiro(tabuleiro);

			jogada.concretizar(tabuleiroClone);

			Jogador adversario = jogo.adversario(this);

			if (simularProximaJogada) {
				adversario.fazerMelhorJogada(tabuleiroClone, false);
			}

			int pontuacao = obterPontucao(tabuleiroClone)
					- adversario.obterPontucao(tabuleiroClone);

			boolean condition = Math.random() * 2 >= 1 ? pontuacao > melhorPontuacao
					: pontuacao >= melhorPontuacao;

			if (melhorJogada == null || condition/* pontuacao > melhorPontuacao */) {
				melhorJogada = jogada;
				melhorPontuacao = pontuacao;
			}
		}

		if (melhorJogada != null) {
			if (simularProximaJogada) {
				melhorJogada.destacar(true);
				sleep(750);
			}

			melhorJogada.concretizar(tabuleiro);
		}
	}

	public boolean fazerJogada(Celula[][] tabuleiro, Celula destino) {
		Jogada jogada = obterJogada(tabuleiro, destino);

		if (jogada != null) {
			jogada.concretizar(tabuleiro);
			return true;
		}

		return false;
	}

	public boolean temJogada(Celula[][] tabuleiro) {
		return !todasJogadas(tabuleiro).isEmpty();
	}

	List<Jogada> todasJogadas(Celula[][] tabuleiro) {
		List<Celula> vazias = vazias(tabuleiro);

		List<Jogada> jogadas = new ArrayList<Jogada>();

		for (Celula celula : vazias) {
			Jogada jogada = obterJogada(tabuleiro, celula);

			if (jogada != null) {
				jogadas.add(jogada);
			}
		}

		return jogadas;
	}

	public Jogada obterJogada(Celula[][] tabuleiro, Celula destino) {
		// a celula destino deve estar vazia
		if (destino.obterDono() != null) {
			return null;
		}

		List<Celula> capturadas = new ArrayList<Celula>();

		// direita
		capturadas.addAll(capturarPedras(tabuleiro, destino, 1, 0));

		// esquerda
		capturadas.addAll(capturarPedras(tabuleiro, destino, -1, 0));

		// cima
		capturadas.addAll(capturarPedras(tabuleiro, destino, 0, 1));

		// baixo
		capturadas.addAll(capturarPedras(tabuleiro, destino, 0, -1));

		// direita/cima
		capturadas.addAll(capturarPedras(tabuleiro, destino, 1, 1));

		// direita/baixo
		capturadas.addAll(capturarPedras(tabuleiro, destino, 1, -1));

		// esquerda/cima
		capturadas.addAll(capturarPedras(tabuleiro, destino, -1, 1));

		// esquerda/baixo
		capturadas.addAll(capturarPedras(tabuleiro, destino, -1, -1));

		return capturadas.isEmpty() ? null : new Jogada(this, destino,
				capturadas);
	}

	private List<Celula> capturarPedras(Celula[][] tabuleiro, Celula origem,
			int dirX, int dirY) {
		// come�a com um movimento, pois o m�nimo que uma pe�a pode mover � 2
		// vezes
		int linha = origem.getLinha();
		int coluna = origem.getColuna();

		// uma lista com todas as celulas capturadas ap�s o movimento, isso
		// ajudar� a determinar o valor do movimento
		List<Celula> celulasCapturadas = new ArrayList<Celula>();

		boolean jogadaValida = false;

		// enquanto o movimento est� dentro de um campo v�lido
		while ((linha + dirY) <= 7 && (linha + dirY) >= 0
				&& (coluna + dirX) <= 7 && (coluna + dirX) >= 0) {
			linha += dirY;
			coluna += dirX;

			Celula celulaAtual = tabuleiro[linha][coluna];

			// se est� vazia a jogada j� n�o � v�lida
			if (celulaAtual.naoTemDono()) {
				break;
			}

			// se � do advers�rio adiciona a lista de capturadas
			if (!celulaAtual.isDono(this)) {
				celulasCapturadas.add(celulaAtual);
			}

			// se a pedra � minha a jogada acaba
			if (celulaAtual.isDono(this)) {
				// para a jogada ser v�lida pelo menos uma pedra j� deveria ter
				// sido capturada
				if (!celulasCapturadas.isEmpty()) {
					celulasCapturadas.add(celulaAtual);
					jogadaValida = true;
				}
				break;
			}
		}

		if (!jogadaValida) {
			celulasCapturadas.clear();
		}

		return celulasCapturadas;
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}

	public JogoReversi getJogo() {
		return jogo;
	}

	public void setJogo(JogoReversi jogo) {
		this.jogo = jogo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Color getCor() {
		return cor;
	}

	public void setCor(Color cor) {
		this.cor = cor;
	}

	public boolean isHumano() {
		return humano;
	}

	public Border getBorda() {
		return borda;
	}
}