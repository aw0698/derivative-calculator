

package derivativecalculator;

import java.util.Stack;



public class Math {
    public static int calculate(String equation){
        //Create operandStack to store operands
        Stack<Integer> operandStack = new Stack<>();
        
        //Create operatorStack to store operators
        Stack<Character> operatorStack = new Stack<>();
        
        //Insert blanks around (, ), +, -, /, and *
        equation = insertBlanks(equation);
        
        //Extract operands and operators
        String[] tokens = equation.split(" ");
        
        //Phase 1: Scan tokens
        for (String token: tokens){
            if(token.length() == 0) //Blank space
                continue; //Back to the while loop to extract the next token
            else if(token.charAt(0) == '+' || token.charAt(0) == '-'){
                //Process all +,-,*,/ in the op of the operator stack
                while(!operatorStack.isEmpty() &&
                        (operatorStack.peek() == '+' ||
                        operatorStack.peek() == '-' ||
                        operatorStack.peek() == '*' ||
                        operatorStack.peek() == '/')){
                    processAnOperator(operandStack, operatorStack);
                }
                
                //Push the + or - operator into the operator stack
                operatorStack.push(token.charAt(0));
            }
            else if (token.charAt(0) == '*' || token.charAt(0) == '/'){
                //Process all *, / in the top of the operator stack
                while(!operatorStack.isEmpty() &&
                        (operatorStack.peek() =='*' ||
                        operatorStack.peek() == '/')){
                    processAnOperator(operandStack,operatorStack);
                }
                
                //Push the * or / operator into the operator stack
                operatorStack.push(token.charAt(0));
            }
            else if(token.trim().charAt(0) == '('){
                operatorStack.push('('); //Push '(' to stack
            }
            else if(token.trim().charAt(0) ==')'){
                //Process all the operators in the stack until seeing '('
                while(operatorStack.peek() != '(') {
                    processAnOperator(operandStack,operatorStack);
                }
                operatorStack.pop(); //Pop the '(' symbol from the stack
            }
            else{ //An operand scanned
                //Push an operand to the stack
                operandStack.push(new Integer(token));
            }
        }
        
        //Phase 2: Process all the remaining operators in the stack
        while (!operatorStack.isEmpty()){
            processAnOperator(operandStack,operatorStack);
        }
        
        //Return the result
        return operandStack.pop();
        
    }
    
    public static void processAnOperator(
        Stack<Integer> operandStack, Stack<Character> operatorStack){
        char op = operatorStack.pop();
        int op1 = operandStack.pop();
        int op2 = operandStack.pop();
        if (op == '+')
            operandStack.push(op2 + op1);
        else if (op == '-')
            operandStack.push(op2 - op1);
        else if (op == '*')
            operandStack.push(op2 * op1);
        else if (op == '/')
            operandStack.push(op2 / op1);
    }
    
    public static String insertBlanks(String s){
        String result = "";
        for(int i=0; i<s.length(); i++){
            if(s.charAt(i) == '(' || s.charAt(i) ==')' ||
                    s.charAt(i) == '+' || s.charAt(i) == '-' ||
                    s.charAt(i) == '*' || s.charAt(i) == '/'){
                result += " " + s.charAt(i) + " ";                
            }
            else
                result += s.charAt(i);
        }
        return result;
    }
}
