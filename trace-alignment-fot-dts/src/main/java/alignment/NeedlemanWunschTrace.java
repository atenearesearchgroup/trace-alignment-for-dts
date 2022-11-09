/*
 * NeedlemanWunsch.java
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

import elements.Attribute;
import elements.Snapshot;
import elements.Trace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements the classic global alignment algorithm (with linear gap penalty
 * function) due to S.B.Needleman and C.D.Wunsch (1970).
 *
 * <P>It is based on a dynamic programming approach. The idea consists of, given two
 * sequences A and B of sizes n and m, respectively, building an (n+1 x m+1) matrix M that
 * contains the similarity of prefixes of A and B. Every position M[i,j] in the matrix
 * holds the score between the subsequences A[1..i] and B[1..j]. The first row and column
 * represent alignments with spaces.</P>
 *
 * <P>Starting from row 0, column 0, the algorithm computes each position M[i,j] with the
 * following recurrence:</P>
 *
 * <CODE><BLOCKQUOTE><PRE>
 * M[0,0] = 0
 * M[i,j] = max { M[i,j-1]   + scoreInsertion (B[j]),
 *                M[i-1,j-1] + scoreSubstitution (A[i], B[j]),
 *                M[i-1,j]   + scoreDeletion(A[i])             }
 * </PRE></BLOCKQUOTE></CODE>
 *
 * <P>In the end, the value at the last position (last row, last column) will contain
 * the similarity between the two sequences. This part of the algorithm is accomplished
 * by the {@link #computeMatrix computeMatrix} method. It has quadratic space complexity
 * since it needs to keep an (n+1 x m+1) matrix in memory. And since the work of computing
 * each cell is constant, it also has quadratic time complexity.</P>
 *
 * <P>After the matrix has been computed, the alignment can be retrieved by tracing a path
 * back in the matrix from the last position to the first. This step is performed by
 * the {@link #buildOptimalAlignment buildOptimalAlignment} method, and since the path can
 * be roughly as long as (m + n), this method has O(n) time complexity.</P>
 *
 * <P>If the similarity value only is needed (and not the alignment itself), it is easy to
 * reduce the space requirement to O(n) by keeping just the last row or column in memory.
 * This is precisely what is done by the {@link #computeScore computeScore} method. Note
 * that it still requires O(n<SUP>2</SUP>) time.</P>
 *
 * <P>For a more efficient approach to the global alignment problem, see the
 * {CrochemoreLandauZivUkelson} algorithm. For local alignment, see the
 * {SmithWaterman} algorithm.</P>
 *
 * @author Sergio A. de Carvalho Jr.
 * @author Paula Muñoz
 *
 * This is a modified version from the Original NeoBio Library to use for trace alignment in a DTs context.
 */
public abstract class NeedlemanWunschTrace<T> extends PairwiseAlignmentAlgorithmTrace
{
	/**
	 * The first sequence of an alignment.
	 */
	protected Trace<T> trace1;

	/**
	 * The second sequence of an alignment.
	 */
	protected Trace<T> trace2;

	/**
	 * The dynamic programming matrix. Each position (i, j) represents the best score
	 * between the firsts i characters of <CODE>seq1</CODE> and j characters of
	 * <CODE>seq2</CODE>.
	 */
	protected double[][] matrix;

	/**
	 * Loads sequences into instances. In case of any error,
	 * an exception is raised by the constructor of <CODE>CharSequence</CODE> (please
	 * check the specification of that class for specific requirements).
	 *
	 * @param input1 Input for first sequence
	 * @param input2 Input for second sequence
	 * @throws IOException If an I/O error occurs when reading the sequences
	 * @throws InvalidSequenceException If the sequences are not valid
	 */
	protected void loadSequencesInternal (Trace input1, Trace input2)
		throws IOException, InvalidSequenceException
	{
		// load sequences into instances of CharSequence
		this.trace1 = input1;
		this.trace2 = input2;
	}

	/**
	 * Frees pointers to loaded sequences and the dynamic programming matrix so that their
	 * data can be garbage collected.
	 */
	protected void unloadSequencesInternal ()
	{
		this.trace1 = null;
		this.trace2 = null;
		this.matrix = null;
	}

	/**
	 * Builds an optimal global alignment between the loaded sequences after computing the
	 * dynamic programming matrix. It calls the <CODE>buildOptimalAlignment</CODE> method
	 * after the <CODE>computeMatrix</CODE> method computes the dynamic programming
	 * matrix.
	 *
	 * @return an optimal global alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 * @see #computeMatrix
	 * @see #buildOptimalAlignment
	 */
	protected List<String[]> computePairwiseAlignment ()
		throws IncompatibleScoringSchemeException
	{
		// compute the matrix
		computeMatrix ();

		// build and return an optimal global alignment
		List<String[]> alignment = buildCSVFile ();

		// allow the matrix to be garbage collected
		matrix = null;

		return alignment;
	}

	/**
	 * Computes the dynamic programming matrix.
	 *
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected void computeMatrix () throws IncompatibleScoringSchemeException
	{
		int r;
        int c;
        int rows;
        int cols;
        double ins;
        double del;
        double sub;

        rows = trace1.getSnapshots().size() + 1;
		cols = trace2.getSnapshots().size() + 1;

		matrix = new double[rows][cols];

		// initiate first row
		matrix[0][0] = 0;
		for (c = 1; c < cols; c++)
			matrix[0][c] = matrix[0][c-1] + scoreInsertion(); // c

		// calculates the similarity matrix (row-wise)
		for (r = 1; r < rows; r++)
		{
			// initiate first column
			matrix[r][0] = matrix[r-1][0] + scoreDeletion(); // r

			for (c = 1; c < cols; c++)
			{
				ins = matrix[r][c-1] + scoreInsertion();
				sub = matrix[r-1][c-1] + scoreSubstitution(trace1.snapshotAt(r-1), trace2.snapshotAt(c-1));
				del = matrix[r-1][c] + scoreDeletion();

				// choose the greatest
				matrix[r][c] = max (ins, sub, del);
			}
		}
	}

	public List<String[]> buildCSVFile() throws IncompatibleScoringSchemeException {
		List<String[]> result = new ArrayList<>(generateHeaders());
		List<String[]> optimalAlignment = buildOptimalAlignment();

		List<Snapshot<T>> snapshotsTrace1 = trace1.getSnapshots();
		List<Snapshot<T>> snapshotsTrace2 = trace2.getSnapshots();

		int i = 0;
		for(String[] row : optimalAlignment){
			List<String> updatedRow = new ArrayList<>();

			if(i<snapshotsTrace1.size()) {
				Snapshot<T> snp1 = snapshotsTrace1.get(i);
				updatedRow.add(String.valueOf(snp1.getTimestamp()));
				updatedRow.addAll(snp1.getValues());
			} else {
				for(int k = 0; k < snapshotsTrace1.get(0).getValues().size() + 1; k++){
					updatedRow.add(EMPTY_SPACE);
				}
			}

			if(i<snapshotsTrace2.size()) {
				Snapshot<T> snp2 = snapshotsTrace2.get(i);
				updatedRow.add(String.valueOf(snp2.getTimestamp()));
				updatedRow.addAll(snp2.getValues());
			} else {
				for(int k = 0; k < snapshotsTrace1.get(0).getValues().size() + 1; k++){
					updatedRow.add(EMPTY_SPACE);
				}
			}

			updatedRow.add(EMPTY_SPACE);
			updatedRow.addAll(Arrays.asList(row));
			result.add(updatedRow.toArray(new String[0]));
			i++;
		}

		return result;
	}

	protected  List<String[]> generateHeaders(){
		List<String[]> result = new ArrayList<>();
		int numberOfAttributes = trace1.getSnapshots().get(0).getAttributes().size() + 1;

		// First row with general headers
		String[] generalHeaders =  {
			TRACE_DT + ORIGINAL, TRACE_PT + ORIGINAL, TRACE_DT + ALIGNED, TRACE_PT + ALIGNED
		};

		// Second row with attributes' headers
		String[] attributesHeaders = new String[numberOfAttributes*4+3];

		for(int i = 0; i < 4 ; i++){
			int j = 0;
			if(i == 3) { j += 1; }
			if(i == 2){
				j++;
			}
			attributesHeaders[j+numberOfAttributes*i] = generalHeaders[i] + "-" + "timestamp";
			j++;
			for(Attribute att : trace1.getSnapshots().get(0).getAttributes().keySet()){
				attributesHeaders[j+numberOfAttributes*i] = generalHeaders[i] + "-" + att.getName();
				j++;
			}
		}
		attributesHeaders[numberOfAttributes*4+1] = OPERATION;
		result.add(attributesHeaders);
		return result;
	}

	protected abstract List<String[]> buildOptimalAlignment ()
			throws IncompatibleScoringSchemeException;

	/**
	 * Computes the score of the best global alignment between the two sequences using the
	 * scoring scheme previously set. This method calculates the similarity value only
	 * (doesn't build the whole matrix so the alignment cannot be recovered, however it
	 * has the advantage of requiring O(n) space only).
	 *
	 * @return score of the best global alignment between the loaded sequences
	 * @throws IncompatibleScoringSchemeException If the scoring scheme is not compatible
	 * with the loaded sequences.
	 */
	protected double computeScore () throws IncompatibleScoringSchemeException
	{
		double[] array;
		int		r;
        int c;
        int rows;
        int cols;
        double tmp;
        double ins;
        double del;
        double sub;

        rows = trace1.getSnapshots().size();
		cols = trace2.getSnapshots().size();

		if (rows <= cols)
		{
			// goes columnwise
			array = new double[rows];

			// initiate first column
			array[0] = 0;
			for (r = 1; r < rows; r++)
				array[r] = array[r-1] + scoreDeletion();

			// calculate the similarity matrix (keep current column only)
			for (c = 1; c < cols; c++)
			{
				// initiate first row (tmp hold values
				// that will be later moved to the array)
				tmp = array[0] + scoreInsertion();

				for (r = 1; r < rows; r++)
				{
					ins = array[r] + scoreInsertion();
					sub = array[r-1] + scoreSubstitution(trace1.snapshotAt(r), trace2.snapshotAt(c));
					del = tmp + scoreDeletion();

					// move the temp value to the array
					array[r-1] = tmp;

					// choose the greatest
					tmp = max (ins, sub, del);
				}

				// move the temp value to the array
				array[rows - 1] = tmp;
			}

			return array[rows - 1];
		}
		else
		{
			// goes rowwise
			array = new double[cols];

			// initiate first row
			array[0] = 0;
			for (c = 1; c < cols; c++)
				array[c] = array[c-1] + scoreInsertion();

			// calculate the similarity matrix (keep current row only)
			for (r = 1; r < rows; r++)
			{
				// initiate first column (tmp hold values
				// that will be later moved to the array)
				tmp = array[0] + scoreDeletion();

				for (c = 1; c < cols; c++)
				{
					ins = tmp + scoreInsertion();
					sub = array[c-1] + scoreSubstitution(trace1.snapshotAt(r), trace2.snapshotAt(c));
					del = array[c] + scoreDeletion();

					// move the temp value to the array
					array[c-1] = tmp;

					// choose the greatest
					tmp = max (ins, sub, del);
				}

				// move the temp value to the array
				array[cols - 1] = tmp;
			}

			return array[cols - 1];
		}
	}
}
