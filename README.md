# Cellular
A musical cellular automation application. Written in Java using Eclipse. Utilizing the TinySound Library: https://github.com/finnkuusisto/TinySound and the JavaOSC Library: https://github.com/hoijui/JavaOSC


**Game Overview:**
* The app consists of a 9x9 grid.
* User defined cells move around the grid in either loops, paths or randomly.
* Each cell has a musical note attributed to it.
* A cell will "play" it's note when it collides with one or more cells.


![Cellular](http://i.imgur.com/VS5JhoT.png)

To quickly see what the game in action press the **Generate** button. Generate creates C4, E4, G4, and B4 notes (CM7 chord) with random paths each starting on a grid square adjacent to the center grid square.

You can also draw your own notes and paths while the game is paused. Simply click the squares in the path order you would like. Once you have your path, note, octave, and color defined press the **Insert** button.

To delete a cell click it and press the **Delete button twice**. The first press deletes the path you are attempting to draw, the second deletes the cells contained in the grid square. 

**Start/Stop** will stop and start the simulation.

**Clear** will delete all cells in the grid.

**Reset** will delete all birth cels and set each note cell to its original position.

The **Birth** toggle allows for a 25% chance for a new cell to be created when two cells collide. This child cell does not have a path but roams randomly through the grid. It cannot participate in new cell births. It may take on 4 forms:
* It may be the same pitch as parent 1 and the same octave as parent 2.
* It may be the same pitch as parent 2 and the same octave as parent 1.
* It may be the sum of the parent pitches and the average of their octaves.
* It may be the difference pf the parent pitches and the average of their octaves.

Its color will be a mix of its two parent's colors. Also in this mode if any square holds 4 or more cells at one time, then all of those cells will be removed from the game. This prevents the game from being overrun with too many cells.

The **OSC** toggle diables the in-game audio playback and instead uses OSC to send the MIDI notes in each collisions to other programs such as MAX, SuperCollider, Processing, ect. Now you can use Cellular to play your favorite VST. By default it sends to your local host on port 57110.


## TO DO
* Allow generate button to generate random notes rather than CEGB.
* Allow user to specify IP address and port for OSC send.
* **Future:** Print one overall pattern to sheet music.

