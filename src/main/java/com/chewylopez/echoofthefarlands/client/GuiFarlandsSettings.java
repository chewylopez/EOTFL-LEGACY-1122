package com.chewylopez.echoofthefarlands.client;

import com.chewylopez.echoofthefarlands.Config;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.List;

public class GuiFarlandsSettings extends GuiScreen {

    private static final int ID_LOCATION = 1;
    private static final int ID_GEN_TYPE = 2;
    private static final int ID_DONE = 3;

    private final GuiScreen parent;

    private GuiCyclingOption locationButton;
    private GuiCyclingOption genTypeButton;

    private int labelX;
    private int locationRowY;
    private int genTypeRowY;

    public GuiFarlandsSettings(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        this.labelX = this.width / 2 - 155;
        int buttonX = this.width / 2 + 5;
        this.locationRowY = this.height / 2 - 30;
        this.genTypeRowY = this.locationRowY + 25;

        this.locationButton = new GuiCyclingOption(ID_LOCATION, buttonX, this.locationRowY, 150, 20,
                PresetLists.buildDistances(), Config.getFarlandsLocation());
        this.genTypeButton = new GuiCyclingOption(ID_GEN_TYPE, buttonX, this.genTypeRowY, 150, 20,
                PresetLists.buildGenTypes(), Config.getFarlandsGenType());

        this.buttonList.add(this.locationButton);
        this.buttonList.add(this.genTypeButton);
        this.buttonList.add(new GuiButton(ID_DONE, this.width / 2 - 100, this.height - 28, 200, 20,
                I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case ID_LOCATION:
                this.locationButton.cycle();
                Config.setFarlandsLocation(this.locationButton.getSelectedValue());
                Config.save();
                break;
            case ID_GEN_TYPE:
                this.genTypeButton.cycle();
                Config.setFarlandsGenType(this.genTypeButton.getSelectedValue());
                Config.save();
                break;
            case ID_DONE:
                this.mc.displayGuiScreen(this.parent);
                break;
            default:
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        this.drawCenteredString(this.fontRenderer, "Farlands Settings", this.width / 2, 15, 0xFFFFFF);
        this.drawString(this.fontRenderer, "Farlands Location", this.labelX, this.locationRowY + 6, 0xFFFFFF);
        this.drawString(this.fontRenderer, "Generation Type", this.labelX, this.genTypeRowY + 6, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawTooltipFor(this.locationButton, mouseX, mouseY);
        drawTooltipFor(this.genTypeButton, mouseX, mouseY);
    }

    private void drawTooltipFor(GuiCyclingOption button, int mouseX, int mouseY) {
        if (button == null || !button.isMouseOver()) {
            return;
        }
        List<String> lines = PresetLists.splitTooltip(button.getSelectedTooltip());
        if (!lines.isEmpty()) {
            this.drawHoveringText(lines, mouseX, mouseY);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
