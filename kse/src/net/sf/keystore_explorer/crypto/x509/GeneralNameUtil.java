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
package net.sf.keystore_explorer.crypto.x509;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import net.sf.keystore_explorer.utilities.io.HexUtil;
import net.sf.keystore_explorer.utilities.oid.ObjectIdUtil;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.x500.DirectoryString;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.GeneralName;

/**
 * General Name utility methods.
 * 
 */
public class GeneralNameUtil {
	private static ResourceBundle res = ResourceBundle.getBundle("net/sf/keystore_explorer/crypto/x509/resources");

	// @formatter:off

	/*
	 * GeneralName ::= CHOICE { otherName [0] AnotherName, rfc822Name [1]
	 * DERIA5String, dNSName [2] DERIA5String, x400Address [3] ORAddress,
	 * directoryName [4] Name, ediPartyName [5] EDIPartyName,
	 * uniformResourceIdentifier [6] DERIA5String, iPAddress [7] OCTET STRING,
	 * registeredID [8] OBJECT IDENTIFIER }
	 * 
	 * AnotherName ::= ASN1Sequence { type-id OBJECT IDENTIFIER, value [0]
	 * EXPLICIT ANY DEFINED BY type-id }
	 * 
	 * EDIPartyName ::= ASN1Sequence { nameAssigner [0] DirectoryString
	 * OPTIONAL, partyName [1] DirectoryString }
	 * 
	 * DirectoryString ::= CHOICE { teletexString TeletexString (SIZE (1..MAX),
	 * printableString PrintableString (SIZE (1..MAX)), universalString
	 * UniversalString (SIZE (1..MAX)), utf8String UTF8String (SIZE (1.. MAX)),
	 * bmpString BMPString (SIZE(1..MAX)) }
	 */

	// @formatter:on

	/**
	 * Get string representation for General names that cannot cause a
	 * IOException to be thrown. Unsupported are ediPartyName, otherName and
	 * x400Address. Returns a blank string for these.
	 * 
	 * @param generalName
	 *            General name
	 * @return String representation of general name
	 */
	public static String safeToString(GeneralName generalName) {
		switch (generalName.getTagNo()) {
		case GeneralName.directoryName: {
			X500Name directoryName = (X500Name) generalName.getName();

			return MessageFormat.format(res.getString("GeneralNameUtil.DirectoryGeneralName"), directoryName
					.toString());
		}
		case GeneralName.dNSName: {
			DERIA5String dnsName = (DERIA5String) generalName.getName();

			return MessageFormat.format(res.getString("GeneralNameUtil.DnsGeneralName"), dnsName.getString());
		}
		case GeneralName.iPAddress: {
			ASN1OctetString ipAddress = (ASN1OctetString) generalName.getName();

			byte[] ipAddressBytes = ipAddress.getOctets();

			// Output the IP Address components one at a time separated by dots
			StringBuffer sbIpAddress = new StringBuffer();

			for (int i = 0; i < ipAddressBytes.length; i++) {
				byte b = ipAddressBytes[i];

				// Convert from (possibly negative) byte to positive int
				sbIpAddress.append(b & 0xFF);

				if ((i + 1) < ipAddressBytes.length) {
					sbIpAddress.append('.');
				}
			}

			return MessageFormat.format(res.getString("GeneralNameUtil.IpAddressGeneralName"), sbIpAddress.toString());
		}
		case GeneralName.registeredID: {
			ASN1ObjectIdentifier registeredId = (ASN1ObjectIdentifier) generalName.getName();

			return MessageFormat.format(res.getString("GeneralNameUtil.RegisteredIdGeneralName"),
					ObjectIdUtil.toString(registeredId));
		}
		case GeneralName.rfc822Name: {
			DERIA5String rfc822Name = (DERIA5String) generalName.getName();

			return MessageFormat.format(res.getString("GeneralNameUtil.Rfc822GeneralName"), rfc822Name.getString());
		}
		case GeneralName.uniformResourceIdentifier: {
			DERIA5String uri = (DERIA5String) generalName.getName();

			return MessageFormat.format(res.getString("GeneralNameUtil.UriGeneralName"), uri.getString());
		}
		default: {
			return "";
		}
		}
	}

	/**
	 * Get string representation for all General Names.
	 * 
	 * @param generalName
	 *            General name
	 * @return String representation of general name
	 * @throws IOException
	 *             If general name is invalid
	 */
	public static String toString(GeneralName generalName) throws IOException {
		switch (generalName.getTagNo()) {
		case GeneralName.ediPartyName: {

			/* EDIPartyName ::= SEQUENCE {
			 *      nameAssigner            [0]     DirectoryString OPTIONAL,
			 *      partyName               [1]     DirectoryString }
			 */      
			ASN1Sequence ediPartyName = (ASN1Sequence) generalName.getName();

			DirectoryString nameAssigner = DirectoryString.getInstance(ediPartyName.getObjectAt(0));
			DirectoryString partyName =  DirectoryString.getInstance(ediPartyName.getObjectAt(1));

			String nameAssignerStr = null;
			if (nameAssigner != null) { // Optional
				nameAssignerStr = nameAssigner.getString();
			}

			String partyNameStr = partyName.getString();
			if (nameAssignerStr != null) {
				return MessageFormat.format(res.getString("GeneralNameUtil.EdiPartyGeneralName"), nameAssignerStr,
						partyNameStr);
			} else {
				return MessageFormat.format(res.getString("GeneralNameUtil.EdiPartyGeneralNameNoAssigner"),
						partyNameStr);
			}
		}
		case GeneralName.otherName: {
			
			/* OtherName ::= SEQUENCE {
			 *      type-id    OBJECT IDENTIFIER,
			 *      value      [0] EXPLICIT ANY DEFINED BY type-id }
			 */      
			ASN1Sequence otherName = (ASN1Sequence) generalName.getName();
			
			ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) otherName.getObjectAt(0);
			ASN1Encodable value = otherName.getObjectAt(1);

			return MessageFormat.format(res.getString("GeneralNameUtil.OtherGeneralName"),
					ObjectIdUtil.toString(oid),
					HexUtil.getHexString(value.toASN1Primitive().getEncoded(ASN1Encoding.DER)));
		}
		case GeneralName.x400Address: {
			/*
			 * No support for this at the moment - just get a hex dump 
			 * The Oracle CertificateFactory blows up if a certificate extension contains this anyway
			 */
			ASN1Encodable x400Address = generalName.getName();

			return MessageFormat.format(res.getString("GeneralNameUtil.X400AddressGeneralName"),
					HexUtil.getHexString(x400Address.toASN1Primitive().getEncoded(ASN1Encoding.DER)));
		}
		default: {
			return safeToString(generalName);
		}
		}
	}
}
