/**
 * This is a solution to Codility Argon 2014 Trek and Swim problem.
 * https://codility.com/programmers/task/trek_and_swim/
 * 
 * Find a longest slice of a binary array that can be split into two parts:
 * in the left part, 0 should be the leader; in the right part, 1 should be the 
 * leader.
 * 
 * The solution is as follows. Find the section starting with the first 0
 * and ending with the last one. All solutions will include this sequence depending
 * on whether you can find a split point in which you can add 1s from the 
 * left and 0s from the right to satisfy the left and right conditions. Left
 * condition is that the left sequence has strictly more 0s, and the right
 * condition is that the right sequence has strictly more 1s. 
 * 
 * @author Dr. Polar Humenn
 *
 */
public class Solution {
    
	/**
	 * Find the index of the first 0 of the array;
	 * @param A
	 * @return the index of the first 0 in the array;
	 */
    int first0(int[] A) {
    	for(int i = 0; i < A.length; i++) {
    		if (A[i] == 0) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * Find the index of the last 1 of the array.
     * @param A
     * @return the index of the last 1 of the array.
     */
    int last1(int[] A) {
    	for(int i = A.length - 1; i >= 0; i--) {
    		if (A[i] == 1) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * Form an array that counts up the number of 0s from first to last.
     * initB0(2, 5, [1, 1, 0, 0, 1, 1, 0, 0]) 
     *      =>            [1, 2, 2, 2]
     * @param first
     * @param last
     * @param A
     * @return
     */
    int[] initB0(int first, int last, int[] A) {
    	int[] b0 = new int[last-first+1];
    	int t0 = 0;
    	for(int i = 0; i < last-first+1; i++) {
    		b0[i] = A[first + i] == 0 ? ++t0: t0;
    	}
    	return b0;
    }
    
    /**
     * Given a particular split point, based on B0, find the number of left 1s
     * needed to satisfy the left condition more than half 0s. 
     * That is, for a split point at i for the section before we 
     * can add 1s until just before the number of zeros contained therein. 
     * It is the difference of 0s and 1s at i minus one.
     * 
     * @param b0
     * @param i
     * @return
     */
    int left1(int[] b0, int i) {
    	// We need the difference of 0s and 1s at i.
    	return Math.max(0,num0s(b0,i) - num1s(b0,i) - 1);
    }
    
    /**
     * Given a particular split point, based on B0, find the number of right 0s
     * needed to satisfy the right condition of more than half 1s on the right. 
     * That is, for a split point at i for the section after, we may add 0s 
     * until just before the number of 1s contained there in. 
     * It is the difference of 1s and 0s, in the reverse order, at i minus 1 in 
     * B0. 
     * 
     * What is meant by reverse order, is that there is an similar array B1 
     * counting 1s from the left. 
     * We don't produce that array, just use the index calculating functions.
     * 
     * @param b0
     * @param i
     * @return
     */
    int right0(int[] b0, int i) {
    	return Math.max(0,(num1sr(b0, i) - num0sr(b0, i) - 1));
    }
    
    /**
     * Return the number of 0s at i, using B0, which is traveling right.
     * 
     * @param b0
     * @param i
     * @return
     */
    int num0s(int[] b0, int i) {
    	//       A  = [ 0, 0, 1, 1 ]
    	// b0(0,3)  = [ 1, 2, 2, 2 ]
    	return b0[i];
    }
    
    /**
     * Return the number of 1s at i using B0, which is traveling right.
     * 
     * @param b0
     * @param i
     * @return
     */
    int num1s(int[] b0, int i) {
    	// This is essentially based on num0s, since  
    	//       A  = [ 0, 0, 1, 1 ]
    	// b0(0,3)  = [ 1, 2, 2, 2 ]
    	//    num0s = [ 1, 2, 2, 2 ]
    	//    num1s = [ 0, 0, 1, 2 ]
    	return (i+1) - num0s(b0, i);
    }
    
    /**
     * Based on B0, find the number of 0s, traveling left.
     *       A  = [ 0, 0, 1, 1 ]
     *  b0(0,3) = [ 1, 2, 2, 2 ]
     *   num0sr = [ 2, 1, 0, 0 ]
     * 
     * @param b0
     * @param i
     * @return
     */
    int num0sr(int[] b0, int i) {
    	// This is essentially based on num0s, since  
    	//       A  = [ 0, 0, 1, 1 ]
    	// b0(0,3)  = [ 1, 2, 2, 2 ]
    	//    num0s = [ 1, 2, 2, 2 ]
    	//    num1s = [ 0, 0, 1, 2 ]
    	//   num1sr = [ 2, 2, 2, 1 ]
    	//   num0sr = [ 2, 1, 0, 0 ]
    	return (b0.length - i) - num1sr(b0, i);
    }
    
    /**
     * Based on B0, find the number of 1s, traveling left.
     *       A  = [ 0, 0, 1, 1 ]
     *  b0(0,3) = [ 1, 2, 2, 2 ]
     *   num1sr = [ 2, 2, 2, 1 ]
     * 
     * @param b0
     * @param i
     * @return
     */
    int num1sr(int[] b0,int i) {
    	if (i == 0) {
    		return num1s(b0, b0.length-1);
    	}
    	return num1s(b0,b0.length-1) - num1s(b0,i-1);
    }

    int solution(int[] A) {
    	// Find the first 0 in the array.
    	int first = first0(A);
    	
    	// Short circuit
    	if (first < 0) {
    		return 0;
    	}
    	
    	// Find the last 1 in the array;
    	int last = last1(A);
    	
    	// Short circuit
    	if (last < 0) {
    		return 0;
    	}
    	
    	// Create the scan array that counts the zeros. 
    	int[] b0 = initB0(first, last, A);
    	
    	// The number of left 1s is the index of the first 0.
    	int left1s = first;
    	
    	// The number of 0s to the right is the last to the length;
    	int right0s = A.length - last - 1;
    	
    	// Find the split point by traversing down the right and testing
    	// how many 1s to take from the left and how many 0s to take from
    	// the right to meet the condition, and find the maximum.
    	int max = b0.length;
    	for(int i = 0; i < b0.length-1; i++) {
    		
    		// Find the number of left 1s available to take from at this split
    		// point.
    		int l1s = Math.min(left1s, left1(b0, i));
    		
    		// Find the number of right 0s available to take from at this split
    		// point.
    		int r0s = Math.min(right0s, right0(b0, i+1));
    		
    		// Are we the maximum?
    		max = Math.max(max, b0.length + l1s + r0s);
    		
    		// Short circuit.
    		if (max == A.length) {
    			return max;
    		}
    	}
    	return max;
    }
	
	static void print(String name, int[] array) {
		System.out.print(String.format("%s[%d] = [", name, array.length));
		for(int a : array) {
			System.out.print(String.format("%d,",a));
		}
		System.out.println();
	}

	
	public static void main(String[] argv) {
		int a[] = {1, 1, 0, 0, 1, 1, 0, 0 };
		print("case 1", a);
		System.out.println(String.format("answer = %d", new Solution().solution(a)));
	}
}
