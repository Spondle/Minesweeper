# Copilot Instructions for Minesweeper

## Project Overview
- This is a Java-based Minesweeper game. The main logic and UI are in the `src/` directory.
- Key files:
  - `Main.java`: Entry point, launches the game.
  - `GameFrame.java`: Main window frame, sets up the game window.
  - `GamePanel.java`: Core game logic, board state, and rendering.
  - `TopPanel.java`: UI for score, timer, and controls.
  - `assets/`: Contains images and `scores.txt` for game resources and high scores.

## Architecture & Patterns
- The game uses a Swing-based GUI. `GameFrame` contains a `GamePanel` and a `TopPanel`.
- `GamePanel` manages the board, user input, and game state transitions.
- `TopPanel` displays the timer, score, and control buttons (e.g., reset).
- Images are loaded from `assets/` using relative paths.
- High scores are read/written to `assets/scores.txt`.

## Developer Workflows
- **Build:** Compile all Java files in `src/`. Example:
  ```sh
  javac src/*.java
  ```
- **Run:**
  ```sh
  java -cp src Main
  ```
- **Assets:** Ensure the working directory is the project root so image and score file paths resolve correctly.
- **No external dependencies** beyond standard Java and Swing.

## Project Conventions
- Board size, mine count, and tile images are hardcoded in `GamePanel.java`.
- UI layout and event handling follow standard Swing patterns.
- All persistent data (scores) is stored in `assets/scores.txt`.
- Use PNG images from `assets/` for rendering tiles, flags, and mines.

## Examples
- To update the board size or mine count, edit constants in `GamePanel.java`.
- To change the appearance, replace or add PNGs in `assets/` and update image loading code if needed.

## Tips for AI Agents
- When adding features, follow the existing separation between UI (`TopPanel`, `GameFrame`) and game logic (`GamePanel`).
- Use relative paths for all asset loading.
- Keep all persistent data in `assets/`.
- Maintain compatibility with standard Java (no external libraries).

---
If any section is unclear or missing, please provide feedback for further refinement.
