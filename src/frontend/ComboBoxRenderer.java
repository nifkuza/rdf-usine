package frontend;

import java.io.File;

import javafx.scene.control.ListCell;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 */
public class ComboBoxRenderer extends ListCell<File> 
{
	@Override 
	protected void updateItem(File item, boolean empty) 
	{
		super.updateItem(item, empty);
		if (item!=null)
		{
			setText(item.getName());
		}
	}
}
