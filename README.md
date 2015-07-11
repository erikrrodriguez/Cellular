# Cellular
A musical cellular automation application. Written in Java using Eclipse. Utilizing the TinySound library by finnkuusisto: https://github.com/finnkuusisto/TinySound


**Game Overview:**
* The app consists of a 9x9 grid.
* User defined cells move around the grid in either loops or paths.
* Each cell has a musical note attributed to it.
* A cell will "play" it's note when it collides with one or more cells.


![Cellular](http://i.imgur.com/4yVcap3.png)

To quickly see what the app does, press the **Generate** button. Generate creates C4, E4, G4, and B4 notes (CM7 chord) with random paths each starting on a grid square adjacent to the center grid square.

You can also draw your own notes and paths while the game is paused. Simply click the squares in the path order you would like. Once you have your path, note, octave, and color defined press the **Insert** button.

To delete a cell click it and press the **Delete button twice**. The first press deletes the path you are attempting to draw, the second deletes the cell contained in the grid. 

**Clear** will delete all cells in the grid.

**Start/Stop** will stop and start the simulation.

The **Birth** toggle allows for a 25% chance for a new cell to be created when two cells collide. This child cell does not have a path but roams randomly through the grid. It cannot participate in new cell births. It may take on 4 forms:
* It may be the same pitch as parent 1 and the same octave as parent 2.
* It may be the same pitch as parent 2 and the same octave as parent 1.
* It may be the sum of the parent pitches and the average of their octaves.
* It may be the difference pf the parent pitches and the average of their octaves.
Its color will be a mix of its two parent's colors. Also in this mode if any square holds 4 or more cells at one time, then all of those cells will be removed from the game. This prevents the game from being overrun with two many cells.


## TO DO
* Allow generate button to generate random notes rather than CEGB.
* ~~Adjust how a child cell's note is determined.~~
* Implement a reset button to move all note cells to their starting positions and delete all child cells.
* Allow only neighboring squares to be added or removed when drawing or erasing a path
* **Future:** Print out what one overall pattern looks like to sheet music.
* **Future:** Output MIDI data to control VSTs

