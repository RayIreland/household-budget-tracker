package ie.tus.budget.model;

public final class CardPayment implements PaymentMode {

	private final Money money;
    private final String last4digits;
    
    public CardPayment(Money money, String last4digits) {
        this.money = money;
        this.last4digits = last4digits;
    }
    
    @Override
    public Money amount() { return money; }

    @Override
    public String description() {
        return "Card ****" + last4digits + " " + money;
    }
    
    public String last4digits() {
        return last4digits;
    }
}
