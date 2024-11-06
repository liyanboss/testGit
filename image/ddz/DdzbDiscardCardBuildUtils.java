
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DdzbDiscardCardBuildUtils {

    public static DdzbDiscardCardResult buildDiscardResult(List<DdzbPokerCard> discardCards) {
        if (discardCards != null && !discardCards.isEmpty() && DdzbDiscardCardCheckUtils.validHandCard(discardCards)) {
            List<DdzbPokerCard> smallToBigList = new ArrayList<DdzbPokerCard>(discardCards);
            Collections.sort(smallToBigList, DdzbPokerCard.cardSortingComperator);

            int cardListLength = smallToBigList.size();
            if (cardListLength == 1) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.Solo, smallToBigList.get(0).getCardNumber(), 1, buildSplitCardGroup(smallToBigList));
            }
            if (cardListLength == 2) {
                DdzbPokerCard cardA = smallToBigList.get(0);
                DdzbPokerCard cardB = smallToBigList.get(1);
                if (DdzbDiscardCardCheckUtils.checkValid2JokerCard(cardA, cardB)) {
                    return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.Rocket, cardB.getCardNumber(), 1, buildSplitCardGroup(smallToBigList));
                }
                if (DdzbDiscardCardCheckUtils.checkValid2SameCard(cardA, cardB)) {
                    return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.Pair, cardB.getCardNumber(), 1, buildSplitCardGroup(smallToBigList));
                }
                return null;
            }
            DdzbDiscardCardAnalyseResult analyseResult = DdzbDiscardCardAnalyseUtils.analyseCardList(smallToBigList);
            return doBuildDiscardResult(analyseResult, smallToBigList);
        }
        return null;
    }

    private static DdzbDiscardCardResult doBuildDiscardResult(DdzbDiscardCardAnalyseResult analyseResult, List<DdzbPokerCard> smallToBigList) {
        if (analyseResult.getFourCount() > 0) {
            if (analyseResult.getFourCount() == 1 && analyseResult.getAllCount() == 4) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.Bomb, buildResultRank(analyseResult.getFourList()), 1, buildSplitCardGroup(analyseResult.getFourList()));
            }
            if (analyseResult.getFourCount() == 1 && analyseResult.getOneCount() == 2 && analyseResult.getAllCount() == 6) {
                if (analyseResult.getHasTwoJoker()) {
                    return null;
                }
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.FourDualSolo, buildResultRank(analyseResult.getFourList()), 1, buildSplitCardGroup(analyseResult.getFourList(), analyseResult.getOneList()));
            }
            if (analyseResult.getFourCount() == 1 && analyseResult.getTwoCount() == 2 && analyseResult.getAllCount() == 8) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.FourDualPair, buildResultRank(analyseResult.getFourList()), 1, buildSplitCardGroup(analyseResult.getFourList(), analyseResult.getTwoList()));
            }
            return null;
        }
        if (analyseResult.getThreeCount() > 0) {
            if (analyseResult.getThreeCount() == 1 && analyseResult.getAllCount() == 3) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.Trio, buildResultRank(analyseResult.getThreeList()), 1, buildSplitCardGroup(analyseResult.getThreeList()));
            }
            if (analyseResult.getThreeCount() == 1 && analyseResult.getAllCount() == 4 && analyseResult.getOneCount() == 1) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.TrioSolo, buildResultRank(analyseResult.getThreeList()), 1, buildSplitCardGroup(analyseResult.getThreeList(), analyseResult.getOneList()));
            }
            if (analyseResult.getThreeCount() == 1 && analyseResult.getAllCount() == 5 && analyseResult.getTwoCount() == 1) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.TrioPair, buildResultRank(analyseResult.getThreeList()), 1, buildSplitCardGroup(analyseResult.getThreeList(), analyseResult.getTwoList()));
            }
            if (analyseResult.getThreeCount() > 1) {
                if (analyseResult.getThreeList().get(analyseResult.getThreeList().size() - 1).getCardNumber() >= DdzbPokerCard.Big2CardNumber) {
                    return null;
                }
                int firstCardNumber = analyseResult.getThreeList().get(0).getCardNumber().intValue();
                for (int i = 1; i < analyseResult.getThreeCount(); i++) {
                    int nextCardNumber = analyseResult.getThreeList().get(i * 3).getCardNumber().intValue();
                    if (firstCardNumber + i != nextCardNumber) {
                        return null;
                    }
                }
            }
            if (analyseResult.getThreeCount() * 3 == analyseResult.getAllCount()) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.TrioChain, buildResultRank(analyseResult.getThreeList()), analyseResult.getThreeCount(), buildSplitCardGroup(analyseResult.getThreeList()));
            }
            if (analyseResult.getThreeCount() == analyseResult.getOneCount() && analyseResult.getThreeCount() * 4 == analyseResult.getAllCount()) {
                if (analyseResult.getHasTwoJoker()) {
                    return null;
                }
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.TrioSoloChain, buildResultRank(analyseResult.getThreeList()), analyseResult.getThreeCount(), buildSplitCardGroup(analyseResult.getThreeList(), analyseResult.getOneList()));
            }
            if (analyseResult.getThreeCount() == analyseResult.getTwoCount() && analyseResult.getThreeCount() * 5 == analyseResult.getAllCount()) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.TrioPairChain, buildResultRank(analyseResult.getThreeList()), analyseResult.getThreeCount(), buildSplitCardGroup(analyseResult.getThreeList(), analyseResult.getTwoList()));
            }
            return null;
        }
        if (analyseResult.getTwoCount() >= 3) {
            if (analyseResult.getTwoList().get(analyseResult.getTwoList().size() - 1).getCardNumber() >= DdzbPokerCard.Big2CardNumber) {
                return null;
            }
            int firstCardNumber = analyseResult.getTwoList().get(0).getCardNumber().intValue();
            for (int i = 1; i < analyseResult.getTwoCount(); i++) {
                int nextCardNumber = analyseResult.getTwoList().get(i * 2).getCardNumber().intValue();
                if (firstCardNumber + i != nextCardNumber) {
                    return null;
                }
            }
            if (analyseResult.getTwoCount() * 2 == analyseResult.getAllCount()) {
                return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.PairChain, buildResultRank(analyseResult.getTwoList()), analyseResult.getTwoCount(), buildSplitCardGroup(analyseResult.getTwoList()));
            }
            return null;
        }
        if (analyseResult.getOneCount() >= 5 && analyseResult.getOneCount() == analyseResult.getAllCount()) {
            if (analyseResult.getOneList().get(analyseResult.getOneList().size() - 1).getCardNumber() >= DdzbPokerCard.Big2CardNumber) {
                return null;
            }
            int firstCardNumber = analyseResult.getOneList().get(0).getCardNumber().intValue();
            for (int i = 1; i < analyseResult.getOneCount(); i++) {
                int nextCardNumber = analyseResult.getOneList().get(i * 1).getCardNumber().intValue();
                if (firstCardNumber + i != nextCardNumber) {
                    return null;
                }
            }
            return new DdzbDiscardCardResult(smallToBigList, DdzbCombineType.SoloChain, buildResultRank(analyseResult.getOneList()), analyseResult.getOneCount(), buildSplitCardGroup(analyseResult.getOneList()));
        }
        return null;
    }

    private static Integer buildResultRank(List<DdzbPokerCard> smallToBigList) {
        return smallToBigList.get(smallToBigList.size() - 1).getCardNumber();
    }

    private static List<List<DdzbPokerCard>> buildSplitCardGroup(List<DdzbPokerCard> splitListA) {
        return buildSplitCardGroup(splitListA, null);
    }

    private static List<List<DdzbPokerCard>> buildSplitCardGroup(List<DdzbPokerCard> splitListA, List<DdzbPokerCard> splitListB) {
        List<List<DdzbPokerCard>> splitCardGroup = new ArrayList<List<DdzbPokerCard>>();
        splitCardGroup.add(splitListA);
        if (splitListB != null && !splitListB.isEmpty()) {
            splitCardGroup.add(splitListB);
        }
        return splitCardGroup;
    }

}
