package converter;

import converter.exceptions.MalformedNumberException;
import converter.exceptions.ValueOutOfBoundsException;


/**
 * This class implements a converter that takes a string that represents a number in either the
 * Elbonian or Arabic numeral form. This class has methods that will return a value in the chosen form.
 *
 * @version 3/18/17
 */
public class ElbonianArabicConverter {
    // A string that holds the number (Elbonian or Arabic) you would like to convert
    private final String number;

    /**
     * Constructor for the ElbonianArabic class that takes a string. The string should contain a valid
     * Elbonian or Arabic numeral. The String can have leading or trailing spaces. But there should be no
     * spaces within the actual number (ie. "9 9" is not ok, but " 99 " is ok). If the String is an Arabic
     * number it should be checked to make sure it is within the Elbonian number systems bounds. If the
     * number is Elbonian, it must be a valid Elbonian representation of a number.
     *
     * @param number A string that represents either a Elbonian or Arabic number.
     * @throws ValueOutOfBoundsException Thrown if the value is an Arabic integer that cannot be represented
     * in the Elbonian number system.
     * @throws MalformedNumberException Thrown if the value is an Elbonian number that does not conform
     * to the rules of the Elbonian number system or any other error in Arabic number input.
	 * Leading and trailing spaces should not throw an error.
     */
    public ElbonianArabicConverter(String number) throws MalformedNumberException, ValueOutOfBoundsException {

        String CleanedNumber = number.trim();

        if (CleanedNumber.isEmpty()){
            throw new MalformedNumberException(CleanedNumber);
        }
        if (!(CleanedNumber.equals("0"))){
            if(CleanedNumber.startsWith("0")){
                throw new MalformedNumberException(CleanedNumber);
            }
        }
        if (CleanedNumber.matches("-?\\d+(\\.\\d+)?")){
            if (Double.parseDouble(CleanedNumber) < 10000 && Double.parseDouble(CleanedNumber) > 0){
                if (Double.parseDouble(CleanedNumber) % 1 != 0){
                    throw new MalformedNumberException(CleanedNumber);
                }
                else{
                    this.number = CleanedNumber;
                }
            }
            else{
                throw new ValueOutOfBoundsException("Value is out of bounds. Enter value between 0 and 9999.");
            }
        }
        else {
            if (CleanedNumber.matches("^[a-zA-Z]*$")) {
                if (!repeatedCertainTimes(CleanedNumber)) {
                    throw new MalformedNumberException("Values repeated illegal number of times");
                } else if (!charThenNoOtherChar(CleanedNumber, "n", "M") || !charThenNoOtherChar(CleanedNumber, "d", "C") || !charThenNoOtherChar(CleanedNumber, "l", "X") ||
                        !charThenNoOtherChar(CleanedNumber, "v", "I")) {
                    throw new MalformedNumberException("Certain Values can't be used with other values.");
                } else if (!GreatestToLeast(CleanedNumber)) {
                    throw new MalformedNumberException("Letters should be valid Elbonian characters and arranged from greatest to least.");
                } else {
                    this.number = CleanedNumber;
                }
            }
            else{
                throw new MalformedNumberException(CleanedNumber);
            }
        }
    }

    /**
     * Converts the number to an Arabic numeral or returns the current value as an int if it is already
     * in the Arabic form.
     *
     * @return An arabic value
     */
    public int toArabic(){
        String[] aListOfCharacters = number.split("");
        int arabicNum = 0;
        for(int i = 0; i < aListOfCharacters.length; i++){
            String c = aListOfCharacters[i];
            if(c.equals("M")) {
                arabicNum += 1000;
            }
            else if(c.equals("N")) {
                arabicNum += 5000;
            }
            else if(c.equals("n")) {
                arabicNum += 4000;
            }
            else if(c.equals("C")) {
                arabicNum += 100;
            }
            else if(c.equals("D")) {
                arabicNum += 500;
            }
            else if(c.equals("d")) {
                arabicNum += 400;
            }
            else if(c.equals("X")) {
                arabicNum += 10;
            }
            else if(c.equals("L")) {
                arabicNum += 50;
            }
            else if(c.equals("l")) {
                arabicNum += 40;
            }
            else if(c.equals("I")) {
                arabicNum += 1;
            }
            else if(c.equals("V")) {
                arabicNum += 5;
            }
            else if(c.equals("v")) {
                arabicNum += 4;
            }
        }
        return arabicNum;
    }

    /**
     * Converts the number to an Elbonian numeral or returns the current value if it is already in the Elbonian form.
     *
     * @return An Elbonian value
     */
    public String toElbonian() {
        int arabicNum = Integer.parseInt(number);
        String elbonianNum = "";
        while (arabicNum!=0){
            if (arabicNum >= 5000) {
                elbonianNum += "N";
                arabicNum -= 5000;
            }
            else if (arabicNum >= 4000) {
                elbonianNum += "n";
                arabicNum -= 4000;
            }
            else if (arabicNum >= 1000) {
                elbonianNum += "M";
                arabicNum -= 1000;
            }
            else if (arabicNum >= 500) {
                elbonianNum += "D";
                arabicNum -= 500;
            }
            else if (arabicNum >= 400) {
                elbonianNum += "d";
                arabicNum -= 400;
            }
            else if (arabicNum >= 100) {
                elbonianNum += "C";
                arabicNum -= 100;
            }
            else if (arabicNum >= 50) {
                elbonianNum += "L";
                arabicNum -= 50;
            }
            else if (arabicNum >= 40) {
                elbonianNum += "l";
                arabicNum -= 40;
            }
            else if (arabicNum >= 10) {
                elbonianNum += "X";
                arabicNum -= 10;
            }
            else if (arabicNum >= 5) {
                elbonianNum += "V";
                arabicNum -= 5;
            }
            else if (arabicNum >= 4) {
                elbonianNum += "v";
                arabicNum -= 4;
            }
            else if (arabicNum >= 1) {
                elbonianNum += "I";
                arabicNum -= 1;
            }
        }
        return elbonianNum;
    }

    //Rule 1: M,C,X,I can be repeated certain times
    //Rule 2: N, n, D, d, L, l, V, v can each only appear once
    public boolean repeatedCertainTimes(String number) {
        String[] aListOfCharacters = number.split("");
        int mNum, cNum, xNum, iNum, NNum, nNum, DNum, dNum, LNum, lNum, VNum, vNum;
        mNum = cNum = xNum = iNum = NNum = nNum = DNum = dNum = LNum = lNum = VNum = vNum = 0;

        for (int i = 0; i < aListOfCharacters.length; i++) {
            String aChar = aListOfCharacters[i];
            if (aChar.equals("M")) {
                mNum += 1;
            } else if (aChar.equals("C")) {
                cNum += 1;
            } else if (aChar.equals("X")) {
                xNum += 1;
            } else if (aChar.equals("I")) {
                iNum += 1;
            }
            else if (aChar.equals("N")) {
                NNum += 1;
            } else if (aChar.equals("n")) {
                nNum += 1;
            } else if (aChar.equals("D")) {
                DNum += 1;
            } else if (aChar.equals("d")) {
                dNum += 1;
            }
            else if (aChar.equals("L")) {
                LNum += 1;
            }
            else if (aChar.equals("l")) {
                lNum += 1;
            }
            else if (aChar.equals("V")) {
                VNum += 1;
            }
            else if (aChar.equals("v")) {
                vNum += 1;
            }
        }
        if (mNum > 3 && cNum > 3 && xNum > 3 && iNum >3) {
                return false;
        }
        else if (NNum > 1 && nNum > 1 && DNum > 1 && dNum >1 && LNum >1 && lNum >1 && VNum >1 && vNum >1) {
            return false;
        }
        return true;
    }

    //Rule 3, 4, 5, 6: If a appears, b cannot appear
    public boolean charThenNoOtherChar(String number, String a, String b) {
        String[] aListOfCharacters = number.split("");
        int aNum = 0;
        int bNum = 0;

        for (int i = 0; i < aListOfCharacters.length; i++) {
            String aChar = aListOfCharacters[i];
            if (aChar.equals(a)) {
                aNum += 1;
            } else if (aChar.equals(b)) {
                bNum += 1;
            }
        }
        if (aNum > 0) {
            if (bNum > 0) {
                return false;
            }
        }
        else if (bNum > 0){
            if (aNum > 0) {
                return false;
            }
        }
        return true;
    }

    //Rule 7: Numbers are represented by the letters from the greatest magnitude down to the least magnitude
    public boolean GreatestToLeast(String number) {
        if (number.matches("N?n?M?M?M?D?d?C?C?C?L?l?X?X?X?V?v?I?I?I?")){
                return true;
        }
        return false;
    }
}
