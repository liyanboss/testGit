

import java.util.List;


@SuppressWarnings("serial")
public class DdzbDiscardCardAnalyseResult extends BaseModel {

    private final List<DdzbPokerCard> fourList;
    private final List<DdzbPokerCard> threeList;
    private final List<DdzbPokerCard> twoList;
    private final List<DdzbPokerCard> oneList;
    private final Boolean hasTwoJoker;
    private final Integer fourCount;
    private final Integer threeCount;
    private final Integer twoCount;
    private final Integer oneCount;
    private final Integer allCount;

    public DdzbDiscardCardAnalyseResult(List<DdzbPokerCard> fourList, List<DdzbPokerCard> threeList, List<DdzbPokerCard> twoList, List<DdzbPokerCard> oneList, Boolean hasTwoJoker) {
        this.fourList = fourList;
        this.threeList = threeList;
        this.twoList = twoList;
        this.oneList = oneList;
        this.hasTwoJoker = hasTwoJoker;
        this.fourCount = fourList.size() / 4;
        this.threeCount = threeList.size() / 3;
        this.twoCount = twoList.size() / 2;
        this.oneCount = oneList.size() / 1;
        this.allCount = fourList.size() + threeList.size() + twoList.size() + oneList.size();
    }

    public List<DdzbPokerCard> getFourList() {
        return fourList;
    }

    public List<DdzbPokerCard> getThreeList() {
        return threeList;
    }

    public List<DdzbPokerCard> getTwoList() {
        return twoList;
    }

    public List<DdzbPokerCard> getOneList() {
        return oneList;
    }

    public Boolean getHasTwoJoker() {
        return hasTwoJoker;
    }

    public Integer getFourCount() {
        return fourCount;
    }

    public Integer getThreeCount() {
        return threeCount;
    }

    public Integer getTwoCount() {
        return twoCount;
    }

    public Integer getOneCount() {
        return oneCount;
    }

    public Integer getAllCount() {
        return allCount;
    }

}
