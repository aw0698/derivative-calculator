package derivativecalculator;

import java.util.List;
import java.util.ArrayList;


public class DerivativeCalculator {

    
    public static List<String> functions = new ArrayList<>();
    public static List<String> derivatives = new ArrayList<>();
    public static List<String> operands = new ArrayList<>();
    public static String equation;

        
    public static void main(String[] args) {
        equation = "4x+10x^3-1/x+x/2+1/2x-6x+4cos(x)-120sin(x)";
//        equation = ("5sin(x)+3cos(x)-tan(x)+cot(x)-601x");
        System.out.println("Equation: " + equation);
        
        separateEquation();
        for(String function: functions){
            takeDerivative(function);
        }
        
        //combine derivatives
        String longDerivative = combineDerivatives();
        String simplifyDerivative = SimplifyEquation.simplify(derivatives, operands);
        System.out.println("\nFunctions: " + functions);
        System.out.println("Operands: " + operands);
        System.out.println("Derivatives: " + derivatives);
        System.out.println("\nf(x) = " + equation);
        System.out.println("long f'(x) = " + longDerivative);
        System.out.println("simplified f'(x) = " + simplifyDerivative);
        
    }
    
    //splits equation to functions and operands
    public static void separateEquation(){
        int start=0;
            for(int j=start; j<equation.length(); j++){
                if(equation.charAt(j) == '+' || equation.charAt(j) == '-'){
                    functions.add(equation.substring(start,j));
                    operands.add(Character.toString(equation.charAt(j)));
                    start = j+1;
                }
            }
            if(start != equation.length()){
                functions.add(equation.substring(start));
            }
    }
    
    public static void takeDerivative(String tempEquation){
        System.out.println("\nTAKEDERIVATIVE EQUATION: " +tempEquation);
        
        //condition to know if function qualifies for constantRule
        if (!tempEquation.contains("^") && !tempEquation.contains("*") && !tempEquation.contains("/") && tempEquation.contains("x"))
            tempEquation = constantRule(tempEquation);
        System.out.println("After constant rule: "+ tempEquation);
        
        //condition to know if function qualifies for powerRule
        if(tempEquation.contains("^"))
            tempEquation = powerRule(tempEquation);
        System.out.println("After power rule: "+ tempEquation);
        
        //condition to know if function qualifies for 1/x
        if(tempEquation.contains("/")){
            int slashIndex = tempEquation.indexOf("/");
            //if x is the denominator
            if(tempEquation.charAt(slashIndex+1) == 'x'){
                tempEquation = "ln(x)";
            }
            //if x is divided by a constant
            else if(tempEquation.charAt(slashIndex-1) == 'x'){
                String constant = "1/" + tempEquation.substring(slashIndex+1);
                tempEquation = constant + "x";
                tempEquation = constantRule(tempEquation);
            }
            //if / is for a constant fraction
            else {
                tempEquation = constantRule(tempEquation);
            }
        }
        
        //condition for cos(x)
        if(tempEquation.contains("cos")){
            int cosIndex = tempEquation.indexOf("cos");
            String temp = tempEquation.substring(0,cosIndex);
            temp += "(-1)sin(x)";
            tempEquation = temp;
        }
        //condition for sin(x)
        else if(tempEquation.contains("sin")){
            int sinIndex = tempEquation.indexOf("sin");
            String temp = tempEquation.substring(0,sinIndex);
            temp += "cos(x)";
            tempEquation = temp;
        }
        if(tempEquation.contains("tan")){
            int tanIndex = tempEquation.indexOf("tan");
            String temp = tempEquation.substring(0,tanIndex);
            temp += "cot(x)";
            tempEquation = temp;
        }
        else if (tempEquation.contains("cot")){
            int cotIndex = tempEquation.indexOf("cot");
            String temp = tempEquation.substring(0,cotIndex);
            temp += "(-1)tan(x)";
            tempEquation = temp;
        }
            
        System.out.println("After 1/x check: " + tempEquation);
        
        derivatives.add(tempEquation);
    }
    
    public static String constantRule(String tempEquation){
        if(tempEquation.charAt(tempEquation.length()-1) == 'x')
            return tempEquation.substring(0,tempEquation.length()-1);
        else
            return tempEquation;
    }
    
    public static String powerRule(String tempEquation){
        int caretIndex = tempEquation.indexOf("^");
        int xIndex = tempEquation.indexOf("x");
        String exponent = tempEquation.substring(caretIndex+1);
        String newExponent = "(" +exponent+ "-1)";
        String coefficient = tempEquation.substring(0, xIndex);
        String newCoefficient = exponent +"("+ coefficient +")";
        
        String result = newCoefficient + "x^" +newExponent;
        return result;
    }
    
    public static String combineDerivatives(){
        String result = "";
        for(int f=0; f<derivatives.size(); f ++){
            result += derivatives.get(f);
            if (f < operands.size())
                result += operands.get(f);
        }
        return result;
    }
    
            
}
