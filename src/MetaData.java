
public class MetaData{ //possui a tabela de funcao de transicao e variaveis importantes para o funcionamento dos objetos
	static TableMetaData transitionFunction[][];
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
		transitionFunction=new TableMetaData[amountOfStates][inputAndAuxiliaryAlphabet.length];

		for(int i=0;i<amountOfStates;i++){
			for(int j=0;j<inputAndAuxiliaryAlphabet.length;j++){
				transitionFunction[i][j]=new TableMetaData();
				transitionFunction[i][j].setTableState(i);
				transitionFunction[i][j].setTableSymbol(inputAndAuxiliaryAlphabet[j]);
			}
		}
	}
}
