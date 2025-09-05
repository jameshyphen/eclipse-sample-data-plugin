package com.hyphen.sampledata.views;

import com.hyphen.sampledata.Activator;
import com.hyphen.sampledata.core.Issue;
import com.hyphen.sampledata.net.IssueClient;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

import java.net.URI;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class IssuesView extends ViewPart {

	public static final String ID = "com.hyphen.sampledata.views.IssuesView";

	/** Allow overriding the API base via JVM arg: -Dissues.api=http://host:port */
	private static final URI API_BASE = URI.create(System.getProperty("issues.api", "http://localhost:8080"));

	private static final DateTimeFormatter HUMAN_LOCAL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
			.withZone(ZoneId.systemDefault());

	private TableViewer viewer;
	private Button fetchBtn;
	private Label status;
	private Label errorLabel;
	private IssueClient client;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		// Action row
		fetchBtn = new Button(parent, SWT.PUSH);
		fetchBtn.setText("Fetch Data");
		fetchBtn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		errorLabel = new Label(parent, SWT.WRAP);
		errorLabel.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
		GridData egd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		egd.horizontalIndent = 8;
		errorLabel.setLayoutData(egd);
		errorLabel.setVisible(false);
		egd.exclude = true;

		// Status below action row
		status = new Label(parent, SWT.NONE);
		status.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		status.setText("Ready.");

		// Results table
		viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		createColumns();
		getSite().setSelectionProvider(viewer);

		// Client
		client = new IssueClient(API_BASE);

		// Wire up actions
		fetchBtn.addListener(SWT.Selection, e -> fetchAsync());
	}

	private void createColumns() {
		addColumn("ID", 60, i -> Integer.toString(i.id()));
		addColumn("Name", 280, Issue::name);
		addColumn("Severity", 100, Issue::severity);
		addColumn("Updated", 240, i -> fmtUpdated(i.updatedAt()));
	}

	private void addColumn(String title, int width, Function<Issue, String> textFn) {
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		col.getColumn().setText(title);
		col.getColumn().setWidth(width);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return textFn.apply((Issue) element);
			}
		});
	}

	private static String fmtUpdated(String s) {
		if (s == null || s.isBlank())
			return "";
		try {
			Instant instant = OffsetDateTime.parse(s).toInstant(); // handles Z and offsets
			return HUMAN_LOCAL.format(instant);
		} catch (Exception ex) {
			return s; // fallback: show raw if unparsable
		}
	}

	private void fetchAsync() {
		clearError();
		setBusy(true, "Loading...");

		CompletableFuture.supplyAsync(() -> {
			try {
				List<Issue> list = client.fetch();
				return (list != null) ? list : List.of();
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}).whenComplete((result, err) -> {
			// Switch to UI thread safely
			Display display = viewer.getControl().getDisplay();
			if (display.isDisposed())
				return;
			display.asyncExec(() -> {
				try {
					if (viewer.getControl().isDisposed())
						return;
					if (err != null) {
						Throwable cause = (err.getCause() != null) ? err.getCause() : err;
						Activator.error("Failed to fetch issues", cause);
						viewer.setInput(List.of());
						showError("Error fetching issues",
								(cause.getMessage() != null) ? cause.getMessage() : cause.toString());
						status.setText("Ready.");
					} else {
						viewer.setInput(result);
						clearError();
						status.setText("Loaded " + result.size() + " item(s).");
					}
				} finally {
					setBusy(false, null);
				}
			});
		});
	}

	private void clearError() {
		GridData gd = (GridData) errorLabel.getLayoutData();
		errorLabel.setText("");
		errorLabel.setToolTipText(null);
		errorLabel.setVisible(false);
		gd.exclude = true;
		errorLabel.getParent().layout(true, true);
	}

	private void showError(String msg, String details) {
		GridData gd = (GridData) errorLabel.getLayoutData();
		errorLabel.setText(msg);
		errorLabel.setToolTipText(details);
		errorLabel.setVisible(true);
		gd.exclude = false;
		errorLabel.getParent().layout(true, true);
	}

	private void setBusy(boolean busy, String msg) {
		fetchBtn.setEnabled(!busy);
		status.setText(msg != null ? msg : "Ready.");
	}

	@Override
	public void setFocus() {
		if (viewer != null && !viewer.getControl().isDisposed()) {
			viewer.getControl().setFocus();
		}
	}
}
