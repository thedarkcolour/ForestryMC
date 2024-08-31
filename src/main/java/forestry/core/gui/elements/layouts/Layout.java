package forestry.core.gui.elements.layouts;

import javax.annotation.Nullable;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;

import forestry.core.gui.elements.Alignment;
import forestry.core.gui.elements.GuiElement;

public interface Layout {
	void layoutContainer(Rectangle bounds, List<GuiElement> elements);

	default Dimension getLayoutSize(ContainerElement container) {
		return container.getPreferredSize();
	}

	static void alignElement(Rectangle parent, Rectangle element, Alignment align, @Nullable LayoutDirection direction) {
		if (direction == null || direction == LayoutDirection.VERTICAL) {
			if (parent.width >= 0 && parent.width > element.width) {
				element.x = (int) ((parent.width - element.width) * align.getXOffset()) + element.x;
			}
		}
		if (direction == null || direction == LayoutDirection.HORIZONTAL) {
			if (parent.height >= 0 && parent.height > element.height) {
				element.y = (int) ((parent.height - element.height) * align.getYOffset()) + element.y;
			}
		}
	}

	static void alignElement(Rectangle parent, Rectangle element, Alignment align) {
		alignElement(parent, element, align, null);
	}
}
