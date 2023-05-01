package valorless.valorlessutils.json;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlRepresenter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.representer.Represent;

/**
 * Extends Bukkit's {@link YamlRepresenter}, but ensures that {@link ConfigurationSection}s can
 * still be represented.
 * <p>
 * There are plans in Spigot to change how {@link YamlConfiguration} serializes
 * {@link ConfigurationSection}s in order to properly support comments inside of configurations.
 * These changes move the representation of configuration sections from Bukkit's
 * {@link YamlRepresenter} into {@link YamlConfiguration}. However, since we reuse Bukkit's
 * {@link YamlRepresenter} for our own Yaml serialization purposes, for which we don't need the
 * comment support provided by {@link YamlConfiguration}, we need the Yaml representer to still be
 * able to represent configuration sections in the future. This class therefore extends Bukkit's
 * {@link YamlRepresenter} and ensures that the previous {@link Represent} for
 * {@link ConfigurationSection}s is still registered.
 * <p>
 * History:
 * <ul>
 * <li>This class was added 2021-12-16 (v2.14.0):
 * https://github.com/Shopkeepers/Shopkeepers/commit/193283b0661363f02b71fc1327fe3cb46633b4fe
 * <li>It became necessary 2021-12-20 (MC 1.18.1):
 * https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/commits/3e2dd2bc120754ea4db193e878050d0eb31a6894
 * <li>This class might no longer be necessary because some change was reverted 2022-4-22 (MC
 * 1.18.2):
 * https://hub.spigotmc.org/stash/projects/SPIGOT/repos/bukkit/commits/31514774479a17c4a8cfb2d44f392a590a2b282c
 * </ul>
 */
class OldBukkitYamlRepresenter extends YamlRepresenter {

	OldBukkitYamlRepresenter() {
		super();
		this.multiRepresenters.put(ConfigurationSection.class, new RepresentConfigurationSection());
	}

	private class RepresentConfigurationSection extends RepresentMap {
		@Override
		public Node representData(@Nullable Object data) {
			assert data instanceof ConfigurationSection;
			ConfigurationSection configSection = Unsafe.castNonNull(data);
			return Unsafe.assertNonNull(super.representData(configSection.getValues(false)));
		}
	}
}