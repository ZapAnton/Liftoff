package com.example.liftoff.command;

import java.util.Optional;

/**
 * {@code CommandManager} parses the passed CLI arguments and chooses
 * the appropriate {@code Command} interface implementation.
 * If none is found, returns {@code UnknownCommand}.
 */
public class CommandManager {
    public static Command buildFromCliArguments(String[] args) throws IllegalArgumentException {
        if (args.length == 0) {
            throw new IllegalArgumentException("Command argument expected. For usage help use the 'gradle run --args=\"help\"' command");
        }
        final var commandStr = args[0];
        switch (commandStr) {
            case "help":
                return new HelpCommand();
            case "pull":
                if (args.length < 3) {
                    throw new IllegalArgumentException("The 'pull' command expects at least 2 arguments. For usage help use the 'gradle run --args=\"help\"' command");
                }
                final var email = args[1];
                final var userToken = args[2];
                final var destinationDir = Optional.ofNullable(args.length > 3 ? args[3] : null);
                return new PullCommand(email, userToken, destinationDir);
            default:
                return new UnknownCommand();
        }
    }
}
