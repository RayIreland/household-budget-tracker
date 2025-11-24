package ie.tus.budget.model;

import java.time.LocalDate;

import ie.tus.budget.model.enums.Category;

public record Expense(
		String title,
        Money money,
        Category category,
        LocalDate date,
        PaymentMode paymentMode) {
	

	public LocalDate date() {
        return date;
    }

}
