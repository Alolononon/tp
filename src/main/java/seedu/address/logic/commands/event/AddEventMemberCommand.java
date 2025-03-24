package seedu.address.logic.commands.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_INVALID_EXTERNAL_PARTY_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_INVALID_STAFF_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_EXTERNAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_STAFF;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_STUDENT;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.person.ExternalParty;
import seedu.address.model.person.Staff;
import seedu.address.model.person.Student;

/**
 * Adds a member (Student, Staff, or External Party) to an event.
 */
public class AddEventMemberCommand extends Command {

    public static final String COMMAND_WORD = "add_event_member";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to an event.\n"
            + "Parameters: <Event Index> " + PREFIX_EVENT_STUDENT + "<Student Index> OR "
            + PREFIX_EVENT_STAFF + "<Staff Index> OR " + PREFIX_EVENT_EXTERNAL + "<External Index>\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_EVENT_STUDENT + "3";

    public static final String MESSAGE_SUCCESS = "Added student %s to event: %s";
    public static final String MESSAGE_INVALID = "You must specify exactly one member type: stu/, staff/, or ext/.";

    private final Index eventIndex;
    private final Optional<Index> studentIndex;
    private final Optional<Index> staffIndex;
    private final Optional<Index> externalIndex;

    /**
     * Creates an {@code AddEventMemberCommand}.
     *
     * @param eventIndex   The index of the event (required).
     * @param studentIndex The optional index of the student.
     * @param staffIndex   The optional index of the staff member.
     * @param externalIndex The optional index of the external member.
     */
    public AddEventMemberCommand(Index eventIndex, Optional<Index> studentIndex, Optional<Index> staffIndex,
                                 Optional<Index> externalIndex) {
        requireNonNull(eventIndex);
        this.eventIndex = eventIndex;
        this.studentIndex = studentIndex;
        this.staffIndex = staffIndex;
        this.externalIndex = externalIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        // Ensure only one type of member is provided
        long count = studentIndex.isPresent() ? 1 : 0;
        count += staffIndex.isPresent() ? 1 : 0;
        count += externalIndex.isPresent() ? 1 : 0;
        if (count != 1) {
            throw new CommandException(MESSAGE_INVALID + "\n" + MESSAGE_USAGE);
        }


        List<Event> lastShownEventList = model.getFilteredEventList();
        List<Student> lastShownStudentList = model.getFilteredStudentList();

        if (eventIndex.getZeroBased() >= lastShownEventList.size()) {
            throw new CommandException(MESSAGE_INVALID_EVENT_DISPLAYED_INDEX + "\n" + MESSAGE_USAGE);
        }
        Event event = lastShownEventList.get(eventIndex.getZeroBased());


        // Add Student
        if (studentIndex.isPresent()) {
            if (studentIndex.get().getZeroBased() >= model.getFilteredStudentList().size()) {
                throw new CommandException(MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX + "\n" + MESSAGE_USAGE);
            }
            Student student = model.getFilteredStudentList().get(studentIndex.get().getZeroBased());
            event.addStudent(student);
            return new CommandResult(String.format(MESSAGE_SUCCESS, student.getName().fullName, event.getEventName()));
        }

        // Add Staff
        if (staffIndex.isPresent()) {
            if (staffIndex.get().getZeroBased() >= model.getFilteredStaffList().size()) {
                throw new CommandException(MESSAGE_INVALID_STAFF_DISPLAYED_INDEX + "\n" + MESSAGE_USAGE);
            }
            Staff staff = model.getFilteredStaffList().get(staffIndex.get().getZeroBased());
            event.addStaff(staff);
            return new CommandResult(String.format(MESSAGE_SUCCESS, staff.getName().fullName, event.getEventName()));
        }

        // Add External Member
        if (externalIndex.isPresent()) {
            if (externalIndex.get().getZeroBased() >= model.getFilteredExternalPartyList().size()) {
                throw new CommandException(MESSAGE_INVALID_EXTERNAL_PARTY_DISPLAYED_INDEX + "\n" + MESSAGE_USAGE);
            }
            ExternalParty external = model.getFilteredExternalPartyList().get(externalIndex.get().getZeroBased());
            event.addExternalParty(external);
            return new CommandResult(String.format(MESSAGE_SUCCESS, external.getName(), event.getEventName()));
        }

        throw new CommandException(MESSAGE_INVALID + "\n" + MESSAGE_USAGE);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddEventMemberCommand)) {
            return false;
        }

        AddEventMemberCommand otherCommand = (AddEventMemberCommand) other;
        return eventIndex.equals(otherCommand.eventIndex)
                && studentIndex.equals(otherCommand.studentIndex)
                && staffIndex.equals(otherCommand.staffIndex)
                && externalIndex.equals(otherCommand.externalIndex);
    }
}
