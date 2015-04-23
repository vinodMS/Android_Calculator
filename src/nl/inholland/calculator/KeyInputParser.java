package nl.inholland.calculator;

public class KeyInputParser
{
 private static final char[] operators = {'/','*','+','-','S'};

	    private float evaluateResult(String lvalues, char operators, String rvalues)
	     throws IllegalArgumentException {
	        
	    	float 	total = 0;
	    	float 	lResults = 0;
	    	float 	rResults = 0;
	    	int 	operatorPosition;
	        
	        operatorPosition = getOperatorPosition(lvalues);
	        
	        if( operatorPosition > 0 && operatorPosition < lvalues.length()-1 )	{
	        	lResults = evaluateResult(lvalues.substring(0,operatorPosition),
	            lvalues.charAt(operatorPosition),
	            lvalues.substring(operatorPosition+1,lvalues.length()));
	        }
	        else {
	        	try { lResults = Float.parseFloat(lvalues); }
	            catch(Exception e) {throw new IllegalArgumentException(e);}
	        }
	
	        operatorPosition = getOperatorPosition(rvalues);
	        
	        if( operatorPosition > 0 && operatorPosition < rvalues.length()-1 ) {
	        	rResults = evaluateResult(rvalues.substring(0,operatorPosition),
	            rvalues.charAt(operatorPosition),
	            rvalues.substring(operatorPosition+1,rvalues.length()));
	        }
	        else {
	        	try	{ rResults = Float.parseFloat(rvalues);}
	            catch(Exception e){ throw new IllegalArgumentException(e);}
	        }
	
	        switch(operators)	{
	            case '/':
	            	total = lResults / rResults; break;
	            case '*':
	            	total = lResults * rResults; break;
	            case '+':
	            	total = lResults + rResults; break;
	            case '-':
	            	total = lResults - rResults; break;
	            case 'S':
	            	total = (float) Math.sqrt(rResults);
	            case '%':
	            	float proportionCorrect = (lResults) / (rResults);
	            	total = (proportionCorrect * 100);
	            default:
	             throw new IllegalArgumentException("Not valid");
	        }
	        
	        return total;
	    }

    private int getOperatorPosition(String string)
    {
        int index = -1;
        for(int i = operators.length-1; i >= 0; i--)
        {
            index = string.indexOf(operators[i]);
            if(index >= 0)
             return index;
        }
        return index;
    }


    public float processEquation(String equation)
      throws IllegalArgumentException
    {
          return evaluateResult(equation,'+',"0");
    }

}