package net.ashindigo.prismatic;

import dev.architectury.registry.menu.MenuRegistry;
import net.ashindigo.prismatic.client.screen.EnchanterScreen;

public class PrismaticEnchanterModClient {

    public static void clientInit() {
        MenuRegistry.registerScreenFactory(PrismaticEnchanterMod.ENCHANTER_MENU.get(), EnchanterScreen::new);
    }
}
