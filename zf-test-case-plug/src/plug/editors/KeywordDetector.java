package plug.editors;

import org.eclipse.jface.text.rules.IWordDetector;

public class KeywordDetector implements IWordDetector{

	@Override
	public boolean isWordPart(char c) {
		if(c == ']'){
			return true;
		}
		return false;		
	}

	@Override
	public boolean isWordStart(char c) {
		if(c == '['){
			return true;
		}
		return false;
	}

}
