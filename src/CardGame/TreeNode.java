package CardGame;

import java.util.*;

/**
 * Created by Kristján on 20.6.2014.
 */
public class TreeNode {

    private State state;
    private float stateScore;
    private Map<List<Card>, Map<TreeNode, Float>> attacks;
    private Map<List<Card>, Map<Action, TreeNode>> actions;
    private Map<List<Card>, Float> scorePerAttack;
    private Map<List<Card>, Map<Action, Float>> scorePerAction;


    TreeNode(State data) {
        this.state = data;
        stateScore = 0;
        attacks = new HashMap<List<Card>, Map<TreeNode, Float>>();
        actions = new HashMap<List<Card>, Map<Action, TreeNode>>();
        scorePerAttack = new HashMap<List<Card>, Float>();
        scorePerAction = new HashMap<List<Card>, Map<Action, Float>>();
    }

    public State getState() { return state; }

    public void setStateScore(int newScore) { stateScore = newScore; }
    public float getStateScore() { return stateScore; }
    public boolean canAttack() { return !attacks.isEmpty(); }
    public List<Card> getBestAttack() {
        List<Card> bestKey = null;
        for(List<Card> key: scorePerAttack.keySet()) {
            if(bestKey==null) bestKey=key;
            if(scorePerAttack.get(bestKey)< scorePerAttack.get(key) && key.get(0).isOnBoard() && key.get(1).isOnBoard()) bestKey = key;
        } if(bestKey == null || !bestKey.get(0).isOnBoard() || !bestKey.get(1).isOnBoard()) return null;
        return bestKey;
    }
    public TreeNode getBestLeafState() {
        if(actions.isEmpty()) return this;
        List<Card> bestPair = null; Action bestAction = null;
        for(List<Card> key:scorePerAction.keySet()) for(Action action:scorePerAction.get(key).keySet()) {
            if(bestPair == null) bestPair = key; if(bestAction == null) bestAction = action;
            else if(scorePerAction.get(bestPair).get(bestAction)<scorePerAction.get(key).get(action)) {
                bestPair = key; bestAction = action;
            }
        } return actions.get(bestPair).get(bestAction).getBestLeafState();
    }
    public TreeNode getTreeNodeWithState(State state) {
        if(!attacks.isEmpty()) for(Map<TreeNode, Float> mapOfStates:attacks.values()) {
            for(TreeNode treeNode:mapOfStates.keySet()) if(treeNode.getState().equals(state)) return treeNode;
        }
        return null;
    }
    public void whatAttacks() {
        if(attacks.isEmpty()) System.out.println("Attack is empty");
        else {
            for(List<Card> pair:attacks.keySet()) {
                CardThatCanBattle a = (CardThatCanBattle)pair.get(0), t = (CardThatCanBattle)pair.get(1);
                System.out.println("For pair ("+a.getID()+","+a.getName()+","+t.getHealth()+","+t.getCurrentLocation()+"), ("+t.getID()+","+t.getName()+","+t.getHealth()+","+t.getCurrentLocation()+"):");
                for(TreeNode treeNode:attacks.get(pair).keySet())
                    System.out.print("TreeNode "+treeNode+", chance "+attacks.get(pair).get(treeNode)+". ");
                if(attacks.get(pair).keySet().size()>0) System.out.println();
            }
        }
    }

    public void addChild(List<Card> key, TreeNode child, Float probability) {
        Map<TreeNode, Float> children;
        if(!attacks.containsKey(key)) {
            children = new HashMap<TreeNode, Float>();
            attacks.put(key, children);
        }
        children = attacks.get(key);
        if(!children.containsKey(child)) children.put(child, probability);
        else children.put(child, probability+children.get(child));
    }

    public int numOfKey() { return attacks.keySet().size(); }

    public void addChildForAction(List<Card> key, TreeNode child, Action action) {
        if(!actions.containsKey(key)) { actions.put(key, new HashMap<Action, TreeNode>()); }
        Map<Action, TreeNode> actionTreeNodeMap = actions.get(key);
        actionTreeNodeMap.put(action, child);
    }

    public List<TreeNode> getLeafNodes() {
        List<TreeNode> leafs = new ArrayList<TreeNode>();
        //if(attacks.isEmpty()) { leafs.add(this); return leafs; }
        if(!attacks.isEmpty()) for(List<Card> key: attacks.keySet()) for(TreeNode treeNode: attacks.get(key).keySet())
            leafs.addAll(treeNode.getLeafNodes());
        else if(actions.isEmpty()) { leafs.add(this); return leafs; }
        else {
            for(Map<Action, TreeNode> map:actions.values()) for(TreeNode treeNode:map.values())
                leafs.addAll(treeNode.getLeafNodes());
        }
        return leafs;
    }

    public float setTreesScore() {
        float score;
        if(!attacks.isEmpty()) {
            for (List<Card> key : attacks.keySet()) {
                score = 0;
                Map<TreeNode, Float> children = attacks.get(key);
                for (TreeNode node : children.keySet()) { score += node.setTreesScore() * children.get(node); }
                scorePerAttack.put(key, score);
            }
            score = Integer.MIN_VALUE;
            for (Float number : scorePerAttack.values()) if (score < number) score = number;
            stateScore = score; return score;
        } else if(actions.isEmpty()) return stateScore;
        else {
            score = Integer.MIN_VALUE;
            for(List<Card> key : actions.keySet()) for(Action action : actions.get(key).keySet()) {
                float childScore = actions.get(key).get(action).setTreesScore();
                if(!scorePerAction.containsKey(key)) scorePerAction.put(key, new HashMap<Action, Float>());
                scorePerAction.get(key).put(action, childScore);
                if(score < childScore) score = childScore;
            }
            stateScore = score; return score;
        }
    }
}
