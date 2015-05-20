package ee.fxgame.main;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import ee.fxgame.objects.Joon;
import ee.fxgame.objects.Punkt;
import ee.fxgame.objects.Purchase;
import ee.fxgame.objects.Sale;
import ee.fxgame.objects.Scale;
/**
 * Mängu loogika klass. Sisaldab meetodeid graafi manipuleerimiseks.
 * @author Handre
 *
 */
public class GameLogic {

	private Stage stage;
	private Array<Punkt> punktid;
	private Array<Scale> scales;
	private Array<Purchase> purchases;
	private Array<Punkt> highlighted;
	private Array<Sale> sales;

	private float gamex, gamey;

	private Random rnd;

	private float change;
	private boolean add, del, touched;
	public static int korda, alla, yles;

	public static int money;

	private final Pool<Punkt> punktPool;

	public GameLogic(Stage stage, Array<Punkt> punktid, Array<Scale> scales,
			Array<Purchase> purchases, Array<Punkt> highlighted,
			Array<Sale> sales, float gamex, float gamey, int money,
			float change, Pool<Punkt> punktPool) {
		this.stage = stage;
		this.punktid = punktid;
		this.scales = scales;
		this.purchases = purchases;
		this.highlighted = highlighted;
		this.sales = sales;
		this.gamex = gamex;
		this.gamey = gamey;
		this.punktPool = punktPool;
		GameLogic.money = money;
		this.change = change;
		rnd = new Random();
	}
	
	/**
	 * Lisab graafile uue suvalise punkti.
	 */

	public void addNewPunkt() {
		Punkt last = punktid.peek();
		Punkt toAdd = punktPool.obtain();
		toAdd.init();
		//System.out.println("vahe: " + (GameMain.viewport.getWorldWidth() - 75 - last.getX()));
		toAdd.setX(GameMain.viewport.getWorldWidth() - 75);
		toAdd.setValue(last.getValue());
		int times = 1;
		if (rnd.nextDouble() < 0.1) {
			korda++;
			times = 3;
		}
		if (add) {
			add = false;
			if (touched) {
				add(last, toAdd, 2);
				touched = false;
			} else {
				add(last, toAdd, times);
			}

		} else if (del) {
			del = false;
			if (touched) {
				sub(last, toAdd, 2);
				touched = false;
			} else {
				sub(last, toAdd, times);
			}

		} else if (rnd.nextDouble() >= 0.5) {
			yles++;
			add(last, toAdd, times);
		} else {
			alla++;
			sub(last, toAdd, times);
		}
		// vaatame kas läheb vastu joont
		checkIfTouchingLine();
		handleMoney();

	}

	/**
	 *  Kontrollib ostusid/müüke ja arvutab raha seisu 
	 */
	private void handleMoney() {
		int eelviimane = punktid.get(punktid.size - 2).getValue();
		int viimane = punktid.peek().getValue();
		for (int i = 0; i < purchases.size; i++) {
			Purchase purchase = purchases.get(i);
			money -= eelviimane - purchase.getPrice();
			money += viimane - purchase.getPrice();
		}
		for (int i = 0; i < sales.size; i++) {
			Sale sale = sales.get(i);
			money -= sale.getPrice() - eelviimane;
			money += sale.getPrice() - viimane;
		}
	}
	
	/**
	 * Kontrollib kas viimane punkt on vastu mingit joont ja
	 * kui on käivitab handleJoonTouch.
	 */
	private void checkIfTouchingLine() {
		for (Punkt punkth : highlighted) {
			Joon joon = punkth.getJoon();
			if (joon != null && joon.getWidth() == 0) {
				if ((punktid.peek().getY() >= punkth.getY() && punkth.isTipp())
						|| (punktid.peek().getY() <= punkth.getY() && !punkth
								.isTipp())) {
					// oleme vastu joont
					handleJoonTouch(punkth, joon);
					
				}
			}
		}
	}
	/**
	 * Mõjutab graafikut vastavalt joonele.
	 * Kui joon on tipp viib graafikut alla, kui põhi siis viib üles.
	 * @param punkth Punkt mille joone vastu läksime.
	 * @param joon Vastav joon.
	 */
	
	private void handleJoonTouch(Punkt punkth, Joon joon) {
		if (punkth.isTipp()) {
			if (joon.isHitted()) {
				punkth.getJoon().setWidth(
						punktid.peek().getX() - punkth.getX());
				touched = true;
				del = true;
			} else {
				joon.hit();
				touched = true;
				del = true;
			}
		} else {
			if (joon.isHitted()) {
				punkth.getJoon().setWidth(
						punktid.peek().getX() - punkth.getX());
				touched = true;
				add = true;
			} else {
				joon.hit();
				touched = true;
				add = true;
			}
		}
	}
	
	/**
	 * Eemaldab kõik surnud punktid
	 */
	public void removeDeadPunktid() {
		for (int i = punktid.size - 1; i >= 0; i--) {
			Punkt punkt = punktid.get(i);
			if (!punkt.isAlive()) {
				punktid.removeIndex(i);
				punktPool.free(punkt);
				stage.getActors().removeValue(punkt.getJoon(), true);
				stage.getActors().removeValue(punkt, true);
				if (punkt.isHighlighted()) {
					highlighted.removeValue(punkt, true);
				}
			}
		}
	}
	
	/**
	 * Lisab graafikule uue punkti alla ning kontrollib,
	 * et graaf jääks piiridesse.
	 * @param last Viimane punkt.
	 * @param toAdd Lisatav punkt.
	 * @param times Mitu korda graafik väheneb uue punktiga.
	 */

	private void add(Punkt last, Punkt toAdd, int times) {
		toAdd.setY(last.getY() + times * change);
		toAdd.add(times);
		punktid.add(toAdd);
		stage.addActor(toAdd);
		if (toAdd.getY() > gamey - 100) {
			// scales.removeIndex(0);
			Punkt lowest = getLowestPunkt();
			if (lowest.getY() - 5 * change < 100f) {
				setNewCoords(true);
			} else {
				scales.removeIndex(0);
				addScales(true);
				moveGraph(false);
			}

		}

	}
	
	/**
	 * Lisab graafikule uue punkti üles ning kontrollib,
	 * et graaf jääks piiridesse.
	 * @param last Viimane punkt.
	 * @param toAdd Lisatav punkt.
	 * @param times Mitu korda graafik suureneb uue punktiga.
	 */

	private void sub(Punkt last, Punkt toAdd, int times) {
		if (toAdd.getValue() > 0) {
			toAdd.setY(last.getY() - times * change);
			toAdd.sub(times);
			punktid.add(toAdd);
			stage.addActor(toAdd);
		} else {
			toAdd.setY(last.getY() + times * change);
			toAdd.add(times);
			punktid.add(toAdd);
			stage.addActor(toAdd);
		}
		if (toAdd.getY() < 100) {
			Punkt highest = getHighestPunkt();
			if (highest.getY() + 5 * change > gamey - 100) {
				setNewCoords(true);
			} else {
				if (scales.first().getValue() > 0) {
					scales.pop();
					addScales(false);
					moveGraph(true);
				}
			}
		}

	}
	
	/**
	 * Kontrollib kas graafi saab suurendada ning suurendab seda kui võimalik.
	 */
	
	public void checkGraph() {
		if ((scales.size-1)*5 > Settings.scalecount) {
			float c = (gamey - 200) / ((scales.size - 2) * 5);
			if (getLowestPunkt().getY() - 5 * c >=  100) {
				scales.removeIndex(0);
				setNewCoords(false);
			}
			if (getHighestPunkt().getY() + 5 * c <=  gamey - 100) {
				scales.pop();
				setNewCoords(false);
			}
		}
		
	}
	
	/**
	 * Arvutab uue graafi tiheduse ning liigutab graafi paika.
	 * @param tighten Kui true, siis teeb graafi tihedamaks.
	 */
	private void setNewCoords(boolean tighten) {
		if (tighten) {
			Scale scaleTop = new Scale();
			stage.addActor(scaleTop);

			if (scales.first().getValue() > 0) {
				Scale scaleBottom = new Scale();
				stage.addActor(scaleBottom);
				scaleBottom.setY(scales.first().getY() - 5 * change);
				scaleBottom.setValue(scales.first().getValue() - 5);
				scales.insert(0, scaleBottom);
			}
			scaleTop.setY(scales.peek().getY() + 5 * change);
			scaleTop.setValue(scales.peek().getValue() + 5);
			scales.add(scaleTop);
		}
		float newchange = (gamey - 200) / ((scales.size - 1) * 5);
		for (int i = 0; i < scales.size; i++) {

			Scale s = scales.get(i);
			s.addAction(moveTo(0, 100 + (i) * 5 * newchange, 0.3f));
		}
		for (Punkt punkt : punktid) {
			punkt.addAction(moveTo(punkt.getX(), 100
					+ (punkt.getValue() - scales.first().getValue())
					* newchange, 0.5f));
		}
		change = newchange;

	}
	
	/**
	 * Tagastab graafi madalaima punkti.
	 * @return Graafi madalaim punkt
	 */

	private Punkt getLowestPunkt() {
		Punkt lowest = null;
		if (punktid.first().getX() > 80) {
			lowest = punktid.first();
		}		
		for (Punkt punkt : punktid) {
			if (lowest == null && punkt.getX() > 80) {
				lowest = punkt;
			}
			if (lowest != null && punkt.getValue() < lowest.getValue() && punkt.getX() >  80) {
				lowest = punkt;
			}
		}
		return lowest;
	}
	
	/**
	 * Tagastab graafi kõrgeima punkti.
	 * @return Graafi kõrgeim punkt
	 */

	private Punkt getHighestPunkt() {
		Punkt highest= null;
		if (punktid.first().getX() > 80) {
			highest = punktid.first();
		}		
		for (Punkt punkt : punktid) {
			if (highest == null && punkt.getX() > 80) {
				highest = punkt;
			}
			if (highest != null && punkt.getValue() > highest.getValue() && punkt.getX() >  80) {
				highest = punkt;
			}
		}
		return highest;
	}
	
	/**
	 * Lisab graafile väärtusid.
	 * @param up Kas lisada üles või alla.
	 */

	private void addScales(boolean up) {
		Scale toAdd = new Scale();
		toAdd.setX(scales.peek().getX());
		if (up) {
			int v = scales.peek().getValue();
			toAdd.setY(scales.peek().getY() + 5 * change);
			toAdd.setValue(v + 5);
			scales.add(toAdd);
			stage.addActor(toAdd);
			for (Scale scale : scales) {
				scale.addAction(moveTo(scale.getX(), scale.getY() - 5 * change,
						0.3f));
			}
		} else {
			int v = scales.first().getValue();
			toAdd.setY(scales.first().getY() - 5 * change);
			toAdd.setValue(v - 5);
			scales.insert(0, toAdd);
			stage.addActor(toAdd);
			for (Scale scale : scales) {
				scale.addAction(moveTo(scale.getX(), scale.getY() + 5 * change,
						0.3f));
			}

		}

	}
	
	/**
	 * Liigutab graafi üles või alla
	 * @param up Kui true siis liigutab üles, muidu alla.
	 */

	private void moveGraph(boolean up) {
		for (final Punkt punkt : punktid) {
			if (!up) {
				punkt.addAction(sequence(moveTo(punkt.getX(), punkt.getY() - 5
						* change, 0.5f)));
			} else {
				punkt.addAction(sequence(moveTo(punkt.getX(), punkt.getY() + 5
						* change, 0.5f)));
			}

		}
	}
	
	/**
	 * Otsib graafi põhjasid ja tippusid klassis {@link Settings} märgitud vahemikus.
	 */
	public void checkPohiAndLagi() {
		int size = punktid.size;
		for (int i = 0; i < size; i++) {
			Punkt punkt = punktid.get(i);
			if (i - Settings.tocheck >= 0 && i + Settings.tocheck < size
					&& !punkt.isHighlighted()) {
				if (getLowest(i - Settings.tocheck, i) >= punkt.getValue()
						&& (punkt.getValue() < getLowest(i + 1, i
								+ Settings.tocheck + 1))) {
					punkt.highlight(false);
					Joon joon = new Joon();
					joon.setX(punkt.getX());
					joon.addAction(moveTo(gamex, 0, 3f));
					punkt.setJoon(joon);
					stage.addActor(joon);
					for (int x = highlighted.size - 1; x >= 0; x--) {
						Punkt highl = highlighted.get(x);
						if (!highl.isTipp()) {
						}
					}
					highlighted.add(punkt);
				}
				if (getHighest(i - Settings.tocheck, i) <= punkt.getValue()
						&& (punkt.getValue() > getHighest(i + 1, i
								+ Settings.tocheck + 1))) {
					punkt.highlight(true);
					Joon joon = new Joon();
					joon.setX(punkt.getX());
					joon.addAction(moveTo(gamex, 0, 3f));
					stage.addActor(joon);
					punkt.setJoon(joon);

					for (int x = highlighted.size - 1; x >= 0; x--) {
						Punkt highl = highlighted.get(x);
						if (highl.isTipp()) {
						}
					}
					highlighted.add(punkt);
				}
			}
		}
	}
	
	/**
	 * Tagastab madalaima punkti väärtuse vastavas vahemikus.
	 * @param start Mitmendast punktist alustada.
	 * @param end Mitmenda punktiga lõpetada.
	 * @return Madalaima punkti väärtus vastavas vahemikus
	 */
	private int getLowest(int start, int end) {
		int lowest = punktid.get(start).getValue();
		for (int i = start; i < end; i++) {
			Punkt punkt = punktid.get(i);
			if (punkt.getValue() < lowest) {
				lowest = punkt.getValue();

			}
		}
		return lowest;
	}
	
	/**
	 * Tagastab kõrgeima punkti väärtuse vastavas vahemikus.
	 * @param start Mitmendast punktist alustada.
	 * @param end Mitmenda punktiga lõpetada.
	 * @return Kõrgeima punkti väärtus vastavas vahemikus.
	 */
	private int getHighest(int start, int end) {
		int highest = punktid.get(start).getValue();
		for (int i = start; i < end; i++) {
			Punkt punkt = punktid.get(i);
			if (punkt.getValue() > highest) {
				highest = punkt.getValue();

			}
		}
		return highest;
	}
	
	/**
	 * Lisab ostu/eemaldab müügi ning paneb graafi uue punkti üles liikuma.
	 */

	public void handleBuy() {
		int price = punktid.peek().getValue();
		if (sales.size > 0) {
			add = true;
			sales.removeIndex(0);
		} else {
			add = true;
			if (purchases.size < 10) {
				add = true;
				// punktid.peek().highlight();
				purchases.add(new Purchase(price));
			}

		}
	}
	
	/**
	 * Lisab müügi/eemaldab ostu ning paneb graafi uue punkti alla liikuma.
	 */

	public void handleSell() {
		Punkt current = punktid.peek();
		if (purchases.size > 0) {
			del = true;
			purchases.removeIndex(0);
		} else {
			del = true;
			if (sales.size < 10) {
				del = true;
				Sale sale = new Sale(current.getValue());
				sales.add(sale);
			}

		}
	}

}
