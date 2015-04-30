package plug.editors;

import org.eclipse.jface.text.rules.*;
import org.eclipse.jface.text.*;

public class XMLScanner extends RuleBasedScanner {

	public XMLScanner(ColorManager manager) {
		IToken procInstr =
			new Token(
				new TextAttribute(
					manager.getColor(IXMLColorConstants.PROC_INSTR)));

		IRule[] rules = new IRule[2];
		//Add rule for processing instructions
		rules[0] = new SingleLineRule("#", null, procInstr, (char)0, true);	
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new XMLWhitespaceDetector());		
		
		//rules[2] = new KeywordRule(manager);
		
		setRules(rules);
	}
}
