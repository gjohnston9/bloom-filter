import java.util.BitSet;

public class BloomFilter {
	
	static String[] names = {"Willard", "grik", "Charles", "I am awesome at programming"};   // list of names
	static int n = names.length;
	static double m = Math.ceil((Math.log(0.001)*n)/(Math.log(2)*Math.log(0.5)));    // bitset length
	static double k = Math.ceil(Math.log(2)*m/n);   // number of hash functions
	
	static BitSet bloomFilter = new BitSet();				 // the list of trues or falses
	
	public static void main(String[] args) {
		
		System.out.println(String.format("n (number of elements) is %d.", n));
		System.out.println(String.format("m (the bloom filter's length) is %d.", (int) m));
		System.out.println(String.format("k (the number of hash functions used) is %d.", (int) k));
		
		// adding names to the array
		for(int i = 0; i < names.length; i++) {
			for(int j = 0; j <= k; j++) {
				add(names[i], j);
			}
		}
	
	// the useful part
		String query = "";
		boolean yesno = contains(query);
		if (yesno == true) {
			double falsePositiveProbability = Math.pow(1-Math.exp(-k*n/m), k); 
			System.out.println(String.format("%s is probably an element of the set.", query));
			System.out.println(String.format("false positive rate: %.7f", falsePositiveProbability));
		}
		
		else {
			System.out.println(String.format("%s is not an element of the set.", query));
		}
		
		}
	
	public static void add(String name, int seed) {
		double i = Math.abs(murmurhash3x8632(name.getBytes(), 0, name.getBytes().length, seed));
		i = i % m;
		
		bloomFilter.set((int) i);
		}
	
	public static boolean contains(String name) {
		for(int j = 0; j <= k; j++) {
			double i = Math.abs(murmurhash3x8632(name.getBytes(), 0, name.getBytes().length, j));
			i = i % m;
			
			if(bloomFilter.get((int) i) == false) {
				return false;
			}
		}
		return true;
	}

		
	/** Returns the MurmurHash3_x86_32 hash. */
	  public static int murmurhash3x8632(byte[] data, int offset, int len, int seed) {

	    int c1 = 0xcc9e2d51;
	    int c2 = 0x1b873593;

	    int h1 = seed;
	    int roundedEnd = offset + (len & 0xfffffffc);  // round down to 4 byte block

	    for (int i = offset; i < roundedEnd; i += 4) {
	      // little endian load order
	      int k1 = (data[i] & 0xff) | ((data[i + 1] & 0xff) << 8) | ((data[i + 2] & 0xff) << 16) | (data[i + 3] << 24);
	      k1 *= c1;
	      k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
	      k1 *= c2;

	      h1 ^= k1;
	      h1 = (h1 << 13) | (h1 >>> 19);  // ROTL32(h1,13);
	      h1 = h1 * 5 + 0xe6546b64;
	    }

	    // tail
	    int k1 = 0;

	    switch(len & 0x03) {
	      case 3:
	        k1 = (data[roundedEnd + 2] & 0xff) << 16;
	        // fallthrough
	      case 2:
	        k1 |= (data[roundedEnd + 1] & 0xff) << 8;
	        // fallthrough
	      case 1:
	        k1 |= data[roundedEnd] & 0xff;
	        k1 *= c1;
	        k1 = (k1 << 15) | (k1 >>> 17);  // ROTL32(k1,15);
	        k1 *= c2;
	        h1 ^= k1;
	      default:
	    }

	    // finalization
	    h1 ^= len;

	    // fmix(h1);
	    h1 ^= h1 >>> 16;
	    h1 *= 0x85ebca6b;
	    h1 ^= h1 >>> 13;
	    h1 *= 0xc2b2ae35;
	    h1 ^= h1 >>> 16;

	    return h1;
	  }
}
	


