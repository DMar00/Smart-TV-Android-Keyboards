# Virtual Keyboards for Android Smart TV

This repository contains multiple virtual keyboards for Android Smart TV, implemented in Java. Each keyboard differs in the way suggestions can be inputted.

## Keyboard Types

### Basic Keyboard
This virtual keyboard offers a basic input mode without any suggestion feature. Users manually input characters using the directional keys on the remote control.

### Color PopupBar Static Keyboard
This virtual keyboard introduces a popup suggestion bar that appears directly above the selected letter whenever a letter on the virtual keyboard is chosen using a remote control. The popup bar provides four letter suggestions, each associated with a different color for easy selection:

- **Red Circle**: Selectable with the **Red Button** on the remote.
- **Green Circle**: Selectable with the **Green Button** on the remote.
- **Yellow Circle**: Selectable with the **Yellow Button** on the remote.
- **Blue Circle**: Selectable with the **Blue Button** on the remote.

Additionally, there are two special modes controlled by the **8** and **9** buttons on the remote:

- **Mode 1 (Triggered by Button 8)**:  
  After selecting a suggested letter using one of the color buttons, the four suggestions will be refreshed with new options, allowing for continuous predictive text suggestions.
  - The suggestions will update dynamically after each selection.
  
- **Mode 2 (Triggered by Button 9)**:  
  Once a suggestion is selected using a color button, no updates will be made to the suggestion bar. Instead, when the user selects a new letter on the virtual keyboard, a new popup bar with updated suggestions will appear above that selected letter.
  - The popup bar will reset each time a new letter is selected.

### Color PopupBar Dinamic Keyboard 
This virtual keyboard functions similarly to the previous version, featuring a popup suggestion bar that appears above the selected letter on the virtual keyboard when a letter is chosen using the remote control. The popup bar offers four letter suggestions, each linked to a specific color for easy selection via the corresponding color buttons on the remote.

The key difference in this version is that once a suggestion is selected using one of the color buttons, both the cursor on the virtual keyboard and the popup suggestion bar automatically move to the position of the selected suggested letter. When this happens, the suggestions in the popup bar are updated to reflect the new context, allowing for a continuous and seamless text entry experience.


## Suggestions Corpus Folder
Within this directory, you'll discover the code responsible for creating a file enabling suggestion generation based on user input. The suggestions are derived from a corpus in the English language, sourced from the Leipzig Corpora Collection. This corpus consists of 1 million English sentences.

The process involved analyzing a file containing 1 million sentences and computing occurrences of the 26 English alphabet letters, the space character, and other symbols/numbers. These occurrences were ranked in descending order, from the most frequent to the least frequent, for every possible sequence of 4 characters.

For each sequence of 4 characters, only the top 18 occurrences were retained—representing the 18 most frequent instances. These occurrences were then concatenated into a separate file. This resulting file is subsequently read by the Android application whenever suggestions are required based on user input.


