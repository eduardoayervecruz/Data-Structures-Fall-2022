// LongInt ADT for unbounded integers

public class LongInt {
    // Attributes
    String value;
    boolean isPositive;

    // Constructor
    public LongInt() {}

    public LongInt(String value) {
        this.value = value;
        this.isPositive = value.charAt(0) != '-';
    }

    // Getters and setters
    public String getValue() {
        return value;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public void setValue(String value) {
        this.value = value;
        this.isPositive = value.charAt(0) != '-';
    }

    public void setPositive(boolean positive) {
        isPositive = positive;
    }

    public void pushValue(String pushedString){
        this.value = pushedString + this.value;
    }
    private String invertString(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        stringBuilder.reverse();
        return stringBuilder.toString();
    }

    // returns 'this' + 'opnd'; Both inputs remain intact.
    public LongInt add(LongInt opnd) {
        // Result
        LongInt result = new LongInt();
        String result_string = "";

        // We take the string values and store it in new variables as we need to maintain them intact
        String thisValue = this.getValue();
        String opndValue = opnd.getValue();
        String tmp;
        int carry = 0;

        // Take out the sign for operations
        if(!this.isPositive) thisValue = thisValue.substring(1);
        if(!opnd.isPositive) opndValue = opndValue.substring(1);

        // One number is positive and the other is negative
        if((!this.isPositive && opnd.isPositive) || (this.isPositive && !opnd.isPositive)) {
            // Check which number is larger depending on the size and the sign
            // Change places with tmp if necessary
            if(thisValue.length() == opndValue.length()) {
                for(int i = 0; i < thisValue.length(); i++) {
                    if((thisValue.charAt(i)-'0') < (opndValue.charAt(i)-'0') && !opnd.isPositive){
                        result.setPositive(false);
                        tmp = thisValue;
                        thisValue = opndValue;
                        opndValue = tmp;
                    }
                    if((thisValue.charAt(i)-'0') > (opndValue.charAt(i)-'0') && !this.isPositive) result.setPositive(false);
                }
            }else if(thisValue.length() > opndValue.length() && !this.isPositive) result.setPositive(false);
            else{
                if(!opnd.isPositive) result.setPositive(false);
                tmp = thisValue;
                thisValue  = opndValue;
                opndValue = tmp;
            }
            // SUBTRACTION
            for(int i = opndValue.length() - 1; i >= 0; i--) {
                int sub = ((thisValue.charAt(i+thisValue.length()-opndValue.length()) - (int)'0') - (opndValue.charAt(i) - (int)'0') - carry);
                if(sub >= 0) carry = 0;
                else {
                    sub += 10;
                    carry = 1;
                }
                result_string += String.valueOf(sub);
            }
            for(int i = thisValue.length() - opndValue.length()-1; i >= 0; i--) {
                if(thisValue.charAt(i) == '0' && carry > 0) result_string += "9";
                int subString = ((int)(thisValue.charAt(i)) - (int)'0') - carry;
                if(subString <= 0 && i == 0 ) result_string += String.valueOf(subString);
            }
            // Result is ZERO
            if(result_string.equals("0")){
                result.setValue("0");
                return result;
            }
            // Result ends in ZERO but has additional zeros to be deleted
            String invertedString = invertString(result_string);
            int zeros = 0;
            while(zeros < invertedString.length()-1 && invertedString.charAt(zeros) == '0') zeros++;

            // Assignment and return
            invertedString = invertedString.substring(zeros);
            if(!result.isPositive()) { invertedString = "-" + invertedString; }
            result.setValue(invertedString);
            return result;
        }

        // Compare the numbers based on length, all the other cases are considered above
        if(thisValue.length() > opndValue.length()) {
            tmp = thisValue;
            thisValue = opndValue;
            opndValue = tmp;
        }

        // ADDITION
        carry = 0;
        for(int i = thisValue.length()-1; i >= 0; i--) {
            int sum = ((thisValue.charAt(i)-'0') + (opndValue.charAt(i+opndValue.length() - thisValue.length())-'0') + carry);
            result_string += (char)(sum % 10 + '0');
            carry = sum / 10;
        }

        for(int i = opndValue.length()-thisValue.length()-1; i >= 0; i--) {
            int sum = ((opndValue.charAt(i)-'0') + carry);
            result_string += (char)(sum % 10 + '0');
            carry = sum / 10;
        }
        // ADD overflow if necessary and return
        if(carry != 0) result_string += (char)(carry + '0');
        if(this.isPositive) result.setValue(invertString(result_string));
        else result.setValue(invertString(result_string));
        return result;
    }

    // returns 'this' - 'opnd'; Both inputs remain intact.
    public LongInt subtract(LongInt opnd) {
        LongInt result = new LongInt();
        String result_string = "";

        String thisValue = this.getValue();
        String opndValue = opnd.getValue();
        String tmp;
        // Removing signs
        if(!this.isPositive) thisValue = thisValue.substring(1);
        if(!opnd.isPositive) opndValue = opndValue.substring(1);

        if((!this.isPositive && opnd.isPositive) || (this.isPositive && !opnd.isPositive)) {
            boolean flag = false;
            if (thisValue.length() < opndValue.length()) {
                tmp = thisValue;
                thisValue = opndValue;
                opndValue = tmp;
            }
            LongInt num1 = new LongInt(thisValue);
            LongInt num2 = new LongInt(opndValue);
            if((!this.isPositive && flag) || (!opnd.isPositive && flag)){
            if (!this.isPositive && flag) {
                num2 = new LongInt("-" + opndValue);
            }
            if (!opnd.isPositive && flag)
                num1 = new LongInt("-" + thisValue);
            }

            result = num2.add(num1);
            if(!this.isPositive) { result_string = "-" + result_string; }
            result.value = result_string;
            return result;
        }

        if(this.isPositive && opnd.isPositive) {
            if(thisValue.length() < opndValue.length()) {
                result.setPositive(true);
                tmp = thisValue;
                thisValue = opndValue;
                opndValue = tmp;
            } else if(thisValue.length() == opndValue.length()) {
                for(int i = 0; i < thisValue.length(); i++) {
                    if((thisValue.charAt(i) - '0') > (opndValue.charAt(i) - '0')) result.setPositive(true);
                    else if((thisValue.charAt(i) - '0') < (opndValue.charAt(i) - '0')) {
                        result.setPositive(false);
                        tmp = thisValue;
                        thisValue = opndValue;
                        opndValue = tmp;
                    }
                }
            }
        }else{
            if(thisValue.length() > opndValue.length()) result.setPositive(false);
            else if(thisValue.length() < opndValue.length()) {
                result.setPositive(true);
                tmp = thisValue;
                thisValue = opndValue;
                opndValue = tmp;
            }else {
                for(int i = 0; i < thisValue.length(); i++) {
                    if((thisValue.charAt(i) - '0') > (opndValue.charAt(i)) - '0') result.setPositive(false);
                    else if((thisValue.charAt(i) - '0') < (opndValue.charAt(i)) - '0') {
                        result.setPositive(true);
                        tmp = thisValue;
                        thisValue = opndValue;
                        opndValue = tmp;
                    }
                }
            }
        }
        // SUBTRACTION
        int carry = 0;
        for(int i = opndValue.length() - 1; i >= 0; i--) {
            int digitSub = ((thisValue.charAt(i+thisValue.length()-opndValue.length()) - (int)'0') - (opndValue.charAt(i) - (int)'0') - carry);
            if(digitSub >= 0) carry = 0;
            else{
                digitSub += 10;
                carry = 1;
            }
            result_string += String.valueOf(digitSub);
        }

        for(int i = thisValue.length() - opndValue.length()-1; i >= 0; i--) {
            if(thisValue.charAt(i) == '0' && carry > 0) result_string += "9";
            int digitSub = ((int)(thisValue.charAt(i)) - (int)'0') - carry;
            if(i > 0 || digitSub > 0) result_string += String.valueOf(digitSub);
            carry = 0;
        }

        if(result_string.equals("0")) return new LongInt("0");
        int zeros = 0;
        while(zeros < invertString(result_string).length()-1 && invertString(result_string).charAt(zeros) == '0') zeros++;
        if(!result.isPositive) { result.setValue("-" + invertString(result_string).substring(zeros)); }
        else result.setValue(invertString(result_string).substring(zeros));
        return result;
    }
    // returns 'this' * 'opnd'; Both inputs remain intact.
    public LongInt multiply(LongInt opnd) {
        LongInt result = new LongInt();
        // Multiplication by 0
        if(this.getValue().equals("0") || opnd.value.equals("0")) {
            result.setValue("0");
            return result;
        }
        String thisValue = this.getValue();
        String opndValue = opnd.getValue();
        String str_result = "";

        // Taking the signs for operations
        if(!this.isPositive) thisValue = thisValue.substring(1);
        if(!opnd.isPositive) opndValue = opndValue.substring(1);

        // MULTIPLICATION
        int num1 = 0;
        int num2;
        int[] resultArray = new int[thisValue.length() + opndValue.length()];
        for(int i = opndValue.length()-1; i >= 0; i--) {
            int carry = 0;
            int digit1 = opndValue.charAt(i) - '0';
            num2 = 0;
            for(int j = thisValue.length()-1; j >= 0; j--) {
                int digit2 = thisValue.charAt(j) - '0';
                int digitSum = digit1 * digit2 + carry + resultArray[num1+num2];
                resultArray[num1 + num2] = digitSum % 10;
                carry = digitSum / 10;
                num2++;
            }
            resultArray[num1 + num2] += carry;
            num1++;
        }

        // Ignore zeros at the right
        int i = resultArray.length-1;

        while(i >= 0 && resultArray[i] == 0) i--;
        while(i >= 0) str_result += (resultArray[i--]);

        if((!this.isPositive && opnd.isPositive) || ((this.isPositive && !opnd.isPositive))) result.setValue("-" + str_result);
        else result.setValue(str_result);
        return result;
    }

    // print the value of 'this' element to the standard output.
    public void print() {
        System.out.println(this.getValue());
    }
}
