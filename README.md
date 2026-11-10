# 💣 Minesweeper (Buscaminas) in Java

A fully-featured, classic **Minesweeper** game developed entirely from scratch in **Java (Swing)**. This project implements advanced matrix control, recursive grid-expansion algorithms (Flood Fill style), dynamic difficulty scaling, and a contextual action menu system.

## 🚀 Key Technical Features
- **Dynamic Board Sizing & Difficulty Scaling:** Supports three standard difficulty levels (Easy $9 \times 9$, Medium $16 \times 16$, and Hard $16 \times 30$) with automated mine distribution using random collection shuffling (`Collections.shuffle`)[cite: 2].
- **Recursive Zero-Expansion (`QuitarCero`):** Implements an algorithmic propagation method that automatically clears neighboring safe cells and reveals adjacent numbers when clicking an empty cell (similar to a virus or flood fill)[cite: 2].
- **Contextual Pop-up Controls (`JPopupMenu`):** Replaces standard double-clicks with an intuitive pop-up action menu (`JPopupMenu`) allowing the user to choose between digging (`⛏️`), placing a flag (`🚩`), or removing a flag (`Quitar bandera`)[cite: 2].
- **Real-time Tracking:** Integrates a global Swing `Timer` to track elapsed gameplay time down to the second and monitors remaining flags and mines dynamically[cite: 2].
- **View Management:** Uses `CardLayout` to seamlessly switch between the main configuration menu and the active game board within a single window frame[cite: 2].

## 🛠️ Tech Stack
- **Language:** Java[cite: 2]
- **UI & Layouts:** Java Swing (`JFrame`, `JPanel`, `JButton`, `JComboBox`, `JPopupMenu`, `GridLayout`, `CardLayout`)[cite: 2]
- **Data Structures & Algorithms:** 2D Arrays (`int[][]`, `boolean[][]`), `ArrayList`, Recursion, Matrix Neighbor Iteration[cite: 2]
- **Core Concepts:** Event-Driven Programming, Object-Oriented Programming (OOP), State Management, Concurrency (`javax.swing.Timer`)[cite: 2]

## 🎮 How to Run
1. Clone the repository:
   ```bash
   git clone [https://github.com/RediSlayer/minesweeper-java.git](https://github.com/RediSlayer/minesweeper-java.git)
2.Compile and run the main class
