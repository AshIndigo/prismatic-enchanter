package net.ashindigo.prismatic;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;
import net.ashindigo.prismatic.client.screen.EnchanterScreen;

public class PrismaticEnchanterModClient {

    public static void clientInit() {
        MenuRegistry.registerScreenFactory(PrismaticEnchanterMod.ENCHANTER_MENU.get(), EnchanterScreen::new);
        ClientLifecycleEvent.CLIENT_SETUP.register((mc) -> PrismaticEnchanterModClient.clientInit());
    }
}
