# Virtual Keyboards for Android Smart TV

This repository contains multiple virtual keyboards for Android Smart TV, implemented in Java. Each keyboard differs in the way suggestions can be inputted.

## Keyboard Types

### Basic Keyboard
- This keyboard offers a basic input mode without any suggestion feature. Users manually input characters using the directional keys on the remote control.


## Suggestions Corpus Folder
Within this directory, you'll discover the code responsible for creating a file enabling suggestion generation based on user input. The suggestions are derived from a corpus in the English language, sourced from the Leipzig Corpora Collection. This corpus consists of 1 million English sentences.

The process involved analyzing a file containing 1 million sentences and computing occurrences of the 26 English alphabet letters, the space character, and other symbols/numbers. These occurrences were ranked in descending order, from the most frequent to the least frequent, for every possible sequence of 4 characters.

For each sequence of 4 characters, only the top 18 occurrences were retained—representing the 18 most frequent instances. These occurrences were then concatenated into a separate file. This resulting file is subsequently read by the Android application whenever suggestions are required based on user input.


