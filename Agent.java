/*
 * Agent: A Class representing an agent with preferences in the form
 * of a ranking over other Agents.
 *
 * The main functionality is through the "proposal" and "refusal"
 * methods. These methods allow one to compute a stable matching using
 * McVitie and Wilson's algorithm
 */

import java.util.ArrayList;

public class Agent {

    // the name of this agent
    private String name;

    // the preference list for this Agent order from most to least
    // preferred (i.e., Agent at index 0 is most preferred, etc.)
    private ArrayList<Agent> prefList;  

    // index of current match in prefList (-1 indicates no current match)
    private int curIndex = -1;

    // reference to current match
    private Agent curMatch = null;

    // initialize this Agent with a name and empty prefList
    public Agent(String name) {
	this.name = name;
	prefList = new ArrayList<Agent>();
    }

    // getters for name, current match, and prefList
    public String getName() { return name; }
    public Agent getCurMatch() { return curMatch; }
    public ArrayList<Agent> getPrefList () { return prefList; }

    // sets current match and updates curIndex accordingly
    public void setCurMatch (Agent match) {
	curMatch = match;
	curIndex = prefList.indexOf(match);
    }

    // string representation of an Agent is just their name
    public String toString() { return name; }

    // append Agent a to the prefList
    public void appendToPrefList (Agent a) {
	prefList.add(a);
    }

    /*
     * Method: boolean prefers (Agent a, Agent b)
     * 
     * Returns true if this Agent prefers a to b---i.e., if a appears
     * before b on this prefList. Preference is strict: i.e., this
     * does not prefer a to a. Addtionally, this method should deal
     * with the case where (1) one of the agents a and b could be
     * null, and (2) a and/or b do not appear in
     * prefList. Specifically, the following behavior is expected:
     *   1. if a is not contained in prefList, return false
     *   2. if a is in prefList and b is null or not on prefList,
     *      return true
     *   3. if a is null and b is not in prefList, return true.
     * Otherwise, if a and b are both contained in prefList, return
     * true if and only if only if a appears before b.
     *
     * Interpretation of null: In the specification above, we think of
     * a null agent as representing this agent having no match at
     * all. In particular, item (3) above means that this Agent should
     * prefer being unmatched (i.e., being matched with null) over
     * being matched with anyone not appearing in this Agent's
     * prefList.
     */
    public boolean prefers (Agent a, Agent b) {
        if(prefList.indexOf(a) == -1){
            return false;
        }
        if(prefList.indexOf(a) > -1 && (b == null ||prefList.indexOf(b) == -1)){
            return true;
        }
        if(a == null && prefList.indexOf(b) == -1){
            return true;
        }
        return(prefList.indexOf(a) < prefList.indexOf(b));
    }

    /*
     * Method void proposal (Agent a)
     *
     * This method gets called when Agent a proposes to this Agent. If
     * this Agent prefers its current match to a, a is
     * refused. Otherwise, this Agent updates its current match to a
     * (and its current index accordingly). If its previous current
     * match was not null, its previous match should receive a
     * refusal.
     */
    public void proposal (Agent a) {
        if(prefers(curMatch, a)){
            a.refusal();
        }
        else if (prefers(a, curMatch)){
                Agent previous = curMatch;
                setCurMatch(a);
                if(previous != null){
                    previous.refusal();
                }
        }
    }

    /*
     * Method: void refusal ()
     *
     * refusal() gets call on this Agent when this agent receives a
     * refusal from its previous match. When this occurs, this Agent
     * should propose to the "next" agent in prefList (if any). That
     * is, the "next" Agent is the one immediately following the
     * curMatch (if any). If this Agent had not made any proposals
     * previously, then refusal() should result in this Agent
     * proposing to the first agent in prefList.
     */
    public void refusal () {
        if(curMatch == null){
            setCurMatch(prefList.get(0));
            prefList.get(0).proposal(this);
        }
        else if(curIndex + 1 < prefList.size()){
            setCurMatch(prefList.get(curIndex + 1));
            prefList.get(curIndex).proposal(this);
        }
        else{
            setCurMatch(null);
        }
    }

    // reset this Agent's match to its pristine state: curMatch is
    // null, curIndex is -1
    public void reset () {
	curIndex = -1;
	curMatch = null;
    }
}
