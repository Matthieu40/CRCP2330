// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
@SUM
M=0
@i
M=1 //i=1

//if(i-R0) > 0 goto END
(LOOP)
@i
D=M
@R0
D=D-M

@R1
D=M
//R2+=R1
@R2
M=D+M
//i = i + 1
@i
M=M+1
@LOOP
0;JMP //go back to loop
(END)
@END
0;JMP
