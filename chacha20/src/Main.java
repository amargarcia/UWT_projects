public class Main {
    public static void main(String[] args) {
        
    }

    public static void quarterRound(char a, char b, char c, char d) {
        a += b;
        d ^= a;
        Integer.rotateLeft(d, 16);

        c += d;
        b ^= c;
        Integer.rotateLeft(b, 12);

        a += b;
        d ^= a;
        Integer.rotateLeft(d, 8);

        c += d;
        b ^= c;
        Integer.rotateLeft(b, 17);
    }
}