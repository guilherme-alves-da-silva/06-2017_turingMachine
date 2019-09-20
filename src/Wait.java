import java.util.concurrent.TimeUnit;

public class Wait extends MetaData{ //pausa o funcionamento por um tempo, apos imprimir a fita para o usuario poder ler
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
