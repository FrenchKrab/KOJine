package main;

/**
 * Permet d'indiquer que l'objet poss�de une vie et de controller cette derniere
 */
public interface Living {
	
	public int getLife();
	
	public int getMaxLife();

	public void setLife(int newLife);


}
