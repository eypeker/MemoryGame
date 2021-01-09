

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * View-Klasse des MVC-Pattern fuer ein Gem-Puzzle Spiel.
 * Weitere Erlaeuterungen in der Vorlesung und hier:
 * https://de.wikipedia.org/wiki/15-Puzzle
 * @author Nane Kratzke + Eop
 */
@SuppressWarnings("serial")
public class View extends JFrame {
	
	private static final int SPACE = 5;
	
	private static final int INSET = SPACE / 2;

	private static final int FONT_SIZE = 5 * SPACE;

	private static final Insets INSETS = new Insets(INSET, INSET, INSET, INSET);

	private static final Font FONT = new Font("Sans Serif", Font.BOLD, FONT_SIZE);

	protected Model model;
	
	
	/**
	 * Die 16 memory Karten
	 */
	private List<List<JLabel>> labels = new LinkedList<>();
	/**
	 * Combobox zum auswählen des Themes
	 */
	public JComboBox<String> themeList;
	/**
	 * Start-Button
	 */
	public JButton play;
	
	public JLabel timeLabel;

	JPanel top;

	JPanel middle;

	JPanel bottom;
	
	/**
	 * Konstruktor erstellt die GUI enstprechend nach m
	 * @param m das Model, das die Anzahl der Fahler der Gui bestimmt
	 */
	public View(Model m) {
		super("Memory");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.model = m; 

		GridLayout grid = new GridLayout(
			this.model.getHeight(), 
			this.model.getWidth(),
			SPACE*2,
			SPACE*2
		);
		BorderLayout fenster = new BorderLayout();
		this.setLayout(fenster);
		
		top= new JPanel();
		top.setLayout(new GridLayout(1,1));
		
		middle = new JPanel();
		middle.setLayout(grid);
		
		bottom = new JPanel();
		bottom.setBackground(Color.DARK_GRAY);


		//Top mit Play-Button
		play = new JButton("Play");
		play.setOpaque(true);
		play.setBorderPainted(false);
		play.setFont(FONT);
		play.setMargin(INSETS);
		play.setBackground(Color.ORANGE);
		this.themeList= new JComboBox<String>(model.getCardSets());
		
		
		top.add(play);
		top.add(themeList);
		
		//Middle mit den Zahlen
		for (int row = 0; row < this.model.getHeight(); row++) {
			List<JLabel> label_row = new LinkedList<>();
			labels.add(label_row);
			for (int col = 0; col < this.model.getWidth(); col++) {
				JLabel b = new JLabel(m.get(row, col).getBackImage());
				b.setOpaque(true);
				b.setFont(FONT);
				label_row.add(b);
				middle.add(b);
			}
		}
		
		
		
		//Bottom mit Zeitgebersa
		timeLabel = new JLabel("Start the game");
		timeLabel.setBackground(Color.BLACK);
		timeLabel.setFont(FONT);
		timeLabel.setOpaque(false);
		bottom.add(timeLabel);
		
		this.add(top,BorderLayout.NORTH);
		this.add(middle,BorderLayout.CENTER);
		this.add(bottom,BorderLayout.SOUTH);
		
		

		
		this.setSize(700, 700);
		this.setVisible(true);
	}

	/**
	 * Liefert Das Feld mit den JLabel/Karten zurück
	 * @return Das Feld an JLabels
	 */
	public List<List<JLabel>> getLabels() { return this.labels; }
	
	
	/**
	 * Aktualisert die Anzeige im Fenster nach den Daten des Models
	 */
	public void update() {
		for (int row = 0; row < this.model.getHeight(); row++) {
			for (int col = 0; col < this.model.getWidth(); col++) {
				JLabel b = this.labels.get(row).get(col);
				Card temp = model.get(row, col);
				if(model.get(row, col).isHidden()){
					b.setIcon(temp.getBackImage());
					
				}else{
					b.setIcon(temp.getFrontImage());
//					System.out.println("Sichtbar Nr.: " + temp.getId() + " " + row + ":" +col);
				}
				b.setBackground(model.getCardColor());
				b.setVisible(true);
			}
		}
		this.middle.setBackground(model.getFieldColor());
		if(model.isFinished()){
			themeList.setEnabled(true);
			play.setBackground(Color.GREEN);
			play.setText("Finished (Play again?)");
			timeLabel.setText(String.format("Herzlichen Glückwunsch! Du hast %d Versuche benötigt", model.getTries()));
			timeLabel.setForeground(Color.GREEN);
			
		}else if(model.isIngame()){
			themeList.setEnabled(false);
			play.setText("Play");
			play.setBackground(Color.ORANGE);
			timeLabel.setText(String.format("%5.3f%% Versuche: %d",(double)model.goalReached()*100.0,model.getTries()));
			timeLabel.setForeground(Color.orange);
		}else{
			play.setText("Play");
			play.setBackground(Color.ORANGE);
			timeLabel.setText("Start the game");
			timeLabel.setForeground(Color.WHITE);
		}
		
	}
}