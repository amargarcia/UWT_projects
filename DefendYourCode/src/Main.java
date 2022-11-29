import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static String firstName, lastName, hashedPassword, inputFileText, outputFile, inputFileName;
    private static int firstInt, secondInt, sumOfInts, prodOfInts;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
            firstName = firstName();
            lastName = lastName();
            getInts();
            hashedPassword = password();
            inputFileText = inputFile();
            outputFile = outputFile();
            writeToFile(firstName, lastName, firstInt, secondInt, sumOfInts, prodOfInts, inputFileText,
                    outputFile, inputFileName);
    }


    private static String firstName() {
        String name;
        do {
            System.out.print("""
                ** Enter your first name. **
                - Name should be no more than 50 characters.
                - First name should start with an uppercase letter and followed by lower case letters.
                \tPrompt will continue until correct format is supplied.
                ->\s""");
            name = sc.nextLine();

        } while (!verifyName(name));
        System.out.println("First name accepted.\n\n");
        return name;
    }
    private static String lastName() {
        String name;
        do {
            System.out.print("""
                ** Enter your last name. **
                - Name should be no more than 50 characters.
                - Last name should start with an uppercase letter and followed by lower case letters.
                - Hyphenated last names are not accepted.
                - Special characters are not accepted.
                - All names should be only capitalized in the beginning (Mcdonald, Oreilly)
                \tPrompt will continue until correct format is supplied.
                ->\s""");
            name = sc.nextLine();

        } while (!verifyName(name));
        System.out.println("Last name accepted.\n\n");
        return name;
    }
    private static int firstInt() {
        do {
            System.out.print("""
                    ** Enter first int. **
                    - Int should be between -2147483648 and 2147483647.
                    - Do not include commas.
                    \tPrompt will continue until correct format is supplied.
                    ->\s""");
            try {
                int num = Integer.parseInt(sc.nextLine());
                System.out.println("First integer accepted.\n\n");
                return num;
            } catch (Exception e) {
                System.out.println("Invalid input.\n");
            }
        } while (true);
    }
    private static int secondInt(){
        do {
            System.out.print("""
                    ** Enter second int. **
                    - Int should be between -2147483648 and 2147483647.
                    - Ints should stay within the specified range when adding AND multiplying the two ints.
                    - Do not include commas.
                    \tPrompt will continue until correct format is supplied.
                    ->\s""");
            try {
                int num = Integer.parseInt(sc.nextLine());
                System.out.println("Second integer accepted.\n");
                return num;
            } catch (Exception e) {
                System.out.println("Invalid input.\n");
            }
        } while (true);
    }
    private static void getInts() {
        do {
            firstInt = firstInt();
            secondInt = secondInt();


            if ((long)firstInt + secondInt > Integer.MAX_VALUE || (long)firstInt * secondInt > Integer.MAX_VALUE
                || (long)firstInt + secondInt < Integer.MIN_VALUE || (long)firstInt * secondInt < Integer.MIN_VALUE) {
                System.out.println("Integer overflow.\n");
            } else {
                sumOfInts = firstInt + secondInt;
                prodOfInts = firstInt * secondInt;
                break;
            }
        } while (true);
    }
    private static String inputFile() {
        String fileText = "", line;
        while (true) {
            do {
                System.out.print("""
                        ** Enter the absolute path of the input file name. **
                        - File names must start with a letter.
                        - File names can contain letters, numbers, and some special characters (-, _).
                        - Backslashes are not accepted.
                        - Accepted file extension is .txt.
                        - File names will be at least one letter and no more than 15 total characters.
                        - Paths should not require root access.
                        \tPrompt will continue until correct format is supplied.
                        ->\s""");
                inputFileName = sc.nextLine();

            } while (!verifyInFileType(inputFileName));
            System.out.println("Filename accepted.\n\n");
            try {
                FileReader file = new FileReader(inputFileName);
                BufferedReader buff = new BufferedReader(file);

                line = buff.readLine();
                while (line != null) {
                    fileText += line + "\n";
                    line = buff.readLine();
                }
                buff.close();
                break;

            } catch (Exception e) {
                System.out.println("File not found");
            }
        }
        return fileText;
    }
    private static String outputFile() {
        String fileName;
        do {
            System.out.print("""
                    ** Enter the output file name. **
                    - File names must start with a letter.
                    - File names can contain letters, numbers, and some special characters (-, _).
                    - Accepted file extension is .txt.
                    - File names will be at least one letter and no more than 15 total characters.
                    \tPrompt will continue until correct format is supplied.
                    ->\s""");
            fileName = sc.nextLine();
        } while (!verifyOutFileType(fileName));
        System.out.println("Filename accepted.\n\n");
        return fileName;
    }
    private static void writeToFile(String fN, String lN, int int1, int int2, int sum, int prod, String inF,
                                   String outF, String inFileName) {
        try {
            FileWriter file = new FileWriter(outF);
            file.write("User's first name: " + fN + "\n");
            file.write("User's last name: " + lN + "\n");
            file.write("First int: " + int1 + "\n");
            file.write("Second int: " + int2 + "\n");
            file.write("Sum of two ints:" + sum + "\n");
            file.write("Product of two ints: " + prod + "\n");
            file.write("Input file name: " + inFileName);
            file.write("Contents of input file: " + inF + "\n");
            file.close();
        }catch (Exception e) {
            System.out.println("Unable to write to file.");
        }
    }
    private static String password() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password;
        String hashed1 = "";
        String hashed2 = ".";
        while (!verifyHashes(hashed1, hashed2)) {
            do {
                System.out.print("""
                        ** Enter a password. **
                        - Password should be at least 8 characters and max of 15.
                        - Password should contain at least one uppercase, one lowercase, and one special character ?!.@#$%^&()
                        \tPrompt will continue until correct format is supplied.
                        ->\s""");
                password = sc.nextLine();
            } while (!verifyPassword(password));
            System.out.println("Password accepted.\n");
            byte[] saltedPW = saltPW();
            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltedPW, 65536, 128);
            hashed1 = hashPW(saltedPW, spec);

            storedHashedPW(hashed1);

            password = null;
            System.out.print("Verify password: ");
            password = sc.nextLine();
            spec = new PBEKeySpec(password.toCharArray(), saltedPW, 65536, 128);
            hashed2 = hashPW(saltedPW, spec);
            password = null;

            if (!getHashedPW().equals(hashed2)) {
                System.out.println("Passwords do not match.\n");
            }
        }
        System.out.println("Passwords match.\n\n");
        return hashed1;
    }
    private static boolean verifyName(String name) {
        return name.matches("^[A-Z][[a-z]*?]{0,49}$");
    }
    private static boolean verifyPassword(String pw) {
        return pw.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[?!.@#$%^&()])[A-Za-z\\d?!.@#$%^&()]{8,15}$");
    }
    private static byte[] saltPW() {
        byte[] salt = new byte[16];
        Random random = new Random();
        random.nextBytes(salt);
        return salt;
    }
    private static String hashPW(byte[] salt, KeySpec spec) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = keyFact.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder();
        return enc.encodeToString(hash);
    }
    private static boolean verifyHashes(String hash1, String hash2) {
        return hash2.equals(hash1);
    }
    private static boolean verifyInFileType(String fileType) {
        return fileType.matches("^[A-Z]:[/\\d\\w]*?/[[A-Za-z](\\d\\w)+]{1,15}.(txt)$");
    }
    private static boolean verifyOutFileType(String fileType) {
        return fileType.matches("^[[A-Za-z](\\d\\w)+]{1,15}.(txt)$");
    }

    private static void storedHashedPW(String hash) {
        try {
            FileWriter hashWrite = new FileWriter("pwHashed.pw");
            hashWrite.write(hash);
            hashWrite.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getHashedPW() {
        String returnLine = "";
        try {
            FileReader file = new FileReader("pwHashed.pw");
            BufferedReader buff = new BufferedReader(file);
            String line = buff.readLine();
            while (line != null) {
                returnLine += line ;
                line = buff.readLine();
            }
            buff.close();
        } catch (Exception e) {
            System.out.println("File not found");
        }
        return returnLine;
    }
}