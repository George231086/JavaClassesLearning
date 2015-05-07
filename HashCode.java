import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author George
 */
 //Basic example overriding equals and hashcode.
public class HashCode {

    public static class IceCream {

        private final String flavour;

        public IceCream(String flavour) {
            this.flavour = flavour;

        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj == null) || (obj.getClass() != this.getClass())) {
                return false;
            }
            IceCream test = (IceCream) obj;
            return (flavour.equals(test.flavour));
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + Objects.hashCode(flavour);
            return hash;
        }

    }

    public static void main(String args[]) {
        HashMap<IceCream, String> map = new HashMap<>();
        IceCream chocolate1 = new IceCream("Chocolate");
        map.put(chocolate1, "Yum a chocolate iceCream!");
        IceCream chocolate2 = new IceCream("Chocolate");

        // chocolate1 and chocolate2 are different instances.
        System.out.println(chocolate1 == chocolate2); //false

        // We have overridden the equals method to compare flavours.
        System.out.println(chocolate1.equals(chocolate2)); //true

        // We have overridden the hashcode method so chocolate2 acts as
        // same key as chocolate1. If we hadn't, map.get(chocolate2) would return null
        // even though equals returns true.
        System.out.println(map.get(chocolate2)); // Yum a chocolate iceCream!
    }
}
