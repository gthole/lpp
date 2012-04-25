package lpp;

public class LPPExamples {
		
		public static LPP minimizeExample() throws Exception {		
			return new LPP(
					"Min", 
					new String[] {},
					new double[] {1, -1, 2, -5}, 
					new double[][] {
							{1, 1, 2, 4},	
							{0, 3, 1, 8}
					},
					new String[] {"=", "="},
					new double[] {6, 3}, 
					0);
		}
		
		
		public static LPP smallMinimizeExample() throws Exception {		
			return new LPP(
					"Min", 
					new String[] {"a", "b"},
					new double[] {.6, .8}, 
					new double[][] {
							{.6, .2},	
							{.1, .5}
					},
					new String[] {"³", "³"},
					new double[] {30, 26}, 
					0);

		}

		
		public static LPP maximizeExample() throws Exception {		
			return new LPP(
					"Max", 
					new String[] {},
					new double[] {2, 3, 3}, 
					new double[][] {
							{3, 2, 0},	
							{-1, 1, 4},
							{2, -2, 5}
					},
					new String[] {"²", "²", "²"},
					new double[] {60, 10, 50}, 
					0
				);
		}
		
		public static LPP transshipment() throws Exception {
			return new LPP(
					"Min",
					new String[] {},
					new double[] {16,21,18,16,22,25,23,15,29,20,17,24},
					new double[][] {
						{1,1,0,0,0,0,0,0,0,0,0,0},
						{0,0,1,1,0,0,0,0,0,0,0,0},
						{0,0,0,0,1,1,0,0,0,0,0,0},
						{1,0,1,0,1,0,-1,-1,-1,0,0,0},
						{0,1,0,1,0,1,0,0,0,-1,-1,-1},
						{0,0,0,0,0,0,1,0,0,1,0,0},
						{0,0,0,0,0,0,0,1,0,0,1,0},
						{0,0,0,0,0,0,0,0,1,0,0,1},
					},
					new String[] {"=","=","=","=","=","²","²","²"},
					new double[] {72,105,83,0,0,90,80,120},
					0
				);
		};
			
		public static LPP strictEquality() throws Exception {
			return new LPP(
					"Min",
					new String[] {},
					new double[] {3,4,5,2,7,8},
					new double[][] {
							{1,1,1,0,0,0},
							{0,0,0,0,1,1},
							{1,0,0,-1,0,0},
							{0,1,0,1,-1,0},
							{0,0,1,0,0,-1}
					},
					new String[] {"=","=","=","=","="},
					new double[] {5,5,0,0,0},
					0
				);
		}
	}

