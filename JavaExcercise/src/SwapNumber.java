import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
@author Ravi
You will be given a number and you can swap the only adjacent (side by side) digit of a number.
Now you are given with a number n and a value k. With maximum k number of swaps whats the biggest number that you can form?
Eg. n = 12934, k = 3
swap 9 with 2 - 19234
swap 9 with 1 - 91234
swap 2 with 1 - 92134 */

public class SwapNumber {

	public static String swapAdjecent(Long x, int k){
		List<Integer> op = Arrays.stream(x.toString().split("\\B"))
		 .map(s->Integer.valueOf(s))
		 .collect(toList());
			 
		return op.stream().map(e -> e.toString()).reduce("", String::concat);
	}
	
	private void swapAdjRec(List<Integer> op, int k, int max){
		for (int i = 0 ; i< op.size()-1; i++) {
			for(int j = i+1; j<op.size() ; j++){
				if(op.get(i) < op.get(j)){
					Collections.swap(op, i, j);
				}
			}
		}
	}
	public static void main(String[] args) {
		System.out.println(SwapNumber.swapAdjecent(12934L, 3));

	}

}
