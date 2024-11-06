

import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("serial")
public class BaseTableSubPot extends BaseModel {

    private final Integer potId;
    private final BigDecimal potAmount;
    private final List<Integer> positionList;

    public BaseTableSubPot(Integer potId, BigDecimal potAmount, List<Integer> positionList) {
        this.potId = potId;
        this.potAmount = potAmount;
        this.positionList = positionList;
    }

    public Integer getPotId() {
        return potId;
    }

    public BigDecimal getPotAmount() {
        return potAmount;
    }

    public List<Integer> getPositionList() {
        return positionList;
    }

}
