
import javax.swing.ImageIcon;


public class Card {
	
	/**
	 * Angabe ob die Karte vom Benutzer umgedreht wurde
	 */
	private boolean hidden=true;
	/**
	 * Angabe, ob der Benutzer in einem Zug den partner gefunden hat
	 */
	private boolean partnerFound=false;
	
	/**
	 * Bild der Vorderseite
	 */
	private ImageIcon frontImage;
	/**
	 * Bild der Rückseite
	 */
	private ImageIcon backImage;
	
	/**
	 * Nummer zu vergleichen der Karten.
	 * Karten mit identischen Bildern haben die gleich number
	 */
	private int number;
	
	/**
	 * Konstruktor
	 * @param front_image Bild der Vorderseite
	 * @param back_image Bild der Rückseite
	 * @param number Number zum vergleich der Karten
	 */
	public Card(ImageIcon front_image,ImageIcon back_image, int	number){
		this.frontImage = front_image;
		this.backImage = back_image;
		this.number=number;
//		snumber = (int)(Math.random()*10) +number;
	}
	
	/**
	 * Liefert Bild der Vorderseite
	 * @return Bild
	 */
	public ImageIcon getFrontImage(){
		return this.frontImage;
	}
	
	/**
	 * Liefert Bild der Rückseite
	 * @return Bild
	 */
	public ImageIcon getBackImage(){
		return this.backImage;
	}
	
	/**
	 * Dreht eine Karte um (gegenteil von hide)
	 */
	public void flip(){
//		System.out.println("Flip: " + number + ":" +snumber);
		hidden= false;
	}
	
	/**
	 * Versteckt eine Karte wieder (gegenteil von flig)
	 */
	public void hide(){
			hidden=true;
	}
	
	/**
	 * Gibt an ob eine Karte angezeigt werden soll, oder nicht
	 * @return true, falls nicht angezeigt werden soll; false, falls angezeigt werden soll
	 */
	public boolean isHidden(){
		return hidden&&!partnerFound;
	}
	
	/**
	 * Überprüft, ob eine Karte der Partner ist
	 * @param c Karte die Überprüft werden soll
	 */
	public void isPartner(Card c){
		if(c.getId()==this.number) {
			this.partnerFound=true;
		}
	}
	
	/**
	 * Gibt an, ob der Partner gefunden wurde
	 * @return
	 */
	public boolean isPartnerFound(){
		return partnerFound;
	}

	/**
	 * Liefert die number zum Vergleichen der karten zurück
	 * @return
	 */
	public int getId() {
		return this.number;
	}
}
