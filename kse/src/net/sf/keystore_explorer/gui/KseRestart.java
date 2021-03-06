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
package net.sf.keystore_explorer.gui;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import net.sf.keystore_explorer.KSE;

/**
 * Restart KeyStore Explorer.
 * 
 */
public class KseRestart {
	private KseRestart() {
	}

	/**
	 * Restart KeyStore Explorer in the same manner in which it was started.
	 */
	public static void restart() {
		if (System.getProperty("kse.exe") != null) {
			restartAsKseExe();
		} else if (System.getProperty("kse.app") != null) {
			restartAsKseApp();
		} else if (System.getProperty("java.class.path").equals("kse.jar")) {
			restartAsKseJar();
		} else {
			restartAsKseClass();
		}
	}

	private static void restartAsKseExe() {
		File kseInstallDir = new File(System.getProperty("kse.install.dir"));

		File kseExe = new File(kseInstallDir, "kse.exe");

		String toExec[] = new String[] { kseExe.getPath() };

		try {
			Runtime.getRuntime().exec(toExec);
		} catch (IOException ex) {
			ex.printStackTrace(); // Ignore
		}
	}

	private static void restartAsKseJar() {
		File javaBin = new File(new File(System.getProperty("java.home"), "bin"), "java");

		File kseInstallDir = new File(System.getProperty("kse.install.dir"));

		File kseJar = new File(kseInstallDir, "kse.jar");

		String toExec[] = new String[] { javaBin.getPath(), "-jar", kseJar.getPath() };

		try {
			Runtime.getRuntime().exec(toExec);
		} catch (IOException ex) {
			ex.printStackTrace(); // Ignore
		}
	}

	private static void restartAsKseApp() {
		File kseInstallDir = new File(System.getProperty("kse.install.dir"));

		String kseApp = MessageFormat.format("{0} {1}.app", KSE.getApplicationName(), KSE.getApplicationVersion());

		File javaAppStub = new File(new File(new File(new File(kseInstallDir, kseApp), "Contents"), "MacOS"),
				"JavaApplicationStub");

		String toExec[] = new String[] { javaAppStub.getPath() };

		try {
			Runtime.getRuntime().exec(toExec);
		} catch (IOException ex) {
			ex.printStackTrace(); // Ignore
		}
	}

	private static void restartAsKseClass() {
		File javaBin = new File(new File(System.getProperty("java.home"), "bin"), "java");

		String kseClasspath = System.getProperty("java.class.path");

		String toExec[] = new String[] { javaBin.getPath(), "-classpath", kseClasspath, KSE.class.getName() };

		try {
			Runtime.getRuntime().exec(toExec);
		} catch (IOException ex) {
			ex.printStackTrace(); // Ignore
		}
	}
}
