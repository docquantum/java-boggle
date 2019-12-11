package edu.unl.cse.csce361.boggle.backend.network;

/**
 * List of codes that denote what is being requested and sent
 * over the network.
 */
public enum OpCode {
    /**
     * Opt 1:
     * Server: Asks for player name from client
     * Client: returns it's name to server.
     *
     * Opt 2:
     * Client: Asks for the other player's names
     * Server: returns a list of player names.
     */
    PLAYER_NAME,

    /**
     * Client: Asks for game board
     * Server: Returns a copy of the game board.
     */
    GAME_BOARD,

    /**
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
     * Server: Returns a list of all scores attached to players (maybe HashSet[name, score])
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
