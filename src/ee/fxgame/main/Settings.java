package ee.fxgame.main;

/**
 * Siin on kirjas k�ik m�ngu seaded.
 * @author Handre
 *
 */

public class Settings {
	
	/**
	 * Algusraha.
	 */
	public static int startMoney = 1000;
	/**
	 * Alustushind.
	 */
	public static int price = 1000;
	/**
	 * Kui kiiresti uued punktid tulevad sekundites.
	 */
	public static float spawnSpeed = 0.075f;
	/**
	 * Kui kiiresti graaf liigub sekundites.
	 */
	public static float graphSpeed = 1 / spawnSpeed;
	/**
	 * Mitu punkti kontrollida tippude ja p�hjade registreerimiseks.
	 */
	public static int tocheck = 60;
	/**
	 * Graafiku tihedus alguses
	 */
	public static int scalecount = 40;

}
