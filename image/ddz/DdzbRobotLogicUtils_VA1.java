

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.xxx.robot.utils.CollectionUtils;

public class DdzbRobotLogicUtils_VA1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(DdzbRobotLogicUtils_VA1.class);

    public static Integer buildBetTimes(List<Object> betList) {
        List<Integer> availableMultiplierList = new ArrayList<Integer>();
        for (Object multiplier : betList) {
            Integer multiplierInt = Integer.parseInt(multiplier.toString());
            availableMultiplierList.add(multiplierInt);
        }
        availableMultiplierList.add(NumberUtils.INTEGER_ZERO);
        Collections.shuffle(availableMultiplierList);
        return availableMultiplierList.isEmpty() ? NumberUtils.INTEGER_ZERO : availableMultiplierList.get(0);
    }

    public static String buildDiscardResult(List<DdzbPokerCard> playerHandCards, List<DdzbPokerCard> lastDiscardList, Map<String, Integer> opponentMap) {
        List<DdzbPokerCard> smallToBigList = new ArrayList<DdzbPokerCard>(playerHandCards);
        Collections.sort(smallToBigList, DdzbPokerCard.cardSortingComperator);
        if (lastDiscardList.isEmpty()) {
            List<DdzbDiscardCardResult> allDiscardResultList = findAllDiscardResult(smallToBigList);
            Collections.shuffle(allDiscardResultList);
            return buildResult(allDiscardResultList.get(0).getSmallToBigList());
        } else {
            DdzbDiscardCardResult lastDiscardResult = DdzbDiscardCardBuildUtils.buildDiscardResult(lastDiscardList);
            if (DdzbCombineType.Rocket == lastDiscardResult.getCombineType()) {
                return "";
            } else {
                String beatCards = tryToBeat_LastHand(smallToBigList, lastDiscardResult, lastDiscardResult.getCombineType(), lastDiscardResult.getSmallToBigList().size());
                if (beatCards.isEmpty()) {
                    if (DdzbCombineType.Bomb == lastDiscardResult.getCombineType()) {
                        return "";
                    } else {
                        return tryToBeat_ByBombOrRocket(smallToBigList);
                    }
                } else {
                    return beatCards;
                }
            }
        }
    }

    private static List<DdzbDiscardCardResult> findAllDiscardResult(List<DdzbPokerCard> smallToBigList) {
        Map<DdzbCombineType, List<DdzbDiscardCardResult>> listMap = new HashMap<DdzbCombineType, List<DdzbDiscardCardResult>>();
        for (int i = smallToBigList.size(); i > 0; i--) {
            List<List<DdzbPokerCard>> allCombination = CollectionUtils.buildAllCombination(smallToBigList, i);
            for (List<DdzbPokerCard> combination : allCombination) {
                DdzbDiscardCardResult discardResult = DdzbDiscardCardBuildUtils.buildDiscardResult(combination);
                if (discardResult != null) {
                    DdzbCombineType combineType = discardResult.getCombineType();
                    if (!listMap.containsKey(combineType)) {
                        listMap.put(combineType, new ArrayList<DdzbDiscardCardResult>());
                    }
                    listMap.get(combineType).add(discardResult);
                }
            }
        }
        LOGGER.debug("start findAllDiscardResult for Length<{}> of smallToBigList<{}>", smallToBigList.size(), smallToBigList);
        List<DdzbDiscardCardResult> resultList = new ArrayList<DdzbDiscardCardResult>();
        for (DdzbCombineType combineType : listMap.keySet()) {
            List<DdzbDiscardCardResult> list = listMap.get(combineType);
            LOGGER.debug("combineType<{}>, discardResultList<{}>, smallToBigList<{}>", combineType, list.size(), smallToBigList);
            resultList.addAll(list);
        }
        LOGGER.debug("finish findAllDiscardResult for Length<{}> of smallToBigList<{}>", smallToBigList.size(), smallToBigList);
        return resultList;
    }

    private static String tryToBeat_LastHand(List<DdzbPokerCard> smallToBigList, DdzbDiscardCardResult lastDiscardResult, DdzbCombineType targetType, Integer targetTypeSize) {
        int playerHandSize = smallToBigList.size();
        if (playerHandSize >= targetTypeSize) {
            List<List<DdzbPokerCard>> tryToBeatList = CollectionUtils.buildAllCombination(smallToBigList, targetTypeSize);
            for (List<DdzbPokerCard> tryToBeatCards : tryToBeatList) {
                DdzbDiscardCardResult tryToBeatResult = DdzbDiscardCardBuildUtils.buildDiscardResult(tryToBeatCards);
                if (tryToBeatResult != null) {
                    if (tryToBeatResult.getCombineType() == targetType) {
                        if (lastDiscardResult == null) {
                            return buildResult(tryToBeatCards);
                        } else {
                            if (tryToBeatResult.compareTo(lastDiscardResult) == 1) {
                                return buildResult(tryToBeatCards);
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    private static String tryToBeat_ByBombOrRocket(List<DdzbPokerCard> smallToBigList) {
        String beatCards = tryToBeat_LastHand(smallToBigList, null, DdzbCombineType.Bomb, 4);
        if (!beatCards.isEmpty()) {
            return beatCards;
        } else {
            int playerHandSize = smallToBigList.size();
            if (playerHandSize >= 2) {
                DdzbPokerCard pokerCardA = smallToBigList.get(playerHandSize - 1);
                DdzbPokerCard pokerCardB = smallToBigList.get(playerHandSize - 2);
                if (DdzbDiscardCardCheckUtils.checkValid2JokerCard(pokerCardA, pokerCardB)) {
                    return buildResult(Arrays.asList(pokerCardA, pokerCardB));
                }
            }
        }
        return "";
    }

    private static String buildResult(List<DdzbPokerCard> cardList) {
        List<String> x = new ArrayList<String>();
        for (DdzbPokerCard pokerCard : cardList) {
            x.add(String.valueOf(pokerCard.getValue()));
        }
        return StringUtils.join(x, ",").replaceAll(" ", "");
    }

}
