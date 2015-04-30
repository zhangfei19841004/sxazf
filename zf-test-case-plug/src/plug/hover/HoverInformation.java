package plug.hover;

public class HoverInformation {
	
	private HoverInfo hinfo;	
	
	public HoverInformation() {
		hinfo = new HoverInfo();
	}	

	public HoverInfo getHinfo() {
		return hinfo;
	}

	public String getFocusBeforeHoverInfo(String focusContent){		
		char c = 0;
		boolean flag = false;
		for (int i = focusContent.length()-1; i >= 0; i--) {
			c = focusContent.charAt(i);
			if(c=='"' || c=='{' || c=='[' || c=='<' || c=='('){				
				flag = true;
				hinfo.setBeforeIndex(i);
				break;
			}else if(c=='}' || c==']' || c=='>' || c==')'){
				return null;
			}
		}	
		if(flag){
			return String.valueOf((char)c);
		}
		return null;
	}
	
	public String getFocusAfterHoverInfo(String focusContent, String lineContent){
		char c = 0;
		boolean flag = false;
		for (int i = focusContent.length()-1; i < lineContent.length(); i++) {
			c = lineContent.charAt(i);
			if(c=='"' || c=='}' || c==']' || c=='>' || c==')'){				
				flag = true;
				hinfo.setEndIndex(i);				
				break;
			}else if(c=='{' || c=='[' || c=='<' || c=='('){
				return null;
			}
		}	
		if(flag){
			return String.valueOf((char)c);
		}
		return null;
	}
	
	
	public String getSubjectNameForParameter(String focusContent){		
		int startIndex = 0;
		int endIndex = 0;
		boolean isObject = false;
		boolean isMethod = false;
		boolean isElement = false;	
		boolean isElementW = false;
		for (int i = focusContent.length()-1; i >= 0; i--) {
			char c = focusContent.charAt(i);
			if(c=='"' && !isObject){
				endIndex = i;
				isObject = true;
				continue;
			}else if(c==']' && !isMethod){
				endIndex = i;
				isMethod = true;
				continue;
			}else if(c=='>' && !isElement){
				endIndex = i;
				isElement = true;
				continue;
			}else if(c==')' && !isElementW){
				endIndex = i;
				isElementW = true;
				continue;
			}
			if(c=='"' && isObject){
				startIndex = i;
				isObject = false;
				break;
			}else if(c=='[' && isMethod){
				startIndex = i;
				isMethod = false;
				break;
			}else if(c=='<' && isElement){
				startIndex = i;
				isElement = false;
				break;
			}else if(c=='(' && isElementW){
				startIndex = i;
				isElementW = false;
				break;
			}
		}
		String focusString = focusContent.substring(startIndex, endIndex+1);		
		return focusString;
	}
}
