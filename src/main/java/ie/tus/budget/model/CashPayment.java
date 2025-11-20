package ie.tus.budget.model;

public final class CashPayment implements PaymentMode{
	
	private final Money money;
	
	public CashPayment(Money money) {
	    this.money = money;
	}
	
	@Override
    public Money amount() { return money; }
	
	@Override
	public String description() {
	    return "Cash " + money;
	}
}
