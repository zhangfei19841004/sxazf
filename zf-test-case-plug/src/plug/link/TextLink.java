package plug.link;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import plug.extension.DocumentHandler;
import plug.extension.ParseXml;
import plug.hover.HoverInfo;
import plug.util.Constants;
import plug.util.TestDataInit;

public class TextLink implements IHyperlink{
	
	private final IRegion fUrlRegion; 
	
	private final HoverInfo hinfo;
	
	private boolean needCreate = false;
	
	private InputStream source = null;
	  
	public TextLink(IRegion urlRegion, HoverInfo hinfo) {  
		this.fUrlRegion = urlRegion;  
		this.hinfo = hinfo;
	}  

	@Override
	public IRegion getHyperlinkRegion() {		
		return fUrlRegion;
	}

	@Override
	public String getHyperlinkText() {		
		return null;
	}

	@Override
	public String getTypeLabel() {		
		return null;
	}

	@Override
	public void open() {
		IWorkbenchWindow win = PlatformUI.getWorkbench().getActiveWorkbenchWindow();		
		IWorkbenchPage page = win.getActivePage();	
		IFile file = this.getIFile(page);
		try {
			if(file != null && !needCreate){
				this.openZFFile(page, file);				
			}else if(file != null && needCreate){				
				file.create(source, false, null);	
				this.openZFFile(page, file);
			}			
		} catch (PartInitException e) {			
			e.printStackTrace();
		} catch (CoreException e) {			
			e.printStackTrace();
		}		
	}
	
	private void openZFFile(IWorkbenchPage page, IFile file){		
		try {
			if(Constants.EXTENSION_NAME.equals(file.getFileExtension())){
				IDE.openEditor(page, file, "plug.editors.zf");
			}else{
				IDE.openEditor(page, file);
			}
		} catch (PartInitException e) {				
			e.printStackTrace();
		}
		
	}
	
	private IFile getIFile(IWorkbenchPage page){
		DocumentHandler dh = new DocumentHandler();
		String workspace = dh.getCurrentWorkbenchPath();		
		IFile activeFile = (IFile) page.getActiveEditor().getEditorInput().getAdapter(IFile.class);
		IProject project = activeFile.getProject();
		IFile file = null;
		if(hinfo.isObject() && hinfo.isPage()){	
			file = this.handlePage(project);
		}else if(hinfo.isObject() && !hinfo.isPage()){
			file = this.handleOtherObject(project, dh);
		}else if(hinfo.isMethod()){
			file = this.handleMethod(project, workspace);
		}else if(hinfo.isElement()){
			file = this.handleElement(project);
		}
		return file;
	}	
	
	private IFile handlePage(IProject project){
		IFile file = null;
		String pageName = hinfo.getSn();
		if(pageName==null || pageName.equals("")){
			return null;
		}
		if(pageName.equals("Assert") || pageName.equals("Log") || pageName.equals("this")){
			return null;
		}
		pageName = pageName.substring(0,1).toUpperCase().concat(pageName.substring(1));
		file = project.getFile("/src/"+Constants.ROOT_PAGE.replaceAll("\\.", "/")+"/"+pageName+".java");
		if(file.exists()){
			return file;
		}
		file = project.getFile("/locator/"+pageName+".yaml");
		if(file.exists()){			
			return file;
		}else{
			source = new ByteArrayInputStream("".getBytes());
			needCreate = true;
			return file;
		}		
	}
	
	private IFile handleOtherObject(IProject project, DocumentHandler dh){
		IFile file = null;		
		String fileName = dh.getCurrentFileName();	
		String fname = fileName.substring(0, fileName.lastIndexOf("."));
		file = project.getFile("/test-data/"+fname+".xml");
		if(!file.exists()){
			source = new ByteArrayInputStream(TestDataInit.TEST_DATA_XML_CONTENT.getBytes());
			needCreate = true;
		}
		return file;
	}
	
	private IFile handleMethod(IProject project, String workspace){
		IFile file = null;
		ParseXml px = new ParseXml(workspace+"/config/keyword.xml");
		String keywordPath = "/*/"+Constants.KEYWORD_SELF_API+"/"+hinfo.getHoverText();
		file = project.getFile("/config/keyword.xml");
		if(px.isExist(keywordPath)){
			ParseXml pxLinked = new ParseXml(workspace+"/config/LinkedPages.xml");
			String linkedPath = "/*/*/method[@name='"+px.getElementText(keywordPath)+"']";
			if(pxLinked.isExist(linkedPath)){
				HashMap<String, String> attrMap = pxLinked.getElementAttributes(linkedPath);
				IFile fileLinked = project.getFile("/test-cases/"+attrMap.get("linked")+"."+Constants.EXTENSION_NAME); 
				if(fileLinked.exists()){
					file = fileLinked;
				}else{
					file = null;
				}
			}
		}
		return file;
	}
	
	private IFile handleElement(IProject project){
		IFile file = null;
		String pageName = hinfo.getCurrentObject();
		if(pageName==null || pageName.equals("")){
			return null;
		}
		pageName = pageName.substring(0,1).toUpperCase().concat(pageName.substring(1));		
		file = project.getFile("/locator/"+pageName+".yaml");
		if(file.exists()){			
			return file;
		}else{
			source = new ByteArrayInputStream("".getBytes());
			needCreate = true;
			return file;
		}
	}
	
	
	

}
