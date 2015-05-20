package ee.fxgame.objects;

public class Sale {
	Purchase purchase;
	int price;
	
	
	public Sale(int price) {
		this.price = price;
	}
	
	public int getPrice() {
		return price;
	}

}
