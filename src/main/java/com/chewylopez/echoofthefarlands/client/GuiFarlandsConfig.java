package com.chewylopez.echoofthefarlands.client;

import com.chewylopez.echoofthefarlands.Config;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiFarlandsConfig extends GuiScreen {

    private static final int ID_UPDATE = 1;
    private static final int ID_DONE = 2;
    private static final int FIELD_ID = 10;

    private static final int VALID_TEXT_COLOUR = 14737632;
    private static final int COMMITTED_TEXT_COLOUR = 424242;
    private static final int REJECTED_TEXT_COLOUR = 0xFF5555;

    private final GuiScreen parent;

    private GuiTextField locationField;
    private String feedback = "";

    public GuiFarlandsConfig(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        this.locationField = new GuiTextField(FIELD_ID, this.fontRenderer,
                this.width / 2 - 100, 100, 200, 20);
        this.locationField.setMaxStringLength(10);
        this.locationField.setText(Integer.toString(Config.getFarlandsLocationCustom()));
        this.locationField.setTextColor(VALID_TEXT_COLOUR);
        // Empty must be allowed or the field cannot be cleared to retype; range is checked
        // on commit instead.
        this.locationField.setValidator(text -> {
            if (text == null || text.isEmpty()) {
                return true;
            }
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        this.locationField.setFocused(true);

        this.buttonList.add(new GuiButton(ID_UPDATE, this.width / 2 - 100, 130, 200, 20, "Update Value"));
        this.buttonList.add(new GuiButton(ID_DONE, this.width / 2 - 100, this.height - 40, 200, 20,
                I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == ID_UPDATE) {
            this.commit();
        } else if (button.id == ID_DONE) {
            this.mc.displayGuiScreen(this.parent);
        }
    }

    private void commit() {
        String raw = this.locationField.getText();

        int parsed;
        try {
            parsed = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            this.locationField.setTextColor(REJECTED_TEXT_COLOUR);
            this.feedback = (raw == null || raw.isEmpty()) ? "enter a value" : "not a whole number";
            return;
        }

        if (parsed < 1) {
            this.locationField.setTextColor(REJECTED_TEXT_COLOUR);
            this.feedback = "must be 1 or greater";
            return;
        }

        Config.setFarlandsLocationCustom(parsed);
        Config.save();

        this.locationField.setTextColor(COMMITTED_TEXT_COLOUR);
        this.feedback = "saved";
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.locationField.textboxKeyTyped(typedChar, keyCode)) {
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.locationField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.locationField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        this.drawCenteredString(this.fontRenderer, "Echo of the Farlands Config",
                this.width / 2, 15, 0xFFFFFF);
        this.drawCenteredString(this.fontRenderer, "farlands location (default is 1,000,000)",
                this.width / 2, 76, 0xA0A0A0);

        this.locationField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (!this.feedback.isEmpty()) {
            this.drawCenteredString(this.fontRenderer, this.feedback, this.width / 2, 158, 0xA0A0A0);
        }
    }
}
