public class Main {
    public static void main(String[] args) {
        int a = 0x11111111;
        int b = 0x01020304;
        int c = 0x9b8d6f43;
        int d = 0x01234567;

        quarterRound(a,b,c,d);
    }

    public static void quarterRound(int a, int b, int c, int d) {

        System.out.println("a: " + Integer.toHexString(a) + "\nb: " + Integer.toHexString(b) + "\nc: " + Integer.toHexString(c) + "\nd: " + Integer.toHexString(d));

        for (int i = 0; i < 1; i++) {
            a += b;
            d ^= a;
            d = Integer.rotateLeft(d, 16);

            c += d;
            b ^= c;
            b = Integer.rotateLeft(b, 12);

            a += b;
            d ^= a;
            d = Integer.rotateLeft(d, 8);

            c += d;
            b ^= c;
            b = Integer.rotateLeft(b, 7);
        }

        System.out.println();
        System.out.println("a: " + Integer.toHexString(a) + "\nb: " + Integer.toHexString(b) + "\nc: " + Integer.toHexString(c) + "\nd: " + Integer.toHexString(d));

    }
}
