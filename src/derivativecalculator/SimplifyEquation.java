

package derivativecalculator;

import java.util.ArrayList;
import java.util.List;



public class SimplifyEquation {
    public static List<Double> constants = new ArrayList<>();
    public static List<String> constantFractions = new ArrayList<>();
    public static List<String> constantOperands = new ArrayList<>();
    public static List<String> tempConstants = new ArrayList<>();//added
    public static List<String> tempSpecials = new ArrayList<>(); //added
    public static List<String> xEquations = new ArrayList<>();
    public static String[] special = {"cos", "sin", "tan", "cot", "ln"};
    public static String finalResult = "";
    
    public static String simplify(List<String> derivatives, List<String> operands){
        for(int i=0; i<derivatives.size(); i++){
            String function = derivatives.get(i);                    
            if (function.contains("cos") || function.contains("sin") || function.contains("ln") || function.contains("cot") || function.contains("tan")){
                if (finalResult.length() != 0)
                    finalResult += operands.get(i-1);
                finalResult += function;
            }
            else if(function.contains("x")){
                if (finalResult.length() != 0)
                    finalResult += operands.get(i-1);
                function = simplifyXEquations(function);
                finalResult += function;
            }
            else if (function.contains("/")){
                function = simplifyConstantFraction(function);
                if (i != 0){
                    constantOperands.add(operands.get(i-1));
                }
                constants.add(Double.parseDouble(function));
            }
            else{
                if(i != 0)
                    constantOperands.add(operands.get(i-1));
                constants.add(Double.parseDouble(function));
            }
            //simplify constants in paranthesis
            
        }
        simplifyListConstants();
        if(!constants.isEmpty()){
            if (constants.get(0) >0){
                finalResult += "+"+constants.get(0);
            }    
            else if(constants.get(0) <0){
                finalResult += constants.get(0);
            }
        }
        
        
        return finalResult;
    }
    
    private static String simplifyConstantFraction(String f){
        int slashIndex = f.indexOf("/");
        double a = Double.parseDouble(f.substring(0,slashIndex));
        double b = Double.parseDouble(f.substring(slashIndex+1));
        double result = a/b;
        return Double.toString(result);
    }
    
    private static void simplifyListConstants(){
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
    
    private static String simplifyXEquations(String f){
        String result;
        int xIndex = f.indexOf("x");
        String before = f.substring(0,xIndex);
        before = simplifyParanConstants(before);
        String after = "";
        if(f.contains("^")){
            int caretIndex = f.indexOf("^");
            if(f.indexOf("(", caretIndex) != -1){
                after = f.substring(caretIndex+2,f.length()-1);
                after = "^" +simplifyConstants(after);
            }
            else
                after = f.substring(caretIndex);
        }
        result = before +"x"+ after;
        return result;
    }
    
    private static String simplifyConstants(String f){
        int opIndex;
        double a;
        double b;
        double result = 0;
        if (f.contains("-")){
            opIndex = f.indexOf('-');
            a = Double.parseDouble(f.substring(0,opIndex));
            b = Double.parseDouble(f.substring(opIndex+1));
            result = a-b;
        }
        return Double.toString(result);
    }
    
    private static void simplifyTempConstants(){
        
    }
    
    private static String simplifyParanConstants(String f){
        int openParanIndex = f.indexOf("(");
        
        f = f.replace('(', '*');
        f = f.substring(0,f.length()-1);
        double a = Double.parseDouble(f.substring(0,openParanIndex));
        double b = Double.parseDouble(f.substring(openParanIndex+1));
        f = Double.toString(a*b);
        return f;
    }
}
