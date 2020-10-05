package hu.progmasters.gmistore.emailsending;

public interface ErrorMessegesSettings {
    public String emailErrorMessage();

    public String tooShortOrEmptyErrorMessage();

    public String tooLongErrorMessage();

    public String serverErrorMessage();
}
