package com.example.liftoff.command;

/**
 * A default ${@code Command} implementation,
 * Used if the arguments provided to the application contain no
 * known commands.
 */
class UnknownCommand implements Command {

    /**
     * Nothing to check here
     * @return ${@code true}
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Simply prints an abort message
     */
    @Override
    public void execute() {
        System.out.println("Unknown command received. For usage help use the 'gradle run --args=\"help\"' command");
    }
}
