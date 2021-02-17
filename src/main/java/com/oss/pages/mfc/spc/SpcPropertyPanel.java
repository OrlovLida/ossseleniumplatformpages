package com.oss.pages.mfc.spc;

import com.oss.framework.utils.DelayUtils;
import com.oss.framework.widgets.propertypanel.PropertyPanel;
import com.oss.pages.mfc.spc.SpcWizardPage.Format;
import com.oss.pages.mfc.spc.SpcWizardPage.Length;
import com.oss.pages.mfc.spc.SpcWizardPage.NetworkIdentifier;

public class SpcPropertyPanel {

    protected final PropertyPanel propertyPanel;

    public static SpcPropertyPanel create(PropertyPanel propertyPanel) {
        DelayUtils.sleep();
        return new SpcPropertyPanel(propertyPanel);
    }

    protected SpcPropertyPanel(PropertyPanel propertyPanel) {
        this.propertyPanel = propertyPanel;
    }

    public Format format() {
        return Format.of(propertyPanel.getPropertyValue("format"));
    }

    public int decimalValue() {
        return Integer.parseInt(propertyPanel.getPropertyValue("valueDecimal"));
    }

    public String hexValue() {
        return propertyPanel.getPropertyValue("hexValue");
    }

    public String formatValue() {
        return propertyPanel.getPropertyValue("formatValue");
    }

    public Length length() {
        return Length.valueOf(propertyPanel.getPropertyValue("length"));
    }

    public boolean isAdditional() {
        return Boolean.parseBoolean(propertyPanel.getPropertyValue("isAdditional"));
    }

    public NetworkIdentifier networkIdentifier() {
        return NetworkIdentifier.valueOf(propertyPanel.getPropertyValue("networkIdentifier"));
    }

    public long xid() {
        return Long.parseLong(propertyPanel.getPropertyValue("id"));
    }

    public String identifier() {
        return propertyPanel.getPropertyValue("calculatedIdentifier");
    }

    public String calculatedName() {
        return propertyPanel.getPropertyValue("calculatedcalculatedName");
    }

    public String name() {
        return propertyPanel.getPropertyValue("name");
    }

    public PropertyPanel getStandardPropertyPanel() {
        return propertyPanel;
    }
}
