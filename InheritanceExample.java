/**
 * 
 * @author george
 * 
 *         Very basic example demonstrating inheritance and polymorphism.
 * 
 */

public class InheritanceExample {

	static class A {
		String instanceVariable = "An instance variable for A!";
		String Ainstance = "A only instance variable";

		public void whoami() {
			System.out.println("I am A!");
		}

		public void forA() {
			System.out.println("Method for A!");
		}
	}

	static class B extends A {
		String instanceVariable = "An instance variable for B!";
		String Binstance = "B only instance variable";

		public void whoami() {
			System.out.println("I am B!");
		}

		public void forB() {
			System.out.println("Method for B!");
		}
	}

	public static void main(String args[]) {
		
		A b = new B();

		// b is of type B, but is referenced by type A. The method whoami is overridden in class B.
		// Runtime polymorphism ensures the method defined in class B is called on b, even though b is
		// referenced by class A.
		 
		b.whoami(); // "I am B!"
		
		// Even though both class A and B have instance variable instanceVariable,
		// only type A's can be accessed as b is referenced by A.
		
		System.out.println(b.instanceVariable); // "An instance variable for A!"
		
		// Cannot access non overridden methods or any instance variables in class B.
		
		b.forBs() // Compilation error 
		System.out.println(b.Binstance); // Compilation error
		
		// b2 below is referenced by type B so can access all methods and instance variables in
		// class B. Can also access all non overridden methods and instance variables in class A. 
		
		B b2 = new B();
		
		System.out.println(b2.instanceVariable); // "An instance variable for B!"
		b2.forB(); // "Method for B!"
		
		// Can access instance variables and methods from base class due to inheritance.
		System.out.println(b2.Ainstance);  
		b2.forA(); // "Method for A!"
		
	}
}
