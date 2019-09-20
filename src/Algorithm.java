
public class Algorithm extends MetaData{ //algoritmo principal da MT
	public void machine(){
		Input in=new Input();
		Print out=new Print();
		Wait call=new Wait();
		ClearConsole console=new ClearConsole();
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
