package org.jabe.neverland.smallboy;

public interface IBoyAction {
	
	public void onClick(final IBoy boy);
	
	public void onMove();
	
	public void onMoveEnd();
	
	public void onClose(final IBoy boy);
	
}
