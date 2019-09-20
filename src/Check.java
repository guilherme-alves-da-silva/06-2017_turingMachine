
public class Check extends MetaData{ //realiza verificacoes que podem acontecer mais de uma vez
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
