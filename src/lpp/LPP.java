package lpp;

import java.util.ArrayList;
import java.util.Arrays;
import java.text.DecimalFormat;


public class LPP {

	private objectiveFunctionTypes objectiveFunctionType;
	private String[] variableNames;
	private double[] objectiveFunctionCoefficients;
	private double[][] constraintCoefficients;
	private String[] constraintTypes;
	private double[] constraintRightHandSides;
	private double objectiveFunctionValue;

	private enum objectiveFunctionTypes { MAX, MIN };
	private static final int PIVOT_ITERATION_LIMIT = 1000;

	// constructors
	public LPP(
		String objectiveFunctionType, 
		String[] variableNames, 
		double[] objectiveFunctionCoefficients, 
		double[][] constraintCoefficients, 
		String[] constraintTypes, 
		double[] constraintRightHandSides, 
		double objectiveFunctionValue) {
		
		this.objectiveFunctionType = (objectiveFunctionType == "Max") ? objectiveFunctionTypes.MAX : objectiveFunctionTypes.MIN;
		this.variableNames = variableNames;
		this.objectiveFunctionCoefficients = objectiveFunctionCoefficients;
		this.constraintCoefficients = constraintCoefficients;
		this.constraintTypes = constraintTypes;
		this.constraintRightHandSides = constraintRightHandSides;
		this.objectiveFunctionValue = objectiveFunctionValue;
	}


	public String toString() {
		String output = (objectiveFunctionType == objectiveFunctionTypes.MAX) ? "Maximize" : "Minimize";

		output = output + "  " + displayEqLine(objectiveFunctionCoefficients, variableNames);
		if(objectiveFunctionValue != 0) {
			output = output + " + " + formatDecimals(objectiveFunctionValue);
		}

		output = output + '\n' + "subject to the constraints:" + '\n';

		for(int j = 0; j < constraintRightHandSides.length; j++) {
			double[] constraint = constraintCoefficients[j];
			output += displayEqLine(constraint, variableNames);
			output += " " + constraintTypes[j];
			output += " "  + formatDecimals(constraintRightHandSides[j]) ;
			output += + '\n' + '\n';
		}
		return output;
	}

	private static String displayEqLine(double[] coefficients, String[] variableNames) {
		String output = "";

		int startIndex = 1;
		for(int i = 0; i < variableNames.length; i++) {
			// Had a comparison with 0 OR -0, which seems weird.  TODO: Check.
			if(coefficients[i] != 0) {
				output = output + formatDecimals(coefficients[i]) +  variableNames[i];
				break;
			}
			else {
				startIndex++;
			}
		}

		for(int i = startIndex; i < variableNames.length; i++) {
			String signString = " + ";
			double sign = 1.0;

			// Same 0 OR -0 check here.
			if(coefficients[i] < 0) {
				signString = " - ";
				sign = 1.0;
			}
			if(coefficients[i] != 0) {
				output = output + signString + formatDecimals(sign * coefficients[i]) + variableNames[i];
			}
		}

		return output;
	}
	
	// String formatting helper function.
	private static String formatDecimals(double d) {
		DecimalFormat formatMyDecimal = new DecimalFormat("#,###.###");
		return formatMyDecimal.format(d);
	}
	
	// Convert an integer into a multi-character subscript.
	private String subscriptN(int n) {
		String index = "" + n;
		String subscript = "";
		char c;
		for(int i = 0; i < index.length(); i++) {
			switch (n) {
		        case 0 :  c = '\u2080'; break;
		        case 1 :  c = '\u2081'; break;
		        case 2 :  c = '\u2082'; break;
		        case 3 :  c = '\u2083'; break;
		        case 4 :  c = '\u2084'; break;
		        case 5 :  c = '\u2085'; break;
		        case 6 :  c = '\u2086'; break;
		        case 7 :  c = '\u2087'; break;
		        case 8 :  c = '\u2088'; break;
		        default:  c = '\u2089'; break;
		    }
			subscript += c;
		}
		return subscript;
	}

	public void makeStandardForm() {
		//Change Signs to = by adding variables
		for(int i = 0; i < constraintTypes.length; i++) {
			if(constraintTypes[i] != "=") {
				this.addVariableAt(i, (constraintTypes[i] == "³") ? -1 : 1);
				constraintTypes[i] = "=";
			}
		}
	}
	
	private void makeStandardForm(ArrayList<Integer> artificialVariables) {
		//Change Signs to = by adding variables
		for(int i = 0; i < constraintTypes.length; i++) {
			if(constraintTypes[i] != "=") {
				addVariableAt(i, (constraintTypes[i] == "³") ? -1 : 1);
				constraintTypes[i] = "=";
				artificialVariables = increaseArtificialVariableIndices(artificialVariables);
			}
		}
	}
	
	private static ArrayList<Integer> increaseArtificialVariableIndices(ArrayList<Integer> artificialVariables) {
		for(int j = 0; j < artificialVariables.size(); j++) {
			if(artificialVariables.get(j) != -1) {
				artificialVariables.set(j, artificialVariables.get(j) + 1);
			}
		}
		return artificialVariables;
	}

	private ArrayList<Integer> getArtificialVariableAssignments() {
		ArrayList<Integer> assignments = new ArrayList<Integer>();
		int k = 0;
		for(int j = 0; j < constraintTypes.length; j++) {
			if(constraintTypes[j] == "=") {
				assignments.add(objectiveFunctionCoefficients.length + k);
				k++;
			}
			else {
				assignments.add(-1);
			}
		}
		return assignments;
	}

	private void addArtificialVariables(ArrayList<Integer> artificialVariables) {
		// TODO: Use for each?
		for(int j = 0; j < constraintTypes.length; j++) {
			if(artificialVariables.get(j) != -1) {
				this.addVariableAt(j, 1);
			}
		}
	}

	// TODO: Review
	public void pivot(int varIndex, int constIndex) {

		double[] pivotConstraint = constraintCoefficients[constIndex];
		double pivotConstRHS = constraintRightHandSides[constIndex];
		if(pivotConstraint[varIndex] != 0) {
			
			//Divide the pivot constraint through by the pivot variable coefficient
			double pivotVarCoeff = pivotConstraint[varIndex];
			for(int i = 0; i < pivotConstraint.length; i++) {
				double coeff = pivotConstraint[i];
				pivotConstraint[i] = coeff / pivotVarCoeff;
			}
			pivotConstRHS = (pivotConstRHS/pivotVarCoeff);
			constraintCoefficients[constIndex] = pivotConstraint;
			constraintRightHandSides[constIndex] = pivotConstRHS;

			// eliminate the pivot variable from the other constraints
			for(int j = 0; j < constraintCoefficients.length; j++) {

				// check constraint j != pivot constraint
				if(j != constIndex){

					// make constraint local variables
					double[] constraint = constraintCoefficients[j];
					double constRHS = constraintRightHandSides[j];

					// check the coefficient of the pivot variable in the non-pivot constraint != 0
					if(constraint[varIndex] != 0) {
						double constraintPivotVarCoeff = constraint[varIndex];

						// perform Elimination variable by variable
						for(int i = 0; i < constraint.length; i++) {
							constraint[i] = constraint[i] - (pivotConstraint[i] * constraintPivotVarCoeff);
						}

						// write new constraint to LPP
						constRHS = (constRHS-(pivotConstRHS * constraintPivotVarCoeff));
						constraintCoefficients[j] = constraint;
						constraintRightHandSides[j] = constRHS;
					}
				}
			}
			// substitute pivot variable into the objective function.
			pivotVarCoeff = objectiveFunctionCoefficients[varIndex];
			pivotConstraint = constraintCoefficients[constIndex];
			pivotConstRHS = constraintRightHandSides[constIndex];

			objectiveFunctionCoefficients[varIndex] = 0.0;
			objectiveFunctionValue =objectiveFunctionValue + (pivotVarCoeff * pivotConstRHS);

			for(int i = 0; i < objectiveFunctionCoefficients.length; i++) {
				if(i!=varIndex) {
					objectiveFunctionCoefficients[i] = objectiveFunctionCoefficients[i] + ((-1) * pivotConstraint[i] * pivotVarCoeff);
				}
			}
		}
	}

	// Unfortunate copy and pasting going on here.
	private void addVariableAt(int constraintIndex, double value) {
		String[] newVariableNames = Arrays.copyOf(variableNames, variableNames.length + 1);
		newVariableNames[variableNames.length + 1] = subscriptN(variableNames.length);
		variableNames = newVariableNames;
		
		double[] newObjectiveFunctionCoefficients = Arrays.copyOf(objectiveFunctionCoefficients, objectiveFunctionCoefficients.length + 1);
		newObjectiveFunctionCoefficients[objectiveFunctionCoefficients.length + 1] = 0;
		objectiveFunctionCoefficients = newObjectiveFunctionCoefficients;
		
		for(int j = 0; j < constraintCoefficients.length; j++) {
			double[] constraint = Arrays.copyOf(constraintCoefficients[j], constraintCoefficients[j].length + 1);
			constraint[constraintCoefficients[j].length + 1] = (j != constraintIndex) ? 0 : value;
			constraintCoefficients[j] = constraint;
		}
	}

	private static boolean isFeasible(LPP lpp, ArrayList<Integer> possibleSolution) {
		for(int j = 0; j < lpp.constraintRightHandSides.length; j++) {
			lpp.pivot(possibleSolution.get(j), j);
		}

		for(int j = 0; j < lpp.constraintRightHandSides.length; j++) {
			double[] constraint = lpp.constraintCoefficients[j];

			// Check all basic variables are non-negative
			if(lpp.constraintRightHandSides[j] < 0) {
				return false;
			}
			
			// Ensure there are no unequal constraints
			double q = 0;
			for(int i = 0; i < constraint.length; i++) {
				 if(possibleSolution.contains(i)) {
					 q = q + constraint[i] * lpp.constraintRightHandSides[possibleSolution.indexOf(i)];
				 }
			}
			if(q != lpp.constraintRightHandSides[j]) {
				return false;
			}
		}
		return true;
	}
	
	private ArrayList<Integer> findInitialBasicVariables(ArrayList<Integer> artificialVariables) {
		
		// Declare Basic Variable array, boolean to indicate if a feasible solution has been found
		ArrayList<Integer> alpha = new ArrayList<Integer>();
		boolean foundBasicFeasSol = false;
		
		// Determine the number of regular variables
		int q = 0;
		for(int j = 0; j < artificialVariables.size(); j++) {
			if(artificialVariables.get(j) != -1) {
				q++;
			}
		}
		
		// Set up parameters for finding subsets
		int n = variableNames.length - q;
		int powersetsize = (int) Math.pow(2,n);
		
		for (int i = 0; i < powersetsize; i++) {
			
			// Reinitialize potential basic feasible solution
			alpha = new ArrayList<Integer>();
			
			//  Convert the binary number to a string containing n digits
			String binary = intToBinary(i, n);

			//  Create the corresponding subset
			for (int j = 0; j < binary.length(); j++) {
				if (binary.charAt(j) == '1') {
				    alpha.add(j);
				}
			}
			
			// Check to see if the basic variable set alpha is feasible
			if(isFeasible(this, alpha)==true) {
				foundBasicFeasSol = true;
				break;
			}
		}
		
		//  No feasible solution is found, create dummy solution vector.
		if(!foundBasicFeasSol) {
			alpha = new ArrayList<Integer>();
			for(int j = 0; j < constraintRightHandSides.length; j++) {
				alpha.add(-1);
			}
		}

		return alpha;
	}

	private static String intToBinary(int binary, int digits) {

		String temp = Integer.toBinaryString(binary);
		int foundDigits = temp.length();
		String returner = temp;
		for (int i = foundDigits; i < digits; i++) {
			returner = "0" + returner;
		}

		return returner;
	} 
	
	private int choosePivotVar(ArrayList<Integer> artificialVariables) {
		double q = 0;
		int choice = -1;

		double maxormin = 1;
		if(objectiveFunctionType == objectiveFunctionTypes.MAX) {
			maxormin = maxormin*(-1);
		}

		for(int i = 0; i < objectiveFunctionCoefficients.length; i++) {
			double coefficientTerm = maxormin * objectiveFunctionCoefficients[i];
			if(!artificialVariables.contains(i)) {
				// TODO: Necessary?
				if(coefficientTerm == -0.0) {
					coefficientTerm = 0.0;
				}
				// TODO: Confirm comparison is correct.  Should be >?
				if(coefficientTerm < q) {
					q = coefficientTerm;
					choice = i;
				}
			}
		}
		return choice;
	}

	private int choosePivotConstraint(int n) {
		
		// Correct?
		if (n == -1) {
			return 0;
		}
		
		// Initialize variables
		double q = 0;
		int choice = -1;
		
		// Run down the column for the given variable, compare ratios of coefficient/RHS
		for(int j = 0; j < constraintRightHandSides.length; j++) {
			double[] constraint = constraintCoefficients[j];
			if(constraint[n] > 0) {
				double ratio = constraint[n] / constraintRightHandSides[n];
				
				// q holds the highest ratio, if a higher ratio is found, Output is changed to corresponding constraint index
				if(ratio > q) {
					q = ratio;
					choice = j;
				}
			}
		}

		return choice;
	}

	public LPPSolution solve() {
		// Initialize Variables
		int varNum = variableNames.length; // Point badness if we are going to be incrementing this later?
		ArrayList<Integer> artificialVariables = getArtificialVariableAssignments();
		
		// ArrayList<String> varNames = Input.VariableNames;
		// String LaTeXString = latex.LPPtoLaTeX.displayLPP(Input)+'\n';
		
		String solutionLog = "Make Standard Form\n";
		makeStandardForm(artificialVariables);
		long startTime = System.currentTimeMillis();
		
		// Add artificial variables to the LPP
		addArtificialVariables(artificialVariables);

		// Search for Basic Feasible Solution
		ArrayList<Integer> basicVariables = findInitialBasicVariables(artificialVariables);
		long feasibleSolutionTime = System.currentTimeMillis()-startTime;
		
		// Return fail message if no feasible solution is found
		if(basicVariables.get(0) == -1) {
			return new LPPSolution("Could not find a Basic Feasible Solution.", solutionLog, feasibleSolutionTime);
		}
		
		solutionLog += "Basic Variables: " + basicVariables + '\n';


		// Pivot until optimal solution
		boolean go = true;
		int limiter = 1;
		//LaTeXString += latex.LPPtoLaTeX.beginTableaus(Input);
		
		while(go) {
			
			// Get next variable to pivot on
			int n = choosePivotVar(artificialVariables);
			int next = choosePivotConstraint(n);
			
			// If optimal solution reached, end 'while' statement
			//LaTeXString += latex.LPPtoLaTeX.makeTableau(Input, BasicVars, limiter);
			
			// Found a solution.  Stop pivoting.
			if(n == -1) {
				go = false;
			}
			
			// Check iteration limit not exceeded.
			else if(limiter == PIVOT_ITERATION_LIMIT) {
				return new LPPSolution("The pivot max iteration cap was exceeded.", solutionLog, feasibleSolutionTime);
			}
			
			// Check for unboundedness.
			else if (next == -1) {
				return new LPPSolution("The given LPP is unbounded.", solutionLog, feasibleSolutionTime);
			}
			
			// Get pivot constraint, continue.
			else {
				pivot(n, next);
				basicVariables.set(next, n);
				solutionLog += "Pivot at " + n + ", " + next + "\n";
			}

			limiter++;
		}
		
		// Close LaTeX and initialize sensitivity variables
		// LaTeXString += latex.LPPtoLaTeX.endTableaus();
		double[] optimalSolution = new double[variableNames.length];
		double[] reducedCost = new double[variableNames.length];
		double[] shadowPrice = new double[constraintTypes.length];
		double[] slack = new double[constraintTypes.length];

		// Collect optimal solution and reduced cost from final tableau
		for(int i = 0; i < variableNames.length; i++) {
			if(basicVariables.contains(i)) {
				int basicVariableIndex = basicVariables.indexOf(i);
				
				// Check for redundant constraints
				if(basicVariables.indexOf(i) != basicVariables.lastIndexOf(i)) {
					for(int k = 0; k < basicVariables.size(); k++) {
						if(basicVariables.get(k) == i) {
							double[] constraint = constraintCoefficients[basicVariableIndex];
							for(int m = 0; m < constraint.length; m++) {
								// This had a double 0.0 or -0.0 check.
								if(constraint[m] != 0.0) {
									basicVariableIndex = k;
									break;
								}
							}
						}
					}
				}
				
				// Set values
				optimalSolution[i] = constraintRightHandSides[basicVariableIndex];
				reducedCost[i] = objectiveFunctionCoefficients[i];
			}
			else {
				optimalSolution[i] = 0;
				reducedCost[i] = objectiveFunctionCoefficients[i];
			}
		}

		// Collect Constraint Sensitivity Analysis Data from Final Tableau
		for(int j = 0; j < constraintTypes.length; j++) {
			if(constraintTypes[j] == "=") {
				slack[j] = 0;
				shadowPrice[j] = -1 * objectiveFunctionCoefficients[artificialVariables.get(j)];
			}
			// This had the double 0.0 or -0.0 check.
			else if(objectiveFunctionCoefficients[varNum] == 0.0){
				slack[j] = constraintRightHandSides[j];
				shadowPrice[j] = 0;
				varNum++;
			}
			else {
				slack[j] = 0;
				shadowPrice[j] = -1 * objectiveFunctionCoefficients[varNum];
				varNum++;
			}
		}

		// Return the compiled solution.
		return new LPPSolution(
						optimalSolution, 
						objectiveFunctionValue, 
						variableNames, 
						constraintTypes, 
						slack, 
						shadowPrice, 
						reducedCost,
						System.currentTimeMillis() - startTime,
						feasibleSolutionTime,
						solutionLog
					);
	}
}
