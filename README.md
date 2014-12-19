# Rotcrypt #

Rotcrypt is a simple utility program used to rotate a string's letter indices based on a specified rotation input sequence. This can either be a single digit which is applied to the entire string or a set of inputs that are performed in sequence on the desired string.

## Setup ##

The program itself can be run in two separate modes. The one is the default graphical user interface while the other is the command line approach which allows for the passing of a file name and a rotation sequence. The output is a new file with the file's rotated contents. The syntax for the command line launch is so the following:

    $ java -jar rotcrypt.jar [Input file location] [Rotation integer or rotation array]

## Licence ##

This repository is released under the MIT license. For more information please refer to [LICENSE](https://github.com/Catlinman/Rotcrypt/blob/master/LICENSE)
