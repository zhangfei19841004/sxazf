package plug.hover;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;

import plug.extension.DocumentHandler;
import plug.util.Constants;

public class KeywordHover implements ITextHover{

	@Override
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		if (hoverRegion != null) {
			try {	
				IDocument d = textViewer.getDocument();				
				DocumentHandler dh = new DocumentHandler();
				int lineNumber = dh.getFocusLineNumber(d, hoverRegion.getOffset());
				String focusContent = dh.getLineContent(d, lineNumber-1, hoverRegion.getOffset()+1);				
//				int index = dh.getFocusIndex(d, lineNumber-1, hoverRegion.getOffset()+1)-1;
				IRegion lineRegion = d.getLineInformationOfOffset(hoverRegion.getOffset());
				String lineContent = textViewer.getDocument().get(lineRegion.getOffset(), lineRegion.getLength());				
//				String s = textViewer.getDocument().get(hoverRegion.getOffset(), 1);
				return this.getHoverType(focusContent, lineContent);
			} catch (BadLocationException e) {				
				e.printStackTrace();
			}
			
			
		}
		return null; 
	}
	
	private String getHoverType(String focusContent, String lineContent){		
		int index = focusContent.length()-1;
		char c = focusContent.charAt(index);
		if(c=='#'){
			return "注释";
		}
		HoverInformation hi = new HoverInformation();
		String beforeFlag = hi.getFocusBeforeHoverInfo(focusContent);
		String afterFlag = hi.getFocusAfterHoverInfo(focusContent, lineContent);
		if(beforeFlag==null || afterFlag==null){
			return null;
		}
		if(beforeFlag.equals("{") && afterFlag.equals("}")){
			return "参数";
		}else if(beforeFlag.equals("[") && afterFlag.equals("]")){
			return "方法";
		}else if(beforeFlag.equals("<") && afterFlag.equals(">")){
			return "元素";
		}else if(beforeFlag.equals("(") && afterFlag.equals(")")){
			return "元素";
		}else if(beforeFlag.equals("\"") && afterFlag.equals("\"")){
			if(this.getOtherObject(index, Constants.IF, lineContent) != null){
				return "判断";
			}else if(this.getOtherObject(index, Constants.OR, lineContent) != null){
				return "条件或者";
			}else if(this.getOtherObject(index, Constants.AND, lineContent) != null){
				return "条件且";
			}else if(this.getOtherObject(index, Constants.LOOP, lineContent) != null){
				return "循环";
			}			
			return "对象";
		}
		return null;
	}
	
	private String getOtherObject(int index, String str, String lineContent){
		String s = lineContent;
		while(s.indexOf(str) != -1){
			int begin = s.indexOf(str);
			int end = begin+str.length();
			if(begin<=index && index<=end){
				return str;
			}else{
				s = s.replaceFirst(str, "");
			}
		}
		return null;
	}
	
	
	
	@Override
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {	
		return new Region(offset, 0);
	}
	
}
