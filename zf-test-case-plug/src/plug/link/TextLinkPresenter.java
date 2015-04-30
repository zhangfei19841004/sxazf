package plug.link;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.DefaultHyperlinkPresenter;
import org.eclipse.swt.graphics.Color;

public class TextLinkPresenter extends DefaultHyperlinkPresenter{

	public TextLinkPresenter(Color color, ITextViewer textViewer) {
		super(color);		
//		this.install(textViewer);
	}

}
