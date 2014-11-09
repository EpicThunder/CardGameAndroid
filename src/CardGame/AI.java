package CardGame;

import java.util.*;

/**
 * Created by Kristján on 18.6.2014.
 */
public interface AI {
    void draw(State state);
    void play(State state, AIuses callBackClass) throws IllegalAccessException, InstantiationException;
}

class Test implements AI {
    int id, averageDeckCost;
    Player player;
    Base myBase;
    Map<Base, Integer> baseAndDistance = new HashMap<Base, Integer>();
    Test(int i, State state) {
        id=i; player = state.getPlayer(i); averageDeckCost = player.averageDeckCost();
        myBase = state.findBase(player);
        List<Base> opponentsBase = new ArrayList<Base>();
        for(Player opponent:state.getOpponentsOf(player)) opponentsBase.add(state.findBase(opponent));
        for(Base base:opponentsBase)
            baseAndDistance.put(base, Support.getManhattanDistance(myBase.getCurrentLocation(), base.getCurrentLocation()));
    }
    public void draw(State state) {
        int units=0, items=0, spells=0, structure=0;
        for(Card card:state.getPlayersCardsFromTheBoard(state.getCurrentPlayer())) {
            if(card.getCardCategory().equals("Unit")) units++; if(card.getCardCategory().equals("Structure")) structure++;
        }
        for(Card card:state.getCurrentPlayer().getCards()) {
            if(card.getCardCategory().equals("Unit")) units++; if(card.getCardCategory().equals("Structure")) structure++;
            if(card.getCardCategory().equals("Spell")) spells++; if(card.getCardCategory().equals("Item")) items++;
        }
        if(units<3) { state.getCurrentPlayer().drawUnit(); return; }
        if((structure+items)<1 || (structure+items)<(units/4) && (structure+items)<spells) {
            state.getCurrentPlayer().drawStructureOrItem(); return;
        }
        if((units>3 && spells==1 && structure>0 && items>0) || (spells==0 && units>2)) {
            state.getCurrentPlayer().drawSpell(); return;
        }
        state.getCurrentPlayer().drawUnit();
    }

    public void play(State state, AIuses callBackClass) throws IllegalAccessException, InstantiationException {

        long startTime = System.currentTimeMillis();
        State newCopyOfState = new State(state);
        List<Card> cardsThatCurrentPlayerCanPlay = newCopyOfState.getCurrentPlayer().getCards();

        HashSet<State> setOfStates = new HashSet<State>(); setOfStates.add(newCopyOfState);
        HashSet<State> nextIterationForSetOfStates = new HashSet<State>();

        List<Cell> cells;

        Set<State> test = new HashSet<State>();
        test.add(state);
        System.out.println("Test set size:" + test.size());
        test.add(new State(state));
        System.out.println("Test set size after:"+test.size());
        for(State aState:test) aState.printNotEqual();

        System.out.println("Number of cards that can be played:" + cardsThatCurrentPlayerCanPlay.size());
        // Place cards
        for(Card card:cardsThatCurrentPlayerCanPlay) { // Place cards on board
            if(!card.getCardCategory().equals("Unit") && !card.getCardCategory().equals("Structure") &&
                    !card.canTargetLocation()) continue; // Can not place card, continue

            nextIterationForSetOfStates = new HashSet<State>();
            System.out.println("Number of states at the start:" + setOfStates.size());
            List<Player> opponents;
            for (State aState : setOfStates) {
                aState.setUpBoardInfluence();
                nextIterationForSetOfStates.add(aState);
                if(!aState.getCurrentPlayer().getCard(card.getID()).canPay()) continue; // Continue if you can not pay for the card
                cells = aState.getInfluencedCells(aState.getCurrentPlayer());
                Cell closestCell; List<Cell> threeClosestCells = new ArrayList<Cell>();
                if(cells.size()>0) for(int i=0; i<3; i++) {
                    closestCell = null;
                    int closestCellDist = Integer.MAX_VALUE;
                    for(Cell cell:cells) {
                        int shortestManhattanDistance = Integer.MAX_VALUE;
                        for(Player aPlayer:aState.getOpponents()) {
                            int manDist = Support.getManhattanDistance(cell, aState.findBase(aPlayer).getCurrentLocation());
                            if (manDist < shortestManhattanDistance) shortestManhattanDistance = manDist;
                        }
                        if(closestCell == null) {
                            closestCell = cell; closestCellDist = shortestManhattanDistance; continue;
                        }
                        if(shortestManhattanDistance < closestCellDist) {
                            closestCell = cell; closestCellDist = shortestManhattanDistance;
                        }
                    }
                    threeClosestCells.add(closestCell);
                    cells.remove(closestCell);
                }
                for (Cell cell : threeClosestCells) {
                    if(nextIterationForSetOfStates.size() >= 900) break;
                    newCopyOfState = new State(aState);
                    Card cardToPlay = newCopyOfState.getCurrentPlayer().getCard(card.getID());
                    if(newCopyOfState.addCardToBoard(cardToPlay, cell.getRowPos(), cell.getColPos()))
                        nextIterationForSetOfStates.add(newCopyOfState);
                } if(nextIterationForSetOfStates.size() >= 900) break;
            }
            setOfStates = nextIterationForSetOfStates;
            System.out.println("Next iteration size at the end:" + nextIterationForSetOfStates.size());
            if(setOfStates.size() >= 900) break;
        }
        System.out.println("Number of states after placing cards:" + setOfStates.size());

        // Play cards
        for(Card card:cardsThatCurrentPlayerCanPlay) { // Use spell and item cards
            if(setOfStates.size() >= 900) break;
            if (card.getCardCategory().equals("Unit") || card.getCardCategory().equals("Structure") ||
                    !card.canTargetCard()) continue; // Not a card that you do not place on board, continue

            nextIterationForSetOfStates = new HashSet<State>();
            for (State aState : setOfStates) {
                nextIterationForSetOfStates.add(aState);
                if(!aState.getCurrentPlayer().getCard(card.getID()).canPay()) continue; // Continue if you can not pay for the card
                cells = aState.getInfluencedCells(aState.getCurrentPlayer());
                for (Cell cell : cells) {
                    for(int i=0, size=aState.getCell(cell.getRowPos(), cell.getColPos()).getCards().size(); i<size; i++) {
                        if(nextIterationForSetOfStates.size() >= 900) break;
                        newCopyOfState = new State(aState);
                        Card cardToPlay = newCopyOfState.getCurrentPlayer().getCard(card.getID());
                        List<Card> targets = new ArrayList<Card>();
                        targets.add(newCopyOfState.getCell(cell.getRowPos(), cell.getColPos()).getCards().get(i));
                        cardToPlay.getActions().get(0).useActionOn(targets, cardToPlay);
                        nextIterationForSetOfStates.add(newCopyOfState);
                    } if(nextIterationForSetOfStates.size() >= 900) break;
                } if(nextIterationForSetOfStates.size() >= 900) break;
            }
            setOfStates = nextIterationForSetOfStates;
        }
        System.out.println("Number of states after playing cards:"+setOfStates.size());
        Set<State> movementStates = new HashSet<State>();
        Set<State> nextIterationMovementStates = new HashSet<State>(), allStatesAfterMovement = new HashSet<State>();

        for(State aStartState:setOfStates) {
            State stateBeforeMove = new State(aStartState);
            stateBeforeMove.checkCardDeath();
            movementStates = new HashSet<State>();
            movementStates.add(stateBeforeMove);
            for (Card card : stateBeforeMove.getPlayersCardsFromTheBoard(stateBeforeMove.getCurrentPlayer())) {
                for (State aState : movementStates) {
                    if (!Support.isBattleCard(card) || aState.findCard(card.getID()).hasMoved()) {
                        nextIterationMovementStates.add(new State(aState)); continue;
                    }
                    int top, bottom, left, right;
                    CardThatCanBattle cardThatCanBattle = (CardThatCanBattle) card;

                    top = card.getCurrentLocation().getRowPos() - cardThatCanBattle.getMoveRange();
                    if (top < 0) top = 0;
                    left = card.getCurrentLocation().getColPos() - cardThatCanBattle.getMoveRange();
                    if (left < 0) left = 0;
                    bottom = card.getCurrentLocation().getRowPos() + cardThatCanBattle.getMoveRange();
                    if (aState.boardRowSize() - 1 < bottom) bottom = aState.boardRowSize() - 1;
                    right = card.getCurrentLocation().getColPos() + cardThatCanBattle.getMoveRange();
                    if (aState.boardColSize() - 1 < right) right = aState.boardColSize() - 1;
                    for (int row = top; row <= bottom; row++) for (int col = left; col <= right; col++) {
                        int cardR = card.getCurrentLocation().getRowPos(),cardC = card.getCurrentLocation().getColPos();
                        int distance = Support.getManhattanDistance(row, col, cardR, cardC);
                        if(distance == 0) { nextIterationMovementStates.add(new State(aState)); continue; }
                        if (distance > cardThatCanBattle.getMoveRange()) continue;
                        State newState = new State(aState);
                        Player opponent=null;
                        for(Player aPlayer:newState.getPlayers())
                            if(!aPlayer.equals(newState.getCurrentPlayer())) opponent = aPlayer;
                        Card opponentsBase = newState.findBase(opponent);
                        int opponentR = opponentsBase.getCurrentLocation().getRowPos();
                        int opponentC = opponentsBase.getCurrentLocation().getColPos();
                        int cardsDistanceToOpponentsBase = Support.getManhattanDistance(opponentR,opponentC,cardR,cardC);
                        int newLocationDistanceToOpponentsBase = Support.getManhattanDistance(opponentR,opponentC,row,col);
                        if(newLocationDistanceToOpponentsBase >= cardsDistanceToOpponentsBase) continue;
                        Card cardToMove = newState.findCard(card.getID());
                        cardToMove.getCurrentLocation().removeCard(cardToMove);
                        newState.newPlacementOfCard(cardToMove, row, col);
                        cardToMove.moved();
                        nextIterationMovementStates.add(newState);
                    }
                }
                movementStates = nextIterationMovementStates;
                nextIterationMovementStates = new HashSet<State>();
                if(movementStates.size() >= 700) break;
            }
            allStatesAfterMovement.addAll(movementStates);
            if(allStatesAfterMovement.size() >= 900) break;
        }
        setOfStates.clear(); nextIterationForSetOfStates.clear(); movementStates.clear(); nextIterationMovementStates.clear();

        System.out.println("States after movement:"+allStatesAfterMovement.size());
        // after move

        TreeNode treeNode;
        List<TreeNode> currentIterationTreeNodes = new ArrayList<TreeNode>();
        List<TreeNode> nextIterationTreeNodes = new ArrayList<TreeNode>();
        List<TreeNode> rootStates = new ArrayList<TreeNode>();

        // Try all attacks
        int totalStates = 0;
        for(State aStartState:allStatesAfterMovement) {
            List<List<Card>> allPairs = new ArrayList<List<Card>>();
            List<Card> pair = new ArrayList<Card>();
            List<Card> opponentsCards = new ArrayList<Card>();
            for(Player opponent:aStartState.getOpponentsOf(aStartState.getCurrentPlayer()))
                opponentsCards.addAll(aStartState.getPlayersCardsFromTheBoard(opponent));
            for(Card attacker:aStartState.getPlayersCardsFromTheBoard(aStartState.getCurrentPlayer())) for(Card target:opponentsCards) {
                if(attacker.getCurrentLocation() == null || target.getCurrentLocation() == null) continue;
                int distance = Support.getManhattanDistance(attacker.getCurrentLocation(), target.getCurrentLocation());
                if (attacker.getCardCategory().equals("Unit") && Support.isBattleCard(target) && distance <= ((UnitCard)attacker).getAttackRange())
                {
                    pair.clear(); pair.add(attacker); pair.add(target); allPairs.add(Support.copyCards(pair));
                }
            }
            TreeNode rootTreeNode = new TreeNode(new State(aStartState));
            rootStates.add(rootTreeNode);
            currentIterationTreeNodes.add(rootTreeNode);
            for(List<Card> aPair:allPairs) {
                UnitCard attacker = (UnitCard)aPair.get(0); CardThatCanBattle target = (CardThatCanBattle)aPair.get(1);
                for(TreeNode aNode:currentIterationTreeNodes) {
                    nextIterationTreeNodes.add(aNode);
                    Card cardThatMayNotHaveAttacked = aNode.getState().findCard(attacker.getID());
                    if(cardThatMayNotHaveAttacked == null || cardThatMayNotHaveAttacked.hasActed()) continue;
                    State newState, newerState;
                    //System.out.print("Attacker, "+attacker.getName()+": ");
                    Map<List<Damage>, Float> attack = attacker.whatAreThePossibleAttacks(target, attacker);
                    //System.out.print("ForPair:(N:"+attacker.getName()+" H:"+attacker.getHealth()+",N:"+target.getName()+" H:"+target.getHealth()+"). ");
                    List<List<Float>> damage = target.possibleDamage(attacker, attack), damage2;
                    //System.out.print("Target, "+target.getName()+": ");
                    //System.out.print(". Damage1size:"+damage.size());
                    attack = target.whatAreThePossibleAttacks(attacker, target);
                    damage2 = attacker.possibleDamage(target, attack); //System.out.println();
                    //System.out.println(". Damage2size:"+damage2.size());
                    //System.out.println("NumPoss:("+damage.size()+","+damage2.size()+")");
                    for(List<Float> aDamage:damage) {
                        newState = new State(aNode.getState());
                        CardThatCanBattle targetCard = (CardThatCanBattle)newState.findCard(target.getID());
                        if(targetCard == null || targetCard.getHealth()<1) continue;
                        targetCard.setHealth(targetCard.getHealth() - aDamage.get(1).intValue());
                        int distance = Support.getManhattanDistance(attacker.getCurrentLocation(), target.getCurrentLocation());
                        if(targetCard.getHealth()>0 && distance <= targetCard.getAttackRange()) for(List<Float> returnDamage:damage2) {
                            newerState = new State(newState);
                            UnitCard attackerCard = (UnitCard)newerState.findCard(attacker.getID());
                            attackerCard.acted();
                            attackerCard.setHealth(attackerCard.getHealth() - returnDamage.get(1).intValue());
                            if(attackerCard.getHealth()<1) attackerCard.getCurrentLocation().removeCard(attackerCard);
                            List<Card> key = new ArrayList<Card>();
                            key.add(attackerCard); key.add(newerState.findCard(target.getID()));
                            treeNode = new TreeNode(newerState);
                            aNode.addChild(key, treeNode, aDamage.get(0)*returnDamage.get(0));
                            nextIterationTreeNodes.add(treeNode);
                        } else {
                            targetCard.getCurrentLocation().removeCard(targetCard);
                            List<Card> key = new ArrayList<Card>();
                            Card attackerCard = newState.findCard(attacker.getID()); attackerCard.acted();
                            key.add(attackerCard); key.add(targetCard);
                            treeNode = new TreeNode(newState);
                            aNode.addChild(key, treeNode, aDamage.get(0));
                            nextIterationTreeNodes.add(treeNode);
                        }
                    } CardThatCanBattle at = attacker, ta = target;
                    //System.out.println("ForPair:(N:"+at.getName()+" H:"+at.getHealth()+",N:"+ta.getName()+" H:"+ta.getHealth()+") NumPoss:("+damage.size()+","+damage2.size()+"). NumOfKey"+aNode.numOfKey()+". \n");
                }
                System.out.print("AS:"+nextIterationTreeNodes.size()+". ");
                currentIterationTreeNodes = nextIterationTreeNodes;
                nextIterationTreeNodes = new ArrayList<TreeNode>();
                if(currentIterationTreeNodes.size() > 1000) break;
            }
            totalStates += currentIterationTreeNodes.size();
            System.out.print("ASE:"+currentIterationTreeNodes.size()+", TS:"+totalStates+" end. ");
            currentIterationTreeNodes.clear();
            if(totalStates > 1000) break;
        }System.out.println();

        allStatesAfterMovement.clear();

        // Try all actions
        List<TreeNode> leafTreeNodes = new ArrayList<TreeNode>();
        for(TreeNode rootState:rootStates) {
            leafTreeNodes.addAll(rootState.getLeafNodes());
        }
        List<Card> cards = new ArrayList<Card>();

        for(TreeNode aStartState:leafTreeNodes) {
            List<List<Card>> allPairs = new ArrayList<List<Card>>();
            List<Card> pair = new ArrayList<Card>();
            List<Card> opponentsCards = new ArrayList<Card>();
            aStartState.getState().checkCardDeath();
            for(Player opponent:aStartState.getState().getOpponentsOf(player))
                opponentsCards.addAll(aStartState.getState().getPlayersCardsFromTheBoard(opponent));
            for(Card user:aStartState.getState().getPlayersCardsFromTheBoard(player)) for(Card target:opponentsCards) {
                List<Action> targetCardActions = new ArrayList<Action>();
                for(Action action:user.getActions()) if(action.actionTypeIs("TargetCard")) targetCardActions.add(action);
                if (!(Support.isBattleCard(target) && targetCardActions.size()>0)) continue;
                pair.clear(); pair.add(user); pair.add(target); allPairs.add(Support.copyCards(pair));
            }
            currentIterationTreeNodes.add(aStartState);
            for(List<Card> aPair:allPairs) {
                UnitCard user = (UnitCard)aPair.get(0); CardThatCanBattle target = (CardThatCanBattle)aPair.get(1);
                int distance = Support.getManhattanDistance(user.getCurrentLocation(), target.getCurrentLocation());
                for(TreeNode aNode:currentIterationTreeNodes) {
                    nextIterationTreeNodes.add(aNode);
                    if(aNode.getState().findCard(user.getID()).hasActed()) continue;
                    State newState;
                    cards.clear();
                    for(Action action:user.getActions()) if(action.actionTypeIs("TargetCard") && action.range()<=distance) {
                        newState = new State(aNode.getState());
                        Card userCard = newState.findCard(user.getID());
                        Card targetCard = newState.findCard(target.getID());
                        cards.add(targetCard);
                        action.useActionOn(cards,userCard);

                        List<Card> key = new ArrayList<Card>();
                        key.add(userCard); key.add(targetCard);
                        treeNode = new TreeNode(newState);
                        aNode.addChildForAction(key, treeNode, action);
                        nextIterationTreeNodes.add(treeNode);
                    }
                }
                currentIterationTreeNodes = nextIterationTreeNodes;
                nextIterationTreeNodes = new ArrayList<TreeNode>();
            }
            System.out.print("AcS:"+currentIterationTreeNodes.size()+". ");
            currentIterationTreeNodes.clear();
        } System.out.println();

        leafTreeNodes.clear();
        for(TreeNode rootState:rootStates) {
            leafTreeNodes.addAll(rootState.getLeafNodes());
        }
        System.out.println("Number of states after attack and use action:"+leafTreeNodes.size());
        currentIterationTreeNodes.clear(); nextIterationTreeNodes.clear(); leafTreeNodes.clear();

        //state card movement
        /*Map<State, TreeNode> stateTreeNodeMap, nextIterationTreeNodeMap, allTreeNodeMap;
        nextIterationTreeNodeMap = new HashMap<State, TreeNode>();
        allTreeNodeMap = new HashMap<State, TreeNode>();

        for(TreeNode aStartState:leafTreeNodes) {
            State stateForTreeNodeMap = new State(aStartState.getState());
            stateForTreeNodeMap.checkCardDeath();
            stateTreeNodeMap = new HashMap<State, TreeNode>();
            stateTreeNodeMap.put(stateForTreeNodeMap, aStartState);
            for (Card card : stateForTreeNodeMap.getPlayersCardsFromTheBoard(stateForTreeNodeMap.getCurrentPlayer())) {
                for (State aState : stateTreeNodeMap.keySet()) {
                    if (!Support.isBattleCard(card) || aState.findCard(card.getID()).hasMoved()) {
                        nextIterationTreeNodeMap.put(new State(aState), aStartState); continue;
                    }
                    int top, bottom, left, right;
                    CardThatCanBattle cardThatCanBattle = (CardThatCanBattle) card;

                    top = card.getCurrentLocation().getRowPos() - cardThatCanBattle.getMoveRange();
                    if (top < 0) top = 0;
                    left = card.getCurrentLocation().getColPos() - cardThatCanBattle.getMoveRange();
                    if (left < 0) left = 0;
                    bottom = card.getCurrentLocation().getRowPos() + cardThatCanBattle.getMoveRange();
                    if (aState.boardRowSize() - 1 < bottom) bottom = aState.boardRowSize() - 1;
                    right = card.getCurrentLocation().getColPos() + cardThatCanBattle.getMoveRange();
                    if (aState.boardColSize() - 1 < right) right = aState.boardColSize() - 1;
                    for (int row = top; row <= bottom; row++) for (int col = left; col <= right; col++) {
                        int cardR = card.getCurrentLocation().getRowPos(),cardC = card.getCurrentLocation().getColPos();
                        int distance = Support.getManhattanDistance(row, col, cardR, cardC);
                        if(distance == 0) { nextIterationTreeNodeMap.put(new State(aState), aStartState); continue; }
                        if (distance > cardThatCanBattle.getMoveRange()) continue;
                        State newState = new State(aState);
                        Player opponent=null;
                        for(Player aPlayer:newState.getPlayers())
                            if(!aPlayer.equals(newState.getCurrentPlayer())) opponent = aPlayer;
                        Card opponentsBase = newState.findBase(opponent);
                        int opponentR = opponentsBase.getCurrentLocation().getRowPos();
                        int opponentC = opponentsBase.getCurrentLocation().getColPos();
                        int cardsDistanceToOpponentsBase = Support.getManhattanDistance(opponentR,opponentC,cardR,cardC);
                        int newLocationDistanceToOpponentsBase = Support.getManhattanDistance(opponentR,opponentC,row,col);
                        if(newLocationDistanceToOpponentsBase >= cardsDistanceToOpponentsBase) continue;
                        Card cardToMove = newState.findCard(card.getID());
                        cardToMove.getCurrentLocation().removeCard(cardToMove);
                        newState.newPlacementOfCard(cardToMove, row, col);
                        cardToMove.moved();
                        nextIterationTreeNodeMap.put(newState, aStartState);
                    }
                }
                stateTreeNodeMap = nextIterationTreeNodeMap;
                nextIterationTreeNodeMap = new HashMap<State, TreeNode>();
            }
            allTreeNodeMap.putAll(stateTreeNodeMap);
        }

        System.out.println("States after movement:"+allTreeNodeMap.keySet().size());

        Map<TreeNode, List<State>> allFollowingStatesToTreeNode = new HashMap<TreeNode, List<State>>();
        for(State finalState:allTreeNodeMap.keySet()) {
            if(!allFollowingStatesToTreeNode.containsKey(allTreeNodeMap.get(finalState)))
                allFollowingStatesToTreeNode.put(allTreeNodeMap.get(finalState), new ArrayList<State>());
            allFollowingStatesToTreeNode.get(allTreeNodeMap.get(finalState)).add(finalState);
        }

        allTreeNodeMap.clear();
        for(TreeNode aTreeNode:allFollowingStatesToTreeNode.keySet()) {
            State bestState = null;
            for(State finalState:allFollowingStatesToTreeNode.get(aTreeNode))
                if(bestState == null || stateScore(bestState)<stateScore(finalState)) bestState = finalState;
            allTreeNodeMap.put(bestState, aTreeNode);
        }

        for(State finalState:allTreeNodeMap.keySet())
            allTreeNodeMap.get(finalState).setStateScore(stateScore(finalState));*/

        System.out.println("Number of rootStates:"+rootStates.size());
        // Select a best state based on score
        for(TreeNode rootState:rootStates) rootState.setTreesScore();
        TreeNode bestRootState = null; boolean entered = false;
        for(TreeNode rootState:rootStates) {
            entered = true;
            if(bestRootState == null) bestRootState = rootState;
            else if(bestRootState.getStateScore()<rootState.getStateScore()) bestRootState = rootState;
        } if(entered) System.out.println("Did enter"); else System.out.println("Did not enter");
        rootStates.clear();

        if(bestRootState == null) System.out.println("BestRootState is null");
        treeNode = bestRootState;
        State currentState;
        System.out.print("Perform attack: "); int num = 0;
        while(treeNode.canAttack()) { // perform all attacks
            System.out.print(num+". "); num++;
            currentState = treeNode.getState();
            List<Card> bestAttack = treeNode.getBestAttack();
            if(bestAttack == null) break;
            UnitCard attacker = (UnitCard)currentState.findCard(bestAttack.get(0).getID());
            CardThatCanBattle target = (CardThatCanBattle)currentState.findCard(bestAttack.get(1).getID());

            if(!attacker.hasActed()) {
                List<String> terrainPlacement = Support.terrainsUsedForBattle(attacker, target);
                List<Damage> attack = attacker.whatIsYourAttack(target, terrainPlacement.get(1),
                        attacker, terrainPlacement.get(0));
                target.takeHit(attacker, terrainPlacement.get(0), terrainPlacement.get(1), attack);
                attacker.acted();
                if (target.getHealth() == 0) {
                    target.getCurrentLocation().removeCard(target);
                }
                int distance = Support.getManhattanDistance(attacker.getCurrentLocation(), target.getCurrentLocation());
                if (target.getHealth() > 0 && distance <= target.getAttackRange()) {
                    attack = target.whatIsYourAttack(attacker, terrainPlacement.get(0),
                            target, terrainPlacement.get(1));
                    attacker.takeHit(target, terrainPlacement.get(1), terrainPlacement.get(0), attack);
                }
                if (attacker.getHealth() == 0) {
                    attacker.getCurrentLocation().removeCard(attacker);
                }
            }
            TreeNode old = treeNode;
            treeNode = treeNode.getTreeNodeWithState(currentState);
            //if(treeNode == null) {
                System.out.println("TreeNode is null. Old TreeNode was "+old+". Attacker is ("+attacker.getID()+","+attacker.getName()+","+attacker.getHealth()+", ("+attacker.getCurrentLocation().getRowPos()+","+attacker.getCurrentLocation().getColPos()+")) target is ("+target.getID()+","+target.getName()+","+target.getHealth()+", ("+target.getCurrentLocation().getRowPos()+","+target.getCurrentLocation().getColPos()+"))");
                old.whatAttacks();
            //}
        } System.out.println();

        treeNode = treeNode.getBestLeafState(); //tree state after all actions are made

        State stateAtTheEndOfPlayersTurn = treeNode.getState();
        /*if(allTreeNodeMap.size() > 0) System.out.print("Has treeNodes: "); else System.out.print("Has not got treeNodes.");
        for(State finalState:allTreeNodeMap.keySet()) if(allTreeNodeMap.get(finalState).getState().equals(treeNode.getState())) {
            System.out.print("final state set. ");
            stateAtTheEndOfPlayersTurn = finalState;
        } System.out.println();*/

        if((stateAtTheEndOfPlayersTurn.getPlayersCardsFromTheBoard(newCopyOfState.getCurrentPlayer()).size()+
           stateAtTheEndOfPlayersTurn.getCurrentPlayer().getUnitDeck().size()+stateAtTheEndOfPlayersTurn.getCurrentPlayer().getCards().size()) == 0) {
            System.out.println("Can not win");
        }

        if(stateAtTheEndOfPlayersTurn == null) System.out.println("Future state is null");
        if(stateAtTheEndOfPlayersTurn != null) { callBackClass.setState(stateAtTheEndOfPlayersTurn); player = stateAtTheEndOfPlayersTurn.getPlayer(id); System.out.println("Gold at the end of turn:"+stateAtTheEndOfPlayersTurn.getCurrentPlayer().getGold()); }
        callBackClass.endTurn();
        /*for(Player aPlayer:stateAtTheEndOfPlayersTurn.getPlayers()) {
            System.out.println("Player"+aPlayer.getID()+" opponent cards at base:");
            Card base = stateAtTheEndOfPlayersTurn.findBase(aPlayer);
            Cell cell = stateAtTheEndOfPlayersTurn.getCell(base.getCurrentLocation().getRowPos(),base.getCurrentLocation().getColPos());
            for(Card card:cell.getCards()) if(!card.getOwner().equals(aPlayer)) System.out.print(card.getName()+". ");
            System.out.println();
        }*/
        stateAtTheEndOfPlayersTurn.allCardsOnBoardAndLocation();
        System.out.print("Time AI took to make a move is( ");
        long timeTaken = System.currentTimeMillis() - startTime; timeTaken *= 0.001;
        if(timeTaken>=60) { int minute=0; while(timeTaken>=60) { minute++; timeTaken = timeTaken-60; } System.out.print("Minutes:"+minute+", "); }
        System.out.println("Seconds:"+timeTaken+").\n");
    }

    private int stateScore(State state) {
        if(state == null) return Integer.MIN_VALUE;
        List<Card> myCardsOnBoard = state.getPlayersCardsFromTheBoard(state.getCurrentPlayer());
        int score = 0, distance, numberOfUnitOnHand, numberOfUnitsOnBoard, unitTotal;
        numberOfUnitsOnBoard = Support.numberOfCardsInAGivenCategory("Unit", myCardsOnBoard);
        for(Card card:myCardsOnBoard) {
            if(card.getCardCategory().equals("Unit")) {
                for(Base base:baseAndDistance.keySet()) {
                    distance = baseAndDistance.get(base)-Support.getManhattanDistance(card.getCurrentLocation(), base.getCurrentLocation());
                    score += averageDeckCost*distance*Math.pow(1.2, distance);
                }
                UnitCard uc=(UnitCard)card; score += card.getCost()*0.65;
                score += (uc.getMaxHealth()-(uc.getMaxHealth()-uc.getHealth()))*uc.getCost()*0.65;
            }
            else if(card.getCardCategory().equals("Structure")) {
                StructureCard sc=(StructureCard)card; score += sc.getCost()*0.55;
                score += (sc.getMaxHealth()-(sc.getMaxHealth()-sc.getHealth()))*sc.getCost()*0.55;
            }
            else score += card.getCost()*1.1;
        }
        for(Player opponent:state.getOpponentsOf(state.getCurrentPlayer())) for(Card card:state.getPlayersCardsFromTheBoard(opponent)) {
            if(card.getCardCategory().equals("Unit")) {
                UnitCard uc=(UnitCard)card; score -= card.getCost()*0.65;
                score -= (uc.getMaxHealth()-(uc.getMaxHealth()-uc.getHealth()))*uc.getCost()*0.65;
            }
            else if(card.getCardCategory().equals("Structure")) {
                StructureCard sc=(StructureCard)card; score -= sc.getCost()*0.55;
                score -= (sc.getMaxHealth()-(sc.getMaxHealth()-sc.getHealth()))*sc.getCost()*0.55;
            }
            else score -= card.getCost()*1.1;
        }
        int givesBestScore=0, secondBestScore=0;
        for(Card card:myBase.getCurrentLocation().getCards(state.getCurrentPlayer())) {
            if(card.getCardCategory().equals("Unit")) {
                UnitCard uc = (UnitCard)card; int allDefence=0;
                for(Defence defence:uc.getDefences()) allDefence += defence.getDefence();
                if (givesBestScore < (uc.getHealth()+allDefence*3)) {
                    secondBestScore = givesBestScore; givesBestScore = uc.getHealth()+allDefence*3;
                }
                else if (secondBestScore < (uc.getHealth()+allDefence*3)) givesBestScore = uc.getHealth()+allDefence*3;
            }
        }
        numberOfUnitOnHand = state.getCurrentPlayer().numberOfCardsOfGivenCategoryOnHand("Unit");
        unitTotal = numberOfUnitsOnBoard+numberOfUnitOnHand+state.getCurrentPlayer().getUnitDeck().size();
        if(unitTotal>2) score += givesBestScore+secondBestScore; else if(unitTotal>1) score += givesBestScore;
        for(Base base:baseAndDistance.keySet()) {
            List<Card> cards=base.getCurrentLocation().getCards(base.getOwner());
            List<Card> myCards=base.getCurrentLocation().getCards(state.getCurrentPlayer());
            if(Support.numberOfCardsInAGivenCategory("Unit", cards) == 0 &&
               Support.numberOfCardsInAGivenCategory("Unit", myCards) > 0)
            {
                score += averageDeckCost*1000000;
            }
            cards=myBase.getCurrentLocation().getCards(base.getOwner());
            myCards=myBase.getCurrentLocation().getCards(state.getCurrentPlayer());
            if(Support.numberOfCardsInAGivenCategory("Unit", myCards) == 0 &&
               Support.numberOfCardsInAGivenCategory("Unit", cards) > 0)
            {
                score -= averageDeckCost*100000000;
            }
        }

        if(numberOfUnitOnHand+numberOfUnitsOnBoard==0) return score;
        double outcome2, outcome = -(numberOfUnitsOnBoard/(numberOfUnitOnHand+numberOfUnitsOnBoard));
        outcome *= Math.log(numberOfUnitsOnBoard/(numberOfUnitOnHand+numberOfUnitsOnBoard))/Math.log(2);
        outcome2 = -(numberOfUnitOnHand/(numberOfUnitOnHand+numberOfUnitsOnBoard));
        outcome2 *= Math.log(numberOfUnitOnHand/(numberOfUnitOnHand+numberOfUnitsOnBoard))/Math.log(2);
        outcome += outcome2; outcome *= 2*averageDeckCost; score += outcome;
        return score;
    }
}