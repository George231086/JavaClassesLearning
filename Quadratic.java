import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author george
 * 			
 * 		   Program that asks for a quadratic equation in the form ax^2+bx+c, 
 *         then returns the roots.
 * 
 */

public class Quadratic {
	// Coefficients of quadratic.
	private double a;
	private double b;
	private double c;

	public Quadratic(double a, double b, double c) {
		if (a == 0)
			throw new IllegalArgumentException(
					"Quadratic cannot have zero x^2 term!");
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public boolean hasRealRoots() {
		return (b * b - 4 * a * c >= 0);
	}

	public boolean hasDoubleRoot() {
		return (b * b - 4 * a * c == 0);
	}

	// Solutions are of the form s1=x1+sqrt(x2), s2=x1-sqrt(x2), this method
	// returns [x1,x2].
	public double[] getSolutionComponents() {
		double[] solutions = new double[2];

		solutions[0] = (-b / (2 * a));
		solutions[1] = (b * b - 4 * a * c) / (4 * a * a);

		return solutions;
	}

	static boolean isQuad(String string) {

		Pattern quadPattern = Pattern
				.compile("[+-]?[0-9]*\\.?[0-9]*x\\^2([+-][0-9]*\\.?[0-9]*x)?([+-][0-9]*\\.?[0-9]+)?");
		Matcher m = quadPattern.matcher(string);
		return m.matches();
	}

	static String[] parseQuad(String input) {
		String a = "0", b = "0", c = "0";

		if (isQuad(input)) {
			a = input.substring(0, input.indexOf("x^2"));
			// Append 1 to a if poly of form x^2+bx+c.
			if (a.length() == 0)
				a = "1";
			input = input.substring(input.indexOf("x^2") + 3);
			if (input.contains("x")) {
				b = input.substring(0, input.indexOf("x"));
				if (input.substring(input.indexOf("x")).length() > 1)
					c = input.substring(input.indexOf("x") + 1);
			} else if (input.length() > 0)
				c = input;
		} else
			throw new IllegalArgumentException();

		String[] coeffs = new String[3];
		// If poly of form +x^2+x+1 we would have a=+,b=+ so need to append 1.
		coeffs[0] = (a.endsWith("+") || a.endsWith("-")) ? a + "1" : a;
		coeffs[1] = (b.endsWith("+") || b.endsWith("-")) ? b + "1" : b;
		coeffs[2] = c;

		return coeffs;
	}

	public static void main(String args[]) {
		System.out.println("Give a quadratic with real coefficients!"
				+ " Give in form ax^2+bx+c, ie x^2+2x+1, 2x^2+3, -3x^2+2x");
		Scanner scan = new Scanner(System.in);
		Quadratic quad = null;

		try {
			String[] coeffs = parseQuad(scan.nextLine().replaceAll(" +", ""));
			quad = new Quadratic(Double.parseDouble(coeffs[0]),
					Double.parseDouble(coeffs[1]),
					Double.parseDouble(coeffs[2]));
		} catch (IllegalArgumentException e) {
			System.out
					.println("Either input is not a quadratic or in the form ax^2+bx+c!");
			System.exit(1);
		} finally {
			scan.close();
		}

		double[] solComponents = quad.getSolutionComponents();
		double x1 = solComponents[0];
		double x2 = solComponents[1];

		// Would rather output 0 instead of -0
		if (x1 == -0)
			x1 = 0;

		// Find out type of roots and give result.
		if (quad.hasRealRoots()) {

			if (quad.hasDoubleRoot())
				System.out.printf("Double root! The solution to 2dp is %.2f\n",
						x1);
			else
				System.out.printf(
						"The solutions to 2dp are:\ns1=%.2f and s2=%.2f\n", x1
								+ Math.sqrt(x2), x1 - Math.sqrt(x2));
		} else
			System.out.printf(
					"Complex roots! The solutions to 2dp are:\ns1=%1$.2f + i%2$.2f"
							+ " and s2=%1$.2f - i%2$.2f\n", x1, Math.sqrt(-x2));

	}
}
