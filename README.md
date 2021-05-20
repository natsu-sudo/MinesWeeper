Minesweeper is a single-player puzzle video game. The objective of the game is to clear a rectangular board containing hidden "mines" or bombs without detonating any of them, with help from clues about the number of neighbouring mines in each field.
Every button on the minesweeper board can either be unturned, or marked as a flag.
If the user un-turns a mine or marks all of the mines as flags, the game ends.
Every unturned button on the board holds a number, which represents the number of neighbouring mines.
If the number of neighbouring mines for the unturned button is zero, all its neighbours are unturned as well.


The app should have the following features:
An opening screen that prompts the user to choose from a few preset boards such as easy, medium and hard based on the number of mines the board has) or prompts them to create their own.
The custom boards should take the height/ width of the board as well as the number of mines as an input.
The number of mines should be always less than 1/4th of the board’s buttons to avoid overcrowding of mines. Feel free to add some other constraints if you want to (Don’t forget to mention them in a readme file that will help TAs to understand your logic better).
The game board should have an upper bezel that displays the difference of number of mines and the number of marked flags at all times (if the mine count is 15 and the user has marked 2 flags the mine count should display 15-2=13). A running timer should be added to the bezel to keep track of the time the user completes a game in.
The first click should be free, i.e. the mines should be set on the first click to ensure that the user’s first click doesn’t detonate a mine.
At the end of the game, if the user manages to track down all of the mines, their performance time can be stored . The high scores should be displayed on the home screen along with their last game time.
