# Customer Behaviour Analysis (Java)

This project analyzes customer transactions from a CSV file and identifies:
- Items purchased together
- Frequency of item pairs
- Purchase insights

It reads a CSV file (`transactions.csv`), converts each line into a `Transaction` object, and uses `CustomerAnalyzer` to process the data.

---

# 🚀 How to Run the Project

### **Step 1 — Navigate to the project**
cd CustomerAnalysis

### **Step 2 — Compile the Java files**
javac *.java

### **Step 3 — Run the main program**
java Main

> ⚠️ Make sure `transactions.csv` is inside the **CustomerAnalysis/** folder, because the program reads it from the parent directory by default.

If needed, run with relative path:
java Main ../transactions.csv
```

# 🧪 Sample transactions.csv Format

```
T001,C001,Milk,Bread,Eggs
T002,C002,Soap,Shampoo
T003,C001,Bread,Eggs
T004,C003,Milk,Butter
T005,C002,Soap,Toothpaste
```
# 📤 **Expected Output**

Based on your Java code, the output will look like this:

```
Reading data from CSV...
Loaded 5 transactions.

Most Frequently Bought Item Pairs:
----------------------------------
Bread, Eggs -> 2
Milk, Bread -> 1
Milk, Eggs -> 1
Soap, Shampoo -> 1
Soap, Toothpaste -> 1
Milk, Butter -> 1

Customer-wise Breakdown:
----------------------------------
Customer C001:
   Transactions: 2
   Items Purchased: [Milk, Bread, Eggs, Bread, Eggs]

Customer C002:
   Transactions: 2
   Items Purchased: [Soap, Shampoo, Soap, Toothpaste]

Customer C003:
   Transactions: 1
   Items Purchased: [Milk, Butter]
```
> The exact numbers change based on your CSV file.  
> Your program prints item-pairs in descending order of frequency, then prints customer-wise purchase lists.

# 🛠 Technologies Used

- Java 8+
- File Handling
- Collections Framework (HashMap, List, Set)
- CSV parsing (manual logic)
