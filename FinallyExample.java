import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author george
 * 
 *         Class to demonstrate how finally blocks interact with return
 *         statements.
 * 
 */

public class FinallyExample {

	static public int getInt() {
		int i = 1;

		try {
			return i;
		} finally {
			i += 100;
		}
	}

	static public List<String> getList() {
		List<String> l = new ArrayList<>();
		l.add("Before try");
		try {
			return l;
		} finally {
			l.add("Added in finally block");
		}
	}

	static public List<String> getList2() {
		List<String> l = new ArrayList<>();
		l.add("Before try");
		try {
			return l;
		} finally {
			l = new ArrayList<String>();
			l.add("Added in finally block");
		}
	}

	static public String getString() {
		String s = "Initial String";
		try {
			return s;
		} finally {
			s = "Finally String";
		}
	}

	public static void main(String args[]) {

		// A return statement for a variable x returns the object assigned to x
		// at that time. Assigning to x a new object in a finally block will not be reflected
		// in the returned x. However if the original object is mutable, then modifying its state
		// in a finally block will be felt.

		// i+=100 in the finally block is not felt. i is a primitive so this
		// corresponds to assigning a new value to i.

		System.out.println(getInt()); // Prints 1

		// The object returned reflects the changes from the finally block as it
		// is the same object only with altered state.

		System.out.println(getList()); // Prints [Before try, Added in finally block]

		// In the finally block a new list is assigned to l, rather than altering
		// the existing list, hence the finally block has no effect on
		// the returned value of l.

		System.out.println(getList2()); // Prints [Before try]

		// Same as above. Strings are mutable, the line s="Finally String" is
		// the assignment of a new string object to s. The original string object
		// "Initial String" is the one returned.

		System.out.println(getString()); // Prints Initial String
	}

}
