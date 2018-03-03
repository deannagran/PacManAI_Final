package ufl.cs1.controllers;

import game.controllers.DefenderController;
import game.models.Attacker;
import game.models.Defender;
import game.models.Game;
import game.models.Node;

import java.util.List;

public final class StudentController implements DefenderController
{
	public void init(Game game) { }

	public void shutdown(Game game) { }


	public int[] update(Game game,long timeDue)
	{
		int[] actions = new int[Game.NUM_DEFENDER];
		List<Defender> enemies = game.getDefenders();
		
		//Chooses a random LEGAL action if required. Could be much simpler by simply returning
		//any random number of all of the ghosts
		for(int i = 1; i < 1; i++)
		{
			Defender defender = enemies.get(i);
			List<Integer> possibleDirs = defender.getPossibleDirs();
			if (possibleDirs.size() != 0)
				actions[i]=possibleDirs.get(Game.rng.nextInt(possibleDirs.size()));
			else
				actions[i] = -1;
		}
		actions[0] = ghost1(game, timeDue, enemies.get(0));
		//actions[1] = ghost2(game, timeDue, enemies.get(1));
		actions[2] = ghost3(game, timeDue, enemies.get(2));
		actions[3] = ghost4(game, timeDue, enemies.get(3));

		return actions;
	}

	//This ghost guards the power pills on the right half of the maze.
	public int ghost2(Game game, long time, Defender ghost){

		int action = 0;
		Defender defender = ghost;
		Attacker pacman = game.getAttacker();

		//If mode changes to Vulnerable, run away from Pacman.
		if (defender.isVulnerable()){
			Node location = pacman.getLocation();
			action = defender.getNextDir(location, false);
		}

		//Otherwise, guard the pill that Pacman seems to be approaching.

		List<Node> pillNodes =  game.getPillList();
		Node firstPillLocation = pillNodes.get(0);
		Node secondPillLocation = pillNodes.get(3);
		Node pacmanIsProbablyGoingHere = pacman.getTargetNode(pillNodes, true);
		List<Node> pacmansDistanceToFirst = pacman.getPathTo(firstPillLocation);
		List<Node> ghostsDistanceToFirst = defender.getPathTo(firstPillLocation);
		List<Node> pacmansDistanceToSecond = pacman.getPathTo(secondPillLocation);
		List<Node> ghostsDistanceToSecond = defender.getPathTo(secondPillLocation);

		//See if Pacman is closest to a pill on the side that this ghost is in charge of guarding.
		//If Pacman seems like he will reach the pill before the ghost has an opportunity to get there and guard,
		// ghost should flee.
		if (pacmanIsProbablyGoingHere == firstPillLocation || pacmanIsProbablyGoingHere == secondPillLocation) {
			if (game.checkPill(firstPillLocation) == true && pacmanIsProbablyGoingHere == firstPillLocation) {
				//If ghost will reach the first pill's area before Pacman, send ghost there.
				if(pacmansDistanceToFirst.size() > ghostsDistanceToFirst.size()) {
					//Go guard pill
					action = defender.getNextDir(firstPillLocation, true);
				}
				else{
					//Flee
					Node location = pacman.getLocation();
					action = defender.getNextDir(location, false);
				}
			} else if (game.checkPill(secondPillLocation) == true && pacmanIsProbablyGoingHere == secondPillLocation) {
				if(pacmansDistanceToSecond.size() > ghostsDistanceToSecond.size()){
					//Go guard pill
					action = defender.getNextDir(secondPillLocation, true);
				}
				else{
					//Flee
					Node location = pacman.getLocation();
					action = defender.getNextDir(location, false);
				}
			}
		}

		return action;
	}

	public int ghost1(Game game, long time, Defender ghost) {

		int action = 0;
		Defender defender = ghost;
		Attacker pacman = game.getAttacker();

		//If mode changes to Vulnerable, run away from Pacman.
		if (defender.isVulnerable()) {
			Node location = pacman.getLocation();
			action = defender.getNextDir(location, false);
		}

		//Otherwise, guard the pill that Pacman seems to be approaching.
		//update causes out of bounds update
		List<Node> pillNodes = game.getPillList();
		Node firstPillLocation;
		Node secondPillLocation;

		if(pillNodes.size() == 3 ) {
			firstPillLocation = pillNodes.get(0);
			secondPillLocation = pillNodes.get(2);
			action = guardBehavior(game, time, defender, pacman, firstPillLocation, secondPillLocation, pillNodes);
		}
		if(pillNodes.size() == 2){
			firstPillLocation = pillNodes.get(0);
			secondPillLocation = pillNodes.get(1);
			action = guardBehavior(game, time, defender, pacman, firstPillLocation, secondPillLocation, pillNodes);
		}
		if(pillNodes.size() == 1){
			Node locationOfLastPill = pillNodes.get(0);
			action = defender.getNextDir(locationOfLastPill, true);
		}
		else{
			Node location = pacman.getLocation();
			action = defender.getNextDir(location, true);
		}

		return action;
	}

	public int guardBehavior(Game game, long time, Defender defender, Attacker pacman, Node first, Node second, List<Node> pillNodes){
		int action = 0;
		Node pacmanIsProbablyGoingHere = pacman.getTargetNode(pillNodes, true);
		List<Node> pacmansDistanceToFirst = pacman.getPathTo(first);
		List<Node> ghostsDistanceToFirst = defender.getPathTo(first);
		List<Node> pacmansDistanceToSecond = pacman.getPathTo(second);
		List<Node> ghostsDistanceToSecond = defender.getPathTo(second);

		//See if Pacman is closest to a pill on the side that this ghost is in charge of guarding.
		//If Pacman seems like he will reach the pill before the ghost has an opportunity to get there and guard,
		// ghost should flee.
		if (pacmanIsProbablyGoingHere == first || pacmanIsProbablyGoingHere == second) {
			if (game.checkPill(first) == true && pacmanIsProbablyGoingHere == first) {
				//If ghost will reach the first pill's area before Pacman, send ghost there.
				if (pacmansDistanceToFirst.size() > ghostsDistanceToFirst.size()) {
					//Go guard pill
					action = defender.getNextDir(first, true);
				} else {
					//Flee
					Node location = pacman.getLocation();
					action = defender.getNextDir(location, false);
				}
			} else if (game.checkPill(second) == true && pacmanIsProbablyGoingHere == second) {
				if (pacmansDistanceToSecond.size() > ghostsDistanceToSecond.size()) {
					//Go guard pill
					action = defender.getNextDir(second, true);
				} else {
					//Flee
					Node location = pacman.getLocation();
					action = defender.getNextDir(location, false);
				}
			}
		}
		return action;
	}

	public int ghost3(Game game, long time, Defender ghost){

		int action = 0;
		Defender defender = ghost;
		Attacker pacman = game.getAttacker();

		if(defender.isVulnerable()){
			Node pacmanIsHere = pacman.getLocation();
			action = defender.getNextDir(pacmanIsHere, false);
		}
		else{
			Node pacmanIsHere = pacman.getLocation();
			action = defender.getNextDir(pacmanIsHere, true);
		}
		return action;
	}

	public int ghost4(Game game, long time, Defender ghost){
		int action = 0;
		Defender defender = ghost;
		Attacker pacman = game.getAttacker();

		if(defender.isVulnerable()){
			Node pacmanIsHere = pacman.getLocation();
			action = defender.getNextDir(pacmanIsHere, false);
		}

		//If pacman is sitting (stuck) on a powerpill
		if(pacman.getLocation().isPowerPill()){
			//Flee
			Node location = pacman.getLocation();
			action = defender.getNextDir(location, false);
		}
		else{
			//Pursue
			Node pacmanIsHere = pacman.getLocation();
			action = defender.getNextDir(pacmanIsHere, true);
		}
		return action;
	}
}
