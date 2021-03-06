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
import static net.sf.keystore_explorer.crypto.Password.getDummyPassword;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.security.KeyStore;
import java.text.MessageFormat;

import javax.crypto.SecretKey;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.sf.keystore_explorer.crypto.Password;
import net.sf.keystore_explorer.crypto.keystore.KeyStoreType;
import net.sf.keystore_explorer.crypto.secretkey.SecretKeyType;
import net.sf.keystore_explorer.crypto.secretkey.SecretKeyUtil;
import net.sf.keystore_explorer.gui.KseFrame;
import net.sf.keystore_explorer.gui.dialogs.DGenerateSecretKey;
import net.sf.keystore_explorer.gui.dialogs.DGetAlias;
import net.sf.keystore_explorer.gui.error.DError;
import net.sf.keystore_explorer.gui.password.DGetNewPassword;
import net.sf.keystore_explorer.utilities.history.HistoryAction;
import net.sf.keystore_explorer.utilities.history.KeyStoreHistory;
import net.sf.keystore_explorer.utilities.history.KeyStoreState;

/**
 * Action to generate a secret key.
 * 
 */
public class GenerateSecretKeyAction extends KeyStoreExplorerAction implements HistoryAction {
	/**
	 * Construct action.
	 * 
	 * @param kseFrame
	 *            KeyStore Explorer frame
	 */
	public GenerateSecretKeyAction(KseFrame kseFrame) {
		super(kseFrame);

		putValue(
				ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(res.getString("GenerateSecretKeyAction.accelerator").charAt(0), Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask() + InputEvent.ALT_MASK));
		putValue(LONG_DESCRIPTION, res.getString("GenerateSecretKeyAction.statusbar"));
		putValue(NAME, res.getString("GenerateSecretKeyAction.text"));
		putValue(SHORT_DESCRIPTION, res.getString("GenerateSecretKeyAction.tooltip"));
		putValue(
				SMALL_ICON,
				new ImageIcon(Toolkit.getDefaultToolkit().createImage(
						getClass().getResource(res.getString("GenerateSecretKeyAction.image")))));
	}

	public String getHistoryDescription() {
		return (String) getValue(NAME);
	}

	/**
	 * Do action.
	 */
	protected void doAction() {
		generateSecret();
	}

	/**
	 * Generate a secret key in the currently opened KeyStore.
	 */
	public void generateSecret() {
		try {
			int secretKeySize = applicationSettings.getGenerateSecretKeySize();
			SecretKeyType secretKeyType = applicationSettings.getGenerateSecretKeyType();

			DGenerateSecretKey dGenerateSecretKey = new DGenerateSecretKey(frame, secretKeyType, secretKeySize);
			dGenerateSecretKey.setLocationRelativeTo(frame);
			dGenerateSecretKey.setVisible(true);

			if (!dGenerateSecretKey.isSuccessful()) {
				return;
			}

			secretKeySize = dGenerateSecretKey.getSecretKeySize();
			secretKeyType = dGenerateSecretKey.getSecretKeyType();

			applicationSettings.setGenerateSecretKeySize(secretKeySize);
			applicationSettings.setGenerateSecretKeyType(secretKeyType);

			SecretKey secretKey = SecretKeyUtil.generateSecretKey(secretKeyType, secretKeySize);

			KeyStoreHistory history = kseFrame.getActiveKeyStoreHistory();

			KeyStoreState currentState = history.getCurrentState();
			KeyStoreState newState = currentState.createBasisForNextState(this);

			KeyStore keyStore = newState.getKeyStore();

			DGetAlias dGetAlias = new DGetAlias(frame,
					res.getString("GenerateSecretKeyAction.NewSecretKeyEntryAlias.Title"), null);
			dGetAlias.setLocationRelativeTo(frame);
			dGetAlias.setVisible(true);
			String alias = dGetAlias.getAlias();

			if (alias == null) {
				return;
			}

			if (keyStore.containsAlias(alias)) {
				String message = MessageFormat.format(res.getString("GenerateSecretKeyAction.OverWriteEntry.message"),
						alias);

				int selected = JOptionPane.showConfirmDialog(frame, message,
						res.getString("GenerateSecretKeyAction.NewSecretKeyEntryAlias.Title"),
						JOptionPane.YES_NO_OPTION);
				if (selected != JOptionPane.YES_OPTION) {
					return;
				}
			}

			Password password = getDummyPassword();

			if (!keyStore.getType().equals(KeyStoreType.PKCS12.jce())) {
				DGetNewPassword dGetNewPassword = new DGetNewPassword(frame,
						res.getString("GenerateSecretKeyAction.NewSecretKeyEntryPassword.Title"), DOCUMENT_MODAL,
						applicationSettings.getPasswordQualityConfig());
				dGetNewPassword.setLocationRelativeTo(frame);
				dGetNewPassword.setVisible(true);
				password = dGetNewPassword.getPassword();

				if (password == null) {
					return;
				}
			}

			if (keyStore.containsAlias(alias)) {
				keyStore.deleteEntry(alias);
				newState.removeEntryPassword(alias);
			}

			keyStore.setKeyEntry(alias, secretKey, password.toCharArray(), null);
			newState.setEntryPassword(alias, password);

			currentState.append(newState);

			kseFrame.updateControls(true);

			JOptionPane.showMessageDialog(frame,
					res.getString("GenerateSecretKeyAction.SecretKeyGenerationSuccessful.message"),
					res.getString("GenerateSecretKeyAction.GenerateSecretKey.Title"), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			DError.displayError(frame, ex);
		}
	}
}
