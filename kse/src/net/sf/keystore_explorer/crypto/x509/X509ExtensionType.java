/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2015 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.keystore_explorer.crypto.x509;

import java.util.ResourceBundle;


/**
 * 
 * Enumeration of X.509 certificate extensions.
 * 
 */
public enum X509ExtensionType {
	// ////////////////////////////////
	// Active X509Extension OIDs
	// ////////////////////////////////

	/** Entrust Version Information */
	ENTRUST_VERSION_INFORMATION("1.2.840.113533.7.65.0", "EntrustVersionInformationCertExt"),

	/** Authority Information Access */
	AUTHORITY_INFORMATION_ACCESS("1.3.6.1.5.5.7.1.1", "AuthorityInformationAccessCertExt"),

	/** Authority Information Access */
	SUBJECT_INFORMATION_ACCESS("1.3.6.1.5.5.7.1.11", "SubjectInformationAccessCertExt"),

	/** Subject Directory Attributes */
	SUBJECT_DIRECTORY_ATTRIBUTES("2.5.29.9", "SubjectDirectoryAttributesCertExt"),

	/** Subject Key Identifier */
	SUBJECT_KEY_IDENTIFIER("2.5.29.14", "SubjectKeyIdentifierCertExt"),

	/** Key Usage */
	KEY_USAGE("2.5.29.15", "KeyUsageCertExt"),

	/** Private Key Usage Period */
	PRIVATE_KEY_USAGE_PERIOD("2.5.29.16", "PrivateKeyUsagePeriodCertExt"),

	/** Subject Alternative Name */
	SUBJECT_ALTERNATIVE_NAME("2.5.29.17", "SubjectAlternativeNameCertExt"),

	/** Issuer Alternative Name */
	ISSUER_ALTERNATIVE_NAME("2.5.29.18", "IssuerAlternativeNameCertExt"),

	/** Basic Constraints */
	BASIC_CONSTRAINTS("2.5.29.19", "BasicConstraintsCertExt"),

	/** CRL Number */
	CRL_NUMBER("2.5.29.20", "CrlNumberCertExt"),

	/** Reason code */
	REASON_CODE("2.5.29.21", "ReasonCodeCertExt"),

	/** Hold Instruction Code */
	HOLD_INSTRUCTION_CODE("2.5.29.23", "HoldInstructionCodeCertExt"),

	/** Invalidity Date */
	INVALIDITY_DATE("2.5.29.24", "InvalidityDateCertExt"),

	/** Delta CRL Indicator */
	DELTA_CRL_INDICATOR("2.5.29.27", "DeltaCrlIndicatorCertExt"),

	/** Issuing Distribution Point */
	ISSUING_DISTRIBUTION_POINT("2.5.29.28", "IssuingDistributionPointCertExt"),

	/** Certificate Issuer */
	CERTIFICATE_ISSUER("2.5.29.29", "CertificateIssuerCertExt"),

	/** Name Constraints */
	NAME_CONSTRAINTS("2.5.29.30", "NameConstraintsCertExt"),

	/** CRL Distribution Points */
	CRL_DISTRIBUTION_POINTS("2.5.29.31", "CrlDistributionPointsCertExt"),

	/** Certificate Policies */
	CERTIFICATE_POLICIES("2.5.29.32", "CertificatePoliciesCertExt"),

	/** Policy Mappings */
	POLICY_MAPPINGS("2.5.29.33", "PolicyMappingsCertExt"),

	/** Authority Key Identifier */
	AUTHORITY_KEY_IDENTIFIER("2.5.29.35", "AuthorityKeyIdentifierCertExt"),

	/** Policy Constraints */
	POLICY_CONSTRAINTS("2.5.29.36", "PolicyConstraintsCertExt"),

	/** Extended Key Usage */
	EXTENDED_KEY_USAGE("2.5.29.37", "ExtendedKeyUsageCertExt"),

	/** Freshest CRL */
	FRESHEST_CRL("2.5.29.46", "FreshestCrlCertExt"),

	/** Inhibit Any Policy */
	INHIBIT_ANY_POLICY("2.5.29.54", "InhibitAnyPolicyCertExt"),

	/** Netscape Certificate Type */
	NETSCAPE_CERTIFICATE_TYPE("2.16.840.1.113730.1.1", "NetscapeCertificateTypeCertExt"),

	/** Netscape Base URL */
	NETSCAPE_BASE_URL("2.16.840.1.113730.1.2", "NetscapeBaseUrlCertExt"),

	/** Netscape Revocation URL */
	NETSCAPE_REVOCATION_URL("2.16.840.1.113730.1.3", "NetscapeRevocationUrlCertExt"),

	/** Netscape CA Revocation URL */
	NETSCAPE_CA_REVOCATION_URL("2.16.840.1.113730.1.4", "NetscapeCaRevocationUrlCertExt"),

	/** Netscape Certificate Renewal URL */
	NETSCAPE_CERTIFICATE_RENEWAL_URL("2.16.840.1.113730.1.7", "NetscapeCertificateRenewalUrlCertExt"),

	/** Netscape CA Policy URL */
	NETSCAPE_CA_POLICY_URL("2.16.840.1.113730.1.8", "NetscapeCaPolicyUrlCertExt"),

	/** Netscape SSL Server Name */
	NETSCAPE_SSL_SERVER_NAME("2.16.840.1.113730.1.12", "NetscapeSslServerNameCertExt"),

	/** Netscape Comment */
	NETSCAPE_COMMENT("2.16.840.1.113730.1.13", "NetscapeCommentCertExt"),

	// ////////////////////////////////
	// Undocumented X509Extension OIDs
	// ////////////////////////////////

	/** Authority Attribute Identifier */
	AUTHORITY_ATTRIBUTE_IDENTIFIER("2.5.29.38", "AuthorityAttributeIdentifierCertExt"),

	/** Role Spec Cert Identifier */
	ROLE_SPECIFICATION_CERTIFICATE_IDENTIFIER("2.5.29.39", "RoleSpecificationCertificateIdentifierCertExt"),

	/** CRL Stream Identifier */
	CRL_STREAM_IDENTIFIER("2.5.29.40", "CrlStreamIdentifierCertExt"),

	/** Basic Att Constraints Identifier */
	BASIC_ATT_CONSTRAINTS("2.5.29.41", "BaseAttConstraintsCertExt"),

	/** Delegated Name Constraints */
	DELEGATED_NAME_CONSTRAINTS("2.5.29.42", "DelegatedNameConstraintsCertExt"),

	/** Time Specification */
	TIME_SPECIFICATION("2.5.29.43", "TimeSpecificationCertExt"),

	/** CRL Scope */
	CRL_SCOPE("2.5.29.44", "CrlScopeCertExt"),

	/** Status Referrals */
	STATUS_REFERRALS("2.5.29.45", "StatusReferralsCertExt"),

	/** Ordered List */
	ORDERED_LIST("2.5.29.47", "OrderedListCertExt"),

	/** Attribute Descriptor */
	ATTRIBUTE_DESCRIPTOR("2.5.29.48", "AttributeDescriptorCertExt"),

	/** User Notice */
	USER_NOTICE("2.5.29.49", "UserNoticeCertExt"),

	/** SOA Identifier */
	SOA_IDENTIFIER("2.5.29.50", "SoaIdentifierCertExt"),

	/** Base Update Time */
	BASE_UPDATE_TIME("2.5.29.51", "BaseUpdateTimeCertExt"),

	/** Acceptable Certificate Policies */
	ACCEPTABLE_CERTIFICATE_POLICIES("2.5.29.52", "AcceptableCertificatePoliciesCertExt"),

	/** Delta Information */
	DELTA_INFORMATION("2.5.29.53", "DeltaInformationCertExt"),

	/** Target Information */
	TARGET_INFORMATION("2.5.29.55", "TargetInformationCertExt"),

	/** No Revocation Availability */
	NO_REVOCATION_AVAILABILITY("2.5.29.56", "NoRevocationAvailabilityCertExt"),

	/** Acceptable Privilege Policies */
	ACCEPTABLE_PRIVILEGE_POLICIES("2.5.29.57", "AcceptablePrivilegePoliciesCertExt"),

	// ////////////////////////////////
	// Obsolete X509Extension OIDs
	// ////////////////////////////////

	/** Obsolete Authority Key Identifier */
	AUTHORITY_KEY_IDENTIFIER_OBS("2.5.29.1", "AuthorityKeyIdentifierObsCertExt"),

	/** Obsolete Primary Key Attributes */
	PRIMARY_KEY_ATTRIBUTES_OBS("2.5.29.2", "PrimaryKeyAttributesObsCertExt"),

	/** Obsolete Certificate Policies */
	CERTIFICATE_POLICIES_OBS("2.5.29.3", "CertificatePoliciesObsCertExt"),

	/** Obsolete Primary Key Usage Restriction */
	PRIMARY_KEY_USAGE_RESTRICTION_OBS("2.5.29.4", "PrimaryKeyUsageRestrictionObsCertExt"),

	/** Obsolete Policy Mappings */
	POLICY_MAPPINGS_OBS("2.5.29.5", "PolicyMappingsObsCertExt"),

	/** Obsolete Subtrees Constraint */
	SUBTREES_CONSTRAINT_OBS("2.5.29.6", "SubtreesConstraintObsCertExt"),

	/** Obsolete Subject Alternative Name */
	SUBJECT_ALTERNATIVE_NAME_OBS("2.5.29.7", "SubjectAlternativeNameObsCertExt"),

	/** Obsolete Issuer Alternative Name */
	ISSUER_ALTERNATIVE_NAME_OBS("2.5.29.8", "IssuerAlternativeNameObsCertExt"),

	/** Obsolete Basic Constraints */
	BASIC_CONSTRAINTS_OBS("2.5.29.10", "BasicConstraintsObsCertExt"),

	/** Obsolete Name Constraints */
	NAME_CONSTRAINTS_OBS("2.5.29.11", "NameConstraintsObsCertExt"),

	/** Obsolete Policy Constraints */
	POLICY_CONSTRAINTS_OBS("2.5.29.12", "PolicyConstraintsObsCertExt"),

	/** Additional Obsolete Basic Constraints */
	BASIC_CONSTRAINTS_OBS1("2.5.29.13", "BasicConstraintsObsCertExt"),

	/** Obsolete Expiration Date */
	EXPIRATION_DATE_OBS("2.5.29.22", "ExpirationDateObsCertExt"),

	/** Obsolete CRL Distribution Points */
	CRL_DISTRIBUTION_POINTS_OBS("2.5.29.25", "CrlDistributionPointsObsCertExt"),

	/** Obsolete Issuing Distribution Point */
	ISSUING_DISTRIBUTION_POINT_OBS("2.5.29.26", "IssuingDistributionPointObsCertExt"),

	/** Additional Obsolete Policy Constraints */
	POLICY_CONSTRAINTS_OBS1("2.5.29.34", "PolicyConstraintsObsCertExt");

	private static ResourceBundle res = ResourceBundle.getBundle("net/sf/keystore_explorer/crypto/x509/resources");
	private String oid;
	private String friendlyKey;

	private X509ExtensionType(String oid, String friendlyKey) {
		this.oid = oid;
		this.friendlyKey = friendlyKey;
	}

	/**
	 * Get type's Object Identifier.
	 * 
	 * @return Object Identifier
	 */
	public String oid() {
		return oid;
	}

	/**
	 * Get type's friendly name.
	 * 
	 * @return Friendly name
	 */
	public String friendly() {
		return res.getString(friendlyKey);
	}

	/**
	 * Resolve the supplied object identifier to a matching type.
	 * 
	 * @param oid
	 *            Object identifier
	 * @return Type or null if none
	 */
	public static X509ExtensionType resolveOid(String oid) {
		for (X509ExtensionType type : values()) {
			if (oid.equals(type.oid())) {
				return type;
			}
		}

		return null;
	}

	/**
	 * Returns friendly name.
	 * 
	 * @return Friendly name
	 */
	public String toString() {
		return friendly();
	}
}
