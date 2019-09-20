
public class Main{
	static public void main(String[] args){
		MetaData data=new MetaData();
		Input in=new Input();
		Algorithm run=new Algorithm();
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
