package com.chewylopez.echoofthefarlands.client;

import net.minecraft.client.gui.GuiButton;

public class GuiCyclingOption extends GuiButton {

    private final MenuOptionsGrouping[] options;
    private int index;

    public GuiCyclingOption(int id, int x, int y, int width, int height,
                            MenuOptionsGrouping[] options, int initialValue) {
        super(id, x, y, width, height, "");
        this.options = options;
        this.index = PresetLists.getIndexFromIntValue(initialValue, options);
        this.updateLabel();
    }

    public void cycle() {
        if (this.options.length == 0) {
            return;
        }
        this.index = (this.index + 1) % this.options.length;
        this.updateLabel();
    }

    public int getSelectedValue() {
        return this.options[this.index].getValue();
    }

    public String getSelectedTooltip() {
        return this.options[this.index].getTooltip();
    }

    private void updateLabel() {
        this.displayString = this.options[this.index].getText();
    }
}
