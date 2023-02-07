package com.oss.pages.transport.ipam.helper;

import java.util.Optional;

public class IPAddressAssignmentWizardProperties {
    private final Optional<String> ipNetwork;
    private final Optional<String> subnet;
    private final Optional<String> address;
    private final Optional<String> mask;
    private final Optional<String> isPrimary;
    private final Optional<String> isInNAT;
    private final Optional<String> role;
    private final Optional<String> description;
    private final Optional<String> assignmentType;
    private final Optional<String> assignmentName;
    private final Optional<String> wizardMode;

    private IPAddressAssignmentWizardProperties(Optional<String> ipNetwork, Optional<String> subnet, Optional<String> address, Optional<String> mask, Optional<String> isPrimary, Optional<String> isInNAT,
                                                Optional<String> role, Optional<String> description, Optional<String> assignmentType, Optional<String> assignmentName, Optional<String> wizardMode) {
        this.ipNetwork = ipNetwork;
        this.subnet = subnet;
        this.address = address;
        this.mask = mask;
        this.isPrimary = isPrimary;
        this.isInNAT = isInNAT;
        this.role = role;
        this.description = description;
        this.assignmentType = assignmentType;
        this.assignmentName = assignmentName;
        this.wizardMode = wizardMode;
    }

    public static IPAddressAssignmentWizardPropertiesBuilder builder() {
        return new IPAddressAssignmentWizardPropertiesBuilder();
    }

    public Optional<String> getIpNetwork() {
        return ipNetwork;
    }

    public Optional<String> getSubnet() {
        return subnet;
    }

    public Optional<String> getAddress() {
        return address;
    }

    public Optional<String> getMask() {
        return mask;
    }

    public Optional<String> getRole() {
        return role;
    }

    public Optional<String> getDescription() {
        return description;
    }

    public Optional<String> isPrimary() {
        return isPrimary;
    }

    public Optional<String> isInNAT() {
        return isInNAT;
    }

    public Optional<String> getAssignmentType() {
        return assignmentType;
    }

    public Optional<String> getAssignmentName() {
        return assignmentName;
    }

    public Optional<String> getWizardMode() {
        return wizardMode;
    }

    public static final class IPAddressAssignmentWizardPropertiesBuilder {
        private Optional<String> ipNetwork = Optional.empty();
        private Optional<String> subnet = Optional.empty();
        private Optional<String> address = Optional.empty();
        private Optional<String> mask = Optional.empty();
        private Optional<String> isPrimary = Optional.empty();
        private Optional<String> isInNAT = Optional.empty();
        private Optional<String> role = Optional.empty();
        private Optional<String> description = Optional.empty();
        private Optional<String> assignmentType = Optional.empty();
        private Optional<String> assignmentName = Optional.empty();
        private Optional<String> wizardMode = Optional.empty();

        public IPAddressAssignmentWizardPropertiesBuilder ipNetwork(String ipNetwork) {
            this.ipNetwork = Optional.of(ipNetwork);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder subnet(String subnet) {
            this.subnet = Optional.of(subnet);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder address(String address) {
            this.address = Optional.of(address);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder mask(String mask) {
            this.mask = Optional.of(mask);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder isPrimary(String isPrimary) {
            this.isPrimary = Optional.of(isPrimary);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder isInNAT(String isInNAT) {
            this.isInNAT = Optional.of(isInNAT);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder role(String role) {
            this.role = Optional.of(role);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder description(String description) {
            this.description = Optional.of(description);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder assignmentType(String assignmentType) {
            this.assignmentType = Optional.of(assignmentType);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder assignmentName(String assignmentName) {
            this.assignmentName = Optional.of(assignmentName);
            return this;
        }

        public IPAddressAssignmentWizardPropertiesBuilder wizardMode(String wizardMode) {
            this.wizardMode = Optional.of(wizardMode);
            return this;
        }

        public IPAddressAssignmentWizardProperties build() {
            return new IPAddressAssignmentWizardProperties(ipNetwork, subnet, address, mask, isPrimary, isInNAT, role, description, assignmentType, assignmentName, wizardMode);
        }
    }
}
