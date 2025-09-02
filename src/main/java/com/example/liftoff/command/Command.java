package com.example.liftoff.command;


/**
 * The {@code Command} interface describes the functionality
 * of every class that want to implement any new application command.
 */
public interface Command {
    /**
     * Checks if the ${@code Command} object and it's arguments contain no logical
     * error, eg. provided file system path exists in the current file system
     * @return {@code true} if no logical errors are found, ${@code false} otherwise
     */
    boolean isValid();

    /**
     * This method contains all the necessary logic needed to perform this specific ${@code Command} implementation
     */
    void execute();
}
