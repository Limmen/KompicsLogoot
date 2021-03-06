package se.kth.app.logoot;

import com.google.common.collect.Lists;
import se.kth.app.events.Patch;
import se.sics.ktoolbox.util.identifiable.Identifier;

import java.util.*;

/**
 * Logoot-undo document
 *
 * @author Maxime Dufour on 2017-04-07.
 */
public class Document {

    private Identifier site;
    private Random random = new Random();
    private ArrayList<String> documentLines;
    private ArrayList<LineId> idTable;
    private HashMap<UUID, Patch> hb;
    private HashMap<LineId, Integer> cemetery;

    /**
     * Class constructor. Initializes document for the given site by creating two identifiers, one marking the beginning of document
     * and one marking the end of the document.
     *
     * @param site
     */
    public Document(Identifier site) {
        this.site = site;
        documentLines = new ArrayList<>();
        idTable = new ArrayList<>();
        hb = new HashMap<>();
        cemetery = new HashMap<>();
        idTable.add(new LineId(Lists.newArrayList(new Position(0, null, null))));
        idTable.add(new LineId(Lists.newArrayList(new Position(Integer.MAX_VALUE - 1, null, null))));
    }


    /**
     * Execute a patch of operations
     *
     * @param patch - list of operations from a single peer
     */
    public void execute(Patch patch) {
        for (Operation op : patch.getOps()) {
            int position, degree;
            switch (op.getOperationType()) {
                case INSERT:
                    degree = getCemetery(op.getId()) + 1;
                    if (degree != 1) {
                        setCemetery(op.getId(), degree);
                        continue;
                    }
                    if (!idTable.contains(op.getId())) {
                        idTable.add(op.getId());
                        Collections.sort(idTable);
                        position = Collections.binarySearch((List) idTable, op.getId());
                        if (position > documentLines.size())
                            documentLines.add(op.getContent());
                        else
                            documentLines.add(position - 1, op.getContent());
                    }
                    break;
                case DELETE:
                    if(idTable.contains(op.getId())) {
                        position = Collections.binarySearch((List) idTable, op.getId());
                        documentLines.remove(position - 1);
                        idTable.remove(position);
                        degree = 0;
                    } else {
                        degree = getCemetery(op.getId()) - 1;
                    }
                    setCemetery(op.getId(), degree);
                    break;
            }
        }

    }

    /**
     * Generates N lineIds between LineId p and LineId q with boundary limiting the distance between two successive
     * Id's
     *
     * @param p        lineId p
     * @param q        lineId q
     * @param N        Number of lines to generate
     * @param boundary boundary between successive lines
     * @return
     */
    public List<LineId> generateLineId(LineId p, LineId q, int N, int boundary, int clock) {
        List<LineId> list = new ArrayList<>();
        int index = 0;
        int interval = 0;
        while (interval < N) {
            index++;
            interval = prefix(q, index) - prefix(p, index) - 1;
        }
        int step = Math.min(interval / N, boundary);
        int r = prefix(p, index);
        for (int j = 0; j < N; j++) {
            list.add(construct(r + random.nextInt(step) + 1, p, q, clock));
            r = r + step;
        }
        return list;
    }

    /**
     * Returns a number in base Integer.MAX_VALUE where the digits of this number are the ith first position digits
     * of lineId p (filled with 0 if |p| < i).
     *
     * @param p lineId
     * @param i number of digits
     * @return prefix number
     */
    public int prefix(LineId p, int i) {
        String digits = "";
        for (int j = 0; j < i; j++) {
            if (j < p.getPositions().size())
                digits = digits + p.getPositions().get(j).getDigit();
            else
                digits = digits + "0";
        }
        return Integer.parseInt(digits);
    }

    /**
     * Constructs a lineId inbetween lineId p and lineId q with the digits in r
     *
     * @param r
     * @param p
     * @param q
     * @return LineId
     */
    private LineId construct(int r, LineId p, LineId q, int clock) {
        ArrayList<Position> positions = new ArrayList<>();
        ArrayList<Integer> digits = getDigits(r, p, q);
        for (int i = 0; i < digits.size(); i++) {
            int d = digits.get(i);
            Identifier s;
            int c;
            if (i < p.getPositions().size() && d == p.getPositions().get(i).getDigit()) {
                s = p.getPositions().get(i).getSiteId();
                c = p.getPositions().get(i).getClock();
            } else if (i < q.getPositions().size() && d == q.getPositions().get(i).getDigit()) {
                s = q.getPositions().get(i).getSiteId();
                c = q.getPositions().get(i).getClock();
            } else {
                s = site;
                c = clock++;
            }
            positions.add(new Position(d, s, c));
        }
        return new LineId(positions);
    }

    /**
     * Helper function that splits r (integer) into list of digits.
     * Pre condition is that r is a number that can be split into digits to create a lineId between p and q.
     *
     * @param r
     * @param p
     * @param q
     * @return
     */
    private ArrayList<Integer> getDigits(int r, LineId p, LineId q) {
        ArrayList<Integer> digits = new ArrayList<>();
        getDigits(digits, Integer.toString(r), p, q);
        return digits;
    }

    /**
     * Helper function that takes a Number-string and extracts the valid digits to be able to create the
     * lineId.
     *
     * @param digits
     * @param prefix
     * @param p
     * @param q
     * @return
     */
    private boolean getDigits(ArrayList<Integer> digits, String prefix, LineId p, LineId q) {
        if(prefix.equals("")){
            return true;
        }
        for (int i = prefix.length(); i > 0; i--) {
            int digit = Integer.parseInt(prefix.substring(0, i));
            ArrayList<Integer> temp = new ArrayList<>(digits);
            temp.add(digit);
            if(validDigits(temp, p, q) && getDigits(temp, prefix.substring(i), p, q)){
                digits.clear();
                digits.addAll(temp);
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function that checks if a list of digits is a valid lineId between p and q
     *
     * @param digits
     * @param p
     * @param q
     * @return
     */
    private boolean validDigits(ArrayList<Integer> digits, LineId p, LineId q){
        boolean greater = false;
        boolean less = false;
        for (int i = 0; i < digits.size(); i++) {
            if(!greater && i < p.getPositions().size() && digits.get(i) < p.getPositions().get(i).getDigit())
                return false;
            if(!greater && i < p.getPositions().size() && digits.get(i) > p.getPositions().get(i).getDigit())
                greater = true;
            if(!less && i < q.getPositions().size() && digits.get(i) > q.getPositions().get(i).getDigit())
                return false;
            if(!less && i < q.getPositions().size() && digits.get(i) < q.getPositions().get(i).getDigit())
                less = true;
        }
        return true;
    }

    /**
     * GetSite of the doucment
     *
     * @return
     */
    public Identifier getSite() {
        return site;
    }

    /**
     * Get lines of the document
     *
     * @return list of lines
     */
    public ArrayList<String> getDocumentLines() {
        return documentLines;
    }

    /**
     * Get table of lineIds.
     * @return
     */
    public ArrayList<LineId> getIdTable() {
        return idTable;
    }

    @Override
    public String toString() {
        String doc = "\n" + "-------- <Document Start> --------" +  "\n";
        for (String line: documentLines) {
            doc = doc + line;
        }
        doc = doc + "\n" + "-------- <Document End> --------" + "\n";
        return doc;
    }

    /**
     * Insert patch into HB
     *
     * @param patch
     * @return
     */
    public Patch insertPatchHB(Patch patch) {
        /**
         * We create a new patch to avoid working on the shared version
         */
        Patch temp = new Patch(patch);
        hb.put(patch.getUuid(), temp);
        return temp;
    }

    /**
     * Get patch with given patchId
     *
     * @param patchId
     * @return
     */
    public Patch getPatch(UUID patchId){
        return hb.get(patchId);
    }

    /**
     * Get cemetery degree of given lineId
     *
     * @param lineId
     * @return
     */
    private int getCemetery(LineId  lineId) {
        Integer degree = cemetery.get(lineId);
        if (degree == null) {
            return 0;
        }
        return degree;
    }

    /**
     * Set degree in cemetery for given lineId
     *
     * @param lineId
     * @param degree
     */
    private void setCemetery(LineId lineId, int degree) {
        if (degree == 0) {
            cemetery.remove(lineId);
            return;
        }
        cemetery.put(lineId, degree);
    }

}

