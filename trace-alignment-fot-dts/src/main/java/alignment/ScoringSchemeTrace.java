/*
 * ScoringScheme.java
 *
 * Copyright 2003 Sergio Anibal de Carvalho Junior
 *
 * This file is part of NeoBio.
 *
 * NeoBio is free software; you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * NeoBio is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with NeoBio;
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * Proper attribution of the author as the source of the software would be appreciated.
 *
 * Sergio Anibal de Carvalho Junior		mailto:sergioanibaljr@users.sourceforge.net
 * Department of Computer Science		http://www.dcs.kcl.ac.uk
 * King's College London, UK			http://www.kcl.ac.uk
 *
 * Please visit http://neobio.sourceforge.net
 *
 * This project was supervised by Professor Maxime Crochemore.
 *
 */

package alignment;

import elements.Snapshot;

/**
 * This abstract class is the superclass of all scoring schemes. It defines basic
 * operations that must be provided by all subclasses. Scoring schemes are used by
 * sequence alignment algorithms to compute the score of an alignment.
 *
 * @author Sergio A. de Carvalho Jr.
 * @author Paula Muñoz
 *
 * This is a modified version from the Original NeoBio Library to use for trace alignment in a DTs context.
 */
public abstract class ScoringSchemeTrace
{
	private final int match;
	private final int mismatch;
	private final int gap;
	private final double tolerance;

	/**
	 * Creates a new instance of an scoring scheme. The case of characters is significant
	 * when subsequently computing their score.
	 */
	public ScoringSchemeTrace(int match, int mismatch, int gap, double tolerance)
	{
		this.match = match;
		this.mismatch = mismatch;
		this.gap = gap;
		this.tolerance = tolerance;
	}

	/**
	 * Returns the score of a substitution of snapshot <CODE>a</CODE> for snapshot
	 * <CODE>b</CODE> according to this scoring scheme. If this substitution is not
	 * defined, an exception is raised.
	 *
	 * @param a first snapshot
	 * @param b second snapshot
	 * @return score of substitution of <CODE>a</CODE> for <CODE>b</CODE>
	 * @throws IncompatibleScoringSchemeException if this substitution is not defined
	 */
	public abstract int scoreSubstitution (Snapshot a, Snapshot b)
		throws IncompatibleScoringSchemeException;

	/**
	 * Returns the score of an insertion of character <CODE>a</CODE> according to this
	 * scoring scheme. If this character is not recognised, an exception is raised.
	 *
	 * @return score of insertion of <CODE>a</CODE>
	 * @throws IncompatibleScoringSchemeException if character is not recognised by this
	 * scoring scheme
	 */
	public abstract int scoreInsertion()
		throws IncompatibleScoringSchemeException;

	/**
	 * Returns the score of a deletion of character <CODE>a</CODE> according to this
	 * scoring scheme. If this character is not recognised, an exception is raised.
	 *
	 * @return score of insertion of <CODE>a</CODE>
	 * @throws IncompatibleScoringSchemeException if character is not recognised by this
	 * scoring scheme
	 */
	public abstract int scoreDeletion()
		throws IncompatibleScoringSchemeException;

	/**
	 * Returns the maximum absolute score that this scoring scheme can return for any
	 * substitution, deletion or insertion.
	 *
	 * @return maximum absolute score that can be returned
	 */
	public abstract int maxAbsoluteScore ();

	/**
	 * Returns <CODE>true</CODE> if this scoring scheme supports partial matches,
	 * <CODE>false</CODE> otherwise. A partial match is a situation when two characters
	 * are not equal but, for any reason, are regarded as similar by this scoring scheme,
	 * which then returns a positive score. This is common when for scoring schemes
	 * that implement amino acid scoring matrices.
	 *
	 * @return <CODE>true</CODE> if this scoring scheme supports partial matches,
	 * <CODE>false</CODE> otherwise
	 */
	public abstract boolean isPartialMatchSupported();

	public int getMatch() {
		return match;
	}

	public int getMismatch() {
		return mismatch;
	}

	public int getGap() {
		return gap;
	}

	public double getTolerance() {
		return tolerance;
	}
}
