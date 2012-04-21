package lpp;

public class LPPExamples {
		
		public static LPP minimizeExample() {		
			return new LPP(
					"Min", 
					// new String[] {"x\u2081", "x\u2081", "x\u2081", "x\u2084"},
					new String[] {"x_1", "x_2", "x_3", "x_4"},
					new double[] {1, -1, 2, -5}, 
					new double[][] {
							{1, 1, 2, 4},	
							{0, 3, 1, 8}
					},
					new String[] {"=", "="},
					new double[] {6, 3}, 
					0);
		}
		
		public static LPP smallMinimizeExample() {		
			return new LPP(
					"Min", 
					// new String[] {"x\u2081", "x\u2081"},
					new String[] {"x_1", "x_2"},
					new double[] {.6, .8}, 
					new double[][] {
							{.6, .2},	
							{.1, .5}
					},
					new String[] {"³", "³"},
					new double[] {30, 26}, 
					0);

		}

		
		public static LPP maximizeExample() {		
			return new LPP(
					"Max", 
					// new String[] {"x\u2081", "x\u2081", "x\u2081"},
					new String[] {"x_1", "x_2", "x_3"},
					new double[] {2, 3, 3}, 
					new double[][] {
							{3, 2, 0},	
							{-1, 1, 4},
							{2, -2, 5}
					},
					new String[] {"²", "²", "²"},
					new double[] {60, 10, 50}, 
					0);
		}
}
