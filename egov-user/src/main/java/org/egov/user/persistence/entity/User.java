package org.egov.user.persistence.entity;

import lombok.*;
import org.egov.user.persistence.enums.BloodGroup;
import org.egov.user.persistence.enums.Gender;
import org.egov.user.persistence.enums.GuardianRelation;
import org.egov.user.persistence.enums.UserType;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.egov.user.persistence.entity.EnumConverter.toEnumType;
import static org.egov.user.persistence.entity.User.SEQ_COMPLAINT;


@Entity
@Table(name = "eg_user")
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = SEQ_COMPLAINT, sequenceName = SEQ_COMPLAINT, allocationSize = 1)
public class User extends AbstractAuditable {

    public static final String SEQ_COMPLAINT = "SEQ_EG_USER";
    private static final long serialVersionUID = 1666623645834766468L;

    @Id
    @GeneratedValue(generator = SEQ_COMPLAINT, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "title")
    private String title;

    @Column(name = "password")
    private String password;

    @Column(name = "salutation")
    private String salutation;

    @Column(name = "guardian")
    private String guardian;

    @Column(name = "guardianrelation")
    @Enumerated(EnumType.STRING)
    private GuardianRelation guardianRelation;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "mobilenumber")
    private String mobileNumber;

    @Column(name = "emailid")
    private String emailId;

    @Column(name = "altcontactnumber")
    private String altContactNumber;

    @Column(name = "pan")
    private String pan;

    @Column(name = "aadhaarnumber")
    private String aadhaarNumber;


    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.EAGER
    )
    private List<Address> address = new ArrayList<>();

    @Column(name = "active")
    private Boolean active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "eg_userrole", joinColumns = @JoinColumn(name = "userid"),
            inverseJoinColumns = @JoinColumn(name = "roleid"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "pwdexpirydate")
    private Date pwdExpiryDate = new Date();

    private String locale = "en_IN";

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column(name = "bloodgroup")
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;

    @Column(name = "identificationmark")
    private String identificationMark;

    @Column(name = "signature")
    private String signature;

    @Column(name = "photo")
    private String photo;

    @Column(name = "accountlocked")
    private Boolean accountLocked;

    public User (org.egov.user.domain.model.User user) {
        this.name = user.getName();
        this.id = user.getId();
        this.username = user.getUsername();
        this.title = user.getTitle();
        this.password = user.getPassword();
        this.salutation = user.getSalutation();
        this.guardian = user.getGuardian();
        this.guardianRelation = toEntityGuardianRelation(user.getGuardianRelation());
        this.gender = toEntityGender(user.getGender());
        this.mobileNumber = user.getMobileNumber();
        this.emailId = user.getEmailId();
        this.altContactNumber = user.getAltContactNumber();
        this.pan = user.getPan();
        this.aadhaarNumber = user.getAadhaarNumber();
        this.active = user.getActive();
        this.dob = user.getDob();
        this.pwdExpiryDate = user.getPwdExpiryDate();
        this.locale = user.getLocale();
        this.type = toEntityUserType(user.getType());
        this.bloodGroup = toEntityBloodGroup(user.getBloodGroup());
        this.identificationMark = user.getIdentificationMark();
        this.signature = user.getSignature();
        this.photo = user.getPhoto();
        this.accountLocked = user.getAccountLocked();
        this.setLastModifiedDate(user.getLastModifiedDate());
        this.setCreatedDate(user.getCreatedDate());
        this.roles = convertDomainRolesToEntity(user.getRoles());
    }



    private Set<Role> convertDomainRolesToEntity(List<org.egov.user.domain.model.Role> domainRoles) {
        return domainRoles.stream().map(Role::new).collect(Collectors.toSet());
    }

    public org.egov.user.domain.model.User toDomain() {
        return
        org.egov.user.domain.model.User.builder()
                .id(id)
                .username(username)
                .title(title)
                .password(password)
                .salutation(salutation)
                .guardian(guardian)
                .guardianRelation(toDomainGuardianRelation())
                .name(name)
                .gender(toDomainGender())
                .mobileNumber(mobileNumber)
                .emailId(emailId)
                .altContactNumber(altContactNumber)
                .pan(pan)
                .aadhaarNumber(aadhaarNumber)
                .address(convertEntityAddressToDomain(address))
                .active(active)
                .roles(convertEntityRoleToDomain(roles))
                .dob(dob)
                .pwdExpiryDate(dob)
                .locale(locale)
                .type(toDomainUserType())
                .bloodGroup(toDomainBloodGroup())
                .identificationMark(identificationMark)
                .signature(signature)
                .photo(photo)
                .accountLocked(accountLocked)
                .lastModifiedDate(getLastModifiedDate())
                .createdDate(getCreatedDate())
                .lastModifiedBy(getLastModifiedId())
                .createdBy(getCreatedById()).build();
    }

    private org.egov.user.domain.model.enums.GuardianRelation toDomainGuardianRelation() {
        return toEnumType(org.egov.user.domain.model.enums.GuardianRelation.class, guardianRelation);
    }

    private GuardianRelation toEntityGuardianRelation(
            org.egov.user.domain.model.enums.GuardianRelation guardianRelation) {
        return toEnumType(GuardianRelation.class, guardianRelation);
    }

    private org.egov.user.domain.model.enums.Gender toDomainGender() {
        return toEnumType(org.egov.user.domain.model.enums.Gender.class, gender);
    }

    private Gender toEntityGender(
            org.egov.user.domain.model.enums.Gender gender) {
        return toEnumType(Gender.class, gender);
    }

    private org.egov.user.domain.model.enums.BloodGroup toDomainBloodGroup() {
        return toEnumType(org.egov.user.domain.model.enums.BloodGroup.class, bloodGroup);
    }

    private BloodGroup toEntityBloodGroup(
            org.egov.user.domain.model.enums.BloodGroup bloodGroup) {
        return toEnumType(BloodGroup.class, bloodGroup);
    }

    private org.egov.user.domain.model.enums.UserType toDomainUserType() {
        return toEnumType(org.egov.user.domain.model.enums.UserType.class, type);
    }

    private UserType toEntityUserType(org.egov.user.domain.model.enums.UserType type) {
        return toEnumType(UserType.class, type);
    }

    private Long getLastModifiedId() {
        return  getLastModifiedBy();
    }

    private Long getCreatedById() {
        return getCreatedBy();
    }

    private List<org.egov.user.domain.model.Role> convertEntityRoleToDomain(Set<Role> entityRoles) {
        return entityRoles.stream().map(Role::toDomain).collect(Collectors.toList());
    }

    private List<org.egov.user.domain.model.Address> convertEntityAddressToDomain(List<Address> entityAddress) {
        return entityAddress.stream().map(Address::toDomain).collect(Collectors.toList());
    }
}