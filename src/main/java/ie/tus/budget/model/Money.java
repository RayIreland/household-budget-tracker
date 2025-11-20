package ie.tus.budget.model;

import java.math.BigDecimal;

public final class Money {
	
	private final BigDecimal amount;
    private final String currency;
	
    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    public static Money MoneyFromDouble(double amount, String currency) {
        return new Money(BigDecimal.valueOf(amount), currency);
    }
    
    public Money add(Money money) {
        if (!this.currency.equalsIgnoreCase(money.currency)) {
            throw new IllegalArgumentException("Currency mismatch!");
        }
        return new Money(this.amount.add(money.amount), this.currency);
    }
    
    public BigDecimal amount() { return amount; }
    public String currency() { return currency; }
    
    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
