import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Encrypt {
    public static void main(String[] arg) {

        byte[] key = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
                0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f};
        byte[] nonce = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x4a, 0x00, 0x00, 0x00, 0x00};
        int counter = 1;
        String text = "Ladies and Gentlemen of the class of '99: If I could offer you only one tip for the future, sunscreen would be it.";

//        chacha20(key, counter, nonce, text);
//
    }
//
//    public static String chacha20(byte[] key, int counter, byte[] nonce, String text) {
//        String encryptedMsg;
//        String keyStream;
//
//        for (int i = 0; i < Math.floor(text.length()/64) - 1; i++) {
//            keyStream =
//
//        }
//
//        return  encryptedMsg;
//    }
//
//    public ChaCha20() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
//        Cipher cipher = Cipher.getInstance("ChaCha20/ECB/NoPadding", "IAIK");
//    }
}
