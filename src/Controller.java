import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.Timer;

public class Controller {
	/**
	 * Model das bearbeitet werden muss
	 */
	protected Model model;
	/**
	 * das View für die Gui
	 */
	protected View view;
	/**
	 * Zeitgeber
	 */
	protected Timer time;
	
	
	/**
	 * Konstruktor, legt view und model fest
	 * @param m
	 * @param v
	 */
	public Controller(Model m, View v){
		this.model=m;
		this.view=v;
	}
	
	
	/**
	 * Startet das Spiel
	 */
	public void start(){
		for(int row = 0;row<this.model.getHeight();row++){
			for(int col = 0; col<this.model.getWidth();col++){
				final int r = row;
				final int c = col;
				JLabel label = this.view.getLabels().get(r).get(c);
				
				label.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						clicked(r,c);
					}
				});
			}
		}
		view.play.addActionListener(click ->this.startPlaying( ) );
		view.themeList.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!model.isIngame()){
					int index =  view.themeList.getSelectedIndex();
					model.changeSet(index);	
					view.update();
				}
			}
			
		});
	}
	



	/**
	 * Spiel startet und Timer startet
	 */
	private void startPlaying() {
		if(!model.isIngame()) {
			model.startGame();
			view.update();
		}
	}
	
	
	/**
	 * Was passieren soll, wenn ein Label geklickt werden soll
	 * @param row Reihe des Labels
	 * @param col Zeile des Labels
	 */
	private void clicked(int row, int col) {
		if(model.isIngame()) {
			if(model.amountChosen()==2){
				hideChosenCards();
				model.flipCard(row, col);
			}else{
				model.flipCard(row, col);
				view.update();
				if(model.amountChosen()==2){
					time = new Timer(2000, trigger -> hideChosenCards());
					time.start();
				}
			}
			if(model.goalReached()>0.99){
				model.endGame();
				view.update();
			}
		}

		this.view.update();
	}
	
	/**
	 * Versteckt die beiden ausgewählten Karten
	 */
	private void hideChosenCards(){
		model.hideChosen();
		this.time.stop();
		view.update();
	}
	
	

}
