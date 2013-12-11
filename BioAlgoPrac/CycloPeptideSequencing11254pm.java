package assignments;

import java.util.Scanner;

/********
 * only works when the sequence is less than 10 proteins;
 * @author Yan
 *
 */

public class CycloPeptideSequencing11254pm {
	public static void main(String[] args){
		int MM = 92;
		int numOfPep = (int) Math.sqrt(MM) +1; // num of peptes are n*(n-1), here we got MM = (n-1) first;

		int[] nums = new int[MM]; 
		Scanner sc = new Scanner(System.in); 
		System.out.print("Enter "+MM+" numbers on one line with spaces: \n"); 
		sc.useDelimiter(" "); 
		for(int i = 0; i<nums.length; i++){ 
		nums[i] = sc.nextInt(); 
		} 
		
		sc.close();
		
		
		System.out.println("There are " + numOfPep + " peptides on the cyclopeptide ring.");
		for(int i=0; i<MM; i++){
			System.out.print(" " + nums[i]);
			if (i% (numOfPep-1) == 0)
				System.out.println();
		} // end of for i loop;
		System.out.println();
		
		
		int[][] Pep2D = new int[numOfPep][numOfPep];
		for(int i=1; i<numOfPep; i++){
				System.out.print(" i= " + i + " ");	
				
				for (int j=0; j< numOfPep; j++){
					Pep2D[i][j] = nums[(i-1)*numOfPep + j +1];
				
					System.out.print(Pep2D[i][j] + "  ");
				}
				System.out.println();
		} // end for i loop, build a 2D array Pep2D to store all a[1][0--9] till a[9][0--9]; 
		for(int i=1; i<numOfPep; i++){
			Pep2D[numOfPep-1][i] = nums[MM-1];
		}
		
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
		for(int i=0; i< numOfPep; i++){
			cycloPeptide[i] = new cycloPep1125();
			cycloPeptide[i].value = Pep2D[1][i];
			for(int k=0; k<20; k++){
				if(cycloPeptide[i].value == Proteins[k].MolWeight)
					cycloPeptide[i].name = Proteins[k].name;
			}
 
			cycloPeptide[i].next = i;
			cycloPeptide[i].before = i;
			
			System.out.println("cycloPeptide[" + i+"]. value=" + cycloPeptide[i].value +" name=" + cycloPeptide[i].name +" next=" + cycloPeptide[i].next +" before=" + cycloPeptide[i].before +". ");
		} 


		/************
		 * initial an array of strings from StrSequences[0] to StrSequences[9];
		 * StrSequences[0] will store all protein sequences in the mass spectrum with only 1 protein unit;
		 * StrSequences[1] will store all protein sequences in the mass spectrum with two protein units;
		 * etc, etc, etc, etc, etc;
		 * StrSequences[9] will store one chain of numOfPep protein sequence with all protein unites in the spectrum;
		 */
		String[] StrSequences = new String[numOfPep]; 
		for(int i=0; i<numOfPep; i++){
			StrSequences[i] = "";
	
		}
		StrSequences[0] += cycloPeptide[0].name +"*";
		
		System.out.println("StrSequences[0]=" + StrSequences[0]);
		
		int Len = 1;
		while(Len<numOfPep){
			
			System.out.print("(Length of the sequence) Len= " + Len);
			System.out.println(" Strseq[Len-1]= " + StrSequences[Len-1]);
			
			for(int stringIndex=0; stringIndex<StrSequences[Len-1].length(); stringIndex += (Len+1)){
				String subStr = StrSequences[Len-1].substring(stringIndex, stringIndex+Len);
			//	System.out.println("subStr= " + subStr +". ");

				for(int i=0; i<numOfPep; i++){
					
					/*****************
					 * have to make sure seqStr has no duplicate characters in it; 
					 * 
					 */
					String temp = "" + cycloPeptide[i].name;
					for(int k=0; k<subStr.length(); k++){
	
						if(subStr.substring(k, k+1).equals(temp))
							temp="";
					}
					
					String seqStr = subStr + temp;
					/**********
					 * hide substring here; 
					 */
					// System.out.println("subStr=" + subStr +" ");
					
					int Weight = getWeight(seqStr, cycloPeptide, Proteins);
							
					if(Len<numOfPep-1){
						for(int j=0; j<numOfPep; j++){
							if (Pep2D[Len+1][j] == Weight && !StrSequences[Len].contains(seqStr)){
								
								// Only when there's no duplicate string in the StrSequences[Len], will we save the seqStr into StrSequences[Len];
								StrSequences[Len] += (seqStr + "*"); 

								} // end of if condition; 
							} // end of for j loop;
						
						} else if(Len>=(numOfPep-1)){
							if(Weight == nums[MM-1]){
								StrSequences[Len] += (seqStr + "*");
							}
						} // end if (Len<9); 

				} // end of for i loop;
											
			} // end of for strings in the strSequences[Len-1];
			
			Len ++;
		} // end of while;
		
		System.out.println("nums[MM-1] = " + nums[MM-1]);
		
		System.out.println("StrSequences["+(numOfPep-1)+"]=" + StrSequences[numOfPep-1] +". "
							+ "\n There are only few sequences left. "
							+ "the last step is the check whether each sequence makes sense;");
		
		/***********
		 * check each sub sequence at the last StrSequences[9].
		 * To see which one meet all the Mass spectrum requirment;
		 */
		for(int i=0; i<StrSequences[numOfPep-1].length(); i+=(numOfPep+1)){
			String subStr = StrSequences[numOfPep-1].substring(i, i+numOfPep);
			
			int count = checkValid(subStr, Pep2D, numOfPep, cycloPeptide, Proteins);
			
			if (count>=(numOfPep*2-3)){ // like when numOfPep = 10, check count>=17)
				System.out.println("final subStr=" + subStr +". ");
				
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
				
			} // end if count>=17;

		} // end for i loop; end printing 10 sequence cycloPeptides;
		
		
		/*****
		 * now try to out put all possible sequences following "next" index:
		 */
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
		 */
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
		
		System.out.println("Game Over!");

	}// end of main();
	
	private static ProteinDic[] buildProteinDictionary() {

        // TO build the protein dictionary; with protein ID and related MolWeight;

        ProteinDic[] ProteinID = new ProteinDic[20];

        for(int i=0; i<20; i++){

               ProteinID[i] = new ProteinDic();

        }

       

        ProteinID[0].name = 'G';

        ProteinID[0].MolWeight = 57;

       

        ProteinID[1].name = 'A';

        ProteinID[1].MolWeight = 71;

       

        ProteinID[2].name = 'S';

        ProteinID[2].MolWeight = 87;

       

        ProteinID[3].name = 'P';

        ProteinID[3].MolWeight = 97;

       

        ProteinID[4].name = 'V';

        ProteinID[4].MolWeight = 99;

       

        ProteinID[5].name = 'T';

        ProteinID[5].MolWeight = 101;

       

        ProteinID[6].name = 'C';

        ProteinID[6].MolWeight = 103;

       

        ProteinID[7].name = 'I';

        ProteinID[7].MolWeight = 113;

       

        ProteinID[8].name = 'L';

        ProteinID[8].MolWeight = 113;

       

        ProteinID[9].name = 'N';

        ProteinID[9].MolWeight = 114;

       

        ProteinID[10].name = 'D';

        ProteinID[10].MolWeight = 115;

       

        ProteinID[11].name = 'K';

        ProteinID[11].MolWeight = 128;

       

        ProteinID[12].name = 'Q';

        ProteinID[12].MolWeight = 128;

                    

        ProteinID[13].name = 'E';

        ProteinID[13].MolWeight = 129;

       

        ProteinID[14].name = 'M';

        ProteinID[14].MolWeight = 131;

       

        ProteinID[15].name = 'H';

        ProteinID[15].MolWeight = 137;

       

        ProteinID[16].name = 'F';

        ProteinID[16].MolWeight = 147;

       

        ProteinID[17].name = 'R';

        ProteinID[17].MolWeight = 156;

       

        ProteinID[18].name = 'Y';

        ProteinID[18].MolWeight = 163;

       

        ProteinID[19].name = 'W';

        ProteinID[19].MolWeight = 186;

       

        return ProteinID;

 }

	private static int checkValid(String finalStr, int[][] pep2d, int numOfPep, cycloPep1125[] cycloPeptide, ProteinDic[] Proteins) {
		// TO check if the final sequence fit the mass spectrum requirement;
		int count2 = 0;
		int count3 = 0;
		for(int i=0; i<finalStr.length()-1; i++){

			String sub2 = finalStr.substring(i, i+2);
			// System.out.print(" " + sub2);
			int subWei = getWeight(sub2, cycloPeptide, Proteins);
			for(int j=0; j<numOfPep; j++){
				if(subWei == pep2d[2][j])
					count2++;
			}
			
		} // end for i loop;
		
		for(int i=0; i<finalStr.length()-2; i++){

			String sub3 = finalStr.substring(i, i+3);
			//System.out.print(" " + sub3);
			int subWei = getWeight(sub3, cycloPeptide, Proteins);
			for(int j=0; j<numOfPep; j++){
				if(subWei == pep2d[3][j])
					count3++;
			}
			
		} // end for i loop;

		return count2+count3;
	}

	private static int getWeight(String subStr, cycloPep1125[] cycloPeptide, ProteinDic[] proteins) {
		// TO get the molecular weight of a certain protein sequences;
		int MolWeight=0;
		
		for(int i=0; i<subStr.length(); i++){
			for(int j=0; j<20; j++){
				if(proteins[j].name == cycloPeptide[i].name)
					MolWeight += proteins[j].MolWeight;
			}
			// System.out.println("subStr.charAt=" + subStr.charAt(i) + ". subStr.numic=" + Character.getNumericValue(subStr.charAt(i)) +". MolWei=" + MolWeight +". ");
			//MolWeight += cycloPeptide[Character.getNumericValue(subStr.charAt(i))].value;
		
		}
		
		return MolWeight;
	}

}