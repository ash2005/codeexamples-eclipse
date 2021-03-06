package com.vogella.swt.fonts.roboto.parts;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class SamplePart {

	private Text txtInput;
	private TableViewer tableViewer;

	@Inject
	private MDirtyable dirty;
	
	@Inject Display display;
	
	@Inject Shell shell;

	private void addFonts(Display display) {

		Bundle bundle = FrameworkUtil.getBundle(getClass());
		final String pathString = "fonts/Roboto-ThinItalic.ttf";
		Path path = new Path(pathString);
		URL url = FileLocator.find(bundle, path, Collections.EMPTY_MAP);
		final boolean isFontLoaded = display.loadFont(url.toExternalForm());
		if (!isFontLoaded) {
			new RuntimeException("Did not load font");
		}
	}
	@PostConstruct
	public void createComposite(Composite parent) {
		addFonts(display);
		
		ResourceManager resManager = 
				  new LocalResourceManager(JFaceResources.getResources(), parent);
		FontDescriptor fontDescriptor = FontDescriptor.createFrom("Roboto-ThinItalic", 11, SWT.NORMAL);

		Font font = resManager.createFont(fontDescriptor);
		parent.setLayout(new GridLayout(1, false));
		
		txtInput = new Text(parent, SWT.BORDER);
		txtInput.setFont(font);
		txtInput.setMessage("Enter text to mark part as dirty");
		txtInput.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
			}
		});
		FontData fd = txtInput.getFont().getFontData()[0];
		txtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Text txtInput2 = new Text(parent, SWT.BORDER);
		txtInput2.setMessage("Enter text to mark part as dirty");
		txtInput2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = new Button(parent, SWT.PUSH);
		button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		button.setFont(font);
		button.setText("Press me");
		
		
		
		Button button2 = new Button(parent, SWT.PUSH);
		button2.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		button2.setText("Press me");

		tableViewer = new TableViewer(parent);

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());;
		tableViewer.setInput(createInitialDataModel());
		tableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
	
	private List<String> createInitialDataModel() {
		return Arrays.asList("Sample item 1", "Sample item 2", "Sample item 3", "Sample item 4", "Sample item 5");
	}
}