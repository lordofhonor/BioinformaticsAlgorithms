package assignments;


/************************************8
 * CODE CHALLENGE: Implement CYCLOPEPTIDESEQUENCING (pseudocode reproduced below).
 * Note: After the failure of the first brute-force algorithm we considered, 
 * you may be hesitant to implement this algorithm for fear that its runtime will be prohibitive. The potential problem with CYCLOPEPTIDESEQUENCING is that it may generate incorrect k-mers at intermediate stages (i.e., k-mers that are not subpeptides of a correct solution). You may wish to wait to implement CYCLOPEPTIDESEQUENCING until after the next section, where we will analyze this algorithm.
 *     CYCLOPEPTIDESEQUENCING(Spectrum)
 *     List �� {0-peptide}
 *     while List is nonempty
 *     	List �� Expand(List)
 *     		for each peptide Peptide in List
 *           if Cyclospectrum(Peptide) = Spectrum
 *                        output Peptide
 *                               remove Peptide from List
 *              else if Peptide is not consistent with Spectrum
 *           	       remove Peptide from List
 *           
 *   Sample Input:
 *   0 113 128 186 241 299 314 427
 *   
 *   Sample Output:
 *   186-128-113 186-113-128 128-186-113 128-113-186 113-186-128 113-128-186
 *   
 */
import java.util.Scanner;

/*******************************************************
 * Input a group of Mass Spectrum peaks; store them into an array;
 * 1st compare with the dictionary; figure out how many proteins are there; 
 * 2nd 
 * only works when the sequence is less than 10 proteins;
 * @author Frog
 *
 */

public class CycloPeptideSequencing1126 {
	public static void main(String[] args){
		
		// System.out.println("How many Integers are there in the document?");
		int MM = 112;
		int numOfPep = (int) Math.sqrt(MM-2) +1; // num of peptes are n*(n-1), here we got MM = (n-1) first;
		// Here we know there are 10 peptide unites in the cycloPeptide; 

		int[] Peaks = new int[MM]; 
		Scanner sc = new Scanner(System.in); 
		System.out.print("Enter "+MM+" numbers on one line with spaces: \n"); 
		
		sc.useDelimiter(" "); 
		for(int i = 0; i<Peaks.length; i++){ 
		Peaks[i] = sc.nextInt(); 
		} 
		
		sc.close();
		
		
		System.out.println("There are " + numOfPep + " peptides on the cyclopeptide ring.");
		for(int i=0; i<MM; i++){
			System.out.print(" " + Peaks[i]);
			if (i% (numOfPep-1) == 0)
				System.out.println();
		} // end of for i loop;
		
		System.out.println(" OK 1st part.\n");
		
		
		/***********************
		 * build the dictionary of proteins and their molecular weight;
		 */
		
		ProteinDic[] Proteins = buildProteinDictionary();
		
		/*******************************
		 * build a new CIRCLE of cycloPeptide[] objects, 
		 * to store the MolWeight of each peptide on the cycloPeptide;
		 * cycloPeptide[i].right and cycloPeptide[i].left both pointing to itself; 
		 */
		cycloPep1125[] cycloPeptide = new cycloPep1125[numOfPep]; // define a new array to story the cycloPeptide; 
		
		int cycle = 0;
		for(int i=0; i< MM; i++){
			for(int j=0; j<20; j++){
				if (Proteins[j].MolWeight == Peaks[i]){
					
					System.out.print("One Match, " + Peaks[i] +". cycle=" + cycle + ". ");
					cycloPeptide[cycle] = new cycloPep1125();
					cycloPeptide[cycle].name = Proteins[j].name;
					cycloPeptide[cycle].value = Proteins[j].MolWeight;
					cycloPeptide[cycle].next = cycle;
					cycloPeptide[cycle].before = cycle;
					System.out.println("cycloPeptide[" +cycle+"]. value=" + cycloPeptide[cycle].value +" name=" + cycloPeptide[cycle].name +" next=" + cycloPeptide[cycle].next +" before=" + cycloPeptide[cycle].before +". ");
					cycle++;
					break; // this break is very necessary; 
				} // end if Proteins[].MolWeight == Peaks[];
			} // end for j loop;
		} // end for i loop; 
		
		System.out.println("All cycloPeptides built. The second part is OK.");

		/************
		 * initial an array of strings from StrSequences[0] to StrSequences[9];
		 * StrSequences[0] will store all protein sequences in the mass spectrum with only 1 protein unit;
		 * StrSequences[1] will store all protein sequences in the mass spectrum with two protein units;
		 * etc, etc, etc, etc, etc;
		 * StrSequences[9] will store one chain of numOfPep protein sequence with all protein unites in the spectrum;
		 */
		String[] StrSequences = new String[numOfPep];  // build numOfPep string[] arrays.
		for(int i=0; i<numOfPep; i++){
			
			StrSequences[i] = "";
		} // end for i loop; at the beginning, assign every string[] array "", empty. 
		
		
		for(int i=0; i<numOfPep; i++){
			StrSequences[0] += cycloPeptide[i].name + "*";
		}
		//	StrSequences[0] += cycloPeptide[0].name +"*"; // make the StrSequences[0] = cycloPeptide[0].name;
		
		System.out.println(" StrSequences[0] = " + StrSequences[0]);
		
		int Len = 1;
		while(Len<numOfPep){
			
			//System.out.print("(Length of the sequence) Len= " + Len); // everything is ok till Len = 8;
			//System.out.println(" Strseq[Len-1]= " + StrSequences[Len-1]);
			
			for(int stringIndex=0; stringIndex<StrSequences[Len-1].length(); stringIndex += (Len+1)){
				String subStr = StrSequences[Len-1].substring(stringIndex, stringIndex+Len);
			//	System.out.println("subStr= " + subStr +". ");

				for(int i=0; i<numOfPep; i++){
					
					String seqStr =subStr + cycloPeptide[i].name; // make the first part of seqStr = subStr (the old sequence of Len-1);

					int Weight = getWeight(seqStr, Proteins);
					//System.out.print(" Weight= " + Weight +" seqStr=" + seqStr +";  ");
					
					for(int j=0; j<MM; j++){
						if (Peaks[j] == Weight && !StrSequences[Len].contains(seqStr)){	
							// Only when there's no duplicate string in the StrSequences[Len], will we save the seqStr into StrSequences[Len];
							if( checkValid(seqStr, Peaks, MM, cycloPeptide, Proteins))
								StrSequences[Len] += (seqStr + "*"); 
								
							} // end of if condition; 
							
						} // end of for j loop;
					
				} // end of for i loop;
											
			} // end of for strings in the strSequences[Len-1];
			System.out.println(" StrSequences["+Len+"] = " + StrSequences[Len]);
			Len ++;
		} // end of while;
		System.out.println("after while loop Len=" + Len);
		
		//System.out.println("\nStrSequences["+(numOfPep-1)+"]=" + StrSequences[numOfPep-1] +". "
		//					+ "the last step is the check whether each sequence makes sense;");
		
		/***********
		 * check each sub sequence at the last StrSequences[9].
		 * To see which one meet all the Mass spectrum requirment;
		 * Final String: PHAKPAIIVHA. 
		 * Mass: 97-137-71-128-97-71-113-113-99-137-71 total weight is: 1134
		 * Final String: PHAKPSPIVHA. 
		 * Mass: 97-137-71-128-97-87-97-113-99-137-71 total weight is: 1134
		 * Sometimes, the coinsicent just happens, like the two subStrings upperl
		 * they have exactly the same MolWeight, and they both meet the validCheck() method;
		 * So here we have to add a final check step for the final strings:
		 * to see if every cycloPeptide is here in the final strings;
		 */
		for(int i=0; i<StrSequences[numOfPep-1].length(); i+=(numOfPep+1)){
			String subStr = StrSequences[numOfPep-1].substring(i, i+numOfPep);
			boolean lastCheck = false;
			lastCheck = finalCheck(subStr, cycloPeptide);
			if(lastCheck){
				System.out.print("\nFinal String: " + subStr +". Mass: ");
				transSeqtoMass(subStr, Proteins);
			} // end if(fCheck);
			
		} // end for i<StrSequence[numOfPep-1].length loop; 	
			
		/********
			int totalWeight = getWeight(subStr, Proteins);
			System.out.println(" total weight is: " + totalWeight);
			/**********
			boolean Check = checkValid(subStr, Peaks ,MM , cycloPeptide, Proteins);
			
			if (Check){ // like when numOfPep = 10, check count>=17)
				System.out.println("Final Full String: " + subStr +".");
				/*****
				int last = 0;
				for(int m=0; m<subStr.length()-1; m++){
					int index = Character.getNumericValue(subStr.charAt(m));
					int next = Character.getNumericValue(subStr.charAt(m+1));
					cycloPeptide[index].next = next;
					cycloPeptide[next].before = index;
					last = next;
					
					System.out.println("cycloPeptide[" + index +"].next = " + cycloPeptide[index].next +". before=" +cycloPeptide[index].before);
				}
				cycloPeptide[last].next = Character.getNumericValue(subStr.charAt(0));
				cycloPeptide[Character.getNumericValue(subStr.charAt(0))].before = last;
				
				System.out.println("cycloPeptide["+last +"].next = " + cycloPeptide[last].next +". befor=" +cycloPeptide[last].before);
				
				System.out.println("OK, now the cycloPeptide is closed with full elements!");
				//break; // we just need one sequence here in this step;
				
				*
			} // end if count>=17;
			*
		
		/*****
		 * now try to out put all possible sequences following "next" index:
		 *
		for(int i=0; i<numOfPep; i++){
			int circle =0;
			int index = i;
			int last = i;
			while(circle<(numOfPep-1)){
				
				System.out.print(cycloPeptide[index].value +"-");
				last = index = cycloPeptide[index].next;
				circle++;
			} // end while;
			System.out.print(cycloPeptide[last].value +" ");

		} // end for loop;
		
		// System.out.println();
		/*****
		 * now try to out put all possible sequences following "before" index:
		 *
		for(int i=0; i<numOfPep; i++){
			int circle =0;
			int index = i;
			int before = i;
			while(circle<(numOfPep-1)){
				
				System.out.print(cycloPeptide[index].value +"-");
				before = index = cycloPeptide[index].before;
				circle++;
			} // end while;
			System.out.print(cycloPeptide[before].value +" ");

		} // end for loop;
		*/
		
		System.out.println("\nGame Over!");

	}// end of main();
	
	private static boolean finalCheck(String subStr, cycloPep1125[] cycloPeptide) {
		// TO see if every cycloPeptide[i] is in the sequence;
		int Leng = cycloPeptide.length;
		int strLen = subStr.length();
		int count =0;
		
		for(int i=0; i<Leng; i++){
			
			for(int j=0; j<strLen; j++){
				
				if(subStr.charAt(j) == cycloPeptide[i].name){
					count++;
					break;
				}
			} // end for (j < strLen) loop;
		} // end for (i<Leng) loop;
		
		// System.out.println("count=" + count + " Leng=" + Leng + " subStr.Length=" + strLen);
		if(count < Leng) {
			return false;
		} else return true;
		
	} // enf finalCheck() method;

	private static void transSeqtoMass(String subStr, ProteinDic[] Proteins) {
		// TO transfer a sequence of amino acids into a Mass sequence;
		int Length = subStr.length();
		for(int i=0; i<Length-1; i++){
			for(int j=0; j<20; j++){
				if(subStr.charAt(i) == Proteins[j].name){
					System.out.print(Proteins[j].MolWeight + "-");
					break;
				}
				
			} // end for j<20 loop
		} // end for i<Length loop
		for(int j=0; j<20; j++){
			if(subStr.charAt(Length-1) == Proteins[j].name){
				System.out.print(Proteins[j].MolWeight);
			}
		}
		
	} // end transSeqtoMass() method;

	private static ProteinDic[] buildProteinDictionary() {
        // TO build the protein dictionary; with protein ID and related MolWeight;
		/*******
		 * As we know there are 20 natural amino acids;
		 * so we build an object array ProteinDic[20], each represents an amino acid;
		 * input all name and molecular weight for each ProteinDic[i];
		 * finish the dictionary;
		 */
		
        ProteinDic[] ProteinID = new ProteinDic[20];
        for(int i=0; i<20; i++){
               ProteinID[i] = new ProteinDic();
        }   
        ProteinID[0].name = 'G';        ProteinID[0].MolWeight = 57; 
        ProteinID[1].name = 'A';        ProteinID[1].MolWeight = 71;     
        ProteinID[2].name = 'S';        ProteinID[2].MolWeight = 87;     
        ProteinID[3].name = 'P';        ProteinID[3].MolWeight = 97;
        ProteinID[4].name = 'V';        ProteinID[4].MolWeight = 99;
        ProteinID[5].name = 'T';        ProteinID[5].MolWeight = 101;
        ProteinID[6].name = 'C';        ProteinID[6].MolWeight = 103;
        ProteinID[7].name = 'I';        ProteinID[7].MolWeight = 113;
        ProteinID[8].name = 'L';        ProteinID[8].MolWeight = 113;
        ProteinID[9].name = 'N';        ProteinID[9].MolWeight = 114;
        ProteinID[10].name = 'D';       ProteinID[10].MolWeight = 115;
        ProteinID[11].name = 'K';       ProteinID[11].MolWeight = 128;
        ProteinID[12].name = 'Q';       ProteinID[12].MolWeight = 128;
        ProteinID[13].name = 'E';       ProteinID[13].MolWeight = 129;
        ProteinID[14].name = 'M';       ProteinID[14].MolWeight = 131;
        ProteinID[15].name = 'H';       ProteinID[15].MolWeight = 137;
        ProteinID[16].name = 'F';       ProteinID[16].MolWeight = 147;
        ProteinID[17].name = 'R';       ProteinID[17].MolWeight = 156;
        ProteinID[18].name = 'Y';       ProteinID[18].MolWeight = 163;
        ProteinID[19].name = 'W';       ProteinID[19].MolWeight = 186;
        
        return ProteinID;
        
 } // end private static ProteinDic[];

	private static boolean checkValid(String testStr, int[] Peaks, int MM, cycloPep1125[] cycloPeptide, ProteinDic[] Proteins) {
		/**************
		 * 1st, get the length of the string to test; assign it to Length;
		 * 2nd, get subStrings, from the Length-1 till 2, we do not need to check 1 unit and the whole unit;
		 * 3rd, for each length of the subString, there might be (Length-subStr.length +1) different subStrings;
		 *      Ex: for a 10-unit testString, there might be 2 9-unit substrings; 3 8-unit substrings;
		 *      untill 9 2-unit substrings;
		 */		
		
		int Length = testStr.length();
		// after we get the length of the string; we should test all subSequences from that testStr;

		int subLen = Length-1;
		// boolean check=false;
		
		while(subLen>1){
			int count=0; // each subString should have a match from the mass spectrum;
			
			for(int i=0; i<Length-subLen; i++){
				String tempStr = testStr.substring(i, i+subLen);
				int tempWeight = getWeight(tempStr, Proteins);
				for(int j=0; j<MM; j++){
					if(tempWeight == Peaks[j])
						count++;
				} // end for (j<MM) loop;
				
			}// end for(i<Length-subLen) loop;
			if (count < (Length-subLen)){
				return false;
			}
			
			subLen--;
		} // end while (subLen>1); 
		
		return true;
	}

	private static int getWeight(String subStr, ProteinDic[] proteins) {
		// TO get the molecular weight of a certain protein sequences;
		/******************
		 * 1st, get each protein on the sequence;
		 * 2nd, compare to the protein-dictionary, to get the molecular weight of each unit;
		 * 3rd, calculate and sum up all protein unit's MolWeight;
		 * 4th, return that sum.
		 */
		int MolWeight=0;
		int Length = subStr.length();
		for(int i=0; i<Length; i++){
			// use ProteinUnite to get each protein on the sequence;
			char ProteinUnite = subStr.charAt(i);
			for(int j=0; j<20; j++){
				//compare the protein to the protein-dictionary; to get the molecular weight of that protein unite;
				if(proteins[j].name == ProteinUnite)
					MolWeight += proteins[j].MolWeight;
			} // end for j<20 loop;
		} // end for i<subStr.length() loop;
		
		return MolWeight;
	} // end getWeight() method;

} // :) end of the whole class;
