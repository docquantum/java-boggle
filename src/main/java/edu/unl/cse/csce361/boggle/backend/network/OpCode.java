package edu.unl.cse.csce361.boggle.backend.network;

/**
 * List of codes that denote what is being requested and sent
 * over the network.
 */
public enum OpCode {
    /**
     * Server: Asks for player name from client
     * Client: returns it's name to server.
     */
    PLAYER_NAME,

    /**
     * Server: Tells client name has been taken
     * Client: Tells player name has been taken
     */
    NAME_TAKEN,

    /**
     * Server: Tells client to wait to start
     * Client: Waits to receive START_GAME
     */
    WAIT_TO_START,

    /**
     * Client: Asks for game board
     * Server: Returns a copy of the game board.
     */
    GAME_BOARD,

    /**
     * Opt 1:
     * Server: Sends to all to start game
     * Client: Starts local game
     */
    START_GAME,

    /**
     * Opt 1:
     * Client: Sends finished game when it has finished
     * Server: Keeps track of which clients have finished
     *
     * Opt 2:
     * Server: Asks if client has finished
     * Client: Sends back whether it is done or not
     */
    FINISHED,

    /**
     * Server: Asks for list of words from client
     * Client: Returns a list of words
     */
    WORD_LIST,

    /**
     * Client: Asks for all scores
     * Server: Returns a list of all players with scores
     */
    ALL_SCORES,

    /**
     * Opt 1:
     * Server: Sends exit signal to notify server is going down
     * Client: Disconnect and notify to user that connection is closed
     *
     * Opt 2:
     * Client: Sends exit signal to notify of server
     */
    EXIT
}
