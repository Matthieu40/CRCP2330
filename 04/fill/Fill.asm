// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

//sets screen to -1(black) by default
@SCREEN
D=A
@Position 
M=D-1

//checks for keystrokes. If a key is pressed, all the bits turn to black(1) If value is 0, Screen goes white
(CHECK)
@KBD
D=M      
@BLACK
D;JGT
@WHITE  
0;JMP

//Blackens the screen until its filled. When filled, nothing happens
//https://www.csie.ntu.edu.tw/~cyy/courses/introCS/13fall/lectures/handouts/lec08_HackML_4up.pdf - link helped with syntax
(BLACK)
@KBD
D=M
@Position
D=D-M
@CHECK
D;JEQ

//blackens sections of the screen
@Position
A=M
M=-1

@Position
D=M+1
@Position
M=D

//Checks to see if keys are being pressed
@CHECK
0;JMP

//Whitens sections of the screen
(WHITE)
@SCREEN
D=A-1

@Position
D=D-M
@CHECK
D;JEQ

@POSITION
A=M
M=0

@POSITION
D=M-1
@POSITION
M=D

//Checks to see if keys are being pressed
@CHECK
0;JMP






