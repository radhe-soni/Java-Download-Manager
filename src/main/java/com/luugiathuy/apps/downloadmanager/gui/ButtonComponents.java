package com.luugiathuy.apps.downloadmanager.gui;

import java.awt.event.ActionListener;

import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import org.springframework.stereotype.Component;

import com.luugiathuy.apps.downloader.Downloader;
import com.luugiathuy.apps.downloadmanager.DOWNLOAD_STATUS;

@Component
public class ButtonComponents {
	// Variables declaration - do not modify//GEN-BEGIN:variables

	javax.swing.JButton jbnAdd;
	javax.swing.JButton jbnAddInfoFile;
	javax.swing.JButton jbnStart;
	javax.swing.JButton jbnCancel;
	javax.swing.JButton jbnExit;
	javax.swing.JButton jbnPause;
	javax.swing.JButton jbnRemove;
	javax.swing.JButton jbnResume;

	// End of variables declaration//GEN-END:variables

	public void initResumeButton(ActionListener jbnResumeActionPerformed) {
		jbnResume = new javax.swing.JButton();
		jbnResume.setText("Resume");
		jbnResume.setEnabled(false);
		jbnResume.addActionListener(jbnResumeActionPerformed);
	}

	public void initExitButton(ActionListener jbnExitActionPerformed) {
		jbnExit = new javax.swing.JButton();
		jbnExit.setText("Exit");
		jbnExit.addActionListener(jbnExitActionPerformed);
	}

	public void initCancelButton(ActionListener jbnCancelActionPerformed) {
		jbnCancel = new javax.swing.JButton();
		jbnCancel.setText("Cancel");
		jbnCancel.setEnabled(false);
		jbnCancel.addActionListener(jbnCancelActionPerformed);
	}

	public void initRemoveButton(ActionListener jbnRemoveActionPerformed) {
		jbnRemove = new javax.swing.JButton();
		jbnRemove.setText("Remove");
		jbnRemove.setEnabled(false);
		jbnRemove.addActionListener(jbnRemoveActionPerformed);
	}

	public void initPauseButton(ActionListener jbnPauseActionPerformed) {
		jbnPause = new javax.swing.JButton();
		jbnPause.setText("Pause");
		jbnPause.setEnabled(false);
		jbnPause.addActionListener(jbnPauseActionPerformed);
	}

	public void initStartButton(ActionListener jbnStartActionPerformed) {
		jbnStart = new javax.swing.JButton();
		jbnStart.setText("Start");
		jbnStart.setEnabled(false);
		jbnStart.addActionListener(jbnStartActionPerformed);
	}

	public void initAddButton(ActionListener jbnAddActionPerformed) {
		jbnAdd = new javax.swing.JButton();
		jbnAdd.setText("Add Download");
		jbnAdd.addActionListener(jbnAddActionPerformed);
	}
	
	public void initAddInfoFileButton(ActionListener jbnAddActionPerformed) {
		jbnAddInfoFile = new javax.swing.JButton();
		jbnAddInfoFile.setText("Process");
		jbnAddInfoFile.addActionListener(jbnAddActionPerformed);
	}

	public void updateButtons(Downloader mSelectedDownloader) {
		if (mSelectedDownloader != null) {
			DOWNLOAD_STATUS state = mSelectedDownloader.getState();
			switch (state) {
			case READY:
				jbnStart.setEnabled(true);
				jbnPause.setEnabled(false);
				jbnResume.setEnabled(false);
				jbnCancel.setEnabled(false);
				jbnRemove.setEnabled(true);
				break;
			case DOWNLOADING:
				jbnStart.setEnabled(false);
				jbnPause.setEnabled(true);
				jbnResume.setEnabled(false);
				jbnCancel.setEnabled(true);
				jbnRemove.setEnabled(false);
				break;
			case PAUSED:
				jbnStart.setEnabled(false);
				jbnPause.setEnabled(false);
				jbnResume.setEnabled(true);
				jbnCancel.setEnabled(true);
				jbnRemove.setEnabled(false);
				break;
			case ERROR:
				jbnStart.setEnabled(false);
				jbnPause.setEnabled(false);
				jbnResume.setEnabled(true);
				jbnCancel.setEnabled(false);
				jbnRemove.setEnabled(true);
				break;
			default: // COMPLETE or CANCELLED
				jbnStart.setEnabled(true);
				jbnPause.setEnabled(false);
				jbnResume.setEnabled(false);
				jbnCancel.setEnabled(false);
				jbnRemove.setEnabled(true);
			}
		} else {
			jbnStart.setEnabled(false);
			jbnPause.setEnabled(false);
			jbnResume.setEnabled(false);
			jbnCancel.setEnabled(false);
			jbnRemove.setEnabled(false);
		}
	}

	public void linkSize(javax.swing.GroupLayout layout) {
		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, jbnStart, jbnCancel, jbnExit, jbnPause, jbnRemove, jbnResume);
	}
	
	public SequentialGroup createBottomButtonPanel(javax.swing.GroupLayout layout) {
		return layout.createSequentialGroup()
				.addComponent(jbnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 104,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18)
				.addComponent(jbnPause, javax.swing.GroupLayout.PREFERRED_SIZE, 104,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18)
				.addComponent(jbnResume, javax.swing.GroupLayout.PREFERRED_SIZE, 109,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18)
				.addComponent(jbnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 104,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18)
				.addComponent(jbnRemove, javax.swing.GroupLayout.PREFERRED_SIZE, 104,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE).addComponent(
						jbnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE);
	}

	public ParallelGroup createBottomButtonPanelVertical(javax.swing.GroupLayout layout) {
		return layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbnStart).addComponent(jbnPause)
				.addComponent(jbnResume).addComponent(jbnCancel).addComponent(jbnRemove).addComponent(jbnExit);
	}
}
