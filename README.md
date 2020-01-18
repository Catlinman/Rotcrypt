
# Rotcrypt #

Rotcrypt is a simple [rotation](https://en.wikipedia.org/wiki/ROT13) based
[substitution cipher](https://en.wikipedia.org/wiki/Substitution_cipher)
utility program, used to rotate a string's letter indices based on a specified
rotation input sequence. This can either be a single digit which is applied to
the entire string or a set of inputs that are performed in sequence on the
desired string.

## Setup ##

To build the application run the following command in the *src* directory.

    $ javac com/catlinman/rotcrypt/Program.java

Following up, to run the program from the command line.

    $ java -cp . com.catlinman.rotcrypt.Program

You can also build a jar file and package up the program which is recommended in
most cases. Don't forget to run ```javac``` beforehand.

    $ jar cfm Rotcrypt.jar manifest.mf com/catlinman/rotcrypt/*.class

From there on you can run the application using *```java -jar Rotcrypt.jar```*.

The program itself can be run in two separate modes. The first being the default
graphical user interface while the other is the command line approach, which
allows for the passing of a file name and a rotation sequence. The output is a
new file with the file's rotated contents. The syntax for the command line
launch is so the following:

    $ java -jar Rotcrypt.jar [Input file path] [Rotation integer or array]

## License ##

This repository is released under the MIT license. For more information please
refer to [LICENSE](https://github.com/catlinman/rotcrypt/blob/master/LICENSE)
