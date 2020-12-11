/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiments.splashScreen;

/**
 *
 * @author Nacer Salah Eddine
 */
public class IntegerUtil {
    public static String formatFixedWidth(int value, int width) {
        char[] chars;
        int i;
        int c;

        chars = new char[width];
        for (i = 0; i < width; i++) {
            c = value % 10;
            chars[width-i-1] = (char)('0' + c);
            value = value / 10;
        }
        return new String(chars);
    }
    public static void main(String[] arg){
       IntegerUtil i = new IntegerUtil();
       
        System.out.print(i.formatFixedWidth(1,2));
        System.out.print(i.formatFixedWidth(2,2));
        System.out.print(i.formatFixedWidth(3,2));
        
        
        
    }
}