package snl.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Max Schwaab, Katharina Laube
 * @since 27.08.2014
 */
public class Game {

    private Map<Integer, JumpRule> specialPositions = new HashMap<>();

    private List<Player> players = new ArrayList<>();

    private int currentPlayer = 0;

    void addPlayer(final Player player) {
        players.add(player);
    }

    void moveCurrentPlayer(int diceValue) {
        final int lastPosition = getCurrentPlayer().getPosition();

        // roll dice
        int newPosition = lastPosition + diceValue;
        if (newPosition > getFieldSize()) {
        	System.out.println("You have to roll a '" 
        			+ (getFieldSize()-lastPosition) + "' or lower!");
        	return;
        }
        System.out.println("The dice you have rolled moves you to " + newPosition);
        
        // use snake or ladder
		JumpRule jumpRule = specialPositions.get(newPosition);
		if (jumpRule != null) {
			newPosition += jumpRule.getEndPosition();
			System.out.println("You have found a " + jumpRule.getType()
					+ ". It moves you to " + newPosition);
		}
		
		getCurrentPlayer().setPosition(newPosition);
    }

    void shiftPlayer() {
        if(this.currentPlayer == players.size() -1 ) {
            this.currentPlayer = 0;
        } else {
            this.currentPlayer ++;
        }
    }

    boolean isOver() {
        return getCurrentPlayer().getPosition() == getFieldSize();
    }

    Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    int getDiceValue() {
        return new Random().nextInt(6) + 1;
    }

    int getFieldSize() {
        return 100;
    }

	int getPlayersPosition(Player searchedPlayer) {
		for (Player player : players) {
			if (player.equals(searchedPlayer)) {
				return player.getPosition();
			}
		}
		throw new IllegalArgumentException("Player [" + searchedPlayer + "] is unknown!");
	}

	void addJumpRule(JumpRule ladder) {
		Type type = ladder.getType();
		switch (type) {
		case LADDER:
			if (ladder.getStartPosition() >= getFieldSize()) {
				throw new IllegalArgumentException("Ladder must not start before endposition!");
			}
			if (ladder.getStartPosition() < 1) {
				throw new IllegalArgumentException("Ladder must start at least on startposition!");
			}
			break;
		case SNAKE:
			if (ladder.getEndPosition() <= getFieldSize()) {
				throw new IllegalArgumentException("Ladder must not start before endposition!");
			}
			break;
		}
	}
}
