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
package net.sf.keystore_explorer.gui.dialogs.extensions;

import static java.awt.Dialog.ModalityType.DOCUMENT_MODAL;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.sf.keystore_explorer.crypto.x509.SubjectInfoAccess;
import net.sf.keystore_explorer.gui.PlatformUtil;
import net.sf.keystore_explorer.gui.crypto.accessdescription.JAccessDescriptions;
import net.sf.keystore_explorer.gui.error.DError;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.x509.AccessDescription;

/**
 * Dialog used to add or edit an Subject Information Access extension.
 * 
 */
public class DSubjectInformationAccess extends DExtension {
	private static ResourceBundle res = ResourceBundle
			.getBundle("net/sf/keystore_explorer/gui/dialogs/extensions/resources");

	private static final String CANCEL_KEY = "CANCEL_KEY";

	private JPanel jpAccessDescriptions;
	private JLabel jlAccessDescriptions;
	private JAccessDescriptions jadAccessDescriptions;
	private JPanel jpButtons;
	private JButton jbOK;
	private JButton jbCancel;

	private byte[] value;

	/**
	 * Creates a new DSubjectInformationAccess dialog.
	 * 
	 * @param parent
	 *            The parent dialog
	 */
	public DSubjectInformationAccess(JDialog parent) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		setTitle(res.getString("DSubjectInformationAccess.Title"));
		initComponents();
	}

	/**
	 * Creates a new DSubjectInformationAccess dialog.
	 * 
	 * @param parent
	 *            The parent dialog
	 * @param value
	 *            Subject Information Access DER-encoded
	 * @throws IOException
	 *             If value could not be decoded
	 */
	public DSubjectInformationAccess(JDialog parent, byte[] value) throws IOException {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		setTitle(res.getString("DSubjectInformationAccess.Title"));
		initComponents();
		prepopulateWithValue(value);
	}

	private void initComponents() {
		jlAccessDescriptions = new JLabel(res.getString("DSubjectInformationAccess.jlAccessDescriptions.text"));

		GridBagConstraints gbc_jlAccessDescriptions = new GridBagConstraints();
		gbc_jlAccessDescriptions.gridx = 0;
		gbc_jlAccessDescriptions.gridy = 0;
		gbc_jlAccessDescriptions.gridwidth = 1;
		gbc_jlAccessDescriptions.gridheight = 1;
		gbc_jlAccessDescriptions.insets = new Insets(5, 5, 5, 5);
		gbc_jlAccessDescriptions.anchor = GridBagConstraints.NORTHEAST;

		jadAccessDescriptions = new JAccessDescriptions(
				res.getString("DSubjectInformationAccess.AccessDescription.Title"));

		GridBagConstraints gbc_jadAccessDescriptions = new GridBagConstraints();
		gbc_jadAccessDescriptions.gridx = 1;
		gbc_jadAccessDescriptions.gridy = 0;
		gbc_jadAccessDescriptions.gridwidth = 1;
		gbc_jadAccessDescriptions.gridheight = 1;
		gbc_jadAccessDescriptions.insets = new Insets(5, 5, 5, 5);
		gbc_jadAccessDescriptions.anchor = GridBagConstraints.WEST;

		jpAccessDescriptions = new JPanel(new GridBagLayout());

		jpAccessDescriptions.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new CompoundBorder(
				new EtchedBorder(), new EmptyBorder(5, 5, 5, 5))));

		jpAccessDescriptions.add(jlAccessDescriptions, gbc_jlAccessDescriptions);
		jpAccessDescriptions.add(jadAccessDescriptions, gbc_jadAccessDescriptions);

		jbOK = new JButton(res.getString("DSubjectInformationAccess.jbOK.text"));
		jbOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				okPressed();
			}
		});

		jbCancel = new JButton(res.getString("DSubjectInformationAccess.jbCancel.text"));
		jbCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cancelPressed();
			}
		});
		jbCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				CANCEL_KEY);
		jbCancel.getActionMap().put(CANCEL_KEY, new AbstractAction() {
			public void actionPerformed(ActionEvent evt) {
				cancelPressed();
			}
		});

		jpButtons = PlatformUtil.createDialogButtonPanel(jbOK, jbCancel, false);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpAccessDescriptions, BorderLayout.CENTER);
		getContentPane().add(jpButtons, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				closeDialog();
			}
		});

		setResizable(false);

		getRootPane().setDefaultButton(jbOK);

		pack();
	}

	@SuppressWarnings("unchecked")
	private void prepopulateWithValue(byte[] value) throws IOException {
		SubjectInfoAccess subjectInformationAccess = SubjectInfoAccess.getInstance(value);

		jadAccessDescriptions.setAccessDescriptions((List<AccessDescription>) subjectInformationAccess
				.getAccessDescriptionList());
	}

	private void okPressed() {
		List<AccessDescription> accessDescriptions = jadAccessDescriptions.getAccessDescriptions();

		if (accessDescriptions.size() == 0) {
			JOptionPane.showMessageDialog(this, res.getString("DSubjectInformationAccess.ValueReq.message"),
					getTitle(), JOptionPane.WARNING_MESSAGE);
			return;
		}

		SubjectInfoAccess subjectInformationAccess = new SubjectInfoAccess(accessDescriptions);

		try {
			value = subjectInformationAccess.getEncoded(ASN1Encoding.DER);
		} catch (IOException ex) {
			DError dError = new DError(this, DOCUMENT_MODAL, ex);
			dError.setLocationRelativeTo(this);
			dError.setVisible(true);
			return;
		}

		closeDialog();
	}

	/**
	 * Get extension value DER-encoded.
	 * 
	 * @return Extension value
	 */
	public byte[] getValue() {
		return value;
	}

	private void cancelPressed() {
		closeDialog();
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}
}
