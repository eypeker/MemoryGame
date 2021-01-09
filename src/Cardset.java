import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;


public class Cardset {

	private List<ImageIcon> frontImages;
	
	/**
	 * Bilder der rückseite der Karten
	 */
	private ImageIcon backImage;
	/**
	 * Farbe der Karten
	 */
	private Color backColor;
	
	/**
	 * Hintergrundfarbe des Spielfeldes
	 */
	private Color fieldColor;
	/**
	 * Titel des Cardsets
	 */
	private String title;
	
	
	
	public Cardset(File themeFolder) throws Exception{
			this.setInfo(themeFolder);
			this.setImages(themeFolder);
	}

	/**
	 * Lädt alle Bilder der Vorderseiten und das Bild der Hinterseiten
	 * @param themeFolder Der Ordner, in dem in dem die Bilder sein sollten
	 * @throws Exception falls ein Bild nicht geladen werden kann
	 */
	private void setImages(File themeFolder) throws Exception {
		frontImages = new LinkedList<>();
		String backImagePath = themeFolder.getPath() + "\\back.png";
		backImage = new ImageIcon(backImagePath);
		for(File i:themeFolder.listFiles()){
			String name = i.getPath();
			if( !(name.equals(backImagePath)||name.equals(themeFolder.getPath() + "\\theme.txt"))){
				ImageIcon ic = new ImageIcon(i.getPath());
				this.frontImages.add(ic);
			}
		}
		if(frontImages.size() != 8) throw new Exception();
	}
	
	







	/**
	 * Liest themefile ein und setzt infos dementsprechend
	 * @param themeDir Der Dateipfad des Themes
	 * @throws Exception falls eine Datei nicht vorhanden ist
	 */
	private void setInfo(File themeDir) throws Exception{

//		System.out.println(themeDir.getPath());
		try{
			String infoFile = themeDir.getPath() + "\\theme.txt";
			BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(infoFile)));
			String line;
			while ((line = bf.readLine()) != null){
				String var = line.split("=")[0];
				String value = line.split("=")[1];
				switch (var){
					case "title":
						this.title=value;
						break;
					case "field_color":
						this.fieldColor = hexToColor(value);
						break;
					case "card_color":
						this.backColor = hexToColor(value);
						break;
				}
					
			}
			bf.close();
			
		}catch(Exception e){
			System.out.print("Fehler beim laden der Infos " + themeDir.getPath());
		}
		
		if(this.title==null){
			this.title = themeDir.getName();
		}
		if(this.fieldColor==null){
			this.fieldColor=hexToColor("#ffffff");
		}
		if(this.backColor==null){
			this.backColor=hexToColor("#ffffff");
		}
	}
	
	
	
	/**
	 * Erstellt aus einer Hexadezimalfarbe ein neues Color;
	 * @param hex Hxadezimalfarbe (#Abcd2F oder a234FB)
	 * @return Die neue Color
	 */
	private Color hexToColor(String hex){
		int[] rgb = {255,255,255};
		hex=hex.toUpperCase();
//		char[] hexnumbers = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F',};
		String hexnumbers = "0123456789ABCDEF";

		switch (hex.length()){
		case 7:
			hex=hex.substring(1, 7);
		case 6:
			for(int i=0;i<rgb.length;i++){
				int main = hexnumbers.indexOf(hex.charAt(0+(i*2)));		//16er stellen der Farbe herausfinden
				int sub = hexnumbers.indexOf(hex.charAt(1+(i*2)));		//1 er stellen der Farbe herausfinden
				rgb[i]=main*16 + sub;									//farbe in Dezimal darstellen
			}
			break;
		default:
			rgb[0]= rgb[1]=rgb[2]=255;
		}
		return new Color(rgb[0],rgb[1],rgb[2]);
	}
	
	
	
	/**
	 * Listet alle Sets in der Jar-Datei auf.
	 * @return Alle vollständigen Sets
	 */
	public static List<Cardset> getAllSets(){
		List<Cardset> sets = new LinkedList<>();
		File themesFolder = new File("src/themes/");
		for(File theme: themesFolder.listFiles()){
			try {
				Cardset cs = new Cardset(theme);
				sets.add(cs);
			} catch (Exception e) {
				System.out.println("Fehler bei " + theme.getName() + "\n");
			}
		}
		return sets;
	}
	
	/**
	 * Liefert alle Bilder der Vorderseiten der Karten
	 * @return Liste an Bildern
	 */
	public List<ImageIcon> getImages(){
		return this.frontImages;
	}
	
	/**
	 * Liefert das Bild, das auf der Rückseite der Karten ist
	 * @return 
	 */
	public ImageIcon getBack(){
		return this.backImage;
	}
	
	/**
	 * Liefert die Hintergrundfarbe des Spielfarbe
	 * @return Farbe des Spielfeldes
	 */
	public Color getFieldColor(){
		if(this.fieldColor==null)return hexToColor("#ffffff");
		return this.fieldColor;
	}
	
	/**
	 * Liefert die Farbe, die die Karten haben sollen
	 * @return
	 */
	public Color getCardColor(){
		if(this.backColor==null)return hexToColor("#ffffff");
		return this.backColor;
	}
	
	/**
	 * Liefert den Namen für das jeweilge Set-Thema
	 * @return Name des Sets
	 */
	public String getTitle(){
		return this.title;
	}
}
