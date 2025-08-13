# Liftoff

This application allows to download files from the specified source and save it to the specified destination.

Currently only the `Gmail` -> `Local Filesystem` connection is implemented. The application expects a provided Google Application password to function.

## Building

The application is developed using `Java 21` and `Gradle`.

To build simply run:

```bash
$ ./gradlew build
```

## Usage

Display the help message:

```bash
$ ./gradlew run --args="help"
```

Download attachments from the specified email to the default directory:

```bash
$ ./gradlew run --args="pull <your email address> \"<some app password>\""
```

You may provide a specific directory to put the downloaded attachments (make sure that the directory exists before using the command):

```bash
$ ./gradlew run --args="pull <your email address> \"<some app password>\" /path/to/your/chosen/directory"
```
