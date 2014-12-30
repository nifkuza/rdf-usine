package frontend;

import frontend.Popup.Type;
import input.Configuration;
import input.Field;
import input.Predicate;
import input.Prefix;
import input.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import output.RDFOutput;
import output.RowNotValidException;

/**
 * FXML Controller class
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 */
public class AppController implements Initializable 
{
	private Configuration conf = new Configuration();
	
	private ResourceBundle bundle = null;
	
	//@FXML private AnchorPane pnTopRight;
	@FXML private Accordion acdLeft;
	
	@FXML private TextField txtSourceFile;
	@FXML private ComboBox<String> cbxEncoding;
	@FXML private ComboBox<String> cbxDelimiter;
	@FXML private TextField txtEscapeCharacter;
	
	@FXML private RadioButton rbtHeadersInRow;
	@FXML private TextField txtHeadersInRow;
	@FXML private RadioButton rbtHeadersCustom;
	
	@FXML private CheckBox chkStart;
	@FXML private CheckBox chkEnd;
	@FXML private TextField txtStart;
	@FXML private TextField txtEnd;
	
	@FXML private TextField txtEntityType;

	@FXML private TextField txtSubjectCol;
	@FXML private TextField txtSubjectPrefix;
	@FXML private RadioButton rbtSubjectCol;
	@FXML private RadioButton rbtSubjectAutonumeric;

	@FXML private ComboBox<File> cbxPreview;
	
	@FXML private TableView lstFields;
	@FXML private TextField txtPredicatePrefix;
	@FXML private TextField txtObjectPrefix;
	@FXML private Button btnAutogenerateFields;
	@FXML private TextField txtAddField;
	@FXML private ComboBox<String> cbxFieldType;
	@FXML private Button btnAddField;
	@FXML private Button btnFieldUpdate;
	@FXML private Button btnFieldMoveUp;
	@FXML private Button btnFieldMoveDown;
	@FXML private Button btnFieldDelete;
	@FXML private Button btnFieldDetectTypes;

	@FXML private Label lblFieldsError;
	@FXML private Label lblFieldsError2;

	@FXML private TableView lstPrefix;
	@FXML private TextField txtAddPrefixName;
	@FXML private TextField txtAddPrefixReference;
	@FXML private Button btnAddPrefix;
	@FXML private Button btnPrefixDelete;
	
	@FXML private TextField txtOutputFolder;
	@FXML private Button btnRun;
	
	@FXML private TextArea txtSourceFileContent;
	@FXML private TableView tblPreview;
	@FXML private TextArea txtTurtle;
	@FXML private TextArea txtNTriple;
	
	@FXML private Menu mnuExport;
	//@FXML private MenuItem mnuRdfXml;
	@FXML private RadioMenuItem rbmPreview10;
	@FXML private RadioMenuItem rbmPreview100;
	@FXML private RadioMenuItem rbmPreview1000;
	@FXML private RadioMenuItem rbmPreviewAll;
	@FXML private RadioMenuItem rbmNoPreview;
	
	@FXML private RadioMenuItem rbmEnglish;
	@FXML private RadioMenuItem rbmFrench;
	@FXML private RadioMenuItem rbmSpanish;

	@FXML
	protected void handleBtnBrowseFolderAction(ActionEvent event)
	{
		List<File> files = Util.getFiles(Util.chooseFolder());
		
		chooseFiles(files, true);
	}

	@FXML 
	protected void handleBtnBrowseAction(ActionEvent event) 
	{
		List<File> files = Util.chooseFiles();
		
		chooseFiles(files, true);
	}

	@FXML 
	protected void handleBtnRunAction(ActionEvent event) 
	{
		String outputDir = txtOutputFolder.getText();
		
		if (!Util.fileExists(outputDir))
			return;
		
		List<RDFOutput> formats = Util.getRDFFormats();
		
		int index = 0; 
		for (File f : conf.getSourceFiles())
		{
	        if (f!=null && f.exists())
	        {
	        	String namePrefix = f.getName().split("\\.")[0];
	        	String content = Util.getFileContents(f, conf.getEncoding(), Long.MAX_VALUE, conf);
	        	
	        	Configuration nc = (Configuration) conf.clone();
	        	nc.setSelectedFileIndex(index);
	        	
	        	for (RDFOutput o : formats)
	        	{
	        		String outputFile = outputDir + "\\" + namePrefix + o.getExtension();
	        		
	        		int i = 1;
	        		while (new File(outputFile).exists())
	        			outputFile = outputDir + "\\" + namePrefix + "-" + ++i + o.getExtension();
	        		
	        		try
	        		{
	        			Util.saveRFDWithPrefixes(nc, o.getFormat(), o.getGraphName(), Long.MAX_VALUE, outputFile);
	        		}
	        		catch (Exception e)
	        		{
	        			Popup.popupMessage(Type.ERROR, bundle.getString("error_saving"), bundle.getString("error_saving_title"));
	        			throw new RuntimeException(e);
	        			//return;
	        		}
	        	}
	        	
	        }	
	        
	        index++;
		}
		
		Popup.popupMessage(Type.ACCEPT, bundle.getString("files_generated"), bundle.getString("files_generated_title"));
	}
	
	@FXML
	protected void handleTxtOutputFolderClick(Event e)
	{
		String dir = Util.chooseFolder();
		
		if (Util.fileExists(dir))
		{
			txtOutputFolder.setText(dir);
			btnRun.setDisable(false);
			btnRun.requestFocus();
		}
		else
		{
			btnRun.setDisable(true);
		}
	}
	
	private void chooseFiles(List<File> files, boolean selectFirst)
	{
		if (files==null || files.size()==0)
			return;
		
		cbxPreview.getItems().clear();
		conf.getSourceFiles().clear();
		
		for (File f : files)
		{
			cbxPreview.getItems().add(f);
			conf.addSourceFile(f);
		}
		
		if (selectFirst || cbxPreview.getItems().size()-1 > conf.getSelectedFileIndex())
			conf.setSelectedFileIndex(0);
			
		cbxPreview.getSelectionModel().select(conf.getSelectedFileIndex());
		
		if (files.size()==1)
			txtSourceFile.setText(files.get(0).getAbsolutePath());
		else
		{
			txtSourceFile.setText("[" + files.size() + " " + bundle.getString("file_count"));
			loadInputFile();
		}
	}
	
	@FXML
	protected void handleCbxEncodingChange(Event event)
	{
		conf.setEncoding(cbxEncoding.getSelectionModel().getSelectedItem());
		loadInputFile();
	}
	
	@FXML
	protected void handleCbxPreviewChange(Event event)
	{
		if (cbxPreview.getSelectionModel().getSelectedIndex()>=0)
		{
			conf.setSelectedFileIndex(cbxPreview.getSelectionModel().getSelectedIndex());
			loadInputFile();
		}
	}
	
	@FXML
	protected void handleCbxDelimiterChange(Event event)
	{
		conf.setFieldDelimiter(Util.getFieldDelimiterSymbols().get(cbxDelimiter.getSelectionModel().getSelectedIndex()));
		processInputFile();
	}
	
	private void loadInputFile()
	{
		if (rbmNoPreview.isSelected())
		{
			txtSourceFileContent.setText(bundle.getString("preview_disabled"));
			txtTurtle.setText(bundle.getString("preview_disabled"));
			txtNTriple.setText(bundle.getString("preview_disabled"));
			tblPreview.getItems().clear();
			return;
		}
			
		File f = cbxPreview.getSelectionModel().getSelectedItem();
		
        if (f!=null && f.exists())
        {
        	String content = Util.getFileContents(f, conf.getEncoding(), getMaxRows(), conf);
        	
        	txtSourceFileContent.setText(content);
        	
        	checkAndProcessInputFile();
        }		
	}
	
	private void processInputFile()
	{
		if (rbmNoPreview.isSelected())
		{
			txtSourceFileContent.setText(bundle.getString("preview_disabled"));
			txtTurtle.setText(bundle.getString("preview_disabled"));
			txtNTriple.setText(bundle.getString("preview_disabled"));
			tblPreview.getItems().clear();
			return;
		}
		
		ArrayList<String> headers = Util.getHeadersFromInputFile(conf, false, true);
		setColumns(headers);
		
		tblPreview.getItems().addAll( Util.getRowsFromInputFile(conf, getMaxRows()) );
		
		try
		{	
			//"TRIX", "TRIG", "RDF/XML", "N-TRIPLE" and "N3".
			
			OutputStream o = new ByteArrayOutputStream();
			Util.saveRFDWithPrefixes(conf, "TURTLE", "http://rdf.org/graph", getMaxRows(), o);
			txtTurtle.setText(o.toString());

			o = new ByteArrayOutputStream();
			Util.saveRFDWithPrefixes(conf, "N-TRIPLE", "http://rdf.org/graph", getMaxRows(), o);
			txtNTriple.setText(o.toString());
			
			mnuExport.setDisable(false);
		}
		catch (RowNotValidException e)
		{
			Popup.popupMessage(Type.ERROR, bundle.getString("error_subject_column"), bundle.getString("error_invalid_row"));
			tblPreview.getItems().clear();
			setRdfFields(bundle.getString("error_subject_column"));
			mnuExport.setDisable(true);
		}
		
		lblFieldsError.setText(conf.getErrorMessage());
		lblFieldsError2.setText(conf.getErrorMessage());
	}
	
	private void setColumns(ArrayList<String> columns)
	{
		if (columns==null)
			return;
		
		if (conf.getHeadersInRow()!=null)
		{
			lstFields.getItems().clear();
			conf.getPredicatesList().clear();
			
			for (String column : columns)
				addField(column, "string", txtPredicatePrefix.getText(), txtObjectPrefix.getText());
			
			btnFieldMoveDown.setDisable(true);
			btnFieldMoveUp.setDisable(true);
			btnFieldDelete.setDisable(true);
			txtAddField.setDisable(true);
			cbxFieldType.setDisable(true);
			btnAddField.setDisable(true);
		}
		else
		{
			btnFieldMoveDown.setDisable(false);
			btnFieldMoveUp.setDisable(false);
			btnFieldDelete.setDisable(false);
			txtAddField.setDisable(false);
			cbxFieldType.setDisable(false);
			btnAddField.setDisable(false);
		}

	    TableColumn [] tableColumns = new TableColumn[columns.size()];     

	    int columnIndex = 0;
	    
		tblPreview.getItems().clear();
		tblPreview.getColumns().clear();
	    
	    for(int i=0 ; i<columns.size(); i++) 
	    {
	        final int j = i;
	        
	        TableColumn col = new TableColumn(columns.get(i));
	        
	        col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>()
	        {                   
	           public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) 
	           {
	        	   if (param.getValue().size()>j && param.getValue().get(j).toString().length()>0)
	        		   return new SimpleStringProperty(param.getValue().get(j).toString());
	        	   else
	        		   return new SimpleStringProperty("");
	           }                   
	        });

	        tblPreview.getColumns().addAll(col);
	    } 
	}

	@FXML
	protected void handleChkStartClick(Event e)
	{
		txtStart.setDisable(!chkStart.isSelected());
		
		if (!chkStart.isSelected())
			txtStart.clear();
		else
			txtStart.setText("1");
		
		processInputFile();
	}
	
	@FXML
	protected void handleChkEndClick(Event e)
	{
		txtEnd.setDisable(!chkEnd.isSelected());
		
		if (!chkEnd.isSelected())
			txtEnd.clear();
		else
			txtEnd.setText("1000");
		
		processInputFile();
	}
	
	@FXML
	protected void handleRbtHeadersInRowChanged(Event e)
	{
		headerSettingsChanged();
	}
	
	@FXML
	protected void handleRbtHeadersCustomChanged(Event e)
	{
		headerSettingsChanged();
	}
	
	private void headerSettingsChanged()
	{
		txtHeadersInRow.setDisable(!rbtHeadersInRow.isSelected());
		
		if (!rbtHeadersInRow.isSelected())
			txtHeadersInRow.clear();
		else
			txtHeadersInRow.setText("1");
		
		processInputFile();
	}
	
	@FXML
	protected void handleRbtSubjectColChange(Event e)
	{
		subjectSettingsChanges();
	}
	
	@FXML
	protected void handlerbtSubjectAutonumericChange(Event e)
	{
		subjectSettingsChanges();
	}
	
	protected void subjectSettingsChanges()
	{
		txtSubjectCol.setDisable(!rbtSubjectCol.isSelected());
		
		if (rbtSubjectCol.isSelected())
		{
			txtSubjectCol.setText("1");
		}
		else
		{
			txtSubjectCol.clear();
			conf.setSubjectColumn(null);
		}
		
		checkAndProcessInputFile();
	}
	
	@FXML
	protected void handleBtnAutogenerateFields(Event e)
	{
		int size = Util.countFieldsInFile(conf);
		
		String root = "";
		char letter = 'a';
		
		conf.getPredicatesList().clear();
		lstFields.getItems().clear();
		
		for (int i = 1; i<=size; i++)
		{
			addField(root + Character.toString(letter), "string", txtPredicatePrefix.getText(), txtObjectPrefix.getText());
			
			if (letter == 'z')
			{
				root += "a";
				letter = 'a';
			}
			else
			{
				letter++;
			}
		}
		
		checkAndProcessInputFile();
	}
	
	@FXML
	protected void handleAddFieldsRow(Event e)
	{
		addField(txtAddField.getText(), 
				 cbxFieldType.getSelectionModel().getSelectedItem(), 
				 txtPredicatePrefix.getText(),
				 txtObjectPrefix.getText());
		
	    checkAndProcessInputFile();
	}
	
	private void addField(String name, String type, String predicatePrefix, String objectPrefix)
	{
	    addFieldTableOnly(name, type, predicatePrefix, objectPrefix);

	    conf.addPredicate(name, type, predicatePrefix, objectPrefix);
	    
	    txtAddField.clear();
	    txtAddField.requestFocus();
	}

	private void addFieldTableOnly(String name, String type, String predicatePrefix, String objectPrefix)
	{
		ObservableList<String> row = FXCollections.observableArrayList();
		
		row.addAll(Integer.toString(lstFields.getItems().size() + 1));
		row.addAll(name);
		row.addAll(type);
		
		lstFields.getItems().add(row);
	}
	
	@FXML
	protected void handleAddPrefixRow(Event e)
	{
		addPrefix(txtAddPrefixName.getText(), txtAddPrefixReference.getText());
		checkAndProcessInputFile();
	}

	protected void addPrefix(String name, String reference)
	{
	    ObservableList<String> row = FXCollections.observableArrayList();
	    
	    row.addAll(name);
	    row.addAll(reference);
	    
	    lstPrefix.getItems().add(row);
	    
	    txtAddPrefixName.clear();
	    txtAddPrefixReference.clear();
	    txtAddPrefixName.requestFocus();
	    
	    conf.addPrefix(name, reference);
	}
	
	@FXML
	protected void handleBtnFieldMoveUp(Event e)
	{
		int index = lstFields.getSelectionModel().getSelectedIndex();
		
		if (index>=1)
		{
			exchageItems(lstFields, index-1, index);
			reindex(lstFields);
			
			Predicate a = conf.getPredicatesList().get(index);
			Predicate b = conf.getPredicatesList().get(index-1);
			conf.getPredicatesList().remove(index);
			conf.getPredicatesList().remove(index-1);
			conf.getPredicatesList().add(a);
			conf.getPredicatesList().add(b);
			
			lstFields.getSelectionModel().select(index-1);
		
			processInputFile();
		}
	}

	@FXML
	protected void handleBtnFieldMoveDown(Event e)
	{
		int index = lstFields.getSelectionModel().getSelectedIndex();
		
		if (index>=0 && index<lstFields.getItems().size()-1)
		{
			exchageItems(lstFields, index, index+1);
			reindex(lstFields);

			Predicate a = conf.getPredicatesList().get(index);
			Predicate b = conf.getPredicatesList().get(index+1);
			conf.getPredicatesList().remove(index+1);
			conf.getPredicatesList().remove(index);
			conf.getPredicatesList().add(b);
			conf.getPredicatesList().add(a);
			
			lstFields.getSelectionModel().select(index+1);
		
			processInputFile();
		}		
	}
	
	private void exchageItems(TableView list, int indexA, int indexB)
	{
		ObservableList<String> newSelected = FXCollections.observableArrayList();
		ObservableList<String> newOther = FXCollections.observableArrayList();
		
		ObservableList<String> selected = (ObservableList<String>) list.getItems().get(indexA);
		ObservableList<String> other = (ObservableList<String>) list.getItems().get(indexB);
		
		for (String s : selected)
			newOther.add(new String(s));
		
		for (String o : other)
			newSelected.add(new String(o));
		
		ObservableList newItems = FXCollections.observableArrayList();
		
		int i = 0;
		for (Object o : lstFields.getItems())
		{
			if (i==indexA)
				newItems.add(newSelected);
			else if (i==(indexB))
				newItems.add(newOther);
			else
				newItems.add(o);
			
			i++;
		}

		list.getItems().clear();
		list.setItems(newItems);
	}
	
	@FXML
	protected void handleChooseField(Event e)
	{
		if (lstFields.getSelectionModel().getSelectedIndex()>=0)
		{
			Predicate p = conf.getPredicatesList().get(lstFields.getSelectionModel().getSelectedIndex());
			
			txtAddField.setText(p.getName());
			txtPredicatePrefix.setText(p.getPredicatePrefix());
			txtObjectPrefix.setText(p.getObjectPrefix());
			cbxFieldType.getSelectionModel().select(p.getType());
			
			btnFieldUpdate.setDisable(conf.getHeadersInRow()!=null);
		}
	}
	
	@FXML
	protected void handleBtnFieldUpdate(Event e)
	{
		int index = lstFields.getSelectionModel().getSelectedIndex();
		if (index<0)
			return;
		
		Predicate p = conf.getPredicatesList().get(index);
	
		p.setName(txtAddField.getText());
		p.setPredicatePrefix(txtPredicatePrefix.getText());
		p.setObjectPrefix(txtObjectPrefix.getText());
		p.setType(cbxFieldType.getSelectionModel().getSelectedItem());
		
	    ObservableList<String> row = FXCollections.observableArrayList();
	    row.add(Integer.toString(index+1));
	    row.add(p.getName());
	    row.add(p.getType());
	    lstFields.getItems().remove(index);
	    lstFields.getItems().add(index, row);
	    
	    lstFields.getSelectionModel().select(index);
	    handleChooseField(null);
	    
	    checkAndProcessInputFile();
	}
	
	@FXML
	protected void handleBtnFieldDelete(Event e)
	{
		int index = lstFields.getSelectionModel().getSelectedIndex();
		
		if (index>=0)
		{
			lstFields.getItems().remove(index);
			conf.getPredicatesList().remove(index);
			
			reindex(lstFields);
			checkAndProcessInputFile();
		}		
	}
	
	@FXML
	protected void handleBtnFieldDetectTypes(Event e)
	{
		List<Field> types = Util.getFieldTypes(conf);
		
		for (Predicate p : conf.getPredicatesList())
		{
			Field type = Util.getField(p.getName(), types);
			
			if (type!=null)
			{
				String newType = type.getType();
				
				if (!newType.equals(p.getType()))
					p.setType(newType);
			}
		}
		
		lstFields.getItems().clear();
		
		for (Predicate p : conf.getPredicatesList())
			addFieldTableOnly(p.getName(), p.getType(), p.getPredicatePrefix(), p.getObjectPrefix());
		
		if (conf.getPredicatesList().size()>0)
		{
			lstFields.getSelectionModel().select(0);
			handleChooseField(null);
		}
		
		processInputFile();
	}
	
	@FXML
	protected void handleMnuPreview(Event e)
	{
		conf.setMaxRows(getMaxRows());

		loadInputFile();
	}
	
	@FXML
	protected void handleMnuSaveTurtle(Event e)
	{
		saveRdf(conf, "TURTLE", "http://rdf.org/graph");
	}

	@FXML
	protected void handleMnuSaveNTriple(Event e)
	{
		saveRdf(conf, "N-TRIPLE", "http://rdf.org/graph");
	}
	
	@FXML
	protected void handleMnuSaveFuseki(Event e)
	{
		saveRdf(conf, "FUSEKI", "http://rdf.org/graph");
	}

	private void saveRdf(Configuration conf, String format, String graphUri)
	{
		String file = Util.chooseFileForSaving();
		
		if (file.length()==0)
			return;
		
		try 
		{
			Util.saveRFDWithPrefixes(conf, format, graphUri, Long.MAX_VALUE, file);
		} 
		catch (Exception e1) 
		{
			Popup.popupMessage(Type.ERROR, bundle.getString("error_saving"), bundle.getString("error_saving_title"));
		}
	}
	
	private void checkAndProcessInputFile()
	{
		String contents = conf.getSourceFileContent(getMaxRows());
		
		if (contents==null || contents.trim().length()==0)
			return;
		
		int fields = Util.countFieldsInFile(conf);
		
		int fieldsDefined = conf.getPredicatesList().size();
		
		if (conf.getSubjectColumn()!=null)
			fields--;

		if (fields!=fieldsDefined)
		{
			// The number of fields does not match
			
			if (conf.getHeadersInRow()!=null)
			{
				// If headers come from a row in the file, an attempt to refresh the list of fields is done
				ArrayList<String> headers = Util.getHeadersFromInputFile(conf, false, true);
				setColumns(headers);

				fieldsDefined = conf.getPredicatesList().size();
				
				if (fields!=fieldsDefined)
				{
					// If the number of fields still does not match, an error message is shown.
					lblFieldsError2.setText(bundle.getString("error_should_be") + fields + " " + bundle.getString("error_fields"));
					setRdfFields(bundle.getString("error_should_be") + fields + " " + bundle.getString("error_fields_2"));
					mnuExport.setDisable(true);
				}
				else
				{
					// Otherwise, the input file is processed.
					lblFieldsError2.setText("");
					setRdfFields("");
					mnuExport.setDisable(true);

					processInputFile();
				}
			}
			else
			{
				// As the headers were input manually, an error message is displayed so that the user can correct the issue.
				lblFieldsError2.setText(bundle.getString("error_should_be") + fields + " " + bundle.getString("error_fields"));
				setRdfFields(bundle.getString("error_should_be") + fields + " " + bundle.getString("error_fields_2"));
				mnuExport.setDisable(true);
			}
		}
		else
		{
			//The number of fields matches
			
			lblFieldsError2.setText("");
			setRdfFields("");
			mnuExport.setDisable(true);
			
			if (conf.getHeadersInRow()!=null)
			{
				long rowsInFile = Util.countRowsInInputFile(conf);
				
				// Headers row number needs to be checked
				if ( conf.getHeadersInRow() > rowsInFile )
				{
					//The row number is not correct					
					tblPreview.getItems().clear();
					tblPreview.getColumns().clear();
					setRdfFields(bundle.getString("error_header") + rowsInFile);
					mnuExport.setDisable(true);
				}
				else
				{
					//The row number is correct
					processInputFile();
				}
			}
			else
			{
				// Headers row number does not need to be checked
				processInputFile();
			}
		}
	}
	
	@FXML
	protected void handleBtnPrefixDelete(Event e)
	{
		int index = lstPrefix.getSelectionModel().getSelectedIndex();
		
		if (index>=0)
		{
			lstPrefix.getItems().remove(index);
			conf.getPrefixesList().remove(index);
			
			processInputFile();
		}
	}
	
	private void reindex(TableView t)
	{
		int c = 1;
		
		for (Object row : t.getItems())
		{
			ObservableList<String> s = (ObservableList<String>) row;
			s.set(0, Integer.toString(c));
			c++;
		}
	}

	@FXML
	protected void handleMnuSave(Event e)
	{
		String file = Util.chooseFileForSaving();
		
		if (file.length()==0)
			return;
		
		try 
		{
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file)); 
			
			os.writeObject(conf);
			os.flush();
			os.close();
		} 
		catch (Exception e1) 
		{
			Popup.popupMessage(Type.ERROR, bundle.getString("error_saving_conf"), bundle.getString("error_saving_title"));
			throw new RuntimeException(e1);
		}
	}

	@FXML
	protected void handleMnuLoad(Event e)
	{
		String file = Util.chooseFile();
		
		if (file.length()==0)
			return;
		
		if (file!=null)
		{
			try 
			{
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(file)); 
				
				Configuration c = (Configuration) is.readObject();
				is.close();

				initialiseSettings(c);
			} 
			catch (Exception e1) 
			{
				Popup.popupMessage(Type.ERROR, bundle.getString("error_loading_conf"), bundle.getString("error_loading_title"));
				
				throw new RuntimeException(e1);
			}
		}		
	}
	
	private void initialiseSettings(Configuration c)
	{
		cbxDelimiter.getSelectionModel().select(Util.getFieldDelimiterSymbols().indexOf(c.getFieldDelimiter()));
		
		if (c.getStartRow()!=null)
		{
			chkStart.setSelected(true);
			txtStart.setText(c.getStartRow().toString());
			txtStart.setDisable(false);
		}

		if (c.getEndRow()!=null)
		{
			chkEnd.setSelected(true);
			txtEnd.setText(c.getEndRow().toString());
			txtEnd.setDisable(false);
		}
		
		txtEscapeCharacter.setText(c.getEscapeCharacter());
		
		txtEntityType.setText(c.getEntityType());
		
		cbxEncoding.getSelectionModel().select(c.getEncoding());
		
		if (c.getHeadersInRow()!=null)
		{
			rbtHeadersInRow.setSelected(true);
			txtHeadersInRow.setText(c.getHeadersInRow().toString());
			txtHeadersInRow.setDisable(false);
		}
		else
		{
			rbtHeadersCustom.setSelected(true);
			txtHeadersInRow.clear();
			txtHeadersInRow.setDisable(true);
		}
		
		rbtSubjectCol.setSelected(c.getSubjectColumn()!=null);
		rbtSubjectAutonumeric.setSelected(c.getSubjectColumn()==null);
		txtSubjectPrefix.setText(c.getSubjectPrefix());
		
		if (c.getSubjectColumn()!=null)
		{
			txtSubjectCol.setText(c.getSubjectColumn().toString());
			txtSubjectCol.setDisable(false);
		}
		else
		{
			txtSubjectCol.clear();
			txtSubjectCol.setDisable(true);
		}
		
		lstFields.getItems().clear();
		conf.getPredicatesList().clear();
		
		for (Predicate p : c.getPredicatesList())
			addField(p.getName(), p.getType(), p.getPredicatePrefix(), p.getObjectPrefix());
		
		lstPrefix.getItems().clear();
		conf.getPrefixesList().clear();
		
		for (Prefix p : c.getPrefixesList())
			addPrefix(p.getName(), p.getReference());
		
		cbxPreview.getItems().clear();
		conf.getSourceFiles().clear();
		
		for (File f : c.getSourceFiles())
		{
			cbxPreview.getItems().add(f);
			conf.getSourceFiles().add(f);
		}
		
		cbxPreview.getSelectionModel().select(c.getSelectedFileIndex());
		
		chooseFiles(c.getSourceFiles(), false);
	}
	
	@FXML
	protected void handleMnuAbout(Event e)
	{
		String message = bundle.getString("about_message");
		
		Popup.popupMessage(Type.INFO, message, bundle.getString("about"));
	}

	@FXML
	protected void handleMnuClose(Event e)
	{
		System.exit(0);
	}

	@FXML
	protected void handleMnuLanguage(Event e)
	{
		//Configuration oldConfiguration = (Configuration) conf.clone();

		if (rbmSpanish.isSelected())
			bundle = ResourceBundle.getBundle("bundles.bundle", Locale.forLanguageTag("es"));
		else if (rbmFrench.isSelected())
			bundle = ResourceBundle.getBundle("bundles.bundle", Locale.forLanguageTag("fr"));
		else
			bundle = ResourceBundle.getBundle("bundles.bundle", Locale.forLanguageTag("en"));
		
		try 
		{
			BorderPane bp = FXMLLoader.load(getClass().getResource("App.fxml"), bundle);
			Main.primaryStage.setScene(new Scene(bp, 1280, 800));
			Main.primaryStage.setTitle(bundle.getString("application"));
		} 
		catch (IOException e1) 
		{
			throw new RuntimeException(e1);
		}
		
		//initialiseSettings(oldConfiguration);
	}
	
	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) 
	{
		bundle = rb;
		
		setTextBoxChangeListeners();
		
		tblPreview.setPlaceholder(new Text(bundle.getString("no_content")));
		
		initializeFieldsTableView(lstFields, new String[] {bundle.getString("col_column"), 
														   bundle.getString("col_name"), 
														   bundle.getString("col_type")}, 
														   new int[] {100, 300, 150});
		
		initializeFieldsTableView(lstPrefix, new String[] {bundle.getString("col_name"), 
														   bundle.getString("col_reference")}, 
														   new int[] {150, 400});
		
		cbxEncoding.getItems().clear();
		cbxEncoding.getItems().addAll(Util.getCharsets());
		cbxEncoding.getSelectionModel().select(0);
		conf.setEncoding(cbxEncoding.getSelectionModel().getSelectedItem());
		
		cbxFieldType.getItems().clear();
		cbxFieldType.getItems().addAll(Util.getDataTypes());
		cbxFieldType.getSelectionModel().select(0);
		
		cbxDelimiter.getItems().clear();
		cbxDelimiter.getItems().addAll(Util.getFieldDelimiterTexts(bundle));
		cbxDelimiter.getSelectionModel().select(0);
		conf.setFieldDelimiter(Util.getFieldDelimiterSymbols().get(cbxDelimiter.getSelectionModel().getSelectedIndex()));
		
		ToggleGroup groupSubject = new ToggleGroup();
		
		rbtSubjectCol.setToggleGroup(groupSubject);
		rbtSubjectCol.setSelected(true);
		txtSubjectCol.setDisable(false);
		txtSubjectCol.setText("1");
		
		rbtSubjectAutonumeric.setToggleGroup(groupSubject);
		rbtSubjectAutonumeric.setSelected(false);
		
		ToggleGroup groupHeaders = new ToggleGroup();
		
		rbtHeadersInRow.setToggleGroup(groupHeaders);
		rbtHeadersInRow.setSelected(true);
		txtHeadersInRow.setText("1");
		
		rbtHeadersCustom.setToggleGroup(groupHeaders);
		rbtHeadersCustom.setSelected(false);
		
		chkStart.setSelected(false);
		txtStart.setDisable(true);
		
		chkEnd.setSelected(false);
		txtEnd.setDisable(true);
		
		acdLeft.setExpandedPane(acdLeft.getPanes().get(0));
		
		cbxPreview.setButtonCell(new ComboBoxRenderer());
		cbxPreview.setCellFactory(new Callback<ListView<File>, ListCell<File>>() 
		{
            @Override public ListCell<File> call(ListView<File> p) 
            {
                return new ComboBoxRenderer();
            }
        });
		
		ToggleGroup groupPreview = new ToggleGroup();
		
		rbmPreview10.setToggleGroup(groupPreview);
		rbmPreview100.setToggleGroup(groupPreview);
		rbmPreview1000.setToggleGroup(groupPreview);
		rbmPreviewAll.setToggleGroup(groupPreview);
		rbmNoPreview.setToggleGroup(groupPreview);
		
		ToggleGroup groupLanguage = new ToggleGroup();
		
		rbmEnglish.setToggleGroup(groupLanguage);
		rbmSpanish.setToggleGroup(groupLanguage);
		rbmFrench.setToggleGroup(groupLanguage);
		
		conf.setMaxRows(getMaxRows());
		
		rbmFrench.setGraphic(new ImageView(new Image("resources/images/french.png")));
		rbmEnglish.setGraphic(new ImageView(new Image("resources/images/english.png")));
		rbmSpanish.setGraphic(new ImageView(new Image("resources/images/spanish.png")));
	}

	private void setTextBoxChangeListeners() 
	{
		txtSourceFile.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
		        String path = newValue;
		        
		        if (Util.fileExists(path))
		        {
		        	conf.getSourceFiles().clear();
		        	conf.addSourceFile(new File(path));
		        	
		        	cbxPreview.getItems().clear();
		        	cbxPreview.getItems().add(new File(path));
		        	cbxPreview.getSelectionModel().select(0);
		        	
		        	loadInputFile();
		        }
		        else
		        {
		        	txtSourceFileContent.clear(); 
		        }
			}
		});
		
		txtHeadersInRow.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				if (!newValue.matches("\\d+") && rbtHeadersInRow.isSelected())
				{
					txtHeadersInRow.setText(oldValue);
				}
				else
				{
					conf.setHeadersInRow(Util.parseInt(newValue));
					checkAndProcessInputFile();
				}
			}
		});
		
		txtStart.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				if (!newValue.matches("\\d+") && chkStart.isSelected())
				{
					txtStart.setText(oldValue);
				}
				else
				{
					conf.setStartRow(Util.parseInt(newValue));
					processInputFile();
				}
			}
		});
		
		txtEnd.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				if (!newValue.matches("\\d+") && chkEnd.isSelected())
				{
					txtEnd.setText(oldValue);
				}
				else
				{
					conf.setEndRow(Util.parseInt(newValue));
					processInputFile();
				}
			}
		});
		
		txtEntityType.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				conf.setEntityType(newValue);
				processInputFile();
			}
		});
		
		txtSubjectCol.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				if (!newValue.matches("\\d+") && rbtSubjectCol.isSelected())
				{
					txtSubjectCol.setText(oldValue);
				}
				else
				{
					conf.setSubjectColumn(Util.parseInt(newValue));
					
					checkAndProcessInputFile();
				}
			}
		});
		
		txtSubjectPrefix.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				conf.setSubjectPrefix(newValue);
				processInputFile();
			}
		});
		
		txtEscapeCharacter.textProperty().addListener(new ChangeListener<String>() 
		{
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
			{
				conf.setEscapeCharacter(newValue);
				processInputFile();
			}
		});	
	}	
	
	public void initializeFieldsTableView(TableView tbl, String[] columns, int[] prefSizes)
	{
	    TableColumn [] tableColumns = new TableColumn[columns.length];     
	    int columnIndex = 0;
	    for(int i=0 ; i<columns.length; i++) 
	    {
	        final int j = i;
	        TableColumn col = new TableColumn(columns[i]);

	        col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>()
	        {                   
	           public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) 
	           {                                                                                             
	                return new SimpleStringProperty(param.getValue().get(j).toString());                       
	           }                   
	        });
	        
	        col.setPrefWidth(prefSizes[i]);
	        
	        tbl.getColumns().addAll(col);
	    }
	    
	    tbl.setPlaceholder(new Text(bundle.getString("no_content")));
	}
	
	public void setRdfFields(String text)
	{
		txtTurtle.setText(text);
		txtNTriple.setText(text);
	}
	
	public long getMaxRows()
	{
		if (rbmPreview10.isSelected())
			return 10;
		else if (rbmPreview100.isSelected())
			return 100;
		else if (rbmPreview1000.isSelected())
			return 1000;
		else if (rbmNoPreview.isSelected())
			return 1;
		else
			return Long.MAX_VALUE;
	}
}