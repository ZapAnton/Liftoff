package com.example.liftoff;

import com.example.liftoff.command.CommandManager;

/**
 * Application main entry point.
 * Constructs a {@code Command} object from the provided cli arguments.
 * If there are no logical error in the arguments provided run the built command.
 */
public class App {
    public static void main(String[] args) {
        try {
            final var command = CommandManager.buildFromCliArguments(args);
            if (!command.isValid()) {
                return;
            }
            command.execute();
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
