public class Main {
    public static void main(String[] args) {

        // Add ChaCha States here
        int[] state = new int[]{0x61707865, 0x3320646e, 0x79622d32, 0x6b206574,
                0x03020100, 0x07060504, 0x0b0a0908, 0x0f0e0d0c,
                0x13121110, 0x17161514, 0x1b1a1918, 0x1f1e1d1c,
                0x00000001, 0x09000000, 0x4a000000, 0x00000000};

        // Run 20 rounds, 10 column rounds and 10 diagonal rounds
        for (int i = 0; i < 10; i++) {
            state = quarterRound(0, 4, 8, 12, state);
            state = quarterRound(1, 5, 9, 13, state);
            state = quarterRound(2, 6, 10, 14, state);
            state = quarterRound(3, 7, 11, 15, state);
            state = quarterRound(0, 5, 10, 15, state);
            state = quarterRound(1, 6, 11, 12, state);
            state = quarterRound(2, 7, 8, 13, state);
            state = quarterRound(3, 4, 9, 14, state);
        }
        // print new state
        for (int s : state) {
            System.out.println(Integer.toHexString(s));
        }
    }

    /**
     * ARX for ChaCha20
     * @param a
     * @param b
     * @param c
     * @param d
     * @param state
     * @return
     */
    public static int[] quarterRound(int a, int b, int c, int d, int[] state) {

        state[a] += state[b];
        state[d] ^= state[a];
        state[d] = Integer.rotateLeft(state[d], 16);

        state[c] += state[d];
        state[b] ^= state[c];
        state[b] = Integer.rotateLeft(state[b], 12);

        state[a] += state[b];
        state[d] ^= state[a];
        state[d] = Integer.rotateLeft(state[d], 8);

        state[c] += state[d];
        state[b] ^= state[c];
        state[b] = Integer.rotateLeft(state[b], 7);

        return state;
    }
}
