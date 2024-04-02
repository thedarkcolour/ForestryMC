package forestry.mail.features;

import forestry.mail.ModuleMail;
import forestry.mail.gui.ContainerCatalogue;
import forestry.mail.gui.ContainerLetter;
import forestry.mail.gui.ContainerMailbox;
import forestry.mail.gui.ContainerStampCollector;
import forestry.mail.gui.ContainerTradeName;
import forestry.mail.gui.ContainerTrader;
import forestry.modules.features.FeatureMenuType;
import forestry.modules.features.FeatureProvider;
import forestry.modules.features.IFeatureRegistry;
import forestry.modules.features.ModFeatureRegistry;

@FeatureProvider
public class MailContainers {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleMail.class);

	public static final FeatureMenuType<ContainerCatalogue> CATALOGUE = REGISTRY.container(ContainerCatalogue::fromNetwork, "catalogue");
	public static final FeatureMenuType<ContainerLetter> LETTER = REGISTRY.container(ContainerLetter::fromNetwork, "letter");
	public static final FeatureMenuType<ContainerMailbox> MAILBOX = REGISTRY.container(ContainerMailbox::fromNetwork, "mailbox");
	public static final FeatureMenuType<ContainerStampCollector> STAMP_COLLECTOR = REGISTRY.container(ContainerStampCollector::fromNetwork, "stamp_collector");
	public static final FeatureMenuType<ContainerTradeName> TRADE_NAME = REGISTRY.container(ContainerTradeName::fromNetwork, "trade_name");
	public static final FeatureMenuType<ContainerTrader> TRADER = REGISTRY.container(ContainerTrader::fromNetwork, "trader");
}
