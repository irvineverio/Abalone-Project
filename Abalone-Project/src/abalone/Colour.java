package abalone;

public enum Colour {
    WHITE("O"), RED("R"), BLACK("@"), GREEN("G");

    private String symbol;

    Colour(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }
}
