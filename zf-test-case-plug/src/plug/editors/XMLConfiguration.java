package plug.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.hyperlink.IHyperlinkPresenter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;

import plug.extension.ExtensionContentAssisProcessor;
import plug.hover.KeywordHover;
import plug.link.TextLinkDetector;
import plug.link.TextLinkPresenter;

public class XMLConfiguration extends SourceViewerConfiguration {
	private XMLDoubleClickStrategy doubleClickStrategy;
	private XMLTagScanner tagScanner;
	private XMLScanner scanner;
	private ColorManager colorManager;	

	public XMLConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;		
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			XMLPartitionScanner.XML_COMMENT,
			XMLPartitionScanner.XML_TAG };
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new XMLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected XMLScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new XMLScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IXMLColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected XMLTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new XMLTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(IXMLColorConstants.TAG))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, XMLPartitionScanner.XML_TAG);
		reconciler.setRepairer(dr, XMLPartitionScanner.XML_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(IXMLColorConstants.XML_COMMENT)));
		reconciler.setDamager(ndr, XMLPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, XMLPartitionScanner.XML_COMMENT);

		return reconciler;
	}	
	
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer viewer){
		IHyperlinkDetector[] detectors = new IHyperlinkDetector[1];
		detectors[0] = new TextLinkDetector();
		return detectors;	
	}
	
	public IHyperlinkPresenter getHyperlinkPresenter(ISourceViewer sourceViewer){		
		return new TextLinkPresenter(colorManager.getColor(IXMLColorConstants.KEY_WORD), sourceViewer);	
//		return null;
	}
	
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
		return new KeywordHover();		
	}
	
	@Override 
	public  IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {	
		this.addShortcutListener(sourceViewer);
         //  生成一个ContentAssistant
        ContentAssistant assistant  =    new  ContentAssistant();
        ExtensionContentAssisProcessor scp = new ExtensionContentAssisProcessor();        
        assistant.setContentAssistProcessor(scp, IDocument.DEFAULT_CONTENT_TYPE);       
         //  设置帮组内容弹出响应时间
        assistant.setAutoActivationDelay( 200 );
        assistant.enableAutoActivation( true );
        assistant.setProposalPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
        assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_BELOW);
        return  assistant;
    }
	
	private void addShortcutListener(final ISourceViewer sourceViewer){			
		((TextViewer) sourceViewer).appendVerifyKeyListener(new VerifyKeyListener() {
		    public void verifyKey(VerifyEvent event) {
		        // Check for Alt+/
		        if (event.stateMask == SWT.ALT && event.character == '/') {
		            // Check if source viewer is able to perform operation
		            if (((TextViewer) sourceViewer).canDoOperation(SourceViewer.CONTENTASSIST_PROPOSALS))
		                // Perform operation
		                ((TextViewer) sourceViewer).doOperation(SourceViewer.CONTENTASSIST_PROPOSALS);
		            // Veto this key press to avoid further processing
		            event.doit = false;
		        }
		    }
		});
	}
	
	

}