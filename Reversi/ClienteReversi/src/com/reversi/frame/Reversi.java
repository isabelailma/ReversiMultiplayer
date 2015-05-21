package com.reversi.frame;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.awt.TextArea;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.border.Border;

import Jogo.Jogador;
import Jogo.JogoReversi;
import Jogo.ReversiWindow;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;



import com.reversi.bean.ChatMessage;
import com.reversi.bean.ChatMessage.Action;
import com.reversi.service.ClienteService;

public class Reversi {

	private static final String Amarelo = null;

	public JFrame frmReversi;
	
	//CÓDIGO SOCKET --------------------------------------------------------------------------------------
	private Socket socket;
    private ChatMessage message;
    private ClienteService service;
    
    //CRIA APLICAÇÃO
  	public Reversi() {
  		initialize();
  	}
 
    private class ListenerSocket implements Runnable {

        private ObjectInputStream input;

        public ListenerSocket(Socket socket) {
            try {
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(Reversi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            ChatMessage message;
            try {
                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();

                    if (action.equals(Action.CONNECT)) {
                        connected(message);
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnected();
                        socket.close();
                    } else if (action.equals(Action.SEND_ONE)) {
                        System.out.println("::: " + message.getText() + " :::");
                        receive(message);
                    } else if (action.equals(Action.USERS_ONLINE)) {
                        refreshOnlines(message);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Reversi.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Reversi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void connected(ChatMessage message) {
        if (message.getText().equals("NO")) {
            this.txtNome.setText(""); 
            JOptionPane.showMessageDialog(frmReversi, "Conexão não realizada!\nTente novamente com um novo nome.");
            return;
        }

        this.message = message;
        this.btnConnectar.setEnabled(false);
        this.txtNome.setEditable(false);
        
        this.btnSair.setEnabled(true);
        this.mensagem.setEnabled(true);
        this.chat.setEnabled(true);
        this.botaoEnviar.setEnabled(true);
        this.btnLimpar.setEnabled(true);

        JOptionPane.showMessageDialog(frmReversi ,"Você esta conectado!");
    }

    private void disconnected() {

        this.btnConnectar.setEnabled(true);
        this.txtNome.setEditable(true);

        this.btnSair.setEnabled(false);
        this.mensagem.setEnabled(false);
        this.chat.setEnabled(false);
        this.botaoEnviar.setEnabled(false);
        this.btnLimpar.setEnabled(false);
        
        this.chat.setText("");
        this.mensagem.setText("");

        JOptionPane.showMessageDialog(frmReversi, "Você saiu do chat!");
    }

    private void receive(ChatMessage message) {
        this.chat.append(message.getName() + " diz: " + message.getText() + "\n");
    }

    private void refreshOnlines(ChatMessage message) {
        System.out.println(message.getSetOnlines().toString());
        
        Set<String> names = message.getSetOnlines();
        
        names.remove(message.getName());
        
        String[] array = names.toArray(new String[names.size()]);
        
        this.listOnlines.setListData(array);
        this.listOnlines.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listOnlines.setLayoutOrientation(JList.VERTICAL);
    }
    
	//FIM DO CÓDIGO DO SOCKET ----------------------------------------------------------------------------
	
	//CÓDIGO PROFESSOR -----------------------------------------------------------------------------------
	private final Border bordaPlayer1 = BorderFactory.createLineBorder(new Color(45, 137, 239), 4,
			false);
	private final Border bordaPlayer2 = BorderFactory.createLineBorder(Color.red, 4, false);

	private Color preto = Color.black;

	private Color amarelo = new Color(255, 196, 13);

	private JComboBox<Cor> cores;
	
	//COMPUTADOR
	void singlePlayer(ActionEvent event)
	{
		Cor cor = (Cor) cores.getSelectedItem();

		Color cor1 = preto;
		Border borda1 = bordaPlayer1;
		Color cor2 = amarelo;
		Border borda2 = bordaPlayer2;
		String nome1 = "Preto";
		String nome2 = "Amarelo";

		if (cor == Cor.Amarelo)
		{
			cor1 = amarelo;
			cor2 = preto;
			borda1 = bordaPlayer2;
			borda2 = bordaPlayer1;
			nome1 = "Amarelo";
			nome2 = "Preto";
		}

		Jogador player1 = new Jogador(nome1, true, cor1, borda1);

		Jogador player2 = new Jogador(nome2, false, cor2, borda2);

		JogoReversi jogo = new JogoReversi(player1, player2);

		new ReversiWindow(jogo).setVisible(true);
	}

	//MULTIPLAYER
	void multiplayer(ActionEvent event)
	{
		Cor cor = (Cor) cores.getSelectedItem();

		Color cor1 = preto;
		Border borda1 = bordaPlayer1;
		Color cor2 = amarelo;
		Border borda2 = bordaPlayer2;
		String nome1 = "Preto";
		String nome2 = "Amarelo";

		if (cor == Cor.Amarelo)
		{
			cor1 = amarelo;
			cor2 = preto;
			borda1 = bordaPlayer2;
			borda2 = bordaPlayer1;
			nome1 = "Amarelo";
			nome2 = "Preto";
		}

		Jogador player1 = new Jogador(nome1, true, cor1, borda1);

		Jogador player2 = new Jogador(nome2, true, cor2, borda2);

		JogoReversi jogo = new JogoReversi(player1, player2);

		new ReversiWindow(jogo).setVisible(true);
	}

	//ASSISTIR
	void assistir(ActionEvent event)
	{

		Cor cor = (Cor) cores.getSelectedItem();
		
		Color cor1 = preto;
		Border borda1 = bordaPlayer1;
		Color cor2 = amarelo;
		Border borda2 = bordaPlayer2;
		String nome1 = "Preto";
		String nome2 = "Amarelo";

		if (cor == Cor.Amarelo)
		{
			cor1 = amarelo;
			cor2 = preto;
			borda1 = bordaPlayer2;
			borda2 = bordaPlayer1;
			nome1 = "Amarelo";
			nome2 = "Preto";
		}

		Jogador player1 = new Jogador(nome1, false, cor1, borda1);

		Jogador player2 = new Jogador(nome2, false, cor2, borda2);

		JogoReversi jogo = new JogoReversi(player1, player2);

		new ReversiWindow(jogo).setVisible(true);
	}

	//ENUM COR
	enum Cor
	{
		Amarelo, Preto
	};
	//FIM DO CÓDIGO DO PROFESSOR -------------------------------------------------------------------------

	//INICIALIZA OS CONTEUDOS DO FRAME
	private void initialize() {
		
		frmReversi = new JFrame();
		frmReversi.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Isabela\\workspace\\ReversiProfessor\\Icones\\Reverse.png"));
		frmReversi.setTitle("Reversi");
		frmReversi.getContentPane().setBackground(Color.DARK_GRAY);
		frmReversi.setBounds(100, 100, 1160, 571);
		frmReversi.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frmReversi.getContentPane().setLayout(null);
		
		mensagem = new TextArea();
		mensagem.setBounds(25, 422, 645, 76);
		frmReversi.getContentPane().add(mensagem);
		mensagem.setEnabled(false);
		
		botaoEnviar = new JButton("ENVIAR");
		botaoEnviar.setHorizontalAlignment(SwingConstants.LEFT);
		botaoEnviar.setIcon(new ImageIcon("C:\\Users\\Isabela\\workspace\\ClienteReversi\\Icones\\Seta.png"));
		botaoEnviar.setBackground(SystemColor.inactiveCaption);
		botaoEnviar.setBounds(697, 422, 175, 76);
		frmReversi.getContentPane().add(botaoEnviar);
		botaoEnviar.setEnabled(false);
		botaoEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });
		
		chat = new TextArea();
		chat.setBounds(25, 24, 645, 363);
		frmReversi.getContentPane().add(chat);
		chat.setEditable(false);
		chat.setEnabled(false);
		
		btnComputador = new JButton("COMPUTADOR");
		btnComputador.setIcon(new ImageIcon("C:\\Users\\Isabela\\workspace\\ClienteReversi\\Icones\\Games.png"));
		btnComputador.setBackground(SystemColor.inactiveCaption);
		btnComputador.setForeground(Color.BLACK);
		btnComputador.setBounds(902, 24, 217, 76);
		frmReversi.getContentPane().add(btnComputador);
		btnComputador.addActionListener(this::singlePlayer);
		
		btnMultiplayer = new JButton("MULTIPLAYER");
		btnMultiplayer.setIcon(new ImageIcon("C:\\Users\\Isabela\\workspace\\ClienteReversi\\Icones\\Multiplayer.png"));
		btnMultiplayer.setBackground(SystemColor.inactiveCaption);
		btnMultiplayer.setForeground(Color.BLACK);
		btnMultiplayer.setBounds(902, 202, 217, 76);
		frmReversi.getContentPane().add(btnMultiplayer);
		btnMultiplayer.addActionListener(this::multiplayer);

		btnAssistir = new JButton("ASSISTIR");
		btnAssistir.setIcon(new ImageIcon("C:\\Users\\Isabela\\workspace\\ClienteReversi\\Icones\\Assistir.png"));
		btnAssistir.setBackground(SystemColor.inactiveCaption);
		btnAssistir.setBounds(902, 113, 217, 76);
		frmReversi.getContentPane().add(btnAssistir);
		btnAssistir.addActionListener(this::assistir);
		
		
		listOnlines = new JList();
		
		panelOnline = new JScrollPane();
		panelOnline.setBounds(697, 24, 175, 334);
		frmReversi.getContentPane().add(panelOnline);
		panelOnline.add(listOnlines);
		panelOnline.setViewportView(listOnlines);
		
		btnLimpar = new JButton("LIMPAR");
		btnLimpar.setBackground(SystemColor.inactiveCaption);
		btnLimpar.setBounds(697, 371, 175, 40);
		frmReversi.getContentPane().add(btnLimpar);
		btnLimpar.setEnabled(false);
		btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
		
		panel = new JPanel();
		panel.setBackground(SystemColor.windowBorder);
		panel.setBounds(902, 380, 217, 118);
		frmReversi.getContentPane().add(panel);
		panel.setLayout(null);
		
		
		txtNome = new JTextField();
		txtNome.setHorizontalAlignment(SwingConstants.CENTER);
		txtNome.setBackground(SystemColor.control);
		txtNome.setBounds(12, 38, 193, 30);
		panel.add(txtNome);
		txtNome.setColumns(10);
		
		lblNome = new JLabel("NOME");
		lblNome.setForeground(SystemColor.textHighlight);
		lblNome.setBounds(12, -2, 204, 50);
		panel.add(lblNome);
		
		btnConnectar = new JButton("CONECTAR");
		btnConnectar.setBackground(SystemColor.inactiveCaption);
		btnConnectar.setBounds(12, 80, 97, 25);
		panel.add(btnConnectar);
		btnConnectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarActionPerformed(evt);
            }
        });
		
		btnSair = new JButton("SAIR");
		btnSair.setBackground(SystemColor.inactiveCaption);
		btnSair.setBounds(121, 80, 84, 25);
		panel.add(btnSair);
		btnSair.setEnabled(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });
		
		
		cores = new JComboBox<Reversi.Cor>(Cor.values());
		cores.setBackground(SystemColor.inactiveCaption);
		cores.setBounds(902, 291, 217, 76);
		frmReversi.getContentPane().add(cores);

	}
	
	//CÓDIGO SOCKET --------------------------------------------------------------------------------------
	private void btnConnectarActionPerformed(java.awt.event.ActionEvent evt) {
        String name = this.txtNome.getText();

        if (!name.isEmpty()) {
            this.message = new ChatMessage();
            this.message.setAction(Action.CONNECT);
            this.message.setName(name);

            this.service = new ClienteService();
            this.socket = this.service.connect();

            new Thread(new ListenerSocket(this.socket)).start();

            this.service.send(message);   
        }
    }
	
	private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {
        ChatMessage message = new ChatMessage();
        message.setName(this.message.getName());
        message.setAction(Action.DISCONNECT);
        this.service.send(message);
        disconnected();
    }
	
	private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {
        this.mensagem.setText(" ");
    }
	
	private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {
        String text = this.mensagem.getText();
        String name = this.message.getName();
        
        this.message = new ChatMessage();
        
        if (this.listOnlines.getSelectedIndex() > -1) {
            this.message.setNameReserved((String) this.listOnlines.getSelectedValue());
            this.message.setAction(Action.SEND_ONE);
            this.listOnlines.clearSelection();
        } else {
            this.message.setAction(Action.SEND_ALL);
        }
        
        if (!text.isEmpty()) {
            this.message.setName(name);
            this.message.setText(text);

            this.chat.append("Você disse: " + text + "\n");
            
            this.service.send(this.message);
        }
        
        this.mensagem.setText("");
    }
	
	
	//FIM DO CÓDIGO DO SOCKET ----------------------------------------------------------------------------
	
	
	//VARIAVEIS
	private JLabel lblNome;
	private TextArea mensagem;
	private JButton botaoEnviar;
	private TextArea chat;
	private JButton btnComputador;
	private JButton btnMultiplayer;
	private JButton btnAssistir;
	private JScrollPane panelOnline;
	private JList listOnlines;
	private JButton btnLimpar;
	private JPanel panel;
	private JTextField txtNome;
	private JButton btnConnectar;
	private JButton btnSair;

}