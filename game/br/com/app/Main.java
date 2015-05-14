package br.com.app;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import br.com.view.JanelaPrincipal;

public class Main {

	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		
		JanelaPrincipal window = new JanelaPrincipal();

	}

}
