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
package net.sf.keystore_explorer.gui.dnd;

import java.awt.Toolkit;
import java.security.cert.Certificate;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import net.sf.keystore_explorer.crypto.CryptoException;
import net.sf.keystore_explorer.crypto.x509.X509CertUtil;

/**
 * Encapsulates a draggable trusted certificate entry.
 * 
 * Product of drag is a PEM'd X.509 encoding of the trusted certificate.
 * 
 */
public class DragTrustedCertificateEntry extends DragEntry {
	private static ResourceBundle res = ResourceBundle.getBundle("net/sf/keystore_explorer/gui/dnd/resources");

	private static final String EXTENSION = "cer";

	private byte[] contentBytes;
	private String contentStr;
	private ImageIcon image;

	/**
	 * Construct DragTrustedCertificateEntry.
	 * 
	 * @param name
	 *            Entry name
	 * @param trustedCertificate
	 *            Trusted certificate
	 * @throws CryptoException
	 *             If there was a problem creating the content
	 */
	public DragTrustedCertificateEntry(String name, Certificate trustedCertificate) throws CryptoException {
		super(name);

		// String content is X.509 PEM
		contentStr = X509CertUtil.getCertEncodedX509Pem(X509CertUtil.convertCertificate(trustedCertificate));

		// Binary content is bytes of same
		contentBytes = contentStr.getBytes();

		// Get drag image
		image = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
				getClass().getResource(res.getString("DragTrustedCertificateEntry.Drag.image"))));
	}

	/**
	 * Get entry image - to display while dragging.
	 * 
	 * @return Entry image
	 */
	public ImageIcon getImage() {
		return image;
	}

	/**
	 * Get entry file extension. Used to generate dragged file name.
	 * 
	 * @return File extension
	 */
	public String getExtension() {
		return EXTENSION;
	}

	/**
	 * Get entry content as binary.
	 * 
	 * @return Content
	 */
	public byte[] getContent() {
		return contentBytes;
	}

	/**
	 * Get entry content as a string.
	 * 
	 * @return Content
	 */
	public String getContentString() {
		return contentStr;
	}
}
