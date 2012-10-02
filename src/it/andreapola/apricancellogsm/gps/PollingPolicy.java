package it.andreapola.apricancellogsm.gps;

public class PollingPolicy {
	
	static int lastdistance = 0;
	static int lasttime = 0;
	static int currentvelocity = 55;
	static int mult = 1;
    static int predict = 0;
	
	protected int next_interval(int distance_to_target){
		int percorso;
		
		//small distance to target max polling
		if(distance_to_target < 2000){
			return remove_ctime(15);
		//big distance, min polling
		}else{
		
			//first time
			if(lastdistance == 0){
				lastdistance = distance_to_target;
				predict = remove_ctime(distance_to_target / currentvelocity);
				return predict;
			}else{
				percorso = lastdistance - distance_to_target;
				/*se ho sbagliato di poco (due km)*/
				if((predict*currentvelocity-1000 <= percorso) && (percorso <= predict*currentvelocity+1000));
				else mult_down();
				
				return 10;

			}
		}
		
	}
		
	static protected int next_interval_multi(int distance_to_target){
		
			//small distance to target max polling
			if(distance_to_target < 2000){
				return 10;
			//big distance, min polling
			}else{
				return distance_to_target/currentvelocity;
			}
			
		}

	
	private int remove_ctime(int time){
		int ctime = 5;
		
		if(time < ctime){
			return time;
		}else{
			return time - ctime;
		}
		
	}
	
	private void mult_up(){	
		mult++;
	};
	
	private void mult_down(){
		
		if(mult <= 1) mult = 1;
		else mult --;
	};

	private void reset(){
		lastdistance = 0;
		currentvelocity = 55;
		mult = 1;
		lasttime = 0;
	}
	
}
