package ie.tus.budget.model;

import java.time.LocalDate;
import java.util.UUID;

import ie.tus.budget.model.enums.Category;

public record Expense(
		UUID id,
		String title,
        Money money,
        Category category,
        LocalDate date,
        PaymentMode paymentMode) {
	
	public static Expense build(
            String title,
            Money amount,
            Category category,
            LocalDate date,
            PaymentMode paymentMode
    ) {
        return new Expense(UUID.randomUUID(), title, amount, category, date, paymentMode);
    }

	public LocalDate date() {
        return date;
    }

}
