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
package net.sf.keystore_explorer.gui.crypto.generalsubtree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import net.sf.keystore_explorer.crypto.x509.GeneralNameUtil;

import org.bouncycastle.asn1.x509.GeneralSubtree;

/**
 * Custom cell renderer for the cells of the general subtrees table.
 * 
 */
public class GeneralSubtreesTableCellRend extends DefaultTableCellRenderer {
	/**
	 * Returns the rendered cell.
	 * 
	 * @param jtGeneralSubtrees
	 *            The JTable
	 * @param value
	 *            The value to assign to the cell
	 * @param isSelected
	 *            True if cell is selected
	 * @param row
	 *            The row of the cell to render
	 * @param col
	 *            The column of the cell to render
	 * @param hasFocus
	 *            If true, render cell appropriately
	 * @return The renderered cell
	 */
	public Component getTableCellRendererComponent(JTable jtGeneralSubtrees, Object value, boolean isSelected,
			boolean hasFocus, int row, int col) {
		JLabel cell = (JLabel) super.getTableCellRendererComponent(jtGeneralSubtrees, value, isSelected, hasFocus, row,
				col);

		GeneralSubtree generalSubtree = (GeneralSubtree) value;

		if (col == 0) {
			cell.setText(GeneralNameUtil.safeToString(generalSubtree.getBase()));
		} else if (col == 1) {
			if (generalSubtree.getMinimum() != null) {
				String minimumStr = "" + generalSubtree.getMinimum().intValue();
				cell.setText(minimumStr);
				cell.setToolTipText(minimumStr);
			} else {
				cell.setText("-");
			}
		} else {
			if (generalSubtree.getMaximum() != null) {
				String maximumStr = "" + generalSubtree.getMaximum().intValue();
				cell.setText(maximumStr);
				cell.setToolTipText(maximumStr);
			} else {
				cell.setText("-");
			}
		}

		cell.setBorder(new EmptyBorder(0, 5, 0, 5));

		return cell;
	}
}
