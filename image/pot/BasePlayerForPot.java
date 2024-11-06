

import java.math.BigDecimal;
import java.util.Comparator;

@SuppressWarnings("serial")
public class BasePlayerForPot extends BaseModel {

    private final Long userId;
    private final Integer position;
    private final PlayerState state;
    private final BigDecimal totalBetAmount;

    public BasePlayerForPot(Long userId, Integer position, PlayerState state, BigDecimal totalBetAmount) {
        this.userId = userId;
        this.position = position;
        this.state = state;
        this.totalBetAmount = totalBetAmount;
    }

    public BasePlayerForPot(BasePlayerForPot playerForPot, BigDecimal totalBetAmount) {
        this(playerForPot.getUserId(), playerForPot.getPosition(), playerForPot.getState(), totalBetAmount);
    }

    public final static Comparator<BasePlayerForPot> betComperator = new Comparator<BasePlayerForPot>() {
        @Override
        public int compare(BasePlayerForPot p1, BasePlayerForPot p2) {
            return p1.getTotalBetAmount().compareTo(p2.getTotalBetAmount());
        }
    };

    public Long getUserId() {
        return userId;
    }

    public Integer getPosition() {
        return position;
    }

    public PlayerState getState() {
        return state;
    }

    public BigDecimal getTotalBetAmount() {
        return totalBetAmount;
    }

}
