package ie.tus.budget.model;

public sealed interface PaymentMode permits CashPayment, CardPayment {

	Money amount();
    String description();
    
    default String identityKey() {
    	return switch (this) {
	        case CashPayment _ -> "Cash";
	        case CardPayment _ -> "Card";
	        //If new payment methods are expanded in the future, you can continue to add them here
	        default -> "Other";
    	};
    }
    
    default String formattedForReport() {
        if (this instanceof CardPayment) {
            CardPayment c = (CardPayment) this;
            return "Card " + PaymentMode.maskLast4(c.last4digits());
        } else if (this instanceof CashPayment) {
            return "Cash";
        } else {
            return this.getClass().getSimpleName();
        }
    }
    
    private static String maskLast4(String last4) {
        if (last4 == null) return "";
        String s = last4.trim();
        return s.length() <= 4 ? "****" + s : "****" + s.substring(s.length() - 4);
    }
    
}
