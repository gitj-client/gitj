package com.redpois0n.gitj.ui.dialogs;

import com.redpois0n.git.Repository;
import com.redpois0n.gitj.tasks.PushTask;
import com.redpois0n.gitj.ui.MainFrame;

@SuppressWarnings("serial")
public class DialogPush extends DialogRemoteAction {

	public DialogPush(MainFrame parent, Repository repo) throws Exception {
		super(parent, repo);
		super.setTitle("Push");
	}

	@Override
	public void ok() {
		parent.runTask(new PushTask(repo, branchComboBox.getSelectedBranch(), remoteComboBox.getSelectedRemote()));
	}
}