// CustomerAnalyzer.java
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerAnalyzer {
    private final List<Transaction> transactions = new ArrayList<>();
    private final Map<String, Integer> itemFrequency = new HashMap<>();
    // coOccurrenceCount: itemA -> (itemB -> count)
    private final Map<String, Map<String, Integer>> coOccurrenceCount = new HashMap<>();

    public void loadFromCSV(File csvFile) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                // assume comma-separated; items may have spaces
                String[] parts = line.split(",");
                List<String> items = Arrays.stream(parts)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                Transaction tx = new Transaction(items);
                transactions.add(tx);
                indexTransaction(tx);
            }
        }
    }

    private void indexTransaction(Transaction tx) {
        List<String> items = tx.getItems();
        // update frequency
        for (String it : items) {
            itemFrequency.put(it, itemFrequency.getOrDefault(it, 0) + 1);
        }
        // update co-occurrence (unordered pairs)
        for (int i = 0; i < items.size(); i++) {
            for (int j = 0; j < items.size(); j++) {
                if (i == j) continue;
                String a = items.get(i);
                String b = items.get(j);
                coOccurrenceCount.putIfAbsent(a, new HashMap<>());
                Map<String, Integer> inner = coOccurrenceCount.get(a);
                inner.put(b, inner.getOrDefault(b, 0) + 1);
            }
        }
    }

    public List<Map.Entry<String, Integer>> topItems(int n) {
        return itemFrequency.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<PairCount> topPairs(int n) {
        // create unordered pair counts by summing a->b and b->a and only keep a < b lexicographically to avoid duplicates
        Map<String, Map<String, Integer>> map = coOccurrenceCount;
        Map<Pair, Integer> pairCounts = new HashMap<>();
        for (String a : map.keySet()) {
            for (Map.Entry<String, Integer> e : map.get(a).entrySet()) {
                String b = e.getKey();
                int count = e.getValue(); // note: this counts directional occurrences (A->B)
                Pair p = new Pair(a, b);
                Pair canonical = p.canonical();
                pairCounts.put(canonical, pairCounts.getOrDefault(canonical, 0) + count);
            }
        }
        // convert to list and sort (divide by 2 because we counted both directions if present)
        List<PairCount> list = pairCounts.entrySet().stream()
                .map(en -> new PairCount(en.getKey().a, en.getKey().b, en.getValue() / 2))
                .sorted((x, y) -> Integer.compare(y.count, x.count))
                .limit(n)
                .collect(Collectors.toList());
        return list;
    }

    public String predictNextItem(String item) {
        item = item.trim().toLowerCase();
        Map<String, Integer> map = coOccurrenceCount.get(item);
        if (map == null || map.isEmpty()) return null;
        return map.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public int totalTransactions() {
        return transactions.size();
    }

    // Simple helpers classes
    public static class Pair {
        final String a, b;
        Pair(String a, String b) {
            this.a = a;
            this.b = b;
        }
        // canonical ordering
        Pair canonical() {
            if (a.compareTo(b) <= 0) return this;
            return new Pair(b, a);
        }
        @Override public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair p = (Pair) o;
            return a.equals(p.a) && b.equals(p.b);
        }
        @Override public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    public static class PairCount {
        public final String itemA;
        public final String itemB;
        public final int count;
        PairCount(String a, String b, int count) {
            this.itemA = a;
            this.itemB = b;
            this.count = count;
        }
        @Override public String toString() {
            return String.format("%s <-> %s : %d", itemA, itemB, count);
        }
    }
}
