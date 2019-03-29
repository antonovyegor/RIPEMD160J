import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main (String [] args){
        String input = "Hello worldfrf ttgrkf eiguwg agfl whg";
        byte [] bytes = input.getBytes();
        ArrayList<Byte> list = new ArrayList<>();

         for (int i =0 ;i< bytes.length;i++){
             System.out.println(bytes[i]);
             list.add(bytes[i]);
         }

        int mod_bits = Math.floorMod( 8  * bytes.length,512);
        int delta = 448 - mod_bits;
        if (delta<=0)  delta = 512 - mod_bits + 448;
        int add_bytes = delta/8;
        for (int i=0;i<add_bytes;i++){
            if (i==0)  list.add((byte)127);
            else list.add((byte)0);
        }
        System.out.println();
    }
}
