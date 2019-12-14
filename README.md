#   BOGGLE

##  Instructions to build and run the program

Commit 446b57a8 (12:01):
- Should be buildable, if it doesnt build, use maven directly and simply run
`mvn compile` to get it built correctly.
- Single player works, but was last set to 15 seconds of game time. There is a 
variable that can be adjusted to set it back to 2 min (180 sec). 
- Multiplayer works to connecting and starting, but not finishing the game
(caluclating scores). 
- For multiplayer, "number of players" corresponds to number of people who will
connect, not total players.

##  Description

Procrastination Pastimes wants you to develop a computer game of Boggle.  The
program should implement the rules of the pencil-and-paper version of Boggle
as faithfully as is possible.

-   <https://www.hasbro.com/common/instruct/boggle.pdf>

The sixteen dice have the following letters:

|   | |   | |   | |   |
| - | - | - | - | - | - | - |
| R I F O B X  | . | I F E H E Y | . | D E N O W S | . | U T O K N D |
| H M S R A O  | . | L U P E T S | . | A C I T O A | . | Y L G K U E |
| Qu B M J O A | . | E H I S P N | . | V E T I G N | . | B A L I Y T |
| E Z A V N D  | . | R A L E S C | . | U W I L R G | . | P A C E M D |

The system shall be playable by a single player or by multiple players on
networked computers. You may allow a limited or unlimited number of players;
however, at least four players must be able to play against each other. You
do not have to do network discovery but may instead use manually-entered
address:port information similar to SocketChat (you will need to set up a
separate socket between the host and each client).

When all connections have been made and the host instructs the system to start
a game, each player will be shown a three-second countdown and then the dice
shall be displayed. The dice can be in any permutation of the sixteen die
positions, and each die can have any of its six faces showing.  The timer
shall then begin.  Given the limitations of text-mode Java, you are not
required to show a visual representation of this timer counting down.

Each player should then be able to enter as many words as they can until the
timer runs out. If the user enters a word that is too short or is a duplicate
of a previously-entered word, the program should silently allow the error to
occur, so as not to distract the player.

After the timer has run out, the program should remove duplicates words, words
that are too short, and words that do not appear in the corpus.  The program
shall then calculate and display each player's score and declare the winner
of the round.

After each round, the host player may initiate another round. If the players
play multiple rounds then after each round the system shall display not only
each player's score for the round but also each player's running score.

The system may be implemented in text-mode or GUI-mode.

### Corpus

A copy of the BSD `words` directory has been placed in [the resources
directory](src/main/resources/words/). You may use these files for your corpus,
but you are not required to do so. Bear in mind that some of the words in these
files do not comply with the Boggle rules.

##  Rubrics

The assignment is worth **100 points**:

-   **8 points** for providing meeting minutes (1 point per meeting).
-   **15 points** for implementing the functionality as required.
-   **10 points** for following Scrum practices.
-   **16 points** for making good design decisions.
-   **16 points** for using good coding style.
-   **6 points** for class diagram (3 points per sprint)
-   **10 points** for meaningful and well-formatted commit messages.
-   **9 points** for otherwise following good software engineering practices.
-   **10 points** for presentation/demonstration.

The contribution is worth **20 points**:

-   **1 point** for completing sprint 1 peer assessment
-   **1 point** for completing sprint 2 peer assessment
-   **5 points** for equitable contribution based on sprint 1 peer assessments
-   **5 points** for equitable contribution based on sprint 2 peer assessments
-   **8 points** for equitable contribution based on git history
