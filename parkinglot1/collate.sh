#!/bin/bash

SRC_DIR="src/main/java/org/example"
OUT_FILE="LLDCollated.java"

> "$OUT_FILE"

for file in "$SRC_DIR"/*.java; do
  fname=$(basename "$file")
  echo "// $fname" >> "$OUT_FILE"
  echo "" >> "$OUT_FILE"
  cat "$file" >> "$OUT_FILE"
  echo -e "\n\n" >> "$OUT_FILE"
done

echo "Done. Output written to $OUT_FILE"
