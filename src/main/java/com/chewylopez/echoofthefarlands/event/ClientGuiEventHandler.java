package com.chewylopez.echoofthefarlands.event;

import com.chewylopez.echoofthefarlands.EchoOfTheFarlands;
import com.chewylopez.echoofthefarlands.client.GuiFarlandsSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = EchoOfTheFarlands.MODID, value = Side.CLIENT)
public class ClientGuiEventHandler {

    private static final int FARLANDS_BUTTON_ID = 9001;
    private static final int VANILLA_MAP_TYPE_ID = 5;
    private static GuiButton farlandsButton;
    private static GuiButton vanillaMapTypeButton;

    @SubscribeEvent
    public static void onInitGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.getGui() instanceof GuiCreateWorld)) {
            return;
        }

        GuiCreateWorld gui = (GuiCreateWorld) event.getGui();

        farlandsButton = new GuiButton(FARLANDS_BUTTON_ID,
                gui.width / 2 - 155, 120, 150, 20, "Farlands Options...");
        farlandsButton.visible = false;
        event.getButtonList().add(farlandsButton);

        vanillaMapTypeButton = null;
        for (GuiButton candidate : event.getButtonList()) {
            if (candidate.id == VANILLA_MAP_TYPE_ID) {
                vanillaMapTypeButton = candidate;
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (!(event.getGui() instanceof GuiCreateWorld)) {
            return;
        }
        if (farlandsButton != null && vanillaMapTypeButton != null) {
            farlandsButton.visible = vanillaMapTypeButton.visible;
        }
    }

    @SubscribeEvent
    public static void onActionPerformed(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (!(event.getGui() instanceof GuiCreateWorld)) {
            return;
        }
        if (event.getButton() == null || event.getButton().id != FARLANDS_BUTTON_ID) {
            return;
        }

        event.setCanceled(true);
        Minecraft.getMinecraft().displayGuiScreen(new GuiFarlandsSettings(event.getGui()));
    }
}
