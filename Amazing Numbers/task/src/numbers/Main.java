package numbers;

import java.util.*;

public class Main {
    public static void main(String[] args) {
//        write your code here
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Welcome to Amazing Numbers!" +
                "\n" +
                "Supported requests:\n"+
                "- enter a natural number to know its properties;\n" +
                "- enter two natural numbers to obtain the properties of the list:\n" +
                "  * the first parameter represents a starting number;\n" +
                "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
                "- two natural numbers and a property to search for;\n" +
                "- a property preceded by minus must not be present in numbers;" +
                "- separate the parameters with one space;\n" +
                "- two natural numbers and two properties to search for;" +
                "- enter 0 to exit.\n");
        while(true) {
            System.out.println("Enter a request: ");
            String str = scanner.nextLine().toUpperCase();
            ArrayList<String> tokens = new ArrayList<>(Arrays.asList(str.split("\\s+")));

            ArrayList<String> listOfProperties = new ArrayList<>(Arrays.asList("EVEN", "ODD", "BUZZ", "DUCK", "PALINDROMIC", "GAPFUL", "SPY", "SUNNY", "SQUARE", "JUMPING", "HAPPY", "SAD"));
            if(tokens.get(0).equals("0")){
                break;
            }
            // -- Error checking
            if(!tokens.get(0).chars().allMatch(Character::isDigit) || tokens.get(0).contains("-")){
                System.out.println("The first parameter should be a natural number or zero.\n");
                continue;
            }
            if(tokens.size() >= 2){
                if(!tokens.get(1).chars().allMatch(Character::isDigit) || tokens.get(1).contains("-")){
                    System.out.println("The second parameter should be a natural number.\n");
                    continue;
                }
            }
            if(tokens.size() >= 3){
                if(!checkProperties(tokens, listOfProperties)){
                    continue;
                }
            }
            if(tokens.size() >= 4){

                checkProperties(tokens,listOfProperties);

                List<List<String>> mutuallyExclusivePais = Arrays.asList(
                        Arrays.asList("EVEN", "ODD"),
                        Arrays.asList("SQUARE", "SUNNY"),
                        Arrays.asList("DUCK", "SPY"),
                        Arrays.asList("-SAD", "HAPPY"),
                        Arrays.asList("SAD", "-HAPPY"),
                        Arrays.asList("-EVEN", "-ODD"),
                        Arrays.asList("SAD", "-HAPPY"),
                        Arrays.asList("-SAD", "SAD"),
                        Arrays.asList("-HAPPY", "HAPPY"),
                        Arrays.asList("-EVEN", "EVEN"),
                        Arrays.asList("-ODD", "ODD"),
                        Arrays.asList("-DUCK", "DUCK")

                );
                if(isMutuallyExclusiveProperties(mutuallyExclusivePais, tokens)){
                    continue;
                }
            }
            // End of Error checking
            // Solo number
            if(tokens.size() == 1) {

                long input = Long.parseLong(str);
                StringBuilder stringBuilder = new StringBuilder(str);

                for (int i = str.length() - 3; i > 0; i -= 3) {
                    stringBuilder.insert(i, ",");
                }
                str = stringBuilder.toString();
                System.out.println("Properties of " + str);
                System.out.println("buzz: " + isBuzzNumber(input));
                System.out.println("duck: " + isDuckNumber(input));
                System.out.println("palindromic: " + isPalindromic(input));
                System.out.println("gapful: " + isGapFull(input));
                System.out.println("even: " + isEven(input));
                System.out.println("odd: " + isOdd(input));
                System.out.println("spy: " + isSpy(input));
                System.out.println("sunny: " + isSquare((input+1)));
                System.out.println("square: " + isSquare(input));
                System.out.println("jumping: " + isJumping(input));
                System.out.println("happy: " + isHappy(input));
                System.out.println("sad: " + !isHappy(input));
            } else if(tokens.size() == 2){

                long input = Long.parseLong(tokens.get(0));
                long iterator = Long.parseLong(tokens.get(1));
                getListStringProperties(input,iterator);

            }else if( tokens.size() >= 3) {
                long input = Long.parseLong(tokens.get(0));
                long iterator = Long.parseLong(tokens.get(1));

                getStringProperties(input, tokens, iterator);

            }
        }
    }

    public static boolean isMutuallyExclusiveProperties(List<List<String>> mutuallyExclusivePais, ArrayList<String> tokens){
        for(List<String> pair : mutuallyExclusivePais){
            if(tokens.containsAll(pair)){
                System.out.println("The request contains mutually exclusive properties: " + pair);
                System.out.println("There are no numbers with these properties.\n");
                return true;
            }
        }
        return false;
    }
    public static boolean checkProperties(ArrayList<String> tokens, ArrayList<String> listOfProperties){
        ArrayList<String> tokensWithoutMinus = new ArrayList<>(tokens);
        for(int i = 2; i < tokensWithoutMinus.size(); i++){
            if(tokensWithoutMinus.get(i).charAt(0) == '-'){
                tokensWithoutMinus.set(i,new StringBuilder(tokensWithoutMinus.get(i)).deleteCharAt(0).toString());
            }
            if(!listOfProperties.contains(tokensWithoutMinus.get(i))){
                System.out.printf("The property [%s] is wrong \n", tokensWithoutMinus.get(i));
                System.out.println("Available properties: " + listOfProperties);
                return false;
            }
        }

        return true;
    }
    public static void getStringProperties(long input, ArrayList<String> tokens, long iterator){

        // Creation of two Array : One with properties the user want and an another with properties excluded
        ArrayList<String> listPropertiesToGet = new ArrayList<>();
        ArrayList<String> listToNotPropertiesToGet = new ArrayList<>();

        for(int i = 2; i < tokens.size(); i++){
            if(!tokens.get(i).contains("-")){
                listPropertiesToGet.add(tokens.get(i));
            } else {
                listToNotPropertiesToGet.add(new StringBuilder(tokens.get(i)).deleteCharAt(0).toString() );
            }
        }
        //---
        for(int i = 0; i < iterator; ){
            ArrayList<String> list = getProperties(input);
            if(list.containsAll(listPropertiesToGet) && listToNotPropertiesToGet.isEmpty()){
                System.out.println(input + " is " + list);
                i++;
            } else if(  listPropertiesToGet.isEmpty() && Collections.disjoint(list, listToNotPropertiesToGet)){
                System.out.println(input + " is " + list);
                i++;
            } else if(list.containsAll(listPropertiesToGet) && Collections.disjoint(list, listToNotPropertiesToGet)) {

                System.out.println(input + " is " + list);
                i++;
            }
            input++;
        }
    }
    public static void getListStringProperties(long input, long iterator){

        for(long i = input; i < input + iterator; i++){

            ArrayList<String> properties = getProperties(i);
            StringBuilder strProperties = new StringBuilder(i + " is ");
            for(int j = 0; j < properties.size(); j++){

                strProperties.append(properties.get(j));
                if(j + 1 < properties.size()){
                    strProperties.append(", ");
                }
            }
            System.out.println(strProperties);
        }
    }
    public static ArrayList<String> getProperties(long i){
        ArrayList<String> properties = new ArrayList<>();

        if(isBuzzNumber(i)){
            properties.add("BUZZ");
        }
        if(isDuckNumber(i)){
            properties.add("DUCK");
        }
        if(isPalindromic(i)){
            properties.add("PALINDROMIC");
        }
        if(isGapFull(i)){
            properties.add("GAPFUL");
        }
        if(isSpy(i)){
            properties.add("SPY");
        }
        if(isSquare(i)){
            properties.add("SQUARE");
        }
        if(isSquare(i + 1)){
            properties.add("SUNNY");
        }
        if(isJumping(i)){
            properties.add("JUMPING");
        }
        if(isHappy(i)){
            properties.add("HAPPY");
        } else {
            properties.add("SAD");
        }
        if(isEven(i)){
            properties.add("EVEN");
        } else {
            properties.add("ODD");
        }
        return properties;
    }
    public static boolean isEven(long input){

        if(input % 2 == 0){
            return true;
        } else {
            return false;
        }
    }
    public static boolean isOdd(long input){
        if(input % 2 == 0){
            return false;
        } else {
            return true;
        }
    }

    public static boolean isBuzzNumber(long input){

        if(input % 10 == 7 && input % 7 == 0){
            return true;

        }
        if(input % 10 == 7){
            return true;

        } else if (input % 7 == 0){
            return true;

        } else {
            return false;

        }

    }

    public static boolean isDuckNumber(long input){
        String str = String.valueOf(input);
        if(str.contains("0")){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPalindromic(long input){

        String str = String.valueOf(input);

        if(str.isEmpty() ){
            return false;
        } else if (str.length() == 1){
            return true;
        }
        String firstHalf = str.substring(0, str.length() / 2);
        String secondHalf = "";
        if(str.length() % 2 == 0){

             secondHalf = str.substring(str.length() / 2 );
        } else {
             secondHalf = str.substring(str.length() / 2  + 1);
        }
        secondHalf = new StringBuilder(secondHalf).reverse().toString();

        if(firstHalf.equals(secondHalf)){
            return true;
        } else {
            return false;
        }

    }

    public static boolean isGapFull(long input){
        String str = String.valueOf(input);

        if(str.length() < 3){
            return false;
        }
        int gapNumber = Integer.parseInt(str.substring(0,1) + str.substring(str.length() - 1));

        if(input % gapNumber == 0){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSpy(long input){
        ArrayList<Long> listOfNumber = new ArrayList<>();
        int lengthOfInput = String.valueOf(input).length();
        long sum = 0;
        long product = 1;
        for(int i = 0; i < lengthOfInput;i++){
            long temp = input % 10;
            sum += temp;
            product *= temp;
            input = input / 10;

        }

        return sum == product;
    }

    public static boolean isSquare(long input){

        if(input == 1){
            return true;
        }
        return Math.sqrt(input) == Math.round(Math.sqrt(input));
    }

    public static boolean isJumping(long input){
        ArrayList<Long> list = new ArrayList<>();
        long lengthInput = String.valueOf(input).length();

        for(int i = 0; i < lengthInput; i++){
            list.add(input % 10);
            input /= 10;
        }

        for(int i = 0; i < list.size()-1; i++){

            if(list.get(i) + 1 == list.get(i+1)
            || list.get(i) - 1 == list.get(i + 1)){

            } else {
                return false;
            }
        }
        return true;

    }

    public static boolean isHappy(long input){

        long sumDigitSquare = 0;
        long remainder ;

        while(true){
            while(input > 0){
                remainder = input % 10;
                sumDigitSquare += (remainder * remainder);
                input /= 10;
            }
            if(sumDigitSquare == 1 || sumDigitSquare ==4){
                break;
            } else {
                input = sumDigitSquare;
                sumDigitSquare = 0;
            }


        }
        return 1 == sumDigitSquare;
    }

}
