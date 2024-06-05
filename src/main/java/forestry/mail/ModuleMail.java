/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.mail;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import forestry.api.mail.EnumAddressee;
import forestry.api.mail.PostManager;
import forestry.api.modules.ForestryModule;
import forestry.core.ISaveEventHandler;
import forestry.core.ModuleCore;
import forestry.core.config.Config;
import forestry.core.config.Constants;
import forestry.core.network.IPacketRegistry;
import forestry.core.network.PacketIdClient;
import forestry.core.network.PacketIdServer;
import forestry.mail.commands.CommandMail;
import forestry.mail.features.MailMenuTypes;
import forestry.mail.gui.GuiCatalogue;
import forestry.mail.gui.GuiLetter;
import forestry.mail.gui.GuiMailbox;
import forestry.mail.gui.GuiStampCollector;
import forestry.mail.gui.GuiTradeName;
import forestry.mail.gui.GuiTrader;
import forestry.mail.network.packets.PacketLetterInfoRequest;
import forestry.mail.network.packets.PacketLetterInfoResponsePlayer;
import forestry.mail.network.packets.PacketLetterInfoResponseTrader;
import forestry.mail.network.packets.PacketLetterTextSet;
import forestry.mail.network.packets.PacketPOBoxInfoResponse;
import forestry.mail.network.packets.PacketTraderAddressRequest;
import forestry.mail.network.packets.PacketTraderAddressResponse;
import forestry.modules.BlankForestryModule;
import forestry.modules.ForestryModuleUids;

@ForestryModule(modId = Constants.MOD_ID, moduleID = ForestryModuleUids.MAIL, name = "Mail", author = "SirSengir", url = Constants.URL, unlocalizedDescription = "for.module.mail.description")
public class ModuleMail extends BlankForestryModule {

	@Override
	public void setupAPI() {
		PostManager.postRegistry = new PostRegistry();
		PostManager.postRegistry.registerCarrier(new PostalCarrier(EnumAddressee.PLAYER));
		PostManager.postRegistry.registerCarrier(new PostalCarrier(EnumAddressee.TRADER));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void registerGuiFactories() {
		MenuScreens.register(MailMenuTypes.CATALOGUE.menuType(), GuiCatalogue::new);
		MenuScreens.register(MailMenuTypes.LETTER.menuType(), GuiLetter::new);
		MenuScreens.register(MailMenuTypes.MAILBOX.menuType(), GuiMailbox::new);
		MenuScreens.register(MailMenuTypes.STAMP_COLLECTOR.menuType(), GuiStampCollector::new);
		MenuScreens.register(MailMenuTypes.TRADE_NAME.menuType(), GuiTradeName::new);
		MenuScreens.register(MailMenuTypes.TRADER.menuType(), GuiTrader::new);
	}

	@Override
	public void preInit() {
		ModuleCore.rootCommand.then(CommandMail.register());

		if (Config.mailAlertEnabled) {
			MinecraftForge.EVENT_BUS.register(new EventHandlerMailAlert());
		}
	}

	@Override
	public void registerPackets(IPacketRegistry registry) {
		registry.serverbound(PacketIdServer.LETTER_INFO_REQUEST, PacketLetterInfoRequest.class, PacketLetterInfoRequest::decode, PacketLetterInfoRequest::handle);
		registry.serverbound(PacketIdServer.TRADING_ADDRESS_REQUEST, PacketTraderAddressRequest.class, PacketTraderAddressRequest::decode, PacketTraderAddressRequest::handle);
		registry.serverbound(PacketIdServer.LETTER_TEXT_SET, PacketLetterTextSet.class, PacketLetterTextSet::decode, PacketLetterTextSet::handle);

		registry.clientbound(PacketIdClient.LETTER_INFO_RESPONSE_PLAYER, PacketLetterInfoResponsePlayer.class, PacketLetterInfoResponsePlayer::decode, PacketLetterInfoResponsePlayer::handle);
		registry.clientbound(PacketIdClient.LETTER_INFO_RESPONSE_TRADER, PacketLetterInfoResponseTrader.class, PacketLetterInfoResponseTrader::decode, PacketLetterInfoResponseTrader::handle);
		registry.clientbound(PacketIdClient.TRADING_ADDRESS_RESPONSE, PacketTraderAddressResponse.class, PacketTraderAddressResponse::decode, PacketTraderAddressResponse::handle);
		registry.clientbound(PacketIdClient.POBOX_INFO_RESPONSE, PacketPOBoxInfoResponse.class, PacketPOBoxInfoResponse::decode, PacketPOBoxInfoResponse::handle);
	}

	@Override
	public ISaveEventHandler getSaveEventHandler() {
		return new SaveEventHandlerMail();
	}
}
