import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class tableMetaData{ //estrutura de dados presente em cada celula na tabela de funcao de transicao
	private boolean transition=false;
	private int tableState=0;
	private char tableSymbol=0;
	private int nextState=0;
	private char newTapeSymbol='0';
	private int headDirection=0;

	public int getTableState(){
		return this.tableState;
	}
	public synchronized void setTableState(int newValue){
		this.tableState=newValue;
	}
	public char getTableSymbol(){
		return this.tableSymbol;
	}
	public synchronized void setTableSymbol(char newValue){
		this.tableSymbol=newValue;
	}
	public int getNextState(){
		return this.nextState;
	}
	public synchronized void setNextState(int newValue){
		this.nextState=newValue;
	}
	public char getNewTapeSymbol(){
		return this.newTapeSymbol;
	}
	public synchronized void setNewTapeSymbol(char newValue){
		this.newTapeSymbol=newValue;
	}
	public int getHeadDirection(){
		return this.headDirection;
	}
	public synchronized void setHeadDirection(int newValue){
		this.headDirection=newValue;
	}
	public boolean getTransition(){
		return this.transition;
	}
	public synchronized void setTransition(boolean newValue){
		this.transition=newValue;
	}
}

class metaData{ //possui a tabela de funcao de transicao e variaveis importantes para o funcionamento dos objetos
	static tableMetaData transitionFunction[][];
	static int TAPE_LENGTH=10000;
	static int TRANSITION_LIMIT=5000;
	static int TIME_DELAY=1500;
	static int amountOfStates=0;
	static int initialState=0;
	static int finalStates[];
	static int headPosition=1;
	static int wordLength=0;
	static int blankSpacesToPrintAfterWord=2;
	static char tape[]=new char[TAPE_LENGTH];
	static char startSymbol='<';
	static char blankSymbol='_';
	static char inputAndAuxiliaryAlphabet[];
	static boolean showTape=true;

	public void setTableDimension(){
		transitionFunction=new tableMetaData[amountOfStates][inputAndAuxiliaryAlphabet.length];

		for(int i=0;i<amountOfStates;i++){
			for(int j=0;j<inputAndAuxiliaryAlphabet.length;j++){
				transitionFunction[i][j]=new tableMetaData();
				transitionFunction[i][j].setTableState(i);
				transitionFunction[i][j].setTableSymbol(inputAndAuxiliaryAlphabet[j]);
			}
		}
	}
}

class algorithm extends metaData{ //algoritmo principal da MT
	public void machine(){
		input in=new input();
		print out=new print();
		wait call=new wait();
		clearConsole console=new clearConsole();
		boolean stateHasTransitionWithTapeSymbol=false;
		boolean currentStateIsFinal=false;
		boolean stopMachine=false;
		int transitionCount=0;
		int state=initialState;
		headPosition=1;

		do{
			stateHasTransitionWithTapeSymbol=false; //caso o estado atual nao encontre uma transicao com o simbolo da fita, a variavel precisa estar "false" como padrao

			for(int symbol=0;symbol<inputAndAuxiliaryAlphabet.length&&!stateHasTransitionWithTapeSymbol;symbol++){ //enquanto existir simbolos no alfabeto para verificar se possuem uma transicao no estado atual, e o estado atual ainda nao ter encontrado um simbolo
				currentStateIsFinal=false;
				for(int i=0;i<finalStates.length;i++){
					if(state==finalStates[i]){ //verifica se o estado atual e um estado final
						currentStateIsFinal=true;
					}
				}
				if(transitionFunction[state][symbol].getTransition()&&tape[headPosition]==transitionFunction[state][symbol].getTableSymbol()){ //se existe uma transicao neste estado com este simbolo, e este simbolo e o mesmo simbolo da fita
					if(showTape){ //se o usuario escolheu mostrar a fita
						console.clear();
						out.printTransitionFunctionTable(state, symbol);
						out.printTape();
						call.delay();
					}
					tape[headPosition]=transitionFunction[state][symbol].getNewTapeSymbol(); //a fita, na posicao da cabeca, recebe o novo simbolo que esta armazenado na tabela de funcao de transicao
					headPosition=headPosition+transitionFunction[state][symbol].getHeadDirection(); //a posicao da cabeca muda para a esquerda ou direita
					state=transitionFunction[state][symbol].getNextState(); //o novo estado se torna o estado que esta armazenado na tabela de funcao de transicao
					stateHasTransitionWithTapeSymbol=true; //o estado atual encontrou uma transicao com o simbolo da fita
					transitionCount++; //contador para controlar o limite de transicoes
					if(transitionCount==TRANSITION_LIMIT){
						if(showTape){
							console.clear();
							out.printTransitionFunctionTable(state, symbol);
							out.printTape();
							call.delay();
						}
						stopMachine=in.getStopMachineChoice(); //pergunta se o usuario deseja parar a maquina
					}
				}
			}
			if(!stateHasTransitionWithTapeSymbol){ //se o estado atual nao possui pelo menos uma transicao com o simbolo da fita
				if(showTape){
					console.clear();
					out.printTransitionFunctionTable(state, transitionFunction[0].length);
					out.printTape();
					call.delay();
				}
				if(currentStateIsFinal){ //se o estado for final
					if(!showTape){
						out.printTape();
					}
					System.out.println("A palavra foi aceita.");
				}
				else if(!currentStateIsFinal){ //se o estado nao for final
					if(!showTape){
						out.printTape();
					}
					System.out.println("A palavra nao foi aceita.");
				}
			}
		}while(stateHasTransitionWithTapeSymbol&&!stopMachine); //enquanto o estado atual possui uma transicao com o simbolo da fita, e o usuario deseja que a maquina continue, caso o limite de transicoes tenha sido atingido
	}
}

class input extends metaData{ //recebe todas as entradas do usuario
	Scanner in=new Scanner(System.in);
	print out=new print();
	check verify=new check();
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

class check extends metaData{ //realiza verificacoes que podem acontecer mais de uma vez
	public int checkLengthCharArrayUntilChar(char[] array, char value){ //verifica o comprimento de um array
		int slots=0;

		while(slots<array.length){
			if(array[slots]==value){
				break;
			}
			slots++;
		}

		return slots;
	}

	public boolean checkCharInCharArray(char[] array, char value){ //verifica se um array possui um determinado char
		boolean result=false;

		for(int i=0;i<array.length;i++){
			if(array[i]==value){
				result=true;
			}
		}

		return result;
	}

	public boolean checkDuplicateFinalState(int value){ //verifica se um estado final ja se encontra no array de estados finais
		boolean result=false;

		for(int i=0;i<finalStates.length;i++){
			if(finalStates[i]==value){
				result=true;
			}
		}

		return result;
	}

	public boolean checkEmptyString(String data){ //verifica se a string esta vazia
		boolean result=false;

		if(data.length()==0){
			result=true;
		}

		return result;
	}

	public boolean checkDuplicateSymbolString(String data){ //verifica se a string possui algum dado duplicado
		boolean result=false;

		for(int i=0;i<data.length();i++){
			for(int j=i+1;j<data.length();j++){
				if(data.charAt(i)==data.charAt(j)){
					result=true;
				}
			}
		}

		return result;
	}

	public boolean checkDuplicateSymbolTwoStrings(String string1, String string2){ //verifica se duas strings compartilham algum dado
		boolean result=false;

		for(int i=0;i<string1.length();i++){
			for(int j=0;j<string2.length();j++){
				if(string1.charAt(i)==string2.charAt(j)){
					result=true;
				}
			}
		}

		return result;
	}
}

class wait extends metaData{ //pausa o funcionamento por um tempo, apos imprimir a fita para o usuario poder ler
	public void delay(){
		boolean interrupted=false;
		int tries=0;
		int MAX_TRIES=10;

		do{
			interrupted=false;

			try{
				TimeUnit.MILLISECONDS.sleep(TIME_DELAY);
			}
			catch(InterruptedException e){
				if(tries==MAX_TRIES){
					System.out.println("Interrompido "+MAX_TRIES+" vezes.");
				}
				interrupted=true;
				tries++;

				Thread.currentThread().interrupt();
			}
		}while(interrupted&&tries<=MAX_TRIES);
	}
}

class clearConsole{ //limpa o console apos cada impressao, solucao multiplataforma
	public void clear(){
		for(int i=0;i<200;i++){
			System.out.println();
		}
	}
}

class print extends metaData{ //mostrar na tela
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

class Main{ //classe principal
	static public void main(String[] args){
		metaData data=new metaData();
		input in=new input();
		algorithm run=new algorithm();
		int menuChoice=0;

		in.getTapeStartSymbol();
		in.getBlankSymbol();
		in.getInputAndAuxiliaryAlphabet();
		in.getAmountOfStates();
		data.setTableDimension();
		in.getCompleteTransitionFunctionInfo();
		in.getInitialState();
		in.getAmountOfFinalStates();
		in.getFinalStates();
		in.getShowTapeChoice();

		do{
			in.getWordInput();
			run.machine();

			do{
				menuChoice=in.getMenuChoice(); //mostra o menu e coleta a escolha

				if(menuChoice==2){
					in.getShowTapeChoice();
				}
				else if(menuChoice==3){
					in.getChangeTransitionFunctionChoice();
				}
				else if(menuChoice==4){
					in.getInitialState();
				}
				else if(menuChoice==5){
					in.getAmountOfFinalStates();
					in.getFinalStates();
				}
			}while(menuChoice>1&&menuChoice<6);

			if(menuChoice==0){
				System.out.println();
				break;
			}
		}while(true);
	}
}

//aluno guilherme alves
