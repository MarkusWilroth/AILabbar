package main;

import java.util.List;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.BoardCellState;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;
import com.eudycontreras.othello.models.GameBoard;
import com.eudycontreras.othello.utilities.GameTreeUtility;

public class AgentOne extends Agent {

	private int depth, eva, maxEva, minEva, value, newValue;
	private MoveWrapper bestMove, testMove;
	private BoardCellState boardCellState;
	
	public AgentOne() {
		super(PlayerTurn.PLAYER_ONE);
		// TODO Auto-generated constructor stub
	}
	public AgentOne(PlayerTurn playerTurn) {
		super(playerTurn);
		// TODO Auto-generated constructor stub
	}
	
	public AgentMove getMove(GameBoardState gameState) {
		value = Integer.MIN_VALUE;
		List<ObjectiveWrapper> moves = AgentController.getAvailableMoves(gameState, playerTurn);
		for (ObjectiveWrapper move : moves) {
			newValue = MiniMax(gameState, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, playerTurn);
			if (newValue > value) {
				value = newValue;
				System.out.println("value: " + value);
				bestMove = new MoveWrapper(move);
			}
		}
		
		return bestMove;
	}
	
	private AgentMove getExampleMove(GameBoardState gameState){
		
		int waitTime = UserSettings.MIN_SEARCH_TIME; // 1.5 seconds
		
		ThreadManager.pause(TimeSpan.millis(waitTime)); // Pauses execution for the wait time to cause delay
		return ABPruning.Minimax(gameState, playerTurn);
		//return AgentController.getExampleMove(gameState, playerTurn); // returns an example AI move Note: this is not AB Pruning
	}
	
	/*private int Maximizer(GameBoardState gameState, int depth, int alpha, int beta, PlayerTurn playerTurn) {
		if (depth <= 0) {
			//gameBoard.resetBoard();
			System.out.println("Yes");
			
			return (int)maxEva; //Ska ändras till något annat!
		}
		return null;
	}*/
	
	private int MiniMax(GameBoardState gameState, int depth, int alpha, int beta, PlayerTurn playerTurn) {
		GameBoard gameBoard = gameState.getGameBoard();
		if (depth <= 0) {
			eva = gameState.getWhiteCount();
			
			return (int)maxEva; //Ska ändras till något annat!
		}
		
		if (playerTurn == PlayerTurn.PLAYER_ONE) {
			int maxEva = Integer.MIN_VALUE;
			
			List<ObjectiveWrapper> moves = AgentController.getAvailableMoves(gameState, playerTurn);
			for (ObjectiveWrapper move : moves) {
				//playerTurn = PlayerTurn.PLAYER_TWO;
				//testMove = new MoveWrapper(move);
				//testMove = MiniMax(gameState, (depth-1), alpha, beta, playerTurn);
				
				gameState = AgentController.getNewState(gameState, move);
				//gameBoard.makeMove(testMove);
				//gameState = new GameBoardState(gameBoard);
				MiniMax(gameState, (depth-1), alpha, beta, PlayerTurn.PLAYER_TWO);
				
				//testMove.setObjectiveInformation(move);
				
				maxEva = Math.max(maxEva, eva);
				System.out.println("New maxEva? " + maxEva);
				alpha = Math.max(alpha, maxEva);
				if (beta <= alpha) {
					break;
				}
			}
			return maxEva; //Slut på for
			
		} else { //PlayerTurn = Player_Two
			int minEva = Integer.MAX_VALUE;
			
			List<ObjectiveWrapper> moves = AgentController.getAvailableMoves(gameState, playerTurn);
			for (ObjectiveWrapper move : moves) {
				//GameBoard gameBoard = gameState.getGameBoard();
				testMove = new MoveWrapper(move);
				testMove.setObjectiveInformation(move);
				
				//gameBoard.makeMove(testMove);
				gameState = new GameBoardState(gameBoard);
				MiniMax(gameState, (depth-1), alpha, beta, PlayerTurn.PLAYER_ONE);
				
				eva = gameState.getTotalReward(testMove);
				//gameBoard.resetBoard();
				minEva = Math.min(minEva, eva);
				beta = Math.min(beta, minEva);
				if (beta <= alpha) {
					break;
				}
			}
			return minEva;
			
		}
	}
	
	/*
	function minimax(node, depth, alpha, beta, maximizingPlayer) is  
	if depth ==0 or node is a terminal node then  
	return static evaluation of node  
	  
	if MaximizingPlayer then      // for Maximizer Player  
	   	maxEva= -infinity            
	   	for each child of node do  
	   		eva= minimax(child, depth-1, alpha, beta, False)  
	  		maxEva= max(maxEva, eva)   
	  		alpha= max(alpha, maxEva)      
	  		if beta<=alpha  
	 			break  
	  		return maxEva  
	    
	else                         // for Minimizer player  
	   minEva= +infinity   
	   for each child of node do  
	   eva= minimax(child, depth-1, alpha, beta, true)  
	   minEva= min(minEva, eva)   
	   beta= min(beta, eva)  
	    if beta<=alpha  
	  break          
	 return minEva 
	 */
}
