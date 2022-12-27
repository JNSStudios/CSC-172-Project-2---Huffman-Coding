# CSC-172-Project-2---Huffman-Coding
Project 2 for CSC 172 Data Structures and Algorithms, which centers around Huffman Coding for compressing and decompressing files.

These files are for Project 2: Huffman Coding. This project contains the following files:
- HuffmanSubmit.java    The file that contains the encode() and decode() methods for the Huffman Coding process. In addition to these methods, this file contains the custom ADT HuffmanNode, a Comparator for HuffmanNode, a constructTree() method for building a Huffman Tree given a PriorityQueue of HuffmanNodes, and a recursive method for creating a Hashtable of all binary paths in a Huffman Tree.  
- README                this file

I recieved help from and/or assisted the following people:
- Krish J.: I assisted him in writing various algorithms for his program and he gave me some pointers for my own implementation.
-  Zach (on the Discord server) assisted me with creating the Comparator for HuffmanNode. The Comparator sorts the Nodes by their Frequency in ascending order, making the PriorityQueue they are placed in a min-heap.

HuffmanSubmit.java imports the following packages and Java ADTs,
- File
- FileWriter
- ArrayList
- Comparator
- Hashtable
- PriorityQueue
- Scanner

Additional changes to note here are that an additional integer is written to the Frequency file on the last line. It denotes the number of bits written to the encoded file. When saving the file, BinaryOut.java adds additional zeroes to the binary stream to make it a standard bit size (ex: 29 bits written gets padded to 32 bits), which can be misinterpreted as accidental characters in an encoded file. Therefore, during the decode process, this integer at the end of the file is saved to a variable "bitsWritten." This variable is used in a for loop when reading from the binary stream in the encoded file, which will repeat either until the bitsWritten number is reached, or until the binary stream is empty. In most cases, the bitsWritten variable is reached before the entire bitstream is read. 

When running HuffmanSubmit.java, it will automatically read for ur.jpg and alice30.txt to encode and decode. Editing the main method of HuffmanSubmit is necessary to change the filenames in the encode and decode methods used in the file's main method.

This program was debugged using a smaller test file, containing the text "Back 2 Back" for less complexity. Once that file was encoded and decoded successfully, it was tested on ur.jpg and alice30.txt.
