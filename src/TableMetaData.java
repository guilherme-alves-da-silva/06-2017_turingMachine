
public class TableMetaData{ //estrutura de dados presente em cada celula na tabela de funcao de transicao
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
