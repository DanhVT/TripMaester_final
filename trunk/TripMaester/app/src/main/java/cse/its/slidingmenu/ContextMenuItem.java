package cse.its.slidingmenu;

import android.graphics.drawable.Drawable;
/**
 * 
 * @author Vo tinh
 * context menu item
 */
public class ContextMenuItem {
	private Drawable drawable;
	private String text;


	public ContextMenuItem(Drawable drawable, String text) {
	    super();
	    this.drawable = drawable;
	    this.text = text;
	}

	public Drawable getDrawable() {
	    return drawable;
	}

	public void setDrawable(Drawable drawable) {
	    this.drawable = drawable;
	}

	public String getText() {
	    return text;
	}

	public void setText(String text) {
	    this.text = text;
	}
}
