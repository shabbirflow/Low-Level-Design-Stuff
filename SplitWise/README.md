# 💸 SplitWise — LLD

A Low Level Design implementation of a bill-splitting application. Users in a group can add expenses with different split strategies, track pairwise balances, and compute a minimal set of settlement transactions to clear all debts.

---

## 🧩 Class Design

```
Group
├── List<User>
├── BalanceSheet                     ← source of truth for all balances
│       └── Map<User, Map<User, Double>>   (who owes whom how much)
└── List<Expense>
        ├── User paidBy
        ├── double amount
        ├── List<User> participants
        └── SplitStrategy            ← pluggable split algorithm
                ├── EqualSplitStrategy
                ├── ExactSplitStrategy
                └── PercentageSplitStrategy

SettlementService                    ← stateless, pure algorithm
└── settleUp(BalanceSheet) → List<Settlement>

Settlement
├── User payer
├── User receiver
└── double amount
```

### Classes

| Class | Role |
|-------|------|
| `User` | A participant with name and ID. |
| `Group` | A set of users. Holds the `BalanceSheet` and expense history. |
| `Expense` | A single expense: who paid, total amount, participants, and split strategy. |
| `SplitStrategy` | Interface: `Map<User, Double> computeSplits(totalAmount, participants)` |
| `EqualSplitStrategy` | Divides equally: each user gets `amount / n`. |
| `ExactSplitStrategy` | Uses a pre-defined `Map<User, Double>` of exact amounts. |
| `PercentageSplitStrategy` | Each user gets `amount × percentage / 100`. |
| `BalanceSheet` | Maintains pairwise balances: `balances[A][B] = X` means A owes B X. |
| `ExpenseValidator` | Validates that split amounts sum to the total amount (within epsilon). |
| `SettlementService` | Stateless service. Computes the minimal settlement plan from the balance sheet. |
| `Settlement` | A single transaction: payer, receiver, amount. |

---

## ⚙️ Design Decisions

### Strategy Pattern for Split Types
`SplitStrategy` is an interface. Adding a new split type (e.g., share-based, tiered) requires only a new class with zero changes to `Expense` or `Group`:
```java
expense.setSplitStrategy(new EqualSplitStrategy());
expense.setSplitStrategy(new ExactSplitStrategy(Map.of(alice, 40.0, bob, 60.0)));
expense.setSplitStrategy(new PercentageSplitStrategy(Map.of(alice, 60.0, bob, 40.0)));
```

### BalanceSheet is the Source of Truth
`BalanceSheet` holds raw pairwise balances and is the only place balances are mutated. `SettlementService` is **read-only** — it computes a derived view without touching the balance sheet. This separation means you can settle up without actually marking debts as paid.

### Minimal Settlements — Greedy Algorithm
Instead of paying every pairwise debt directly (potentially O(n²) transactions), `SettlementService` uses a greedy two-queue approach:

```
1. Flatten pairwise balances to net balance per user:
      net[A] = sum of what A owes − sum of what A is owed

2. Split into:
      debtors  (net > 0, they owe money)
      creditors (net < 0, they are owed money)

3. Greedy match:
      debtor ↔ creditor
      settle = min(owes, isOwed)
      → create Settlement(payer=debtor, receiver=creditor, amount=settle)
      → push remainder back to queue

4. Repeat until both queues empty
```
This minimises total number of transactions.

### SRP Strictly Followed
| Class | Responsibility |
|-------|---------------|
| `BalanceSheet` | State management only |
| `SettlementService` | Algorithm only |
| `ExpenseValidator` | Validation only |
| `Expense` | Data + split orchestration |

---

## 🔄 Request Flows

### Adding an Expense
```
group.addExpense(expense)
  1. ExpenseValidator.validate(expense)   ← amounts sum to total?
  2. splits = expense.getSplitStrategy()
               .computeSplits(amount, participants)
  3. For each (participant, share):
       if participant != paidBy:
           balanceSheet.add(participant owes paidBy: share)
```

### Settling Up
```
settlementService.settleUp(balanceSheet)
  1. computeNetBalances(balanceSheet)      ← flatten pairwise → net per user
  2. partition into debtors / creditors
  3. greedy match loop → List<Settlement>
  4. return settlements   (BalanceSheet NOT mutated)
```

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Strategy** | `SplitStrategy` with 3 implementations |
| **SRP** | `BalanceSheet`, `SettlementService`, `ExpenseValidator` each have one job |
| **Greedy Algorithm** | `SettlementService.settleUp()` minimises transaction count |

---

## 📁 Source Files

```
src/main/java/org/example/
├── User.java                ← Participant
├── Group.java               ← Group of users + balance sheet
├── Expense.java             ← Single expense
├── SplitStrategy.java       ← Interface + 3 implementations
├── BalanceSheet.java        ← Pairwise balance state
├── ExpenseValidator.java    ← Validates split correctness
├── SettlementService.java   ← Minimal settlement algorithm
├── Settlement.java          ← Single transaction record
└── Main.java                ← Demo
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
