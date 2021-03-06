/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2015 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.keystore_explorer.gui.actions;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.sf.keystore_explorer.crypto.Password;
import net.sf.keystore_explorer.crypto.keystore.KeyStoreUtil;
import net.sf.keystore_explorer.gui.CurrentDirectory;
import net.sf.keystore_explorer.gui.FileChooserFactory;
import net.sf.keystore_explorer.gui.KseFrame;
import net.sf.keystore_explorer.gui.error.DError;
import net.sf.keystore_explorer.utilities.history.KeyStoreHistory;
import net.sf.keystore_explorer.utilities.history.KeyStoreState;

/**
 * Action to save KeyStore as.
 * 
 */
public class SaveAsAction extends KeyStoreExplorerAction {
	/**
	 * Construct action.
	 * 
	 * @param kseFrame
	 *            KeyStore Explorer frame
	 */
	public SaveAsAction(KseFrame kseFrame) {
		super(kseFrame);

		putValue(
				ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(res.getString("SaveAsAction.accelerator").charAt(0), Toolkit.getDefaultToolkit()
						.getMenuShortcutKeyMask() + InputEvent.ALT_MASK));
		putValue(LONG_DESCRIPTION, res.getString("SaveAsAction.statusbar"));
		putValue(NAME, res.getString("SaveAsAction.text"));
		putValue(SHORT_DESCRIPTION, res.getString("SaveAsAction.tooltip"));
		putValue(
				SMALL_ICON,
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(
						getClass().getResource(res.getString("SaveAsAction.image")))));
	}

	/**
	 * Do action.
	 */
	protected void doAction() {
		saveKeyStoreAs(kseFrame.getActiveKeyStoreHistory());
	}

	/**
	 * Save the supplied opened KeyStore to disk to what may be a different file
	 * from the one it was opened from (if any).
	 * 
	 * @param history
	 *            KeyStore history
	 * @return True if the KeyStore is saved to disk, false otherwise
	 */
	protected boolean saveKeyStoreAs(KeyStoreHistory history) {
		File saveFile = null;

		try {
			KeyStoreState currentState = history.getCurrentState();

			Password password = currentState.getPassword();

			if (password == null) {
				SetPasswordAction setPasswordAction = new SetPasswordAction(kseFrame);

				if (setPasswordAction.setKeyStorePassword()) {
					currentState = history.getCurrentState();
					password = currentState.getPassword();
				} else {
					return false;
				}
			}

			JFileChooser chooser = FileChooserFactory.getKeyStoreFileChooser();
			chooser.setCurrentDirectory(CurrentDirectory.get());
			chooser.setDialogTitle(res.getString("SaveAsAction.SaveKeyStoreAs.Title"));
			chooser.setMultiSelectionEnabled(false);

			int rtnValue = chooser.showSaveDialog(frame);
			if (rtnValue != JFileChooser.APPROVE_OPTION) {
				return false;
			}
			saveFile = chooser.getSelectedFile();
			CurrentDirectory.updateForFile(saveFile);

			if (saveFile.isFile()) {
				String message = MessageFormat.format(res.getString("SaveAsAction.OverWriteFile.message"), saveFile);

				int selected = JOptionPane.showConfirmDialog(frame, message,
						res.getString("SaveAsAction.SaveKeyStoreAs.Title"), JOptionPane.YES_NO_OPTION);
				if (selected != JOptionPane.YES_OPTION) {
					return false;
				}
			}

			if (isKeyStoreFileOpen(saveFile)) {
				JOptionPane.showMessageDialog(frame, res.getString("SaveAsAction.NoSaveKeyStoreAlreadyOpen.message"),
						res.getString("SaveAsAction.SaveKeyStoreAs.Title"), JOptionPane.WARNING_MESSAGE);
				return false;
			}

			KeyStoreUtil.save(currentState.getKeyStore(), saveFile, password);

			currentState.setPassword(password);
			history.setFile(saveFile);
			currentState.setAsSavedState();

			kseFrame.updateControls(false);

			kseFrame.addRecentFile(saveFile);

			return true;
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(frame,
					MessageFormat.format(res.getString("SaveAsAction.NoWriteFile.message"), saveFile),
					res.getString("SaveAsAction.SaveKeyStoreAs.Title"), JOptionPane.WARNING_MESSAGE);
			return false;
		} catch (Exception ex) {
			DError.displayError(frame, ex);
			return false;
		}
	}
}
