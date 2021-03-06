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

import static java.awt.Dialog.ModalityType.DOCUMENT_MODAL;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.sf.keystore_explorer.crypto.Password;
import net.sf.keystore_explorer.crypto.keystore.KeyStoreLoadException;
import net.sf.keystore_explorer.crypto.keystore.KeyStoreUtil;
import net.sf.keystore_explorer.gui.CurrentDirectory;
import net.sf.keystore_explorer.gui.FileChooserFactory;
import net.sf.keystore_explorer.gui.KseFrame;
import net.sf.keystore_explorer.gui.error.DError;
import net.sf.keystore_explorer.gui.error.DProblem;
import net.sf.keystore_explorer.gui.error.Problem;
import net.sf.keystore_explorer.gui.password.DGetPassword;

/**
 * Action to open a KeyStore.
 * 
 */
public class OpenAction extends KeyStoreExplorerAction {
	/**
	 * Construct action.
	 * 
	 * @param kseFrame
	 *            KeyStore Explorer frame
	 */
	public OpenAction(KseFrame kseFrame) {
		super(kseFrame);

		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(res.getString("OpenAction.accelerator").charAt(0), Toolkit
				.getDefaultToolkit().getMenuShortcutKeyMask()));
		putValue(LONG_DESCRIPTION, res.getString("OpenAction.statusbar"));
		putValue(NAME, res.getString("OpenAction.text"));
		putValue(SHORT_DESCRIPTION, res.getString("OpenAction.tooltip"));
		putValue(
				SMALL_ICON,
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(
						getClass().getResource(res.getString("OpenAction.image")))));
	}

	/**
	 * Do action.
	 */
	protected void doAction() {
		JFileChooser chooser = FileChooserFactory.getKeyStoreFileChooser();
		chooser.setCurrentDirectory(CurrentDirectory.get());
		chooser.setDialogTitle(res.getString("OpenAction.OpenKeyStore.Title"));
		chooser.setMultiSelectionEnabled(false);

		int rtnValue = chooser.showOpenDialog(frame);
		if (rtnValue == JFileChooser.APPROVE_OPTION) {
			File openFile = chooser.getSelectedFile();
			CurrentDirectory.updateForFile(openFile);

			openKeyStore(openFile);
		}
	}

	/**
	 * Open the supplied KeyStore file from disk.
	 * 
	 * @param keyStoreFile
	 *            The KeyStore file
	 */
	public void openKeyStore(File keyStoreFile) {
		try {
			if (!keyStoreFile.isFile()) {
				JOptionPane.showMessageDialog(frame,
						MessageFormat.format(res.getString("OpenAction.NotFile.message"), keyStoreFile),
						res.getString("OpenAction.OpenKeyStore.Title"), JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (isKeyStoreFileOpen(keyStoreFile)) {
				JOptionPane.showMessageDialog(frame, MessageFormat.format(
						res.getString("OpenAction.NoOpenKeyStoreAlreadyOpen.message"), keyStoreFile), res
						.getString("OpenAction.OpenKeyStore.Title"), JOptionPane.WARNING_MESSAGE);
				return;
			}

			DGetPassword dGetPassword = new DGetPassword(frame, MessageFormat.format(
					res.getString("OpenAction.UnlockKeyStore.Title"), keyStoreFile.getName()), DOCUMENT_MODAL);
			dGetPassword.setLocationRelativeTo(frame);
			dGetPassword.setVisible(true);
			Password password = dGetPassword.getPassword();

			if (password == null) {
				return;
			}

			KeyStore openedKeyStore = null;

			try {
				openedKeyStore = KeyStoreUtil.load(keyStoreFile, password);
			} catch (KeyStoreLoadException klex) {
				String problemStr = MessageFormat.format(res.getString("OpenAction.NoOpenKeyStore.Problem"), klex
						.getKeyStoreType().friendly(), keyStoreFile.getName());

				String[] causes = new String[] { res.getString("OpenAction.PasswordIncorrectKeyStore.Cause"),
						res.getString("OpenAction.CorruptedKeyStore.Cause") };

				Problem problem = new Problem(problemStr, causes, klex);

				DProblem dProblem = new DProblem(frame, res.getString("OpenAction.ProblemOpeningKeyStore.Title"),
						DOCUMENT_MODAL, problem);
				dProblem.setLocationRelativeTo(frame);
				dProblem.setVisible(true);

				return;
			}

			if (openedKeyStore == null) {
				JOptionPane.showMessageDialog(
						frame,
						MessageFormat.format(res.getString("OpenAction.FileNotRecognisedType.message"),
								keyStoreFile.getName()), res.getString("OpenAction.OpenKeyStore.Title"),
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			kseFrame.addKeyStore(openedKeyStore, keyStoreFile, password);
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(frame,
					MessageFormat.format(res.getString("OpenAction.NoReadFile.message"), keyStoreFile),
					res.getString("OpenAction.OpenKeyStore.Title"), JOptionPane.WARNING_MESSAGE);
		} catch (Exception ex) {
			DError.displayError(frame, ex);
		}
	}
}
