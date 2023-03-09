// Omar Twafshah 1191768
// Mohammad Halhuli 1191413
// Hanna Hinn 1190336

import java.awt.*;
import java.util.*;

public class Driver {

    public static int numberOfRegister = 6;
    static HashMap<Character, Integer> table = new HashMap<Character, Integer>();
    public static Token[][] tableWithNumber = new Token[8][8];

    public static int length = 0;


    public static void main(String[] args) {
        setTable();
        Scanner cs = new Scanner(System.in);
        System.out.println("Please Enter The key : ");
        String key = cs.nextLine();
        key = key.toLowerCase(Locale.ROOT);
        key = removeDuplicate(key);
        ArrayList<Integer> tapSequence = new ArrayList<Integer>();
        ArrayList<Integer> seedSequence = new ArrayList<Integer>();
        HashSet<Character> hashKey = new HashSet<Character>();
        ArrayList<Character> CharacterTable = new ArrayList<Character>();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            hashKey.add(c);
            CharacterTable.add(c);
            if (table.containsKey(c)) {
                seedSequence.add(table.get(c));
                int tap = table.get(c) % numberOfRegister;
                tapSequence.add(tap);
            } else {
                seedSequence.add(64);
                int tap = 64 % numberOfRegister;
                tapSequence.add(tap);

            }
        }
        for (Character c : table.keySet()) {
            if (!hashKey.contains(c)) {
                CharacterTable.add(c);
            }
        }
        ArrayList<Integer> LFSRNumber = new ArrayList<Integer>();
        HashSet<Integer> LFSRNumber2 = new HashSet<Integer>();
        int mm = 0;
        Random random = new Random();
        while (mm != 64) {
            int number = random.nextInt(64) + 11;
            if (!LFSRNumber2.contains(number)) {
                LFSRNumber2.add(number);
                LFSRNumber.add(number);
                mm++;
            }
        }
        int k = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tableWithNumber[i][j] = new Token(CharacterTable.get(k), LFSRNumber.get(k));
                k++;
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(tableWithNumber[i][j].toString() + "|");
            }
            System.out.println();
        }
        System.out.println("Please enter Plain Text : ");
        String plainText = cs.nextLine();
        String output = cipher(plainText);
        String decodedOutput = decode(output);
        System.out.print("Encrypted Message: ");
        System.out.println(output);
        System.out.println();
        System.out.print("Decrypted Message: ");
        System.out.println(decodedOutput);
    }

    private static Point getPoint(char c) {
        Point pt = new Point(0, 0);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (c == tableWithNumber[i][j].getValue())
                    pt = new Point(i, j);
        return pt;
    }

    private static Point getNumberPoint(String c) {
        Point pt = new Point(0, 0);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (Integer.parseInt(c) == tableWithNumber[i][j].getNumberLFSR())
                    pt = new Point(i, j);
        return pt;
    }

    private static String cipher(String in) {
        length = (int) in.length() / 2 + in.length() % 2;
        for (int i = 0; i < (length - 1); i++) {
            if (in.charAt(2 * i) == in.charAt(2 * i + 1)) {
                in = new StringBuffer(in).insert(2 * i + 1, 'X').toString();
                length = (int) in.length() / 2 + in.length() % 2;
            }
        }
        String[] digraph = new String[length];
        for (int j = 0; j < length; j++) {
            if (j == (length - 1) && in.length() / 2 == (length - 1))
                in = in + "X";
            digraph[j] = in.charAt(2 * j) + "" + in.charAt(2 * j + 1);
        }
        String out = "";
        String[] encDigraphs = new String[length];
        encDigraphs = encodeDigraph(digraph);
        for (int k = 0; k < length; k++)
            if (k == 0) {
                out = encDigraphs[k];
            } else
                out = out + "," + encDigraphs[k];
        return out;
    }

    private static String[] encodeDigraph(String di[]) {
        String[] encipher = new String[length];
        for (int i = 0; i < length; i++) {
            char a = di[i].charAt(0);
            char b = di[i].charAt(1);
            int r1 = (int) getPoint(a).getX();
            int r2 = (int) getPoint(b).getX();
            int c1 = (int) getPoint(a).getY();
            int c2 = (int) getPoint(b).getY();
            if (r1 == r2) {
                c1 = (c1 + 1) % 8;
                c2 = (c2 + 1) % 8;
            } else if (c1 == c2) {
                r1 = (r1 + 1) % 8;
                r2 = (r2 + 1) % 8;
            } else {
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }
            encipher[i] = tableWithNumber[r1][c1].getNumberLFSR() + "," + tableWithNumber[r2][c2].getNumberLFSR();
        }
        return encipher;
    }

    private static String decode(String out) {
        String[] encipher = out.split(",");
        String decoded = "";
        for (int i = 0; i < encipher.length-1; i+=2) {
            String a = encipher[i];
            String b = encipher[i+1];
            int r1 = (int) getNumberPoint(a).getX();
            int r2 = (int) getNumberPoint(b).getX();
            int c1 = (int) getNumberPoint(a).getY();
            int c2 = (int) getNumberPoint(b).getY();
            if (r1 == r2) {
                c1 = (c1 + 7) % 8;
                c2 = (c2 + 7) % 8;
            } else if (c1 == c2) {
                r1 = (r1 + 7) % 8;
                r2 = (r2 + 7) % 8;
            } else {
                int temp = c1;
                c1 = c2;
                c2 = temp;
            }
            decoded = decoded + tableWithNumber[r1][c1].getValue() + tableWithNumber[r2][c2].getValue();
        }
        return decoded;
    }

    private static void setTable() {
        table.put('0', 0);
        table.put('1', 1);
        table.put('2', 2);
        table.put('3', 3);
        table.put('4', 4);
        table.put('5', 5);
        table.put('6', 6);
        table.put('7', 7);
        table.put('8', 8);
        table.put('9', 9);
        table.put('a', 10);
        table.put('b', 11);
        table.put('c', 12);
        table.put('d', 13);
        table.put('e', 14);
        table.put('f', 15);
        table.put('g', 16);
        table.put('h', 17);
        table.put('i', 18);
        table.put('j', 19);
        table.put('k', 20);
        table.put('l', 21);
        table.put('m', 22);
        table.put('n', 23);
        table.put('o', 24);
        table.put('p', 25);
        table.put('q', 26);
        table.put('r', 27);
        table.put('s', 28);
        table.put('t', 29);
        table.put('u', 30);
        table.put('v', 31);
        table.put('w', 32);
        table.put('x', 33);
        table.put('y', 34);
        table.put('z', 35);
        table.put(' ', 36);
        table.put('!', 37);
        table.put('+', 38);
        table.put('-', 39);
        table.put('*', 40);
        table.put('/', 41);
        table.put('=', 42);
        table.put(':', 43);
        table.put('$', 44);
        table.put(';', 45);
        table.put('?', 46);
        table.put('@', 47);
        table.put('%', 48);
        table.put('^', 49);
        table.put('&', 50);
        table.put('{', 51);
        table.put('_', 52);
        table.put('#', 53);
        table.put('.', 54);
        table.put(',', 55);
        table.put('"', 56);
        table.put('<', 57);
        table.put('[', 58);
        table.put('>', 59);
        table.put(']', 60);
        table.put('(', 61);
        table.put(')', 62);
        table.put('}', 63);
    }

    public static String removeDuplicate(String s) {
        int j, index = 0;
        char c[] = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            for (j = 0; j < i; j++) {
                if (c[i] == c[j])
                    break;
            }
            if (i == j)
                c[index++] = c[i];
        }
        s = new String((Arrays.copyOf(c, index)));
        return s;
    }
}
