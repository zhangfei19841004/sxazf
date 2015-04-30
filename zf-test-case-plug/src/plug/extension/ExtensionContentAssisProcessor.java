package plug.extension;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import plug.data.ElementData;
import plug.data.MethodData;
import plug.data.ObjectData;
import plug.data.TestData;
import plug.util.Constants;

public class ExtensionContentAssisProcessor implements IContentAssistProcessor{		

	public ExtensionContentAssisProcessor() {
		super();		
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		DocumentHandler dh = new DocumentHandler();
		String fileName = dh.getCurrentFileName();
		if(fileName==null){
			return null;
		}
		if(fileName.substring(fileName.lastIndexOf(".")+1).equals(Constants.EXTENSION_NAME)){
			RegExp re = new RegExp();
			IDocument d = viewer.getDocument();											
			String doc = d.get();
			String s = doc.substring(offset-1, offset);	
			ICompletionProposal[] proposals = null;
			String line = dh.getLineContent(d, dh.getFocusLineNumber(d, offset)-1, offset);
			if(re.isComments(line)){
				return null;
			}
			if(s.equals("\"")){			
				ObjectData od = new ObjectData();				
				String[] data = od.getData();
				proposals = new ICompletionProposal[data.length];
				for (int i = 0; i< data.length; i++) {
					String r = data[i]+"\"";
					if(data[i].startsWith("如果")){
						r = data[i].replace("如果", "如果\"");
					}
					if(data[i].startsWith("并且")){
						r = data[i].replace("并且", "并且\"");
					}	
					if(data[i].startsWith("或者")){
						r = data[i].replace("或者", "或者\"");
					}
					if(data[i].startsWith("循环")){
						r = data[i].replace("循环", "循环\"");
					}
					proposals[i] = new CompletionProposal(r, offset, 0, data[i].length()+1, null, data[i], null,null) ;
				}
			}else if(s.equals("{") || s.equals("+")){				
				String content = dh.getLineContent(d, dh.getFocusLineNumber(d, offset)-1, offset);
				String[] data = null;
				if(re.isPageOrObjectParameter(content)){
					ObjectData od = new ObjectData();
					od.setRootPath(dh.getCurrentWorkbenchPath());
					data = od.getDataParam();
				}else{
					TestData td = new TestData();
					td.setRootPath(dh.getCurrentWorkbenchPath());
					data = td.getData(fileName.substring(0, fileName.lastIndexOf(".")));					
				}
				if(data != null && data.length>0){
					proposals = new ICompletionProposal[data.length];
					for (int i = 0; i< data.length; i++) {
						String r = data[i];
						if(s.equals("{")){
							r = r + "}";
						}
						proposals[i] = new CompletionProposal(r, offset, 0, data[i].length()+1, null, data[i], null,null) ;
					}
				}				
			} else if(s.equals("[")){
				MethodData md = new MethodData();
				md.setCurrentObj(dh.getCurrentObject(d, offset));
				md.setFlag(dh.hasObjInCurrentLine(d, offset));
				md.setContent(dh.getLineContent(d, dh.getFocusLineNumber(d, offset)-1, offset));
				md.setRootPath(dh.getCurrentWorkbenchPath());
				String[] data = md.getData();
				if(data != null && data.length>0){
					proposals = new ICompletionProposal[data.length];
					for (int i = 0; i< data.length; i++) {						
						proposals[i] = new CompletionProposal(data[i]+"]", offset, 0, data[i].length()+1, null, data[i], null,null) ;
					}
				}				
			}else if(s.equals("(") || s.equals("<")){
				ElementData ed = new ElementData();
				ed.setCurrentObj(dh.getCurrentObject(d, offset));
				ed.setRootPath(dh.getCurrentWorkbenchPath());
				String[] data = ed.getData();
				if(data != null && data.length>0){
					proposals = new ICompletionProposal[data.length];
					for (int i = 0; i< data.length; i++) {						
						proposals[i] = new CompletionProposal(data[i]+(s.equals("(")?")":">"), offset, 0, data[i].length()+1, null, data[i], null,null) ;
					}
				}	
			}
	        return proposals;
		}
		return null;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer arg0,
			int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {	
		return new char[]{'"','{','+','[','<','('};		
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
