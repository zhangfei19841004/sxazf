package plug.extension;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class DocumentHandler {
	
	public String getCurrentFileName() {		
		IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();		
		if (page != null) {			
			IEditorPart editorPart = page.getActiveEditor();			
			if(editorPart != null){
				return editorPart.getEditorInput().getName();
			}					
		}
		return null;
	}
	
	public String getCurrentObject(IDocument d, int offset){
		int lineNumber = this.getFocusLineNumber(d, offset);		
		if(lineNumber>0){
			RegExp re = new RegExp();			
			for (int i = lineNumber; i > 0; i--) {
				if(i < lineNumber){
					try {
						offset = d.getLineInformation(i).getOffset();
					} catch (BadLocationException e) {						
						e.printStackTrace();
					}
				}
				String line = this.getLineContent(d, i-1, offset);
				if(re.isComments(line)){
					continue;
				}
				List<String> pages = re.pageOrObjectName(line);
				if(pages!=null){
					return pages.get(0);
				}
			}
		}
		return null;
	}
	
	public boolean hasObjInCurrentLine(IDocument d, int offset){
		RegExp re = new RegExp();
		List<String> pages = re.pageOrObjectName(this.getLineContent(d, this.getFocusLineNumber(d, offset)-1, offset));
		return pages==null?false:true;
	}
	
	public String getCurrentWorkbenchPath() {		
		IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = win.getActivePage();		
		if (page != null) {			
			IEditorPart editorPart = page.getActiveEditor();			
			if(editorPart != null){
				IFile file = (IFile) editorPart.getEditorInput().getAdapter(IFile.class);
				return file.getProject().getLocation().toFile().getAbsolutePath();				
			}					
		}
		return null;
	}
	
	public int getFocusLineNumber(IDocument d, int offset){
		if(offset==0){
			return 1;
		}
		int lines = d.getNumberOfLines();		
		int index = 0;
		int lineNumber = 0;
		for (int i = 0; i < lines; i++) {
			if(index >= offset){				
				break;
			}
			try {
				index += d.getLineLength(i);				
				lineNumber++;
			} catch (BadLocationException e) {				
				e.printStackTrace();
			}
		}
		return lineNumber;
	}
	
	public String getLineContent(IDocument d, int lineNumber, int focusOffset){		
		try {
			int lineOffset = d.getLineInformation(lineNumber).getOffset();
			return d.get(lineOffset,focusOffset-lineOffset);
		} catch (BadLocationException e) {			
			e.printStackTrace();
		}
		return null;
	}
	
	public int getFocusIndex(IDocument d, int lineNumber, int focusOffset){
		try {			
			int lineOffset = d.getLineInformation(lineNumber).getOffset();			
			return focusOffset-lineOffset;
		} catch (BadLocationException e) {			
			e.printStackTrace();
		}
		return 0;
	}
	
}
