

https://github.com/user-attachments/assets/1c416e07-3445-47de-8319-f235ce85b09e

<img width="1390" height="1021" alt="image" src="https://github.com/user-attachments/assets/1aa3dd70-5447-4864-bf31-d9a69e3efbbe" />

# Chess Game

A **Java chess game** for two players on one computer, drawn in a tilted 3D-style view with Java Swing. It follows the official FIDE Laws of Chess for how pieces move and how a game ends.

## 🧩 Features

✔ 3D-style board: perspective view, pieces standing on their squares with shadows <br>
✔ Start screen with a **Start Game** button <br>
✔ Drag-and-drop moves with the mouse, turn-based for White and Black <br>
✔ All piece moves, captures, **castling**, **en passant** and **pawn promotion** <br>
✔ Check, **checkmate** and **stalemate** detection <br>
✔ Illegal moves are refused (you can't leave your own king in check) <br>

### Rules implemented (FIDE Laws of Chess)

* **Castling (3.8):** not allowed if the king or rook has moved, if a piece is in between, or if the king is in check or passes through or lands on an attacked square.
* **Check (3.9):** a move that leaves your own king attacked is not allowed. Check is detected from any piece, including discovered checks.
* **Checkmate and stalemate (5.1, 5.2):** the game ends when the player to move has no legal move: checkmate if the king is in check, stalemate if not.

Not implemented yet: draw by dead position, threefold repetition, the 50-move rule, the clock and notation.

## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 11 or higher
* Optionally a Java IDE (Eclipse, IntelliJ, VS Code, etc.)

### Run from the command line

1. Clone the repository:

   ```bash
   git clone https://github.com/sathirapramudith123/Chess_Game-.git
   ```

2. Compile and run from the `SimpleChessGame` folder (Windows):

   ```powershell
   cd SimpleChessGame
   javac -d bin src/main/*.java src/piece/*.java
   java -cp "bin;res" main.Main
   ```

   On macOS or Linux use `-cp "bin:res"` instead. The `res` folder holds the piece images, so it must be on the classpath.

### Run from an IDE

Import the `SimpleChessGame` folder (it contains Eclipse project files) and run `src/main/Main.java`.

## 🛠 How to Play

1. Click **Start Game**.
2. White moves first. Press and hold a piece, drag it to a square and release.
3. A valid square is highlighted grey while you drag. If you release on an invalid square the piece goes back.
4. When a pawn reaches the last row, click a piece in the **Promote to** panel on the right.
5. The game ends with "White Wins", "Black Wins" or "Stalemate".

## 📁 Project Structure

```
SimpleChessGame/
  src/main/Main.java        — Opens the window
  src/main/Gamepannel.java  — Game loop, rules, check/checkmate/stalemate, drawing
  src/main/Board.java       — 3D board drawing and perspective projection
  src/main/Mouse.java       — Mouse input (converts the 3D view to board squares)
  src/piece/                — Piece classes (Pawan, Rook, Knight, Bishop, Queen, King)
  res/piece/                — Piece images
README.md                   — This file
.gitignore                  — Files to ignore in Git
```

support for video    <a href="https://www.youtube.com/watch?v=jzCxywhTAUI">RyiSnow</a>
