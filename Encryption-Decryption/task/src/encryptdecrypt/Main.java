package encryptdecrypt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    private static String mode;
    private static String data;
    private static int key;
    private static String algorithm;
    private static String inputFile;
    private static String outputFile;
    private static String result;

    public static void main(String[] args) {

        parseArguments(args);
        checkArguments();
        calculateResult();
        writeOutResult();
    }

    private static void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i += 2) {
            String value = args[i + 1];
            switch (args[i]) {
                case "-mode":
                    mode = value;
                    break;
                case "-data":
                    data = value;
                    break;
                case "-in":
                    inputFile = value;
                    break;
                case "-key":
                    key = Integer.parseInt(value);
                    break;
                case "-alg":
                    algorithm = value;
                    break;
                case "-out":
                    outputFile = value;
                    break;
                default:
                    System.out.println("Illegal argument");
            }
        }
    }

    private static void checkArguments() {
        if (mode.isBlank()) {
            mode = "enc";
        }

        if (algorithm.isBlank()) {
            algorithm = "shift";
        }

        if (!inputFile.isBlank()) {
            try {
                Scanner scanner = new Scanner(new File(inputFile));
                data = scanner.nextLine();
                scanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("File not found");
                exit(1);
            }
        }

        if (inputFile.isBlank() && data.isBlank()) {
            Scanner scanner = new Scanner(System.in);
            data = scanner.nextLine();
            key = scanner.nextInt();
        }

        if (data.isBlank()) {
            System.out.println("Data doesn't have a value");
            exit(1);
        }
    }

    private static void calculateResult() {
        Cryptor cryptor = new Cryptor();

        switch (algorithm) {
            case "shift":
                if (mode.equals("enc")) {
                    cryptor.setAlgorithm(new ShiftingEncryption());
                } else {
                    cryptor.setAlgorithm(new ShiftingDecryption());
                }
                break;
            case "unicode":
                if (mode.equals("enc")) {
                    cryptor.setAlgorithm(new UnicodeEncryption());
                } else {
                    cryptor.setAlgorithm(new UnicodeDecryption());
                }
                break;
        }
        result = cryptor.crypt(data, key);
    }

    private static void writeOutResult() {
        if (outputFile.isBlank()) {
            System.out.println(result);
        } else {
            try {
                FileWriter writer = new FileWriter(new File(outputFile));
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                System.out.println("Error writing result");
                exit(1);
            }
        }
    }
}