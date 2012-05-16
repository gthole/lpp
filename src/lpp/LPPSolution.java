package lpp;

import java.text.DecimalFormat;

public class LPPSolution {


	private double[] solution;
	private double objectiveFunctionValue;
	private String[] variableNames;
	private String[] constraintTypes;
	private double[] slack;
	private double[] shadowPrice;
	private double[] reducedCost;
	private long solveTime;
	private long feasibleSolutionTime;
	private String solutionLog;
	private String failureMessage;
	
	
	public LPPSolution(String failureMessage, String solutionLog, long feasibleSolutionTime) {
		this.failureMessage = failureMessage;
		this.solutionLog = solutionLog;
		this.feasibleSolutionTime = feasibleSolutionTime;
	}
	
	public LPPSolution(
			double[] optimalSolution, 
			double objectiveFunctionValue, 
			String[] variableNames, 
			String[] constraintTypes, 
			double[] slack, 
			double[] shadowPrice, 
			double[] reducedCost,
			long solveTime,
			long feasibleSolutionTime,
			String solutionLog
		) {
		this.solution = optimalSolution;
		this.objectiveFunctionValue = objectiveFunctionValue;
		this.variableNames = variableNames;
		this.constraintTypes = constraintTypes; 
		this.slack = slack;
		this.shadowPrice = shadowPrice;
		this.reducedCost = reducedCost;
		this.solveTime = solveTime;
		this.feasibleSolutionTime = feasibleSolutionTime;
		this.solutionLog = solutionLog;
	}
	
	public String toString() {
		
		if (failureMessage != null) {
			return failureMessage;
		}
		
		String output = "";
		
		// Optimal Solution
		for(int i = 0; i < solution.length; i++) {
			output += variableNames[i] + " = "+ formatDecimals(solution[i]) + "\n";
		}
		
		// Objective Function
		output += '\n' + "Objective Function Value = " + formatDecimals(objectiveFunctionValue) + '\n' + '\n';
		
		return output;
	}
		
	public String constraintSensitivityString() {
		String output = "";
		
		for(int j = 0; j < slack.length; j++) {
			// Double 0.0 or -0.0 condition was here.
			if(slack[j] == 0) {
				output += "Constraint " + (j + 1) + " is binding";
				if(shadowPrice[j] > Double.NEGATIVE_INFINITY) {
					output += " with shadow price " + formatDecimals(shadowPrice[j]) + ".\n";
				}
				else {
					output += ".\n";
				}
			}
			else if(Double.compare(slack[j], 0.0) > 0) {
				output += "Constraint " + (j + 1) + " is non-binding with " + formatDecimals(slack[j]) + " slack.\n";
			}
			else if(Double.compare(slack[j], 0.0) < 0) {
				output += "Constraint " + (j + 1) + " is non-binding with " + formatDecimals(slack[j]) + " surplus.\n";
			}
		}
		
		return output+'\n';
	}
	
	public String coefficientSensitivityString() {
		String output = "";
		
		for(int i = 0; i < solution.length; i++) {
			output += variableNames[i] + ": " + "Reduced Cost = " + formatDecimals(reducedCost[i]) + '\n';
		}
		
		return output+'\n';
	}
	
	public String getSolutionLog() {
		return this.solutionLog;
	}
	
	public long getSolveTime () {
		return this.solveTime;
	}
	
	public long getFeasibleSolutionTime () {
		return this.feasibleSolutionTime;
	}
	
	public String[] getConstraintTypes () {
		return this.constraintTypes;
	}
	
	public double getObjectiveFunctionValue () {
		return this.objectiveFunctionValue;
	}
	
	private static String formatDecimals(double d) {
		DecimalFormat formatMyDecimal = new DecimalFormat("#,###.###");
		return formatMyDecimal.format(d);
	}
}
