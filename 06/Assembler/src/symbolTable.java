import java.util.*;
public class symbolTable extends code{
    public static HashMap<String,Integer> cMap = new HashMap<String, Integer>();

    static {
        //put all predefined vars into a HashMap
        cMap.put("SP",0);cMap.put("LCL",1);cMap.put("ARG",2);cMap.put("THIS",3);
        cMap.put("THAT",4);cMap.put("R0",0);cMap.put("R1",1);cMap.put("R2",2);
        cMap.put("R3",3);cMap.put("R4",4);cMap.put("R5",5);cMap.put("R6",6);
        cMap.put("R7",7);cMap.put("R8",8);cMap.put("R9",9);cMap.put("R10",10);
        cMap.put("R11",11);cMap.put("R12",12);cMap.put("R13",13);cMap.put("R14",14);
        cMap.put("R15",15);cMap.put("SCREEN",16384);cMap.put("KBD",24576);

    }
}
