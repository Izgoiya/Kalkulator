import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите арифметическое выражение: ");
        String input = scanner.nextLine();

        try {
            String result = calc(input);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("throws Exception " + e.getMessage());
        }
    }

    public static String calc(String input) throws Exception {
        String[] parts = input.trim().split("\\s+");

        if (parts.length != 3) {
            if (parts.length == 1) {
                throw new Exception("//т.к. строка не является математической операцией");
            } else {
                throw new Exception("//т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
            }
        }

        String operand1 = parts[0];
        String operator = parts[1];
        String operand2 = parts[2];

        boolean isArabic = isArabicNumber(operand1) && isArabicNumber(operand2);
        boolean isRoman = isRomanNumber(operand1) && isRomanNumber(operand2);

        if (!(isArabic ^ isRoman)) {
            throw new Exception("//т.к. используются одновременно разные системы счисления");
        }

        if (!operator.matches("[+\\-*/]")) {
            throw new Exception("//т.к. формат математической операции не удовлетворяет заданию - два операнда и один оператор (+, -, /, *)");
        }

        int num1 = isArabic ? Integer.parseInt(operand1) : romanToArabic(operand1);
        int num2 = isArabic ? Integer.parseInt(operand2) : romanToArabic(operand2);

        validateNumbers(num1, num2, isRoman, operator);

        int result = 0;

        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                result = num1 / num2;
                break;
        }

        String output = isArabic ? String.valueOf(result) : arabicToRoman(result);

        return output;
    }

    private static boolean isArabicNumber(String input) {
        try {
            int num = Integer.parseInt(input);
            return num >= 1 && num <= 10;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isRomanNumber(String input) {
        String romanRegex = "^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$";
        return input.matches(romanRegex);
    }

    private static void validateNumbers(int num1, int num2, boolean isRoman, String operator) throws Exception {
        if (isRoman && (num1 <= 0 || num2 <= 0)) {
            throw new Exception("В римской системе нет отрицательных чисел или нуля");
        }

        if (isRoman && num2 > num1 && operator.equals("-")) {
            throw new Exception("//т.к. в римской системе нет отрицательных чисел");
        }
    }

    private static int romanToArabic(String input) {
        Map<Character, Integer> romanMap = new HashMap<>();
        romanMap.put('I', 1);
        romanMap.put('V', 5);
        romanMap.put('X', 10);
        romanMap.put('L', 50);
        romanMap.put('C', 100);
        romanMap.put('D', 500);
        romanMap.put('M', 1000);

        int result = 0;
        int prevValue = 0;

        for (int i = input.length() - 1; i >= 0; i--) {
            int currValue = romanMap.get(input.charAt(i));

            if (currValue < prevValue) {
                result -= currValue;
            } else {
                result += currValue;
            }

            prevValue = currValue;
        }

        return result;
    }

    private static String arabicToRoman(int input) {
        StringBuilder result = new StringBuilder();

        int[] numbers = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        for (int i = 0; i < numbers.length; i++) {
            while (input >= numbers[i]) {
                result.append(symbols[i]);
                input -= numbers[i];
            }
        }

        return result.toString();
    }
}