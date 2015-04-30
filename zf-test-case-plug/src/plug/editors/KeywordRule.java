package plug.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class KeywordRule extends WordRule implements IPredicateRule{
	
	private StringBuffer fBuffer= new StringBuffer();
	private boolean fIgnoreCase= false; 
	private static KeywordDetector kd = new KeywordDetector();
	public KeywordRule(ColorManager manager) {
		super(kd, new Token(new TextAttribute(manager.getColor(IXMLColorConstants.KEY_WORD))));	
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		if (c != ICharacterScanner.EOF && kd.isWordStart((char)c)) {  
            if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {             	
                fBuffer.setLength(0);  
                while(c != ICharacterScanner.EOF && c!=']'){
                	fBuffer.append((char)c);  
                    c= scanner.read();  
                }                 
                scanner.unread();  
                
                String buffer = fBuffer.toString();               
                if (fIgnoreCase) {  
                    buffer = buffer.toLowerCase();  
                }  
               
                IToken token= (IToken)fWords.get(buffer);  
                  
                if (token != null) {  
                    return token;  
                }
                
                if (fDefaultToken.isUndefined()) {  
                    unreadBuffer(scanner);  
                }  
               
                return fDefaultToken;  
            }  
        }  
        scanner.unread();  
        return Token.UNDEFINED; 		
	}

	@Override
	public IToken getSuccessToken() {
		// TODO Auto-generated method stub
		return Token.UNDEFINED;
	}

	@Override
	public IToken evaluate(ICharacterScanner arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return this.fDefaultToken;
	}	
	
}
