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
package net.sf.keystore_explorer.gui.dialogs;

import static java.awt.Dialog.ModalityType.DOCUMENT_MODAL;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import net.sf.keystore_explorer.crypto.CryptoException;
import net.sf.keystore_explorer.gui.CursorUtil;
import net.sf.keystore_explorer.gui.JEscDialog;
import net.sf.keystore_explorer.gui.PlatformUtil;
import net.sf.keystore_explorer.gui.error.DError;

/**
 * Dialog that displays the details of a key pair.
 * 
 */
public class DViewKeyPair extends JEscDialog {
	private static ResourceBundle res = ResourceBundle.getBundle("net/sf/keystore_explorer/gui/dialogs/resources");

	private JPanel jpKeyPairDetails;
	private JButton jbCertificateDetails;
	private JButton jbPrivateKeyDetails;
	private JPanel jpOK;
	private JButton jbOK;

	private PrivateKey privateKey;
	private X509Certificate[] certificateChain;

	private Provider provider;

	/**
	 * Creates a new DViewKeyPair dialog where the parent is a frame.
	 * 
	 * @param parent
	 *            The parent frame
	 * @param title
	 *            The dialog title
	 * @param modality
	 *            Dialog modality
	 * @param privateKey
	 *            Private Private key part of keypair
	 * @param certificateChain
	 *            Certificates Certificates part of keypair
	 */
	public DViewKeyPair(JFrame parent, String title, Dialog.ModalityType modality, PrivateKey privateKey,
			X509Certificate[] certificateChain, Provider provider) {
		super(parent, title, modality);
		this.privateKey = privateKey;
		this.certificateChain = certificateChain;
		this.provider = provider;
		initComponents();
	}

	/**
	 * Creates a new DViewKeyPair dialog where the parent is a dialog.
	 * 
	 * @param parent
	 *            The parent dualog
	 * @param title
	 *            The dialog title
	 * @param modality
	 *            Dialog modality
	 * @param privateKey
	 *            Private Private key part of keypair
	 * @param certificateChain
	 *            Certificates Certificates part of keypair
	 */
	public DViewKeyPair(JDialog parent, String title, Dialog.ModalityType modality, PrivateKey privateKey,
			X509Certificate[] certificateChain, Provider provider) {
		super(parent, title, modality);
		this.privateKey = privateKey;
		this.certificateChain = certificateChain;
		this.provider = provider;
		initComponents();
	}

	private void initComponents() {
		jbPrivateKeyDetails = new JButton(res.getString("DViewKeyPair.jbPrivateKeyDetails.text"));
		PlatformUtil.setMnemonic(jbPrivateKeyDetails, res.getString("DViewKeyPair.jbPrivateKeyDetails.mnemonic")
				.charAt(0));
		jbPrivateKeyDetails.setToolTipText(res.getString("DViewKeyPair.jbPrivateKeyDetails.tooltip"));
		jbPrivateKeyDetails.setEnabled(true);
		jbPrivateKeyDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					CursorUtil.setCursorBusy(DViewKeyPair.this);
					privateKeyDetailsPressed();
				} finally {
					CursorUtil.setCursorFree(DViewKeyPair.this);
				}
			}
		});

		jbCertificateDetails = new JButton(res.getString("DViewKeyPair.jbCertificateDetails.text"));
		PlatformUtil.setMnemonic(jbCertificateDetails, res.getString("DViewKeyPair.jbCertificateDetails.mnemonic")
				.charAt(0));
		jbCertificateDetails.setToolTipText(res.getString("DViewKeyPair.jbCertificateDetails.tooltip"));
		jbCertificateDetails.setEnabled(true);
		jbCertificateDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					CursorUtil.setCursorBusy(DViewKeyPair.this);
					certificateDetailsPressed();
				} finally {
					CursorUtil.setCursorFree(DViewKeyPair.this);
				}
			}
		});

		jpKeyPairDetails = new JPanel(new FlowLayout(FlowLayout.LEFT));
		jpKeyPairDetails.setBorder(new CompoundBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5),
				new EtchedBorder()), new EmptyBorder(5, 5, 5, 5)));
		jpKeyPairDetails.add(jbPrivateKeyDetails);
		jpKeyPairDetails.add(jbCertificateDetails);

		jbOK = new JButton(res.getString("DViewCertificate.jbOK.text"));
		jbOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				okPressed();
			}
		});

		jpOK = PlatformUtil.createDialogButtonPanel(jbOK, false);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpKeyPairDetails, BorderLayout.CENTER);
		getContentPane().add(jpOK, BorderLayout.SOUTH);

		setResizable(false);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				closeDialog();
			}
		});

		getRootPane().setDefaultButton(jbOK);

		pack();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				jbOK.requestFocus();
			}
		});
	}

	private void privateKeyDetailsPressed() {
		try {
			DViewPrivateKey dViewPrivateKey = new DViewPrivateKey(this,
					res.getString("DViewKeyPair.ViewPrivateKeyDetails.Title"), DOCUMENT_MODAL, privateKey, provider);
			dViewPrivateKey.setLocationRelativeTo(this);
			dViewPrivateKey.setVisible(true);
		} catch (CryptoException ex) {
			DError.displayError(this, ex);
		}
	}

	private void certificateDetailsPressed() {
		try {
			DViewCertificate dViewCertificate = new DViewCertificate(this,
					res.getString("DViewKeyPair.ViewCertificateDetails.Title"), DOCUMENT_MODAL, certificateChain,
					null, DViewCertificate.NONE);
			dViewCertificate.setLocationRelativeTo(this);
			dViewCertificate.setVisible(true);
		} catch (CryptoException ex) {
			DError.displayError(this, ex);
		}
	}

	private void okPressed() {
		closeDialog();
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}
}
