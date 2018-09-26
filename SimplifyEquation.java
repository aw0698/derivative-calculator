

package derivativecalculator;

import java.util.ArrayList;
import java.util.List;



public class SimplifyEquation {
    public static List<Double> constants = new ArrayList<>();
    public static List<String> constantFractions = new ArrayList<>();
    public static List<String> constantOperands = new ArrayList<>();
    public static List<String> simplifiedDerivatives = new ArrayList<>();//added
    public static List<String> simplifiedDerivativesOp = new ArrayList<>(); //added
    public static List<String> xEquations = new ArrayList<>();
    public static String[] special = {"cos", "sin", "tan", "cot", "ln"};
    public static String finalResult = "";
    
    public static String simplify(List<String> derivatives, List<String> operands){
        //goes through each derivative and simpifies it
        for(int i=0; i<derivatives.size(); i++){
            String function = derivatives.get(i);
            //function with x variable
            if(function.contains("x")){
                function = simplifyXEquation(function);
                simplifiedDerivatives.add(function);
                if(simplifiedDerivatives.size() != 1){
                    simplifiedDerivativesOp.add(operands.get(i-1));
                }
            }
            //function with only constants
            else{
                //if constant is a fraction
                if (function.contains("/")){
                    function = fractionToDecimal(function);
                }
                constants.add(Double.parseDouble(function));
                if (constants.size() > 1 || i==derivatives.size()-1){
                    constantOperands.add(operands.get(i-1));
                }
                
            }
        }
        
        if(constants.size()>1)
            sumListConstants();
        finalResult = combineDerivatives(simplifiedDerivativesOp);
        if(!constants.isEmpty()){
            
            if(constantOperands.isEmpty()){
                if (constants.get(0) >0){
                    finalResult += "+"+constants.get(0);
                }    
                else if(constants.get(0) <0){
                    finalResult += constants.get(0);
                }
            }
            else{
                finalResult += constantOperands.get(0) + constants.get(0);
            }
            
        }
        
        
        return finalResult;
    }
    
    //changes fraction to decimal
    private static String fractionToDecimal(String f){
        int slashIndex = f.indexOf("/");
        double a = Double.parseDouble(f.substring(0,slashIndex));
        double b = Double.parseDouble(f.substring(slashIndex+1));
        double result = a/b;
        return Double.toString(result);
    }
    
    //takes the sum of the constants in the list
    private static void sumListConstants(){
        double a;
        double b;
        double result = 0;
        char op;
        
        //while loop to perform multiplication and divison
        while (constantOperands.contains("*") || constantOperands.contains("/"))// <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
        {
            int multiIndex = constantOperands.indexOf('*');
            int divisIndex = constantOperands.indexOf('/');
            //if there is only * in the operands
            if (multiIndex != -1 && divisIndex == -1) {
                a = constants.get(multiIndex);
                b = constants.get(multiIndex + 1);
                result = a * b;
                constants.set(multiIndex, result);
                constantOperands.remove(multiIndex);
                constants.remove(multiIndex + 1);
            } //if there is only / in the operands
            else if (multiIndex == -1 && divisIndex != -1) {
                a = constants.get(divisIndex);
                b = constants.get(divisIndex + 1);
                result = a / b;
                constants.set(divisIndex, result);
                constantOperands.remove(divisIndex);
                constants.remove(divisIndex + 1);
            } //if * comes before /
            else if (multiIndex < divisIndex) {
                a = constants.get(multiIndex);
                b = constants.get(multiIndex + 1);
                result = a * b;
                constants.set(multiIndex, result);
                constantOperands.remove(multiIndex);
                constants.remove(multiIndex + 1);
            } //if / comes before *
            else {
                a = constants.get(divisIndex);
                b = constants.get(divisIndex + 1);
                result = a / b;
                constants.set(divisIndex, result);
                constantOperands.remove(divisIndex);
                constants.remove(divisIndex + 1);
            }
        }

// </editor-fold>

        //while loop to perform addition and substraction
        while(constantOperands.contains("+") || constantOperands.contains("-"))// <editor-fold defaultstate="collapsed" desc=" DESCRIPTION ">
        {
            
            a = constants.get(0);
            b = constants.get(1);
            op = constantOperands.get(0).charAt(0);
            if (op == '+') {
                result = a + b;
            } 
            else {
                result = a - b;
            }
            constantOperands.remove(0);
            constants.remove(0);
            constants.set(0, result);
        }

// </editor-fold>
    }
    
    //adds, subtracts, multiplies, divides constants
    private static String mathConstants(String f){
        int opIndex;
        double a;
        double b;
        double result = 0;
        if(f.contains("*")){
            opIndex = f.indexOf('*');
            a = Double.parseDouble(f.substring(0,opIndex));
            b = Double.parseDouble(f.substring(opIndex+1));
            result = a*b;
        }
        else if(f.contains("/")){
            opIndex = f.indexOf('/');
            a = Double.parseDouble(f.substring(0,opIndex));
            b = Double.parseDouble(f.substring(opIndex+1));
            result = a/b;
        }
        else if (f.contains("-")){
            opIndex = f.indexOf('-');
            if(opIndex == 0){
                return f;
            }
            a = Double.parseDouble(f.substring(0,opIndex));
            b = Double.parseDouble(f.substring(opIndex+1));
            result = a-b;
        }
        else if(f.contains("+")){
            opIndex = f.indexOf('+');
            a = Double.parseDouble(f.substring(0,opIndex));
            b = Double.parseDouble(f.substring(opIndex+1));
            result = a+b;
        }
        else{
            return f;
        }
        return Double.toString(result);
    }
    
    //constants wrapped in paranthesis are multiplied by adjacent constants
    private static String multiplyParanConstants(String f){
        String tempF = "";
        //if function has a special, remove special from function
        for (String s: special){
            if(f.contains(s)){
                int start = f.indexOf(s);
                int end = findMatchingCloseParan(f.substring(start))+start;
                tempF = f.substring(start, end+1);
                f = f.substring(0,start) + f.substring(end+1);
            }
            if(f.contains("^")){
                int end = f.indexOf("^")-1;
                tempF = f.substring(end);
                f = f.substring(0,end);
            }
        }
        if (f.contains("(")){
            if(f.indexOf("(") != 0){
                f = f.replace("(", "*");
            }
            else{
                f = f.replace("(", "" );
            }
            if(f.indexOf(")") != f.length()-1){
                f = f.replace(")", "*");
            }
            else{
                f = f.replace(")", "");
            }
            f = mathConstants(f);
        }
        if(f.isEmpty()){
            f = tempF;
        }
        else{
            f = "(" +f+ ")" +tempF;
        }
        return f;
    }
    
    //simplify constants within function
    private static String simplifyXEquation(String f){
        String result;
        result = multiplyParanConstants(f);
        System.out.println("BEFORE: "+result);
        String after = "";
        String before ="";
        
        //for exponents
        if(result.contains("^")){
            int caretIndex = result.indexOf("^");
            before = result.substring(0,caretIndex);
            if(result.indexOf("(", caretIndex) != -1){
                after = result.substring(caretIndex+2,result.length()-1);
                after = "^" +mathConstants(after);
            }
            else
                after = result.substring(caretIndex);
            result = before + after;
        }
        
        return result;
    }
    
    //finds the index of the mathing closing paranthesis to the open paranthesis
    private static int findMatchingCloseParan(String f){
        int openParanCounter = 0;
        int closedParanCounter = 0;
        //goes through each character of function
        for(int i=0; i<f.length() ;i++){
            char c = f.charAt(i);
            if (c == '('){
                openParanCounter ++;
            }
            else if (c == ')'){
                closedParanCounter ++;
            }
            //if openParanCounter = closedParanCounter than the paranthesis are a pair
            if(openParanCounter != 0 && openParanCounter == closedParanCounter){
                return i;
            }
        }
        return -1;
    }
    
    //takes the simplified derivatives and adds the operands to make the derivative string
    private static String combineDerivatives(List operands){
        System.out.println("\nSimplified Derivatives: "+simplifiedDerivatives);
        System.out.println("operands: "+ simplifiedDerivativesOp);
        String result = "";
        for(int f=0; f<simplifiedDerivatives.size(); f ++){
            result += simplifiedDerivatives.get(f);
            if (f < operands.size())
                result += operands.get(f);
        }
        return result;
    }
}
