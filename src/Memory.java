
public class Memory {

	public static void main(String[] args) {
		
		Model m = new Model();
		View v = new View(m);
		v.update();
		Controller c = new Controller(m, v);
		c.start();
	}

}
