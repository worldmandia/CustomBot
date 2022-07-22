package ua.mani123.ticket;

public class TicketBlank implements Ticket{
    @Override
    public String getId() {
        return "0";
    }

    @Override
    public String getCategory() {
        return "0";
    }
}
