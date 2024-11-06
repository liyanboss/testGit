

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BaseTablePotUtils {

    private static PlayerState[] forActivePot = new PlayerState[] { inGame, inBet, inDiscard };

    protected static List<BaseTableSubPot> buildTableSubPotList(List<BasePlayerForPot> playerForPotList) {
        List<BaseTableSubPot> tableSubPotList = new ArrayList<BaseTableSubPot>();
        Integer potIndex = new Integer(0);
        Integer i = 1;
        do {
            playerForPotList = buildTableSubPot(playerForPotList, tableSubPotList, potIndex++);
        } while (!playerForPotList.isEmpty() && i++ < 10);
        return tableSubPotList;
    }

    private static List<BasePlayerForPot> buildTableSubPot(List<BasePlayerForPot> playerForPotList, List<BaseTableSubPot> tableSubPotList, Integer potIndex) {
        List<BasePlayerForPot> playerForPotListX = new ArrayList<BasePlayerForPot>(playerForPotList);
        Collections.sort(playerForPotListX, BasePlayerForPot.betComperator);
        List<BasePlayerForPot> playerForPotListSub = new ArrayList<BasePlayerForPot>();
        if (!playerForPotListX.isEmpty()) {
            BigDecimal balanceBetAmount = BigDecimal.ZERO;
            for (int i = 0; i < playerForPotListX.size(); i++) {
                BasePlayerForPot playerForPot = playerForPotListX.get(i);
                PlayerState playerState = playerForPot.getState();
                if (checkState(forActivePot, playerState)) {
                    balanceBetAmount = playerForPot.getTotalBetAmount();
                    break;
                }
            }
            BigDecimal potAmount = BigDecimal.ZERO;
            List<Integer> positionList = new ArrayList<Integer>();
            for (BasePlayerForPot playerForPot : playerForPotListX) {
                PlayerState playerState = playerForPot.getState();
                if (checkState(forActivePot, playerState)) {
                    positionList.add(playerForPot.getPosition());
                }
                BigDecimal playerBetAmount = playerForPot.getTotalBetAmount();
                if (playerBetAmount.compareTo(balanceBetAmount) > 0) {
                    potAmount = potAmount.add(balanceBetAmount);
                    playerForPotListSub.add(new BasePlayerForPot(playerForPot, playerBetAmount.subtract(balanceBetAmount)));
                } else {
                    potAmount = potAmount.add(playerBetAmount);
                }
            }
            if (potAmount.compareTo(BigDecimal.ZERO) > 0) {
                tableSubPotList.add(new BaseTableSubPot(potIndex, potAmount, positionList));
            }
        }
        return playerForPotListSub;
    }

    private static Boolean checkState(PlayerState[] stateArray, PlayerState playerState) {
        for (PlayerState playerStateIn : stateArray) {
            if (playerStateIn.equals(playerState)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

}
