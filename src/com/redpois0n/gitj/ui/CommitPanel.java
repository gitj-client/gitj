package com.redpois0n.gitj.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.redpois0n.git.Commit;
import com.redpois0n.git.Diff;
import com.redpois0n.git.Repository;
import com.redpois0n.gitj.Main;
import com.redpois0n.gitj.ui.components.DiffHolderPanel;
import com.redpois0n.gitj.ui.components.DiffPanel;
import com.redpois0n.gitj.ui.components.IDiffSelectionListener;

@SuppressWarnings("serial")
public class CommitPanel extends AbstractPanel {

	private MainFrame parent;
	private PanelUncommited panelList;
	private CommitButtonPanel buttonPanel;
	private MainPanel parentPanel;
	private DiffHolderPanel diffHolderPanel;

	public CommitPanel(MainFrame parent, MainPanel parentPanel, Repository repo) {
		super(repo);
		this.parent = parent;
		this.parentPanel = parentPanel;
		setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.75);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

		add(splitPane, BorderLayout.CENTER);

		JSplitPane topSplitPane = new JSplitPane();
		topSplitPane.setResizeWeight(0.5);
		panelList = new PanelUncommited(this, repo);
		panelList.addListener(new DiffSelectionListener());
		reload();

		diffHolderPanel = new DiffHolderPanel();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(diffHolderPanel);
		topSplitPane.setRightComponent(scrollPane);

		topSplitPane.setLeftComponent(panelList);

		splitPane.setLeftComponent(topSplitPane);

		buttonPanel = new CommitButtonPanel(this);
		splitPane.setRightComponent(buttonPanel);
	}

	public PanelUncommited getListPanel() {
		return panelList;
	}

	/**
	 * Closes current tab
	 */
	public void cancel() {
		parent.removePanel(this);
		reload();
	}

	@Override
	public void reload() {
		try {
			panelList.reload();
			parentPanel.reload();
		} catch (Exception e) {
			e.printStackTrace();
			Main.displayError(e);
		}
	}

	public void loadDiffs(List<Diff> diffs) {
		diffHolderPanel.clear();

		for (Diff diff : diffs) {
			DiffPanel diffPanel = new DiffPanel(diff);
			diffHolderPanel.addDiffPanel(diffPanel);
		}

		diffHolderPanel.revalidate();
		
		new Thread() {
			public void run() {
				try { Thread.sleep(1000L); } catch (Exception ex) { }
				
				diffHolderPanel.repaint();
				diffHolderPanel.revalidate();
			}
		}.start();
	}
	
	public class DiffSelectionListener implements IDiffSelectionListener {
		@Override
		public void onSelect(Commit c, List<Diff> d, List<Diff> all) {
			try {
				loadDiffs(d);
			} catch (Exception ex) {
				ex.printStackTrace();
				Main.displayError(ex);
			}
		}
	}

}