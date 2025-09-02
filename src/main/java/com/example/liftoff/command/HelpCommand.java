package com.example.liftoff.command;

/**
 * Provides instructions how to use the application
 */
class HelpCommand implements Command {
    private final String USAGE = """
            Liftoff
            An application that downloads email attachments from the provided email address.
            Available commands:
                help - prints this message
                pull <your email address> <your gmail application password> [destination directory for the downloaded attachments]
            Examples:
                ./gradlew run --args="pull my_email@example.com "some application password"" - will download all of the attachments to the default directory "./email_attachments_downloads"
                ./gradlew run --args="pull my_email@example.com "some application password" /path/to/download/directory" - will download all of the attachments to the provided directory
            """;

    /**
     * Nothing to check here
     * @return ${@code true}
     */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * Prints the usage help message
     */
    @Override
    public void execute() {
        System.out.println(USAGE);
    }
}
