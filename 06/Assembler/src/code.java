import java.util.*;
public class code {
    public static HashMap<String,String> compAMap = new HashMap<String, String>();
    public static HashMap<String,String> compMMap = new HashMap<String, String>();
    public static HashMap<String,String> dstMap = new HashMap<String, String>();
    public static HashMap<String,String> jmpMap = new HashMap<String, String>();

    static{
    //for c instructions comp=dst;jmp
    //put all comp posibilities with A into a HashMap,a=0
        compAMap.put("0","101010");compAMap.put("1","111111");compAMap.put("-1","111010");
        compAMap.put("D","001100");compAMap.put("A","110000");compAMap.put("!D","001101");
        compAMap.put("!A","110001");compAMap.put("-D","001111");compAMap.put("-A","110011");
        compAMap.put("D+1","011111");compAMap.put("A+1","110111");compAMap.put("D-1","001110");
        compAMap.put("A-1","110010");compAMap.put("D+A","000010");compAMap.put("D-A","010011");
        compAMap.put("A-D","000111");compAMap.put("D&A","000000");compAMap.put("D|A","010101");

    //put all comp posibilities with M into a HashMap,a=1
        compMMap.put("M","110000");compMMap.put("!M","110001");compMMap.put("-M","110011");
        compMMap.put("M+1","110111");compMMap.put("M-1","110010");compMMap.put("D+M","000010");
        compMMap.put("D-M","010011");compMMap.put("M-D","000111");compMMap.put("D&M","000000");
        compMMap.put("D|M","010101");

    //put all dst posibilities into a HashMap
        dstMap.put("","000");dstMap.put("M","001");dstMap.put("D","010");dstMap.put("MD","011");
        dstMap.put("A","100");dstMap.put("AM","101");dstMap.put("AD","110");dstMap.put("AMD","111");

    //put all jmp posibilities into a HashMap
        jmpMap.put("","000");jmpMap.put("JGT","001");jmpMap.put("JEQ","010");jmpMap.put("JGE","011");
        jmpMap.put("JLT","100");jmpMap.put("JNE","101");jmpMap.put("JLE","110");jmpMap.put("JMP","111");
        }
}
