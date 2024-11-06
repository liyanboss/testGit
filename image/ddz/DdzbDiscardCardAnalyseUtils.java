

import java.util.ArrayList;
import java.util.List;

public class DdzbDiscardCardAnalyseUtils {

    public static DdzbDiscardCardAnalyseResult analyseCardList(List<DdzbPokerCard> smallToBigList) {
        int cardListLength = smallToBigList.size();
        List<DdzbPokerCard> fourList = new ArrayList<DdzbPokerCard>();
        List<DdzbPokerCard> threeList = new ArrayList<DdzbPokerCard>();
        List<DdzbPokerCard> twoList = new ArrayList<DdzbPokerCard>();
        List<DdzbPokerCard> oneList = new ArrayList<DdzbPokerCard>();
        for (int i = 0; i < cardListLength; i++) {
            int sameCount = 1;
            int cardNumber = smallToBigList.get(i).getCardNumber().intValue();
            for (int j = i + 1; j < cardListLength; j++) {
                if (smallToBigList.get(j).getCardNumber() != cardNumber) {
                    break;
                }
                sameCount++;
            }
            switch (sameCount) {
            case 1:
                oneList.add(smallToBigList.get(i));
                break;
            case 2:
                twoList.add(smallToBigList.get(i));
                twoList.add(smallToBigList.get(i + 1));
                break;
            case 3:
                threeList.add(smallToBigList.get(i));
                threeList.add(smallToBigList.get(i + 1));
                threeList.add(smallToBigList.get(i + 2));
                break;
            case 4:
                fourList.add(smallToBigList.get(i));
                fourList.add(smallToBigList.get(i + 1));
                fourList.add(smallToBigList.get(i + 2));
                fourList.add(smallToBigList.get(i + 3));
                break;
            default:
                break;
            }
            i += sameCount - 1;
        }
        Boolean hasTwoJoker = (cardListLength >= 2) && (smallToBigList.get(cardListLength - 1).getValue() == DdzbPokerCard.BIG_JOKER_VALUE) && (smallToBigList.get(cardListLength - 2).getValue() == DdzbPokerCard.SMALL_JOKER_VALUE);
        return new DdzbDiscardCardAnalyseResult(fourList, threeList, twoList, oneList, hasTwoJoker);
    }

}
