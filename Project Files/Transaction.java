// Transaction.java
import java.util.*;

public class Transaction {
    private final List<String> items;

    public Transaction(List<String> items) {
        // normalize items: trim and lowercase for consistent counting
        List<String> norm = new ArrayList<>();
        for (String it : items) {
            String cleaned = it.trim().toLowerCase();
            if (!cleaned.isEmpty()) norm.add(cleaned);
        }
        this.items = Collections.unmodifiableList(norm);
    }

    public List<String> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return items.toString();
    }
}
