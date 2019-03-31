import java.util.ArrayList;


import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedBytes;
import com.google.common.primitives.UnsignedLong;

public class Main {
    static final int [] r ={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,
            7, 4, 13, 1, 10, 6, 15, 3, 12, 0, 9, 5, 2, 14, 11, 8,
            3, 10, 14, 4, 9, 15, 8, 1, 2, 7, 0, 6, 13, 11, 5, 12,
            1, 9, 11, 10, 0, 8, 12, 4, 13, 3, 7, 15, 14, 5, 6, 2,
            4, 0, 5, 9, 7, 12, 2, 10, 14, 1, 3, 8, 11, 6, 15, 13};
    static final int [] rshtr = {5, 14, 7, 0, 9, 2, 11, 4, 13, 6, 15, 8, 1, 10, 3, 12,
            6, 11, 3, 7, 0, 13, 5, 10, 14, 15, 8, 12, 4, 9, 1, 2,
            15, 5, 1, 3, 7, 14, 6, 9, 11, 8, 12, 2, 10, 0, 4, 13,
            8, 6, 4, 1, 3, 11, 15, 0, 5, 12, 2, 13, 9, 7, 10, 14,
            12, 15, 10, 4, 1, 5, 8, 7, 6, 2, 13, 14, 0, 3, 9, 11};
    static final int s [] = {
            11, 14, 15, 12, 5, 8, 7, 9, 11, 13, 14, 15, 6, 7, 9, 8,
            7, 6, 8, 13, 11, 9, 7, 15, 7, 12, 15, 9, 11, 7, 13, 12,
            11, 13, 6, 7, 14, 9, 13, 15, 14, 8, 13, 6, 5, 12, 7, 5,
            11, 12, 14, 15, 14, 15, 9, 8, 9, 14, 5, 6, 8, 6, 5, 12,
            9, 15, 5, 11, 6, 8, 13, 12, 5, 12, 13, 14, 11, 8, 5, 6};
    static final int sshtr [] = {
            8, 9, 9, 11, 13, 15, 15, 5, 7, 7, 8, 11, 14, 14, 12, 6,
            9, 13, 15, 7, 12, 8, 9, 11, 7, 7, 12, 7, 6, 15, 13, 11,
            9, 7, 15, 11, 8, 6, 6, 14, 12, 13, 5, 14, 13, 13, 7, 5,
            15, 5, 8, 11, 14, 14, 6, 14, 6, 9, 12, 9, 12, 5, 15, 8,
            8, 5, 12, 9, 12, 5, 14, 6, 8, 13, 6, 5, 15, 13, 11, 11};
    public static void main (String [] args){



        String input = "a";
        System.out.println(input);
        byte [] bytes = input.getBytes();
        ArrayList<Byte> list = new ArrayList<>();
        for (int i =0 ;i< bytes.length;i++)
            list.add(bytes[i]);

        UnsignedLong input_length_bits = UnsignedLong.fromLongBits(bytes.length*8);
        String input_length_bits_str = input_length_bits.toString(2);
        while(input_length_bits_str.length()!=64){
            input_length_bits_str="0"+input_length_bits_str;
        }
        int mod_bits = Math.floorMod( 8  * bytes.length,512);
        int delta = 448 - mod_bits;
        if (delta<=0)  delta = 512 - mod_bits + 448;
        int add_bytes = delta/8;
        for (int i=0;i<add_bytes;i++){
            if (i==0)  list.add((byte)-128);
            else list.add((byte)0);
        }
        for ( int i=0;i<input_length_bits_str.length();i+=8){
            String split = input_length_bits_str.substring(i,i+8);
            byte b = UnsignedBytes.parseUnsignedByte(split,2);
            list.add(b);
        }

        UnsignedInteger[][] M = new UnsignedInteger[list.size()*8/512][16];
        for (int i=0;i<list.size()*8/512;i++){
            for (int j =0 ; j<16;j++){
                String sub="";
                        for (int k=3;k>=0;k--) {
                            String str = UnsignedBytes.toString(list.get(4 * j + 64*i + k), 2);
                            while (str.length()!=8) str = "0" + str;
                            sub+=str;
                        }
                M[i][j]=UnsignedInteger.valueOf(sub,2);
            }
        }
        UnsignedInteger h0 = UnsignedInteger.valueOf("67452301",16);
        UnsignedInteger h1 = UnsignedInteger.valueOf("EFCDAB89",16);
        UnsignedInteger h2 = UnsignedInteger.valueOf("98BADCFE",16);
        UnsignedInteger h3 = UnsignedInteger.valueOf("10325476",16);
        UnsignedInteger h4  = UnsignedInteger.valueOf("C3D2E1F0",16);
        //==============================================================================
        //=======================================================
        UnsignedInteger T;
        for (int i=0; i<list.size()*8/512;i++){
            UnsignedInteger A = h0;
            UnsignedInteger B = h1;
            UnsignedInteger C = h2;
            UnsignedInteger D = h3;
            UnsignedInteger E = h4;
            UnsignedInteger A_ = h0;
            UnsignedInteger B_ = h1;
            UnsignedInteger C_ = h2;
            UnsignedInteger D_ = h3;
            UnsignedInteger E_ = h4;
            for (int j=0; j<80;j++){
                T=SHL((A.plus(F(j,B,C,D).plus(M[i][r[j]].plus(K(j))))),s[j]).plus(E);
                A=E;
                E=D;
                D=SHL(C,10);
                C=B;
                B=T;
                T=SHL((A_.plus(F(79-j,B_,C_,D_).plus(M[i][rshtr[j]].plus(Ksht(j))))),sshtr[j]).plus(E_);
                A_=E_;
                E_=D_;
                D_=SHL(C_,10);
                C_=B_;
                B_=T;
            }
            T=h1.plus(C.plus(D_));
            h1=h2.plus(D.plus(E_));
            h2=h3.plus(E.plus(A_));
            h3=h4.plus(A.plus(B_));
            h4=h0.plus(B.plus(C_));
            h0=T;
        }
        System.out.println(h0.toString(16)+" "+h1.toString(16)+" "+h2.toString(16)+" "+h3.toString(16)+" "+h4.toString(16)+" " );

    }

    public static UnsignedInteger SHL (UnsignedInteger N , int i){
        String str = N.toString(2);
        String left_part = str.substring(0,i);
        String right_part = str.substring(i,str.length());

        str = right_part + left_part;

        return  UnsignedInteger.valueOf(str,2);
    }
    public static UnsignedInteger NOT (UnsignedInteger x){
        String str = x.toString(2);
        str=str.replace("0","2");
        str=str.replace('1','0');
        str=str.replace('2','1');
        return UnsignedInteger.valueOf(str,2);

    }
    public static UnsignedInteger OR ( UnsignedInteger x,UnsignedInteger y ){
        StringBuilder str1 = new StringBuilder();
        str1.append(x.toString(2));
        StringBuilder str2 = new StringBuilder();
        str2.append(y.toString(2));
        StringBuilder str3 = new StringBuilder();
        if (str1.length()>str2.length())
            while ( str1.length()!=str2.length())
                str2.insert(0,"0");
        else
            while ( str1.length()!=str2.length())
                str1.insert(0,"0");


        for (int i=0;i<str1.length();i++)
            if (str1.charAt(i)=='1' | str2.charAt(i)=='1') str3.append('1');
            else str3.append('0');
        return UnsignedInteger.valueOf(str3.toString(),2);


    }
    public static UnsignedInteger AND ( UnsignedInteger x,UnsignedInteger y ){
        StringBuilder str1 = new StringBuilder();
        str1.append(x.toString(2));
        StringBuilder str2 = new StringBuilder();
        str2.append(y.toString(2));
        StringBuilder str3 = new StringBuilder();
        if (str1.length()>str2.length())
            while ( str1.length()!=str2.length())
                str2.insert(0,"0");
        else
            while ( str1.length()!=str2.length())
                str1.insert(0,"0");


        for (int i=0;i<str1.length();i++)
            if (str1.charAt(i)=='1' & str2.charAt(i)=='1')str3.append('1');
            else str3.append('0');
        return UnsignedInteger.valueOf(str3.toString(),2);


    }
    public static UnsignedInteger XOR ( UnsignedInteger x,UnsignedInteger y ){
        StringBuilder str1 = new StringBuilder();
        str1.append(x.toString(2));
        StringBuilder str2 = new StringBuilder();
        str2.append(y.toString(2));
        StringBuilder str3 = new StringBuilder();
        if (str1.length()>str2.length())
            while ( str1.length()!=str2.length())
                str2.insert(0,"0");
        else
            while ( str1.length()!=str2.length())
                str1.insert(0,"0");


        for (int i=0;i<str1.length();i++)
            if (str1.charAt(i)!=str2.charAt(i))str3.append('1');
            else str3.append('0');
        UnsignedInteger u = UnsignedInteger.valueOf(str3.toString(),2);
        return UnsignedInteger.valueOf(str3.toString(),2);


    }

    public static UnsignedInteger F (int j, UnsignedInteger x , UnsignedInteger y, UnsignedInteger z){
        if (j<=15){
            return  XOR(x,XOR(y,z));
        }
        if(j<=31){
             return OR(AND(x,y) ,AND(NOT(x),z));
        }
        if(j<=47 ){
            return XOR(z,OR(x,NOT(y)));
        }
        if (j<=63){
            return OR(AND(x,z),AND(y,NOT(z)));
        }
        if(j<=79){
            return XOR(x,OR(y,NOT(z)));
        }
        System.err.println("Error F");
        return UnsignedInteger.ZERO;
    }

    public static UnsignedInteger K (int j){
        if (j<=15){
            return  UnsignedInteger.valueOf("00000000",16);
        }
        if(j<=31){
            return  UnsignedInteger.valueOf("5A827999",16);
        }
        if(j<=47 ){
            return  UnsignedInteger.valueOf("6ED9EBA1",16);
        }
        if (j<=63){
            return  UnsignedInteger.valueOf("8F1BBCDC",16);
        }
        if(j<=79){
            return  UnsignedInteger.valueOf("A953FD4E",16);
        }
        System.err.println("Error K");
        return UnsignedInteger.ZERO;
    }
    public static UnsignedInteger Ksht (int j){
        if (j<=15){
            return  UnsignedInteger.valueOf("50A28BE6",16);
        }
        if(j<=31){
            return  UnsignedInteger.valueOf("5C4DD124",16);
        }
        if(j<=47 ){
            return  UnsignedInteger.valueOf("6D703EF3",16);
        }
        if (j<=63){
            return  UnsignedInteger.valueOf("7A6D76E9",16);
        }
        if(j<=79){
            return  UnsignedInteger.valueOf("00000000",16);
        }
        System.err.println("Error Ksht");
        return UnsignedInteger.ZERO;
    }

}
