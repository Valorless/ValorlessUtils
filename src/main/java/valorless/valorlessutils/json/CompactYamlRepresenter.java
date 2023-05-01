package valorless.valorlessutils.json;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.yaml.snakeyaml.DumperOptions.ScalarStyle;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;


/**
 * There are situations in which SnakeYaml uses the literal block style by default and thereby
 * produces output with line breaks. This includes for example:
 * <ul>
 * <li>Strings that contain non-printable characters and are therefore considered to be binary data.
 * <li>Strings and characters that contain newline characters when the used scalar style is
 * {@link ScalarStyle#PLAIN}.
 * <li>Byte arrays, which are also considered binary data.
 * <li>Custom representers may also decide to forcefully use block styles.
 * </ul>
 * <p>
 * For our compact Yaml representation we want to produce Yaml output without any line breaks in all
 * situations. This {@link Representer} therefore enforces the use of the scalar style
 * {@link ScalarStyle#DOUBLE_QUOTED} for any scalars that contain newline characters.
 */
// Extends Bukkit's YamlRepresenter:
public class CompactYamlRepresenter extends OldBukkitYamlRepresenter {

	public CompactYamlRepresenter() {
	}

	// We expect that all Representers use this method to create scalar nodes. The alternative would
	// be to replace all registered Representers with wrappers that analyze the created nodes and
	// replace them if necessary.
	@Override
	protected Node representScalar(
			@Nullable Tag tag,
			@Nullable String value,
			@Nullable ScalarStyle style
	) {
		assert value != null;
		ScalarStyle effectiveStyle = style;
		if (effectiveStyle == null) {
			effectiveStyle = Unsafe.assertNonNull(this.getDefaultScalarStyle());
		}

		// Only the double-quoted style is guaranteed to not output line breaks, but escape them
		// instead:
		if (effectiveStyle != ScalarStyle.DOUBLE_QUOTED && StringUtils.containsNewline(value)) {
			// The value contains newline characters but is not double-quoted. Enforce the use of
			// the double-quoted style:
			effectiveStyle = ScalarStyle.DOUBLE_QUOTED;
		} // Else: Stick to the default representation:
		return Unsafe.assertNonNull(super.representScalar(
				Unsafe.nullableAsNonNull(tag),
				Unsafe.nullableAsNonNull(value),
				effectiveStyle
		));
	}
}