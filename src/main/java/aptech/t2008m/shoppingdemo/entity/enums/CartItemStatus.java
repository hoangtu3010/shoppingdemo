package aptech.t2008m.shoppingdemo.entity.enums;

public enum CartItemStatus {
    ACTIVE(1), DELETED(-1), UNDEFINED(-2);

    private final int value;

    CartItemStatus(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

    public static CartItemStatus of(int value){
        for (CartItemStatus status : CartItemStatus.values()){
            if(status.getValue() == value){
                return status;
            }
        }
        return CartItemStatus.UNDEFINED;
    }
}
