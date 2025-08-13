# On-Tour-Board-Game
The user's goal is to go through the route that allows them to achieve the biggest score.

This project implements a **dynamic programming** algorithm to solve a scoring problem from the board game *On Tour*.  
Given a completed game board, the program outputs:
- **Maximum possible score** achievable.
- **Tour path** that yields this score.

This was developed as part of **CS 590 – Spring 2025, Project 2**.

---

## Problem Description
In *On Tour*, players fill a map with numbers and aim to form the **longest increasing tour**:
- Numbers must **strictly increase** along the path.
- Some spaces are **circled** (worth double points).
- No wildcards or equal-valued adjacent spaces (restricted version).
- Some spaces may be unusable and are omitted from the board.


---

## Input Format
The program reads from `input.txt`:

1. **First line**: Integer `n` – number of spaces.
2. **Next n lines**:  

- `value` → Number assigned to the space.
- `points` → Point value (1 or 2).
- `neighbors_count` → Number of connected spaces.
- `neighbor_i` → IDs of neighboring spaces (1-indexed).

**Example:**
Meaning:
- Value = 21  
- Points = 2 (circled)  
- 2 neighbors: spaces 6 and 7

---

## Output Format
The program writes to `output.txt`:
1. **Line 1** → Maximum score (integer).
2. **Line 2** → Space IDs of the maximum-scoring tour in order.

---

## Approach
- Uses **dynamic programming** with memoization.
- **State** → Current space & last visited value.
- **Transition** → Move to connected spaces with higher values, accumulate points.
- Final implementation is **iterative DP** for efficiency.

---

### Java
```bash
javac src/OnTourScorer.java
java -cp src OnTourScorer

---

## License

This project was completed as an academic assignment. Redistribution or modification should respect the course’s academic integrity policy.

This project was completed as an academic assignment. Redistribution or modification should respect the course’s academic integrity policy.
./on_tour
