/*
 * SMInstanceTester.java: Tests for the SMInstance class. Performs
 * basic tests for the the follwoing methods: getmatching,
 * setMatching, getBlockingPair, isStable, and
 * computeStableMatching. To test a single method, comment out the
 * other tests in the main method below.
 */


public class SMInstanceTester {

    public static void main (String[] args) {

	// test getMatching
	System.out.println("Testing 'getMatching' method...");
	if (testGetMatchingMethod()) 
	    System.out.println("Test passed!\n");
	else
	    System.out.println("Test failed!\n");

	// test setMatching
	System.out.println("Testing 'setMatching' method...");
	if (testSetMatchingMethod()) 
	    System.out.println("Test passed!\n");
	else
	    System.out.println("Test failed!\n");


	// test getBlockingPair
	System.out.println("Testing 'getBlockingPair' method...");
	if (testGetBlockingPairMethod()) 
	    System.out.println("Test passed!\n");
	else
	    System.out.println("Test failed!\n");


	// test isStable
	System.out.println("Testing 'isStable' method...");
	if (testIsStableMethod()) 
	    System.out.println("Test passed!\n");
	else
	    System.out.println("Test failed!\n");

	
	// test computeStableMatching
	System.out.println("Testing 'computeStableMatching' method...");
	if (testComputeStableMatchingMethod()) 
	    System.out.println("Test passed!\n");
	else
	    System.out.println("Test failed!\n");

    }


    public static boolean testGetMatchingMethod () {
	SMInstance smi = new SMInstance("small-instance.txt");
	Agent a = smi.getAgentByName("a");
	Agent b = smi.getAgentByName("b");
	Agent g = smi.getAgentByName("g");
	Agent h = smi.getAgentByName("h");

	a.setCurMatch(g);
	g.setCurMatch(a);
	
	Matching m = smi.getMatching();

	// a and g are matched and should be contained in m
	if (m.getMatch(a) != g || m.getMatch(g) != a) {
	    System.out.println("Matched pair not contained in matching.");
	    return false;
	}

	// b and h are both unmatched and should not be contained in m
	if (m.getMatch(b) != null || m.getMatch(h) != null) {
	    System.out.println("Unmatched pair found in matching.");
	    return false;
	}

	// m should have size 1
	int size = 0;
	for (Pair<Agent> p : m) {
	    size++;
	}

	if (size != 1) {
	    System.out.println("Matching incorrect size. Only (non-null) matched pairs should be contained in matching.");
	    return false;
	}

	b.setCurMatch(h);
	h.setCurMatch(b);

	m = smi.getMatching();

	// b and h are matched and should be contained in m
	if (m.getMatch(b) != h || m.getMatch(h) != b) {
	    System.out.println("Matched pair not contained in matching.");
	    return false;
	}

	// m should have size 2
	size = 0;
	for (Pair<Agent> p : m) {
	    size++;
	}

	if (size != 2) {
	    System.out.println("Matching incorrect size.");
	    return false;
	}

	return true;
    }

    public static boolean testSetMatchingMethod () {
	SMInstance smi = new SMInstance("small-instance.txt");
	Agent a = smi.getAgentByName("a");
	Agent b = smi.getAgentByName("b");
	Agent g = smi.getAgentByName("g");
	Agent h = smi.getAgentByName("h");

	Matching m = new Matching(smi);

	m.addPair(new Pair<Agent>(a, g));

	smi.setMatching(m);

	if (a.getCurMatch() != g || g.getCurMatch() != a) {
	    System.out.println("First pair not matched!");
	    return false;
	}
	
	m.addPair(new Pair<Agent>(b, h));

	smi.setMatching(m);

	if (b.getCurMatch() != h || h.getCurMatch() != b) {
	    System.out.println("Second pair not matched!");
	    return false;
	}

	return true;
    }

    public static boolean testGetBlockingPairMethod () {
	SMInstance smi = new SMInstance("small-instance.txt");
	
	Agent a = smi.getAgentByName("a");
	Agent b = smi.getAgentByName("b");
	Agent g = smi.getAgentByName("g");
	Agent h = smi.getAgentByName("h");

	Matching m = new Matching(smi);

	m.addPair(new Pair<Agent>(a, g));

	smi.setMatching(m);

	Pair<Agent> p = smi.getBlockingPair();

	if (p == null || p.getFirst() != b || p.getSecond() != h) {
	    System.out.println("Unmatched blocking pair not identified.");
	    return false;
	}

	m.addPair(new Pair<Agent>(b, h));
	smi.setMatching(m);

	p = smi.getBlockingPair();

	if (p != null) {
	    System.out.println("Blocking pair found in stable matching.");
	    return false;
	}

	m = new Matching(smi);
	m.addPair(new Pair<Agent>(a, h));
	m.addPair(new Pair<Agent>(b, g));
	smi.setMatching(m);
	p = smi.getBlockingPair();

	if (p == null || p.getFirst() != a || p.getSecond() != g) {
	    System.out.println("Blocking pair not found or incorrect blocking pair found in matching.");
	    return false;
	}

	return true;
	    

    }

    public static boolean testIsStableMethod () {
	SMInstance smi = new SMInstance("small-instance.txt");
	
	Agent a = smi.getAgentByName("a");
	Agent b = smi.getAgentByName("b");
	Agent g = smi.getAgentByName("g");
	Agent h = smi.getAgentByName("h");

	Matching m = new Matching(smi);

	m.addPair(new Pair<Agent>(a, g));

	smi.setMatching(m);

	if (smi.isStable()) {
	    System.out.println("Incomplete matching should not be stable.");
	    return false;
	}

	m.addPair(new Pair<Agent>(b, h));
	smi.setMatching(m);

	if (!smi.isStable()) {
	    System.out.println("Stable matching not identified as stable.");
	    return false;
	}

	m = new Matching(smi);
	m.addPair(new Pair<Agent>(a, h));
	m.addPair(new Pair<Agent>(b, g));
	smi.setMatching(m);

	if (smi.isStable()) {
	    System.out.println("Unstable matching identified as stable.");
	    return false;
	}

	return true;
    }

    public static boolean testComputeStableMatchingMethod () {
	SMInstance smi = new SMInstance("small-instance.txt");
	
	Agent a = smi.getAgentByName("a");
	Agent b = smi.getAgentByName("b");
	Agent g = smi.getAgentByName("g");
	Agent h = smi.getAgentByName("h");

	smi.computeStableMatching();
	Matching m = smi.getMatching();

	if (m.getMatch(a) != g || m.getMatch(g) != a ||
	    m.getMatch(b) != h || m.getMatch(h) != b) {
	    System.out.println("Stable matching not found (unique stable matching).");
	    return false;
	}

	smi = new SMInstance("multiple-sm-instance.txt");
	
	a = smi.getAgentByName("a");
	b = smi.getAgentByName("b");
	g = smi.getAgentByName("g");
	h = smi.getAgentByName("h");

	smi.computeStableMatching();
	m = smi.getMatching();
	
	if (m.getMatch(a) != g || m.getMatch(g) != a ||
	    m.getMatch(b) != h || m.getMatch(h) != b) {
	    System.out.println("Incorrect stable matching found.");
	    return false;
	}

	return true;
    }    
}
