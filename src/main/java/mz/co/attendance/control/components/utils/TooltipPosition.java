package mz.co.attendance.control.components.utils;

public enum TooltipPosition {
    TOP("top"),
    RIGHT("right"),
    LEFT("left"),
    BOTTOM("bottom");

    private String pos;

    private TooltipPosition(String pos) {
        this.pos = pos;
    }

    public String getPositionText() {
        return this.pos;
    }

    public static TooltipPosition getPosition(String text) {
        TooltipPosition[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            TooltipPosition position = var1[var3];
            if (position.getPositionText().equals(text)) {
                return position;
            }
        }

        return null;
    }
}
