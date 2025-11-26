import java.io.File;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Simple Customer Behavior Analyzer (Java)");
        if (args.length < 1) {
            System.out.println("Usage: java Main <transactions.csv> [topN]");
            System.out.println("Example: java Main transactions.csv 10");
            return;
        }
        String csvPath = args[0];
        int topN = 10;
        if (args.length >= 2) {
            try { topN = Integer.parseInt(args[1]); } catch (Exception ignored) {}
        }

        CustomerAnalyzer analyzer = new CustomerAnalyzer();
        try {
            analyzer.loadFromCSV(new File(csvPath));
        } catch (Exception e) {
            System.err.println("Failed to load CSV: " + e.getMessage());
            return;
        }

        System.out.printf("Loaded %d transactions.\n", analyzer.totalTransactions());
        System.out.println("\nTop items:");
        List<Map.Entry<String, Integer>> topItems = analyzer.topItems(topN);
        for (int i = 0; i < topItems.size(); i++) {
            Map.Entry<String, Integer> e = topItems.get(i);
            System.out.printf("%d. %s -> %d sales\n", i+1, e.getKey(), e.getValue());
        }

        System.out.println("\nTop item pairs:");
        List<CustomerAnalyzer.PairCount> pairs = analyzer.topPairs(topN);
        for (int i = 0; i < pairs.size(); i++) {
            System.out.printf("%d. %s\n", i+1, pairs.get(i).toString());
        }

        // interactive prediction demo (simple)
        System.out.println("\nPrediction demo (simple). Examples:");
        if (!topItems.isEmpty()) {
            String item = topItems.get(0).getKey();
            String next = analyzer.predictNextItem(item);
            System.out.printf("Given '%s' customers also buy -> %s\n", item, (next==null?"(no suggestion)":next));
        }
        System.out.println("\nDone.");
    }
}
 