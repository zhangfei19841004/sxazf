package plug.hover;

public class HoverInfo {
	
	private int beforeIndex;
	
	private int endIndex;
	
	private boolean isObject = false;
	
	private boolean isPage = false;
	
	private boolean isMethod = false;
	
	private boolean isElement = false;
	
	private String sn;
	
	private String hoverText;
	
	private String currentObject;

	public int getBeforeIndex() {
		return beforeIndex;
	}

	public void setBeforeIndex(int beforeIndex) {
		this.beforeIndex = beforeIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public boolean isObject() {
		return isObject;
	}

	public void setObject(boolean isObject) {
		this.isObject = isObject;
	}

	public boolean isPage() {
		return isPage;
	}

	public void setPage(boolean isPage) {
		this.isPage = isPage;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public boolean isMethod() {
		return isMethod;
	}

	public void setMethod(boolean isMethod) {
		this.isMethod = isMethod;
	}

	public boolean isElement() {
		return isElement;
	}

	public void setElement(boolean isElement) {
		this.isElement = isElement;
	}

	public String getHoverText() {
		return hoverText;
	}

	public void setHoverText(String hoverText) {
		this.hoverText = hoverText;
	}

	public String getCurrentObject() {
		return currentObject;
	}

	public void setCurrentObject(String currentObject) {
		this.currentObject = currentObject;
	}
	
}
