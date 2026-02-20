# ☕ Coffee Shop — LLD

A Low Level Design implementation of a coffee shop ordering system supporting multiple beverage types, size-based pricing, customizable add-ons, business rule validation, and order total computation.

---

## 🧩 Class Design

```
Order
└── List<OrderItem>
        └── Beverage  (name, Size, basePrices, addOns)
                ├── Size  (SMALL | MEDIUM | LARGE)
                ├── AddOnItem[]
                │       └── AddOn  (name, price)
                └── BeverageValidator  (injected)
```

### Classes

| Class | Role |
|-------|------|
| `Beverage` | A drink. Holds its name, selected size, a `Map<Size, Double>` of base prices, and a list of applied `AddOnItem`s. Computes its own total price. |
| `Size` | Enum: `SMALL`, `MEDIUM`, `LARGE` |
| `AddOn` | A single add-on type (name + price per unit), e.g. "Extra Shot" at $3.00. |
| `AddOnItem` | An `AddOn` paired with a quantity (e.g., 2× Extra Shot). |
| `BeverageValidator` | Enforces business rules on add-ons (e.g., max 3 extra shots per beverage). |
| `Order` | A shopping basket. Holds a list of `OrderItem`s and computes the subtotal. |
| `OrderItem` | A `Beverage` + quantity (e.g., 2× Latte). |

---

## ⚙️ Design Decisions

### Size-Based Pricing via Map
Each `Beverage` is constructed with a `Map<Size, Double>` of base prices rather than a switch/if-else block:
```java
Map<Size, Double> lattePrices = new HashMap<>();
lattePrices.put(Size.SMALL,  10.0);
lattePrices.put(Size.MEDIUM, 12.0);
lattePrices.put(Size.LARGE,  15.0);
Beverage latte = new Beverage("Latte", Size.LARGE, lattePrices, validator);
```
Adding a new size (e.g., `EXTRA_LARGE`) requires zero changes to `Beverage` logic.

### Decorator-Inspired Pricing
`Beverage.calculatePrice()` starts from the base size price and adds each add-on's cost:
```
price = basePrices.get(size)
      + sum(addOnItem.getAddOn().getPrice() × addOnItem.getQuantity())
```

### Validation at Add-On Time (Fail-Fast)
`BeverageValidator` is injected into `Beverage` and validates every `addAddOn()` call immediately, throwing an exception if a rule is violated. This keeps validation centralized and decoupled from pricing logic.

```java
largeLatte.addAddOn(new AddOnItem(extraShot, 5));
// → throws: "Max 3 extra shots allowed per beverage"
```

### Order as Aggregate
`Order` is a pure aggregate — it holds `OrderItem`s and delegates price calculation to each `Beverage`. No pricing logic lives in `Order` itself.

---

## 🔄 Request Flow

```
1. Create Beverage with size + price map + validator
2. addAddOn(AddOnItem)  ← validator checks rule before adding
3. Create Order → addItem(OrderItem(beverage, qty))
4. order.calculateSubtotal()
     → for each OrderItem:
           beverage.calculatePrice() × quantity
     → sum all items
```

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Decorator (inspired)** | `Beverage` + `AddOnItem` layered pricing |
| **Strategy** | `BeverageValidator` can be swapped for different rule sets |
| **Builder (informal)** | Fluent construction of `Beverage` with chained `addAddOn()` calls |

---

## 📁 Source Files

```
src/main/java/org/example/
├── Beverage.java           ← Core drink model + price calculation
├── Size.java               ← SMALL / MEDIUM / LARGE enum
├── AddOn.java              ← Add-on type definition
├── AddOnItem.java          ← Add-on + quantity
├── BeverageValidator.java  ← Business rule enforcement
├── Order.java              ← Shopping basket
├── OrderItem.java          ← Beverage + quantity in an order
└── Main.java               ← Demo: "Shabbir's Ultra Premium Overpriced Coffee Shop"
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
