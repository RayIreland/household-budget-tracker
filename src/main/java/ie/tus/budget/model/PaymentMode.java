package ie.tus.budget.model;

public sealed interface PaymentMode permits CashPayment, CardPayment {

	Money amount();
    String description();
}
