# Cellular
A musical cellular automation application. Written in Java using Eclipse. Utilizing the TinySound Library: https://github.com/finnkuusisto/TinySound and the JavaOSC Library: https://github.com/hoijui/JavaOSC


**Game Overview:**
* The app consists of an initial 9x9 grid, expandable to any grid size.
* User defined cells move around the grid in loops, paths, or randomly.
* Each cell has a musical note attributed to it.
* A cell will "play" it's note when it collides with one or more cells.

![Cellular](http://i.imgur.com/Qie6w4Z.jpg)

To quickly see the game in action press the **Generate** button. Generate creates C4, E4, G4, and B4 notes (CM7 chord) with random paths each starting on a grid square adjacent to the center grid square.

You can also draw your own notes and paths. Simply click the squares in the path order you would like. Once you have your path, note, octave, and color defined press the **Insert** button. You may also specify a starting square and insert a note with a random path, or insert a cell that wanders randomly. A cell with no specified note acts as a "player" cell and will still trigger other note cells when colliding.

To delete a cell, right click it. Or pause the app, click the cell and press **Delete**.

**Start/Stop** will start and stop the simulation.

**Clear** will delete all cells in the grid.

**Reset** will delete all birth cells that were not placed initially and set each note cell to its original position.

The **Birth** toggle allows for a 25% chance for a new cell to be created when exactly two cells collide. This child cell does not have a path but roams randomly through the grid. It cannot participate in new cell births. It may take on 4 forms:
* It may be the same pitch as parent 1 and the same octave as parent 2.
* It may be the same pitch as parent 2 and the same octave as parent 1.
* It may be the sum of the parent pitch numbers and the average of their octaves.
* It may be the difference pf the parent pitch numbers and the average of their octaves.

Its color will be a mix of its two parent's colors and represented with an oval and white text, if enabled. 

The **Death** toggle enforces that if any grid cell holds 4 or more cells at one time, then all of those cells will be removed from the game. This can prevent the grid from being overrun with too many cells.

The **OSC** toggle diables the in-game audio playback and instead uses the OSC protocol to send the MIDI notes in each collisions to other programs such as MAX, SuperCollider, Processing, ect. You can use Cellular to play your favorite VST. By default it sends to your local host on port 57110. You can specify another IP Address or port in the text boxes.

The **Show Notes** toggle prints each note cell's text note for easy note identification.

**Export** will create a text file called "score.txt" encoding one full cycle of the entire board state: starting from its original position and going until it returns to that original position. The text file is formatted to be imported directly into the Lily Pond notation software: http://lilypond.org/. Pressing the export button again will overwrite the file, so save it separately if you wish to keep the pattern. Birth Cells will be represented with parenthesis within the notation.

The **BPM** textfield can be used to set a new tempo for the grid.

## TO DO
* Specify location of "score.txt" file.
