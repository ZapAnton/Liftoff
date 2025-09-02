# Liftoff

This application allows to download files from the specified source and save it to the specified destination.

Currently only the `Gmail` -> `Local Filesystem` connection is implemented. The application expects a provided Google Application password to function.

## Building

The application is developed using `Scala 2.13.12` and `sbt`.

To build simply run:

```bash
$ sbt compile
```

## Usage

Display the help message:

```bash
$ sbt "run help"
```

Download attachments from the specified email to the default directory:

```bash
$ sbt "run pull <your email address> \"<some app password>\""
```

You may provide a specific directory to put the downloaded attachments (make sure that the directory exists before using the command):

```bash
$ sbt "run pull <your email address> \"<some app password>\" /path/to/your/chosen/directory"
```
