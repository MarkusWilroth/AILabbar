package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;

public class AgentOne extends Agent {

	public AgentOne() {
		super(PlayerTurn.PLAYER_ONE);
		// TODO Auto-generated constructor stub
	}
	private AgentOne(PlayerTurn playerTurn) {
		super(playerTurn);
		// TODO Auto-generated constructor stub
	}
	
	public AgentMove getMove(GameBoardState gameState) {
		return getExampleMove(gameState);
	}
	
	private AgentMove getExampleMove(GameBoardState gameState){
		
		int waitTime = UserSettings.MIN_SEARCH_TIME; // 1.5 seconds
		
		ThreadManager.pause(TimeSpan.millis(waitTime)); // Pauses execution for the wait time to cause delay
		return ABPruning.Minimax(gameState, playerTurn);
		//return AgentController.getExampleMove(gameState, playerTurn); // returns an example AI move Note: this is not AB Pruning
	}
}
