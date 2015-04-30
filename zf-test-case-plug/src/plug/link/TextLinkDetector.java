package plug.link;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.hyperlink.AbstractHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;

import plug.extension.DocumentHandler;
import plug.extension.RegExp;
import plug.hover.HoverInfo;
import plug.hover.HoverInformation;


public class TextLinkDetector extends AbstractHyperlinkDetector implements IHyperlinkDetector {
	
	
	
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer viewer, IRegion region, boolean canShowMultipleHyperlinks) {	
		IHyperlink[] links = null;
		try{
			IDocument d = viewer.getDocument();
			DocumentHandler dh = new DocumentHandler();			
			IRegion lineRegion = d.getLineInformationOfOffset(region.getOffset());
			String lineContent = d.get(lineRegion.getOffset(), lineRegion.getLength());
			String focusContent = dh.getLineContent(d, dh.getFocusLineNumber(d, region.getOffset())-1, region.getOffset());
			HoverInfo hinfo = this.getHoverLink(focusContent, lineContent);			
			if(hinfo==null){
				return null;
			}
			hinfo.setCurrentObject(dh.getCurrentObject(d, region.getOffset()));
			IRegion targetRegion = new Region(lineRegion.getOffset()+hinfo.getBeforeIndex()+1, hinfo.getEndIndex()-hinfo.getBeforeIndex()-1);
			if(hinfo.isObject() || hinfo.isPage() || hinfo.isMethod() || hinfo.isElement()){					 
				links = new IHyperlink[]{new TextLink(targetRegion, hinfo)};
				return links;
			}
		}catch (BadLocationException e) {				
			e.printStackTrace();
		}		
		return links;
	}
	
	private HoverInfo getHoverLink(String focusContent, String lineContent){
		HoverInformation hi = new HoverInformation();		
		String beforeFlag = hi.getFocusBeforeHoverInfo(focusContent);
		String afterFlag = hi.getFocusAfterHoverInfo(focusContent, lineContent);
		if(beforeFlag==null || afterFlag==null){
			return null;
		}
		HoverInfo hinfo = hi.getHinfo();
		if(hinfo.getBeforeIndex()>=hinfo.getEndIndex()){
			return null;
		}
		hinfo.setHoverText(lineContent.substring(hinfo.getBeforeIndex()+1, hinfo.getEndIndex()));
		if(beforeFlag.equals("{") && afterFlag.equals("}")){
			String sn = hi.getSubjectNameForParameter(focusContent);
			RegExp re = new RegExp();						
			if(re.isPageOrObject(sn)){
				hinfo.setObject(true);
				hinfo.setPage(true);
				hinfo.setSn(re.pageOrObjectName(lineContent).get(0));
				return hinfo;				 
			}else{	
				hinfo.setObject(true);				
				return hinfo;
			}			
		}else if(beforeFlag.equals("[") && afterFlag.equals("]")){
			hinfo.setMethod(true);
			return hinfo;
		}else if(beforeFlag.equals("<") && afterFlag.equals(">")){
			hinfo.setElement(true);
			return hinfo;
		}else if(beforeFlag.equals("(") && afterFlag.equals(")")){
			hinfo.setElement(true);
			return hinfo;
		}
		return null;
	}

		

}
