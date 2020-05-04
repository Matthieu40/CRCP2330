import processing.core.PApplet;

public class Assembler extends PApplet {

    public static void main(String[] args) {
        String[] processingArgs = {"Assembler"};
        PApplet.main(processingArgs);
    }


    public void setup() {
        //size(500, 500);
    }

    public void draw() {
        parser.translation("Add.asm");
    }
}