

import java.math.BigDecimal;
import java.util.List;


@SuppressWarnings("serial")
public class BaseTablePotInfo extends BaseModel {

    private final List<BaseTableSubPot> potList;
    private final BigDecimal totalPotAmount;

    public BaseTablePotInfo(List<BaseTableSubPot> potList) {
        this.potList = potList;
        this.totalPotAmount = calculateTotalPotAmount();
    }

    public Boolean hasPotInfo() {
        return BigDecimalUtils.greaterThanZero(totalPotAmount);
    }

    private BigDecimal calculateTotalPotAmount() {
        BigDecimal totalPotAmount = BigDecimal.ZERO;
        for (BaseTableSubPot tableSubPot : potList) {
            totalPotAmount = totalPotAmount.add(tableSubPot.getPotAmount());
        }
        return totalPotAmount;
    }

    public List<BaseTableSubPot> getPotList() {
        return potList;
    }

    public BigDecimal getTotalPotAmount() {
        return totalPotAmount;
    }

}
