package mz.co.attendance.control.components.utils;

public enum TooltipAlignment {
    TOP("top"),
    RIGHT("right"),
    LEFT("left"),
    BOTTOM("bottom"),
    CENTER("center");

    private String align;

    private TooltipAlignment(String align) {
        this.align = align;
    }

    public String getAlignmentText() {
        return this.align;
    }

    public static TooltipAlignment getAlignment(String text) {
        TooltipAlignment[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            TooltipAlignment alignment = var1[var3];
            if (alignment.getAlignmentText().equals(text)) {
                return alignment;
            }
        }

        return null;
    }
}
