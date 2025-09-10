import java.io.*;
import java.math.BigInteger;
import java.util.regex.*;

public class PolynomialConstant {
    public static void main(String[] args) throws Exception {
        // Read the entire JSON file into a string
        BufferedReader br = new BufferedReader(new FileReader("input.json"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        String jsonText = sb.toString();

        // Extract n and k using regex
        int n = Integer.parseInt(getValue(jsonText, "\"n\"\\s*:\\s*(\\d+)"));
        int k = Integer.parseInt(getValue(jsonText, "\"k\"\\s*:\\s*(\\d+)"));
        int degree = k - 1;

        BigInteger constant = BigInteger.ONE;

        // Loop through first "degree" roots
        for (int i = 1; i <= degree; i++) {
            String blockRegex = "\"" + i + "\"\\s*:\\s*\\{([^}]*)\\}";
            Matcher m = Pattern.compile(blockRegex).matcher(jsonText);
            if (m.find()) {
                String block = m.group(1);
                String baseStr = getValue(block, "\"base\"\\s*:\\s*\"?(\\w+)\"?");
                String valStr = getValue(block, "\"value\"\\s*:\\s*\"?([0-9a-zA-Z]+)\"?");

                int base = Integer.parseInt(baseStr);
                BigInteger rootValue = new BigInteger(valStr, base);
                constant = constant.multiply(rootValue);
            }
        }

        // Adjust sign depending on degree
        if (degree % 2 == 1) {
            constant = constant.negate();
        }

        System.out.println("Constant term = " + constant.toString());
    }

    // Utility: extract first regex group
    private static String getValue(String text, String regex) {
        Matcher m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        throw new RuntimeException("Pattern not found: " + regex);
    }
}
