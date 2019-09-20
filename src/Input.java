import java.util.Scanner;

public class Input extends MetaData{ //recebe todas as entradas do usuario
	Scanner in=new Scanner(System.in);
	Print out=new Print();
	Check verify=new Check();
	boolean emptyStringFound=false;
	boolean duplicateSymbol=false;
	boolean validInputChoice=false;
	boolean showTable=false;
	String auxiliaryAlphabet;

	private boolean getValidInputChoice(){ //simples "sim ou nao" codigo, para ser usado por outros metodos
		String inputString;
		boolean decision=false;

		do{
			inputString=in.nextLine();
			emptyStringFound=verify.checkEmptyString(inputString);

			if(!emptyStringFound){
				char inputChar=inputString.charAt(0);

				if(inputChar=='s'||inputChar=='S'){
					validInputChoice=true;
					decision=true;
				}
				else if(inputChar=='n'||inputChar=='N'){
					validInputChoice=true;
					decision=false;
				}
				else{
					validInputChoice=false;
					System.out.print("Favor digitar apenas \"s\" para sim, ou \"n\" para nao: ");
				}
			}
		}while(emptyStringFound||!validInputChoice);

		return decision;
	}

	public boolean getStopMachineChoice(){ //pergunta se o usuario quer continuar, se o limite de transicoes foi atingido
		System.out.print(" "+TRANSITION_LIMIT+" transicoes ate o momento, gostaria de continuar?\nDigite \"s\" ou \"n\": ");

		return !getValidInputChoice();
	}

	public int getMenuChoice(){ //menu que aparece apos palavras serem testadas
		int menuChoice=0;

		do{
			System.out.println("\n 0 - Fechar o programa;\n 1 - Continuar testando palavras;\n 2 - Mudar a opcao de mostrar a fita;\n 3 - Mudar alguma funcao de transicao;");
			System.out.print(" 4 - Mudar o estado inicial;\n 5 - Mudar os estados finais.\n\nFavor digitar uma opcao: ");

			while(!in.hasNextInt()){
				in.next();
			}
			menuChoice=in.nextInt();
			in.nextLine();
		}while(menuChoice<0||menuChoice>5);

		return menuChoice;
	}

	public void getChangeTransitionFunctionChoice(){ //pergunta qual transicao o usuario deseja mudar
		int stateToChange=0;
		int symbolToChangeInt=0;

		out.printTransitionFunctionTable(transitionFunction.length, transitionFunction[0].length);
		System.out.println();

		do{
			System.out.print("Favor digitar o estado que deseja mudar: ");

			while(!in.hasNextInt()){
				in.next();
			}
			stateToChange=in.nextInt();
			in.nextLine();
		}while(stateToChange<0||stateToChange>=amountOfStates);

		do{
			String localSymbolToChangeString;
			char localSymbolToChangeChar;

			System.out.print("Favor digitar o simbolo que deseja mudar (presente no alfabeto): ");
			localSymbolToChangeString=in.nextLine();

			emptyStringFound=verify.checkEmptyString(localSymbolToChangeString);
			if(!emptyStringFound){
				localSymbolToChangeChar=localSymbolToChangeString.charAt(0);
				validInputChoice=verify.checkCharInCharArray(inputAndAuxiliaryAlphabet, localSymbolToChangeChar); //verifica se o simbolo que o usuario digitou esta presente nos alfabetos

				if(validInputChoice){
					for(int i=0;i<inputAndAuxiliaryAlphabet.length;i++){
						if(localSymbolToChangeChar==inputAndAuxiliaryAlphabet[i]){ //procura o indice do simbolo que o usuario deseja mudar
							symbolToChangeInt=i;
						}
					}
				}
			}
		}while(emptyStringFound||!validInputChoice);

		getEachTransitionFunctionInfo(stateToChange, symbolToChangeInt); //passa o resultado para o metodo que alimenta a tabela de funcao de transicao

		if(showTable){
			out.printTransitionFunctionTable(transitionFunction.length, transitionFunction[0].length);
			System.out.println();
		}
	}

	public void getShowTapeChoice(){ //pergunta se o usuario quer ver a fita durante o funcionamento da maquina
		System.out.print("Gostaria de ver a fita durante o funcionamento da Maquina?\nDigite \"s\" ou \"n\": ");

		showTape=getValidInputChoice();
	}

	public void getTapeStartSymbol(){ //coleta o simbolo de inicio da fita
		String localStartSymbol;

		do{
			System.out.print("Favor digitar o simbolo de inicio da fita (por exemplo: \"<\"): ");
			localStartSymbol=in.nextLine();

			emptyStringFound=verify.checkEmptyString(localStartSymbol);
			if(!emptyStringFound){
				startSymbol=localStartSymbol.charAt(0);
			}
		}while(emptyStringFound);
	}

	public void getBlankSymbol(){ //coleta o simbolo que representa "espacos em branco" na fita
		String localBlankSymbol;

		do{
			System.out.print("Favor digitar o simbolo que representa \"espacos em branco\" na fita (por exemplo: \"_\"): ");
			localBlankSymbol=in.nextLine();

			emptyStringFound=verify.checkEmptyString(localBlankSymbol);
			if(!emptyStringFound){
				if(startSymbol!=localBlankSymbol.charAt(0)){
					duplicateSymbol=false;
					blankSymbol=localBlankSymbol.charAt(0);
				}
				else{
					duplicateSymbol=true;
					System.out.println("O simbolo precisa ser diferente do simbolo de inicio da fita.");
				}
			}
		}while(emptyStringFound||duplicateSymbol);
	}

	public void getInputAndAuxiliaryAlphabet(){ //coleta o alfabeto de entrada e alfabeto auxiliar
		String localInputAlphabet;
		String localAuxiliaryAlphabet;

		do{ //coleta o alfabeto de entrada
			System.out.print("Favor digitar o alfabeto de entrada: ");
			localInputAlphabet=in.nextLine();

			emptyStringFound=verify.checkEmptyString(localInputAlphabet);
			duplicateSymbol=verify.checkDuplicateSymbolTwoStrings(Character.toString(startSymbol), localInputAlphabet);

			if(duplicateSymbol){
				System.out.println("\""+startSymbol+"\" nao pode estar presente no alfabeto de entrada.");
			}
			else if(!duplicateSymbol){
				duplicateSymbol=verify.checkDuplicateSymbolTwoStrings(Character.toString(blankSymbol), localInputAlphabet);

				if(duplicateSymbol){
					System.out.println("\""+blankSymbol+"\" nao pode estar presente no alfabeto de entrada.");
				}
				else if(!duplicateSymbol){
					duplicateSymbol=verify.checkDuplicateSymbolString(localInputAlphabet);

					if(duplicateSymbol){
						System.out.println("Favor nao repetir o mesmo simbolo.");
					}
				}
			}
		}while(duplicateSymbol||emptyStringFound);

		do{ //coleta o alfabeto auxiliar
			System.out.print("Favor digitar o alfabeto auxiliar (\""+startSymbol+"\" e \""+blankSymbol+"\" ja estao no alfabeto): ");
			localAuxiliaryAlphabet=in.nextLine();

			duplicateSymbol=verify.checkDuplicateSymbolTwoStrings(Character.toString(startSymbol), localAuxiliaryAlphabet);

			if(duplicateSymbol){
				System.out.println("\""+startSymbol+"\" ja esta presente no alfabeto auxiliar.");
			}
			else if(!duplicateSymbol){
				duplicateSymbol=verify.checkDuplicateSymbolTwoStrings(Character.toString(blankSymbol), localAuxiliaryAlphabet);
				if(duplicateSymbol){
					System.out.println("\""+blankSymbol+"\" ja esta presente no alfabeto auxiliar.");
				}
				else if(!duplicateSymbol){
					duplicateSymbol=verify.checkDuplicateSymbolString(localAuxiliaryAlphabet);

					if(duplicateSymbol){
						System.out.println("Favor nao repetir o mesmo simbolo.");
					}
					else if(!duplicateSymbol){
						duplicateSymbol=verify.checkDuplicateSymbolTwoStrings(localInputAlphabet, localAuxiliaryAlphabet);

						if(duplicateSymbol){
							System.out.println("Favor nao digitar simbolos presentes no alfabeto de entrada.");
						}
					}
				}
			}
		}while(duplicateSymbol);

		auxiliaryAlphabet=(Character.toString(startSymbol))+(Character.toString(blankSymbol))+localAuxiliaryAlphabet;
		inputAndAuxiliaryAlphabet=(localInputAlphabet+auxiliaryAlphabet).toCharArray();
	}

	public void getWordInput(){ //coleta a palavra para ser processada
		tape[0]=startSymbol;
		for(int i=1;i<tape.length;i++){
			tape[i]=blankSymbol;
		}

		do{
			System.out.println("Favor digitar a palavra para ser processada:");
			String wordString=in.nextLine();

			duplicateSymbol=verify.checkDuplicateSymbolTwoStrings(wordString, auxiliaryAlphabet);

			if(duplicateSymbol){
				System.out.println("Favor nao digitar simbolos presentes no alfabeto auxiliar.");
			}
			else if(!duplicateSymbol){
				char wordArray[]=wordString.toCharArray();

				for(int i=0;i<wordArray.length;i++){
					tape[i+1]=wordArray[i];
				}

				wordLength=verify.checkLengthCharArrayUntilChar(tape, blankSymbol); //verifica o tamanho da palavra que foi entrada
			}
		}while(duplicateSymbol);
	}

	public void getAmountOfStates(){ //coleta a quantidade de estados
		do{
			System.out.print("Favor digitar a quantidade de estados (no minimo 1 estado): ");

			while(!in.hasNextInt()){
				in.next();
			}
			amountOfStates=in.nextInt();
			in.nextLine();
		}while(amountOfStates<1);
	}

	public void getInitialState(){ //coleta o estado inicial
		System.out.println("O estado inicial deve ser um dos estados:");
		for(int i=0;i<amountOfStates;i++){
			System.out.print(" "+i);
		}
		System.out.println();

		do{
			System.out.print("Favor digitar o estado inicial: ");

			while(!in.hasNextInt()){
				in.next();
			}
			initialState=in.nextInt();
			in.nextLine();
		}while(initialState<0||initialState>=amountOfStates);
	}

	public void getAmountOfFinalStates(){ //coleta a quantidade de estados finais
		int localAmountOfFinalStates=0;

		do{
			System.out.println("Deve existir pelo menos 1 estado final, e no maximo todos os estados podem ser finais.");
			System.out.print("Favor digitar a quantidade de estados finais: ");

			while(!in.hasNextInt()){
				in.next();
			}
			localAmountOfFinalStates=in.nextInt();
			in.nextLine();
		}while(localAmountOfFinalStates<1||localAmountOfFinalStates>amountOfStates);

		finalStates=new int[localAmountOfFinalStates];
		for(int i=0;i<finalStates.length;i++){
			finalStates[i]=-1;
		}
	}

	public void getFinalStates(){ //coleta cada estado final
		System.out.println("Os estados sao:");

		for(int i=0;i<amountOfStates;i++){
			System.out.print(" "+i);
		}
		System.out.println();

		System.out.println("Favor digitar o numero de cada estado final.");
		for(int i=0;i<finalStates.length;i++){
			boolean finalStateFound=false;

			do{
				int localFinalState=0;

				System.out.print((i+1)+" - Digite o estado final: ");

				while(!in.hasNextInt()){
					in.next();
				}
				localFinalState=in.nextInt();
				in.nextLine();

				finalStateFound=verify.checkDuplicateFinalState(localFinalState);
				if(finalStateFound){
					System.out.println("Favor digitar cada estado final apenas uma vez.");
				}
				else if(!finalStateFound){
					finalStates[i]=localFinalState;
				}
			}while(finalStateFound||finalStates[i]<0||finalStates[i]>=amountOfStates);
		}
	}

	public void getEachTransitionFunctionInfo(int state, int symbol){ //coleta informacoes para uma celula da tabela de funcao de transicao, tal celula e controlada pelas variaveis int state, int symbol
		boolean transitionChoice=false;

		if(showTable){
			out.printTransitionFunctionTable(state, symbol);
		}
		System.out.print("No estado \""+transitionFunction[state][symbol].getTableState());
		System.out.print("\", com o simbolo \""+transitionFunction[state][symbol].getTableSymbol());
		System.out.print("\", existe uma transicao? Digite \"s\" ou \"n\": ");

		transitionChoice=getValidInputChoice();
		transitionFunction[state][symbol].setTransition(transitionChoice);

		if(transitionFunction[state][symbol].getTransition()){ //se existe uma transicao na celula atual
			transitionFunction[state][symbol].setTransition(false); //"false" para nao imprimir a celula enquanto esta sendo alimentada

			do{ //na celula atual, coleta o proximo estado de acordo com o padrao de uma funcao de transicao
				int localNextState=0;

				if(showTable){
					out.printTransitionFunctionTable(state, symbol);
				}
				System.out.print("No estado \""+transitionFunction[state][symbol].getTableState());
				System.out.print("\", com o simbolo \""+transitionFunction[state][symbol].getTableSymbol());
				System.out.print("\", digite o proximo estado: ");

				while(!in.hasNextInt()){
					in.next();
				}
				localNextState=in.nextInt();
				in.nextLine();

				if(localNextState>=0&&localNextState<amountOfStates){
					validInputChoice=true;
					transitionFunction[state][symbol].setNextState(localNextState);
				}
				else{
					validInputChoice=false;
				}
			}while(!validInputChoice);

			do{ //na celula atual, coleta o novo simbolo na fita, de acordo com o padrao de uma funcao de transicao
				String localSymbol;

				if(showTable){
					out.printTransitionFunctionTable(state, symbol);
				}
				System.out.print("No estado \""+transitionFunction[state][symbol].getTableState());
				System.out.print("\", com o simbolo \""+transitionFunction[state][symbol].getTableSymbol());
				System.out.print("\", digite o novo simbolo na fita (deve estar no alfabeto): ");
				localSymbol=in.nextLine();

				emptyStringFound=verify.checkEmptyString(localSymbol);
				if(!emptyStringFound){
					validInputChoice=verify.checkCharInCharArray(inputAndAuxiliaryAlphabet, localSymbol.charAt(0));

					if(validInputChoice){
						transitionFunction[state][symbol].setNewTapeSymbol(localSymbol.charAt(0));
					}
				}
			}while(emptyStringFound||!validInputChoice);

			if(transitionFunction[state][symbol].getTableSymbol()==startSymbol){ //se o simbolo atual for o inicio da fita, a cabeca pode ir apenas para a direita
				if(showTable){
					out.printTransitionFunctionTable(state, symbol);
				}
				System.out.print("No estado \""+transitionFunction[state][symbol].getTableState());
				System.out.print("\", com o simbolo \""+transitionFunction[state][symbol].getTableSymbol());
				System.out.print("\", a cabeca pode ir apenas para a direita, pois e o inicio da fita. Pressione enter: ");
				in.nextLine();
				transitionFunction[state][symbol].setHeadDirection(1);
			}
			else if(transitionFunction[state][symbol].getTableSymbol()!=startSymbol){ //se o simbolo atual nao for o inicio da fita, na celula atual, coleta a direcao da cabeca na fita
				do{
					String localHeadDirectionString;

					if(showTable){
						out.printTransitionFunctionTable(state, symbol);
					}
					System.out.print("No estado \""+transitionFunction[state][symbol].getTableState());
					System.out.print("\", com o simbolo \""+transitionFunction[state][symbol].getTableSymbol());
					System.out.print("\", digite a direcao da cabeca na fita (\"e\" para esquerda ou \"d\" para direita): ");
					localHeadDirectionString=in.nextLine();

					emptyStringFound=verify.checkEmptyString(localHeadDirectionString);
					if(!emptyStringFound){
						char localHeadDirectionChar=localHeadDirectionString.charAt(0);

						if(localHeadDirectionChar=='e'||localHeadDirectionChar=='E'){
							validInputChoice=true;
							transitionFunction[state][symbol].setHeadDirection(-1);
						}
						else if(localHeadDirectionChar=='d'||localHeadDirectionChar=='D'){
							validInputChoice=true;
							transitionFunction[state][symbol].setHeadDirection(1);
						}
						else{
							validInputChoice=false;
						}
					}
				}while(emptyStringFound||!validInputChoice);
			}

			transitionFunction[state][symbol].setTransition(true); //apos a celula ter sido completamente alimentada, "true" para ela ser mostrada
		}
	}

	public void getCompleteTransitionFunctionInfo(){ //coleta informacoes para a tabela de funcao de transicao inteira
		int state=0;
		int symbol=0;

		System.out.println("Favor digitar os dados da tabela de funcao de transicao.");
		System.out.print("Gostaria de ver a tabela de funcao de transicao enquanto voce digita os dados?\nDigite \"s\" ou \"n\": ");

		showTable=getValidInputChoice();

		for(state=0;state<transitionFunction.length;state++){
			for(symbol=0;symbol<transitionFunction[0].length;symbol++){
				getEachTransitionFunctionInfo(state, symbol); //usando o metodo de alimentacao de uma unica celula, para todas as celulas
			}
		}

		if(showTable){
			out.printTransitionFunctionTable(state, symbol);
			System.out.println();
		}
	}
}
