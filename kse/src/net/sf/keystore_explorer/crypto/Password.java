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
package net.sf.keystore_explorer.crypto;

import java.util.ResourceBundle;

/**
 * Wraps a character array based password providing the ability to null the
 * password to remove it from memory for security.
 * 
 */
public class Password {
	private static ResourceBundle res = ResourceBundle.getBundle("net/sf/keystore_explorer/crypto/resources");
	private char[] wrappedPassword;
	private boolean nulled;

	/**
	 * Dummy password to use for PKCS #12 KeyStore entries (passwords are not
	 * applicable for these)
	 */
	private static final Password DUMMY_PASSWORD = new Password(new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r',
			'd' });

	/**
	 * Construct password wrapper.
	 * 
	 * @param password
	 *            Password to wrap
	 */
	public Password(char[] password) {
		this.wrappedPassword = password;
		nulled = false; // Initially not nulled
	}

	/**
	 * Copy construct password wrapper.
	 * 
	 * @param password
	 *            Password wrapper to copy
	 */
	public Password(Password password) {
		char[] wrappedPassword = password.toCharArray();
		this.wrappedPassword = new char[wrappedPassword.length];
		System.arraycopy(wrappedPassword, 0, this.wrappedPassword, 0, this.wrappedPassword.length);

		nulled = password.isNulled();
	}

	/**
	 * Get wrapped password as a char array.
	 * 
	 * @return Wrapped password
	 * @throws IllegalStateException
	 *             If pasword requested after it has been nulled
	 */
	public char[] toCharArray() throws IllegalStateException {
		if (nulled) {
			throw new IllegalStateException(res.getString("NoGetPasswordNulled.message"));
		}

		return wrappedPassword;
	}

	/**
	 * Get wrapped password as a byte array.
	 * 
	 * @return Wrapped password
	 * @throws IllegalStateException
	 *             If pasword requested after it has been nulled
	 */
	public byte[] toByteArray() throws IllegalStateException {
		if (nulled) {
			throw new IllegalStateException(res.getString("NoGetPasswordNulled.message"));
		}

		byte[] passwordBytes = new byte[wrappedPassword.length];

		for (int i = 0; i < wrappedPassword.length; i++) {
			passwordBytes[i] = (byte) wrappedPassword[i];
		}

		return passwordBytes;
	}

	/**
	 * Null the wrapped password.
	 */
	public void nullPassword() {
		for (int i = 0; i < wrappedPassword.length; i++) {
			wrappedPassword[i] = 0;
		}
	}

	/**
	 * Has the wrapped password been nulled?
	 * 
	 * @return True is it has
	 */
	public boolean isNulled() {
		return nulled;
	}

	/**
	 * Get a copy of the dummy password for use with PKCS #12 KeyStore entries.
	 * 
	 * @return Dummy password, 'password'
	 */
	public static Password getDummyPassword() {
		return new Password(DUMMY_PASSWORD);
	}

	/**
	 * Is the supplied object equal to the password wrapper, ie do they wrap the
	 * same password.
	 * 
	 * @param object
	 *            Object to check
	 * @return True if the object is equal
	 */
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}

		if (!(object instanceof Password)) {
			return false;
		}

		Password password = (Password) object;

		if (wrappedPassword.length != password.wrappedPassword.length) {
			return false;
		}

		for (int i = 0; i < wrappedPassword.length; i++) {
			if (wrappedPassword[i] != password.wrappedPassword[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Nulls the password. Just a fail-safe, applications should null the
	 * password programmatically.
	 */
	protected void finalize() {
		nullPassword();
	}
}
