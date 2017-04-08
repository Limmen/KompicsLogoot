package se.kth.app.logoot;

import com.google.common.collect.Lists;
import se.sics.ktoolbox.util.identifiable.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author Maxime Dufour on 2017-04-07.
 */
public class Document {

    private int clock = 0;
    private Identifier site;
    private Random random = new Random();
    private ArrayList<String> documentLines;
    private ArrayList<LineId> idTable;
    private HashMap<Position, Operation> hb;
    private HashMap<Position, Integer> cemetery;

    public Document(Identifier site) {
        this.site = site;
        documentLines = new ArrayList<>();
        idTable = new ArrayList<>();
        hb = new HashMap<>();
        cemetery = new HashMap<>();
        idTable.add(new LineId(Lists.newArrayList(new Position(0, null, null))));
        idTable.add(new LineId(Lists.newArrayList(new Position(Integer.MAX_VALUE - 1, null, null))));
    }


    void execute(List<Operation> patch) {

    }

    /**
     * Generates N lineIds between LineId p and LineId q with boundary limiting the distance between two successive
     * Id's
     * @param p lineId p
     * @param q lineId q
     * @param N Number of lines to generate
     * @param boundary boundary between successive lines
     * @return
     */
    public List<LineId> generateLineId(LineId p, LineId q, int N, int boundary) {
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
            list.add(construct(r + random.nextInt(step) + 1, p, q));
            r = r + step;
        }
        return list;
    }

    /**
     * Returns a number in base Integer.MAX_VALUE where the digits of this number are the ith first position digits
     * of lineId p (filled with 0 if |p| < i).
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
        int num = Integer.parseInt(digits);
        if (num < Integer.MAX_VALUE - 1)
            return num;
        else
            return Integer.MAX_VALUE-2;
    }

    /**
     * Constructs a lineId inbetween lineId p and lineId q with the digits in r
     * @param r
     * @param p
     * @param q
     * @return LineId
     */
    private LineId construct(int r, LineId p, LineId q) {
        ArrayList<Position> positions = new ArrayList<>();
        String rDigits = Integer.toString(r);
        for (int i = 0; i < rDigits.length(); i++) {
            int d = Character.getNumericValue(rDigits.charAt(i));
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

    public int getClock() {
        return clock;
    }

    public Identifier getSite() {
        return site;
    }
}
