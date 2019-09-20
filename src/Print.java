
public class Print extends MetaData{ //mostrar na tela
	public void printTape(){ //mostra a fita e a posicao em que a cabeca se encontra
		System.out.println();

		for(int i=0;i<wordLength+blankSpacesToPrintAfterWord||i<headPosition+blankSpacesToPrintAfterWord;i++){
			System.out.print(tape[i]+" ");
		}
		System.out.println("\n");

		for(int i=0;i<headPosition*2;i++){
			System.out.print(" ");
		}
		System.out.println("^");
	}

	public void printTransitionFunctionTable(int state, int symbol){ //mostra a tabela de funcao de transicao inteira
		int horizontal=transitionFunction[0].length*10+6;

		System.out.println();
		if(symbol<transitionFunction[0].length){
			for(int i=0;i<(symbol+1)*10+3;i++){
				System.out.print(" ");
			}
			System.out.print("v");
		}

		System.out.print("\n\n        ");
		for(int i=0;i<transitionFunction[0].length;i++){
			System.out.print("|    "+inputAndAuxiliaryAlphabet[i]+"    ");
		}
		System.out.println("|");

		for(int i=0;i<transitionFunction.length;i++){
			System.out.print("   ");
			for(int j=0;j<horizontal;j++){
				System.out.print("-");
			}

			if(i==state){
				System.out.print("\n >");
			}
			else{
				System.out.print("\n  ");
			}
			System.out.print("  "+String.format("%03d", i)+" ");
			for(int j=0;j<transitionFunction[0].length;j++){
				if(!transitionFunction[i][j].getTransition()){
					System.out.print("|    X    ");
				}
				else{
					System.out.print("| "+String.format("%03d", transitionFunction[i][j].getNextState())+","+transitionFunction[i][j].getNewTapeSymbol()+",");

					if(transitionFunction[i][j].getHeadDirection()==-1){
						System.out.print("E ");
					}
					else if(transitionFunction[i][j].getHeadDirection()==1){
						System.out.print("D ");
					}
				}
			}

			System.out.println("|");
		}

		System.out.print("   ");
		for(int i=0;i<horizontal;i++){
			System.out.print("-");
		}
		System.out.println("\n");
	}
}
