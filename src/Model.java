import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class Model {
	
	/**
	 * Das nxm Feld aus Memorys dargestellt
	 */
	private List<List<Card>> field;
	
	
	/**
	 * Die Kartensets/Themes 
	 */
	private List<Cardset> cardsets;
	
	
	/**
	 * Die erste Karte in einem versuch, die ausgewählt wurde
	 */
	private Card firstChosen;
	
	
	
	/**
	 * Die zweite Karte in einem versuch, die ausgewählt wurde
	 */
	private Card secondChosen;
	
	/**
	 * Angabe ob das Spiel beendet wurde
	 */
	private boolean finished = false;
	/**
	 * Angabe ob eine Runde Läuft
	 */
	private boolean ingame = false;
	/**
	 * Der index des Sets das ausgewählt werden soll
	 */
	private int selectedSet =0;
	
	/**
	 * Die anzahl der Versuche innerhalb einer runde
	 */
	private int tries=0;
	
	

	/**
	 * Konstruktor mit Angabe der Zeilen und Spalten der Zahlenfehler
	 * @param height Anzahl der Zeilen des Feldes
	 * @param width Anzahl der Spalten des Feldes
	 */
	public Model() {
		cardsets = Cardset.getAllSets();
		shuffle(4,4);	//erstellt zufälliges Memoryfeld
		
	}
	
	/**
	 * Dreht eine Karte um, und überprüft bei zwei gedrehten Karten ob sie gleich sind
	 * @param yPos 	Zeile der Karte
	 * @param xPos	Spalte der Karte
	 */
	public void flipCard(int yPos, int xPos){
		if(amountChosen()==0){
			firstChosen = get(yPos,xPos);
			firstChosen.flip();
		}else if(amountChosen()==1){
			secondChosen = get(yPos,xPos);
			secondChosen.flip();
			firstChosen.isPartner(secondChosen);
			secondChosen.isPartner(firstChosen);
			tries++;
		}
	}
	
	
	/**
	 * Liefert die Anzahl der umgedrehten Karten
	 * @return 0=keiner umgedreht, 1=einer umgedreht, 2= beide umgedreht, -1=etwas ist schiefgelaufen
	 */
	public int amountChosen(){
		if(firstChosen==null)return 0;
		if(firstChosen!=null && secondChosen==null)return 1;
		if(firstChosen!=null && secondChosen!=null)return 2;
		return -1;
	}
	
	/**
	 * Versteckt die Ausgewählten Karten
	 */
	public void hideChosen(){
		if(firstChosen!=null)firstChosen.hide();
		if(secondChosen!=null)secondChosen.hide();
		firstChosen=null;
		secondChosen=null;
	}
	
	
	/**
	 * Vermischt die Karten neu
	 * @param height	Anzahl der Zeilen
	 * @param width		Anzahl der Spalten
	 * @param set Wählt das Kartenset\Thema
	 */
	public void shuffle(int height, int width) {
		int set = this.selectedSet;
		List<Card> cards = new LinkedList<>();
		for(int i = 0; i<cardsets.get(set).getImages().size();i++){
			Card c = new Card(cardsets.get(set).getImages().get(i),cardsets.get(set).getBack(),i);
			Card c2 = new Card(cardsets.get(set).getImages().get(i),cardsets.get(set).getBack(),i);
			cards.add(c);
			cards.add(c2);	//Fick mein leben
		}
		List<Integer> rand = Stream.generate(() ->(int) (Math.random() *(height*width) ))//erstellt eine Liste an zufälligen Zahlen
				.distinct().limit(height*width ) 		//nimmt alle Doppelten raus und limitiert noch auf die Anzahl der Felder
				.collect(Collectors.toList()); 	
		field = new LinkedList<>();
		for(int row = 0; row<height;row++){
			field.add(new LinkedList<>());
			for(int col = 0; col<width;col++){
				field.get(row).add(cards.get(rand.get(row*width + col)));
			}
		}
	}
	
	/**
	 * Gibt die Anzahl der Zeilen des Feldes zurück.
	 * @return Anzahl der Zeilen
	 */
	public int getHeight() {
		return this.field.size();
		}
	
	
	/**
	 * Gibt die Anzahl der Spalten des Feldes zurück
	 * @return Anzahl der Spalten
	 */
	public int getWidth() { 
		return this.field.get(0).size();
		}
	
	
	/**
	 * Gibt die Zahl  an einer bestimten Position wieder
	 * @param row Reihe der Karte
	 * @param col Spalte der Karte
	 * @return Karte in der Position
	 */
	public Card get(int row, int col) {
		return this.field.get(row).get(col);
	}
	
	
	/**
	 * Startet eine neue Runde
	 */
	public void startGame(){
		this.ingame = true;
		this.finished =false;
		this.tries=0;
		this.shuffle(this.getHeight(), this.getWidth());
	}
	/**
	 * Beendet das Spiel
	 */
	public void endGame(){
		this.ingame=false;
		this.finished=true;
	}
	
	/**
	 * Gibt an ob das Spiel noch läuft
	 * @return true= Spiel läft noch, false=Spiel läuft nicht mehr
	 */
	public boolean isIngame(){
		return this.ingame;
	}

	/**
	 * Gibt an ob das Spiel beendet wurde
	 * @return true=Spiel ist zu Ende, falls=Spiel ist nicht zu Ende
	 */
	public boolean isFinished(){
		return this.finished;
	}
	
	/**
	 * Gibt den Anteil an richtigen Feldern zurück
	 * @return Anteil an Dezimal zwischen 0-1
	 */
	public double goalReached(){
		int partnerFound=0;
		for(int row=0; row<this.getHeight();row++){
			for(int col=0;col<this.getWidth();col++){
				if(this.get(row,col).isPartnerFound()){
					partnerFound++;
				}
			}
		}
		return partnerFound/((double)getHeight()*(double)getWidth());
	}
	
	/**
	 * Liefet die Farben der karten
	 * @return Farbe der karte
	 */
	public Color getCardColor() {
//		System.out.println(this.cardsets.get(this.selectedSet).getCardColor());
		return this.cardsets.get(this.selectedSet).getCardColor();
	}
	
	
	/**
	 * Liefert die Hintergrundfarbe des Feldes
	 * @return Farbe für das Feld
	 */
	public Color getFieldColor() {
		return this.cardsets.get(this.selectedSet).getFieldColor();
	}
	
	/**
	 * Liefert die Namen der Themes/Kartensets
	 * @return StringArray mit den Namen
	 */
	public String[] getCardSets(){
		String[] list = new String[this.cardsets.size()];
		for(int i=0;i<cardsets.size();i++){
			list[i] = cardsets.get(i).getTitle();
		}
		return list;
	}
	
	/**
	 * Wählt einen anderen Kartenset/Theme aus
	 * @param select index des Sets der ausgewählt werden soll(0 - Anzahl der Cardsets-1)
	 */
	public void changeSet(int select){
		int size = this.cardsets.size();
		if(select>=size){
			this.selectedSet = size-1;
		}else{
			this.selectedSet = select;
		}
		this.shuffle(this.getHeight(), this.getWidth());
	}

	
	/**
	 * Liefert die Anzahl der Versuche innerhalb einer Runde
	 * @return Anzahl der Versuche
	 */
	public int getTries() {
		return tries;
	}
}
