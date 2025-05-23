package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Description;
import seedu.address.model.person.Email;
import seedu.address.model.person.ExternalParty;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Jackson-friendly version of {@link ExternalParty}
 */
public class JsonAdaptedExternalParty {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "External party's $s field is missing";

    private final String name;
    private final String phone;
    private final String email;
    private final String description;

    /**
     * Constructs a {@code JsonAdaptedExternalParty} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedExternalParty(@JsonProperty("name") String name,
                                    @JsonProperty("phone") String phone,
                                    @JsonProperty("email") String email,
                                    @JsonProperty("description") String description) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }

    /**
     * Converts a given {@code ExternalParty} into this class for Jackson use.
     */
    public JsonAdaptedExternalParty(ExternalParty source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        description = source.getDescription().value;
    }

    /**
     * Converts this Jackson-friendly adapted external party object into the model's {@code ExternalParty} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted external party.
     */
    public ExternalParty toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }
        if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        }
        final Description modelDescription = new Description(description);

        return new ExternalParty(modelName, modelPhone, modelEmail, modelDescription);
    }
}
