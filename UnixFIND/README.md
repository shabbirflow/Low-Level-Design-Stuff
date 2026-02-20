# 🔍 Unix FIND — LLD

A Low Level Design implementation of the Unix `find` command. Traverses a file system tree using DFS and returns all files matching a pluggable filter — supporting name, extension, size filters, and arbitrary combinations via a `CompositeFilter`.

---

## 🧩 Class Design

```
FileSearch
└── find(root: FileSystemNode, filter: Filter) → List<FileSystemNode>
        └── dfs(node, filter, results)

FileSystemNode
├── name, extension, size
└── List<FileSystemNode> children    (non-empty only if directory)

Filter  (interface)
├── NameContainsFilter    (substring match on file name)
├── ExtensionsFilter      (exact extension match, e.g. ".java")
├── SizeFilter            (GT / LT / EQ comparison)
└── CompositeFilter       (AND / OR combination of other filters)
```

### Classes

| Class | Role |
|-------|------|
| `FileSystemNode` | Represents a file or directory. Has name, extension, size, and a list of children (empty for files). `isDirectory()` checks if children exist. |
| `FileSearch` | The traversal engine. Runs a recursive DFS. Directories are traversed but never added to results; only matching files are collected. |
| `Filter` | Interface with a single method: `boolean matches(FileSystemNode)` |
| `NameContainsFilter` | Matches if the file name contains a given substring. |
| `ExtensionsFilter` | Matches if the file extension equals the given value. |
| `SizeFilter` | Matches based on file size using `GT`, `LT`, or `EQ` operator. |
| `CompositeFilter` | Holds multiple `Filter`s and combines them with AND or OR logic. |

---

## ⚙️ Design Decisions

### Composite Pattern for Filters
`CompositeFilter` implements `Filter` and holds a `List<Filter>`. This enables arbitrarily complex filter trees with no changes to `FileSearch` or individual filters:
```java
// Find .java files larger than 1KB
Filter filter = new CompositeFilter(AND,
    new ExtensionsFilter(".java"),
    new SizeFilter(1024, GT)
);

// Find files named "Main" OR files smaller than 100 bytes
Filter filter = new CompositeFilter(OR,
    new NameContainsFilter("Main"),
    new SizeFilter(100, LT)
);
```

### Open/Closed Principle
Adding a new filter type (e.g., `ModifiedAfterFilter`, `ReadOnlyFilter`) requires only a new class implementing `Filter` — zero changes to `FileSearch` or `CompositeFilter`.

### Traversal Decoupled from Filtering
`FileSearch` knows nothing about filter specifics — it only calls `filter.matches(node)`. Filters know nothing about traversal. Both sides can evolve independently.

### DFS Traversal
```java
dfs(node, filter, results):
    if node.isDirectory():
        for child in node.getChildren():
            dfs(child, filter, results)
        return   // directories are never added to results
    if filter.matches(node):
        results.add(node)
```
The early `return` after recursing into directories ensures parent directories are not evaluated against the filter.

---

## 🔄 Request Flow

```
fileSearch.find(root, filter)
  1. dfs(root, filter, results)
  2. At each node:
       → if directory: recurse into children
       → if file: filter.matches(node)?
           → true: add to results
           → false: skip
  3. Return results
```

### Example
```java
FileSearch search = new FileSearch();

// Find all .java files anywhere under root
List<FileSystemNode> results = search.find(
    root,
    new ExtensionsFilter(".java")
);

// Find files whose name contains "Config" AND are > 500 bytes
List<FileSystemNode> results = search.find(
    root,
    new CompositeFilter(AND,
        new NameContainsFilter("Config"),
        new SizeFilter(500, GT)
    )
);
```

---

## 🧠 Design Patterns Used

| Pattern | Where |
|---------|-------|
| **Composite** | `CompositeFilter` combines `Filter`s with AND/OR |
| **Chain of Responsibility** | `CompositeFilter` delegates to each child filter |
| **Open/Closed Principle** | New filters possible without modifying existing code |
| **Strategy** | The `Filter` passed to `FileSearch.find()` is swappable |

---

## 📁 Source Files

```
src/main/java/org/example/
├── FileSystemNode.java    ← File or directory tree node
├── FileSearch.java        ← DFS traversal engine
├── Filter.java            ← Interface + NameContains, Extension, Size filters
├── CompositeFilter.java   ← AND/OR combination of filters
└── Main.java              ← Demo with sample file tree
```

## ▶️ Running

```bash
mvn compile exec:java -Dexec.mainClass="org.example.Main"
```
