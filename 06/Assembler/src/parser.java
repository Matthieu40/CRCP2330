import java.util.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class parser extends symbolTable{
    public static HashMap<String,Integer> findLabels(String codes){

        HashMap<String,Integer> labels = new HashMap<String, Integer>();
        Scanner scan = new Scanner(codes);
        String line = "";
        int pc = 0;
        Pattern p = Pattern.compile("^\\([^0-9][0-9A-Za-z\\_\\:\\.\\$]+\\)$");//start with ( and end with ) and consist of uppercase
        Matcher m =null;

        while (scan.hasNextLine()){

            line = scan.nextLine();

            m = p.matcher(line);

            //if find, it is a L instruction
            if (m.find()){

                //get rid of ( and )
                labels.put(m.group().substring(1,m.group().length()-1), pc);

            }else {

                pc++;

            }

        }

        return labels;
    }


    public static String asmToHack(String codes){

        Scanner scan = new Scanner(codes);

        int addressDec = 0,//value of @value
                pc = 0,//pc count
                lineNumber = 0,//record lineNumber for exception
                startAddress = 16,//start address for variable
                temp = 0,//temp var
                flag1 = -1,//flag for "="
                flag2 = -1;//flag for ";"

        String line = "",//line read from Scanner
                varName = "",//value name of @value
                value = "",//for A instruction, 0+value
                a = "",//for C instruction, 111+a+comp+dst+jmp
                dst = "",
                comp = "",
                jmp = "",
                instructions = "";


        Pattern p = Pattern.compile("^[^0-9][0-9A-Za-z\\_\\:\\.\\$]+");
        // A user-defined symbol can be any sequence of letters, digits, underscore (_),
        //dot (.), dollar sign ($), and colon (:) that does not begin with a digit.

        Pattern pL = Pattern.compile("^\\([^0-9][0-9A-Za-z\\_\\:\\.\\$]+\\)$");
        //start with ( and end with ) and consist of uppercase
        //for L instruction

        HashMap<String,Integer> labels = findLabels(codes);

        HashMap<String,Integer> symbols = new HashMap<String, Integer>();

        while (scan.hasNextLine()){

            lineNumber++;

            line = scan.nextLine();

            if (line.charAt(0) == '@'){
                //A instructions

                varName = line.substring(1);

                //if this is jump address for next instruction
                if (labels.containsKey(varName)){

                    value = FileHelper.padLeftZero(Integer.toBinaryString(labels.get(varName)),15);

                }else {

                    //varName is a value
                    if (varName.matches("[0-9]+")) {

                        value = FileHelper.padLeftZero(Integer.toBinaryString(Integer.parseInt(varName)), 15);

                    } else {
                        //varName is an user-defined symbol

                        if (cMap.containsKey(varName)){

                            value = FileHelper.padLeftZero(Integer.toBinaryString(cMap.get(varName)), 15);

                        }else {


                            if (p.matcher(varName).find()) {

                                //if map contains this key then get its value and translate into binary
                                if (symbols.containsKey(varName)) {

                                    temp = symbols.get(varName);

                                    value = FileHelper.padLeftZero(Integer.toBinaryString(temp), 15);

                                } else {
                                    //if not put it into map and its value is startAddress + map.size()
                                    addressDec = symbols.size() + startAddress;

                                    //if use too much memory, give "out of memory" exception
                                    if (addressDec >= 16384) {

                                        throw new IllegalStateException("Out of memory!Too many user defined symbols! Line " + lineNumber);

                                    }

                                    symbols.put(varName, addressDec);

                                    value = FileHelper.padLeftZero(Integer.toBinaryString(addressDec), 15);

                                }

                            } else {

                                throw new IllegalStateException("Illegal user-defined symbol! Line " + lineNumber);

                            }
                        }

                    }
                }

                instructions += "0" + value + "\n";

                pc++;

            }else if (pL.matcher(line).find()) {

                //if it is a L instruction just negelect it
                continue;

            }else {
                //check whether this instruction is a C instructions
                //if it is not throw an exception

                flag1 = line.indexOf("=");
                flag2 = line.indexOf(";");
                dst = "";
                comp = "";
                jmp = "";

                //dest=comp;jump
                if (flag1 != -1 && flag2 != -1){

                    dst = line.substring(0,flag1);
                    comp = line.substring(flag1 + 1,flag2);
                    jmp = line.substring(flag2 + 1);

                    //comp;jump
                }else if (flag1 == -1 && flag2 != -1){

                    comp = line.substring(0,flag2);
                    jmp = line.substring(flag2 + 1);

                    //dest=comp
                }else if (flag1 != -1 && flag2 == -1){

                    dst = line.substring(0,flag1);
                    comp = line.substring(flag1 + 1);

                    //dest
                }else {

                    dst = line;

                }

                if (dstMap.containsKey(dst) && (compMMap.containsKey(comp) || compAMap.containsKey(comp)) && jmpMap.containsKey(jmp)){

                    if (compAMap.containsKey(comp)){

                        a = "0";
                        comp = compAMap.get(comp);

                    }else {

                        a = "1";
                        comp = compMMap.get(comp);

                    }

                    instructions += "111" + a + comp + dstMap.get(dst) + jmpMap.get(jmp) + "\n";

                }else{

                    throw new IllegalStateException("Wrong instruction format!Line " + lineNumber);

                }


                //System.out.println(dst + " " + comp + " " + jmp);


            }

        }
        //System.out.println(instructions);

        return instructions;
    }

    /**
     * translate .asm file to .hack file
     * @param dir
     */

    public static void translation(String dir){

        File fIn = new File(dir);

        //if input file is not an .asm file, throw an exception and stop translation
        if (!FileHelper.isAsm(fIn)){
            throw new IllegalArgumentException("Wrong file format! Only .asm is accepted!");
        }

        try {
            Scanner scan = new Scanner(fIn);
            String preprocessed = "";

            while (scan.hasNextLine()){

                String line = scan.nextLine();

                line = FileHelper.noSpaces(FileHelper.noComments(line));

                if (line.length() > 0){
                    preprocessed += line + "\n";
                }

            }

            //get rid of last "\n"
            preprocessed = preprocessed.trim();

            //System.out.println(preprocessed);
            String result = asmToHack(preprocessed);

            String fileName = fIn.getName().substring(0,fIn.getName().indexOf("."));

            PrintWriter p = new PrintWriter(new File(fIn.getParentFile().getAbsolutePath() + "/" + fileName + ".hack"));

            p.print(result);

            p.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class FileHelper {

    /**
     * Delete comments(String after "//") from a String
     * @param strIn
     * @return
     */
    public static String noComments(String strIn){

        int position = strIn.indexOf("//");

        if (position != -1){

            strIn = strIn.substring(0, position);

        }

        return strIn;
    }

    /**
     * Delete spaces from a String
     * @param strIn
     * @return
     */
    public static String noSpaces(String strIn){
        String result = "";

        if (strIn.length() != 0){

            String[] segs = strIn.split(" ");

            for (String s: segs){
                result += s;
            }
        }

        return result;
    }

    /**
     * return whether a file is an .asm file
     * @param fileIn
     * @return
     */
    public static boolean isAsm(File fileIn){

        String filename = fileIn.getName();
        int position = filename.lastIndexOf(".");

        if (position != -1) {

            String ext = filename.substring(position);

            if (ext.toLowerCase().equals(".asm")) {
                return true;
            }
        }

        return false;
    }

    /**
     * pad 0 to the input string on the left until the length is equal to the input length
     * @param strIn
     * @param len
     * @return
     */
    public static String padLeftZero(String strIn, int len){

        for (int i = strIn.length(); i < len; i++){
            strIn = "0" + strIn;
        }

        return strIn;
    }

}
