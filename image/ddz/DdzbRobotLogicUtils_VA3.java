package com.xxx.robot.game.ddz;

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

import com.ccc.base.game.ddzb.DdzbCombineType;
import com.ccc.base.game.ddzb.DdzbDiscardCardBuildUtils;
import com.ccc.base.game.ddzb.DdzbDiscardCardCheckUtils;
import com.ccc.base.game.ddzb.DdzbDiscardCardResult;
import com.ccc.base.game.ddzb.DdzbPokerCard;
import com.xxx.robot.utils.CollectionUtils;
import com.xxx.robot.utils.RandomUtils;

public class DdzbRobotLogicUtils_VA3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(DdzbRobotLogicUtils_VA3.class);
    private static final DdzbCombineType[] priorityList = { DdzbCombineType.TrioPairChain, DdzbCombineType.TrioPair, DdzbCombineType.TrioSoloChain, DdzbCombineType.TrioSolo, DdzbCombineType.TrioChain, DdzbCombineType.Trio, DdzbCombineType.PairChain,
            DdzbCombineType.SoloChain, DdzbCombineType.Pair, DdzbCombineType.Solo };
    private static final DdzbCombineType[] reverseList = { DdzbCombineType.Solo, DdzbCombineType.Pair, DdzbCombineType.SoloChain, DdzbCombineType.PairChain, DdzbCombineType.Trio, DdzbCombineType.TrioChain, DdzbCombineType.TrioSolo,
            DdzbCombineType.TrioSoloChain, DdzbCombineType.TrioPair, DdzbCombineType.TrioPairChain };

    public static Integer buildBetTimes(List<Object> betList) {
        List<Integer> availableMultiplierList = new ArrayList<Integer>();
        for (Object multiplier : betList) {
            Integer multiplierInt = Integer.parseInt(multiplier.toString());
            availableMultiplierList.add(multiplierInt);
        }
        if (availableMultiplierList.contains(NumberUtils.INTEGER_ONE)) {
            return NumberUtils.INTEGER_ONE;
        } else {
            Integer bid2 = Integer.parseInt("2");
            if (availableMultiplierList.contains(bid2)) {
                return RandomUtils.randomInt(100) < 50 ? NumberUtils.INTEGER_ZERO : bid2;
            } else {
                return NumberUtils.INTEGER_ZERO;
            }
        }
    }

    public static String doLandlordAction(List<DdzbPokerCard> playerHandCards, List<DdzbPokerCard> lastDiscardList, Map<String, Integer> opponentMap) {
        List<DdzbPokerCard> smallToBigList = new ArrayList<DdzbPokerCard>(playerHandCards);
        Collections.sort(smallToBigList, DdzbPokerCard.cardSortingComperator);
        if (lastDiscardList.isEmpty()) {
            // Leading
            DdzbDiscardCardResult discardResultList = findDiscardResultForNewRound(smallToBigList, priorityList);
            return buildResult(discardResultList.getSmallToBigList());
        } else {
            // Following
            DdzbDiscardCardResult lastDiscardResult = DdzbDiscardCardBuildUtils.buildDiscardResult(lastDiscardList);
            if (DdzbCombineType.Rocket == lastDiscardResult.getCombineType()) {
                return "";
            } else {
                List<DdzbPokerCard> bigToSmallList = new ArrayList<DdzbPokerCard>(smallToBigList);
                Collections.reverse(bigToSmallList);
                String beatCards = tryToBeat_LastHand(bigToSmallList, lastDiscardResult, lastDiscardResult.getCombineType(), lastDiscardResult.getSmallToBigList().size());
                if (!beatCards.isEmpty()) {
                    return beatCards;
                } else {
                    if (DdzbCombineType.Bomb == lastDiscardResult.getCombineType()) {
                        return tryToBeat_ByRocket(smallToBigList);
                    } else {
                        return tryToBeat_ByBombOrRocket(smallToBigList);
                    }
                }
            }
        }
    }

    public static String doBlockerAction(Boolean lastDiscardIsLandlord, List<DdzbPokerCard> playerHandCards, List<DdzbPokerCard> lastDiscardList, Map<String, Integer> opponentMap) {
        List<DdzbPokerCard> smallToBigList = new ArrayList<DdzbPokerCard>(playerHandCards);
        Collections.sort(smallToBigList, DdzbPokerCard.cardSortingComperator);
        if (lastDiscardList.isEmpty()) {
            // Leading
            DdzbDiscardCardResult discardResultList = findDiscardResultForNewRound(smallToBigList, reverseList);
            return buildResult(discardResultList.getSmallToBigList());
        } else {
            // Following
            if (!lastDiscardIsLandlord) {
                return "";
            } else {
                DdzbDiscardCardResult lastDiscardResult = DdzbDiscardCardBuildUtils.buildDiscardResult(lastDiscardList);
                if (DdzbCombineType.Rocket == lastDiscardResult.getCombineType()) {
                    return "";
                } else {
                    String beatCards = tryToBeat_LastHand(smallToBigList, lastDiscardResult, lastDiscardResult.getCombineType(), lastDiscardResult.getSmallToBigList().size());
                    if (!beatCards.isEmpty()) {
                        return beatCards;
                    } else {
                        if (DdzbCombineType.Bomb == lastDiscardResult.getCombineType()) {
                            return tryToBeat_ByRocket(smallToBigList);
                        } else {
                            return tryToBeat_ByBombOrRocket(smallToBigList);
                        }
                    }
                }
            }
        }
    }

    public static String doDiscarderAction(Boolean lastDiscardIsLandlord, List<DdzbPokerCard> playerHandCards, List<DdzbPokerCard> lastDiscardList, Map<String, Integer> opponentMap) {
        List<DdzbPokerCard> smallToBigList = new ArrayList<DdzbPokerCard>(playerHandCards);
        Collections.sort(smallToBigList, DdzbPokerCard.cardSortingComperator);
        if (lastDiscardList.isEmpty()) {
            // Leading
            List<DdzbPokerCard> bigToSmallList = new ArrayList<DdzbPokerCard>(smallToBigList);
            Collections.reverse(bigToSmallList);
            DdzbDiscardCardResult discardResultList = findDiscardResultForNewRound(bigToSmallList, priorityList);
            return buildResult(discardResultList.getSmallToBigList());
        } else {
            // Following
            DdzbDiscardCardResult lastDiscardResult = DdzbDiscardCardBuildUtils.buildDiscardResult(lastDiscardList);
            if (DdzbCombineType.Rocket == lastDiscardResult.getCombineType()) {
                return "";
            } else {
                String beatCards = tryToBeat_LastHand(smallToBigList, lastDiscardResult, lastDiscardResult.getCombineType(), lastDiscardResult.getSmallToBigList().size());
                if (!beatCards.isEmpty()) {
                    return beatCards;
                } else {
                    if (lastDiscardIsLandlord) {
                        if (DdzbCombineType.Bomb == lastDiscardResult.getCombineType()) {
                            return tryToBeat_ByRocket(smallToBigList);
                        } else {
                            return tryToBeat_ByBombOrRocket(smallToBigList);
                        }
                    } else {
                        return "";
                    }
                }
            }
        }
    }
    
    private static DdzbDiscardCardResult findDiscardResultForNewRound(List<DdzbPokerCard> smallToBigList, DdzbCombineType[] combineTypeList) {
        Map<DdzbCombineType, List<DdzbDiscardCardResult>> listMap = buildListMap(smallToBigList);
        List<DdzbCombineType> allCombineTypes = new ArrayList<DdzbCombineType>(listMap.keySet());
        LOGGER.debug("buildAllDiscardResult, allCombineTypes<{}> for Length<{}> of smallToBigList<{}>", allCombineTypes, smallToBigList.size(), smallToBigList);
        for (DdzbCombineType combineType : combineTypeList) {
            if (allCombineTypes.contains(combineType)) {
                return listMap.get(combineType).get(0);
            }
        }
        return listMap.get(DdzbCombineType.Solo).get(0);
    }

    private static Map<DdzbCombineType, List<DdzbDiscardCardResult>> buildListMap(List<DdzbPokerCard> smallToBigList) {
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
        return listMap;
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
        return beatCards.isEmpty() ? tryToBeat_ByRocket(smallToBigList) : beatCards;
    }

    private static String tryToBeat_ByRocket(List<DdzbPokerCard> smallToBigList) {
        int playerHandSize = smallToBigList.size();
        if (playerHandSize >= 2) {
            DdzbPokerCard pokerCardA = smallToBigList.get(playerHandSize - 1);
            DdzbPokerCard pokerCardB = smallToBigList.get(playerHandSize - 2);
            if (DdzbDiscardCardCheckUtils.checkValid2JokerCard(pokerCardA, pokerCardB)) {
                return buildResult(Arrays.asList(pokerCardA, pokerCardB));
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
